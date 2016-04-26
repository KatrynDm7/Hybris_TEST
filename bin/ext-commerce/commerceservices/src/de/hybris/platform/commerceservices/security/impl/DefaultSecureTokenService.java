/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package de.hybris.platform.commerceservices.security.impl;

import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.servicelayer.exceptions.SystemException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SecureTokenService}
 */
public class DefaultSecureTokenService implements SecureTokenService, InitializingBean
{
	protected static final int ENCRYPT_KEY_LENGTH = 16;
	protected static final int MD5_LENGTH = 16;
	protected static final int AESIV_LENGTH = 16;
	protected static final String ENCRYPTION_CIPHER = "AES/CBC/PKCS5Padding";
	protected static final String RANDOM_ALGORITHM = "SHA1PRNG";
	protected static final String MESSAGEDIGEST_ALGORITHM = "MD5";

	private static final Logger LOG = Logger.getLogger(DefaultSecureTokenService.class);

	private SecureRandom random;

	private String signatureKeyHex;
	private String encryptionKeyHex;

	private byte[] signatureKeyBytes;
	private byte[] encryptionKeyBytes;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws DecoderException, NoSuchAlgorithmException
	{
		signatureKeyBytes = decodeHexString(getSignatureKeyHex());
		encryptionKeyBytes = decodeHexString(getEncryptionKeyHex());
		random = SecureRandom.getInstance(RANDOM_ALGORITHM);
	}

	protected SecureRandom getRandom()
	{
		return random;
	}

	protected byte[] getSignatureKeyBytes()
	{
		return signatureKeyBytes;
	}
	
	protected byte[] getEncryptionKeyBytes()
	{
		return encryptionKeyBytes;
	}

	@Override
	public String encryptData(final SecureToken data)
	{
		if (data == null || StringUtils.isBlank(data.getData()))
		{
			throw new IllegalArgumentException("missing token");
		}
		try
		{
			final int[] paddingSizes = computePaddingLengths();

			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
			dataOutputStream.write(generatePadding(paddingSizes[0]));
			dataOutputStream.writeUTF(data.getData());
			dataOutputStream.writeUTF(createChecksum(data.getData()));
			dataOutputStream.writeLong(data.getTimeStamp());
			dataOutputStream.write(generatePadding(paddingSizes[1]));

			dataOutputStream.flush();
			final byte[] unsignedDataBytes = byteArrayOutputStream.toByteArray();

			final byte[] md5SigBytes = generateSignature(unsignedDataBytes, 0, unsignedDataBytes.length, getSignatureKeyBytes());
			byteArrayOutputStream.write(md5SigBytes);
			byteArrayOutputStream.flush();

			final byte[] signedDataBytes = byteArrayOutputStream.toByteArray();

			return encrypt(signedDataBytes, getEncryptionKeyBytes());
		}
		catch (final IOException e)
		{
			LOG.error("Could not encrypt", e);
			throw new SystemException(e.toString(), e);
		}
		catch (final GeneralSecurityException e)
		{
			LOG.error("Could not encrypt", e);
			throw new SystemException(e.toString(), e);
		}
	}

	@Override
	public SecureToken decryptData(final String token)
	{
		if (token == null || StringUtils.isBlank(token))
		{
			throw new IllegalArgumentException("missing token");
		}
		try
		{
			final byte[] decryptedBytes = decrypt(token, getEncryptionKeyBytes());

			// Last 16 bytes are the MD5 signature
			final int decryptedBytesDataLength = decryptedBytes.length - MD5_LENGTH;
			if (!validateSignature(decryptedBytes, 0, decryptedBytesDataLength, decryptedBytes, decryptedBytesDataLength,
					getSignatureKeyBytes()))
			{
				throw new IllegalArgumentException("Invalid signature in cookie");
			}
			final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedBytes, 0, decryptedBytesDataLength);
			final DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

			skipPadding(dataInputStream);

			final String userIdentifier = dataInputStream.readUTF();
			final String userChecksum = dataInputStream.readUTF();
			if (userChecksum == null || !userChecksum.equals(createChecksum(userIdentifier)))
			{
				throw new IllegalArgumentException("invalid token");
			}
			final long timeStampInSeconds = dataInputStream.readLong();

			return new SecureToken(userIdentifier, timeStampInSeconds);
		}
		catch (final IOException e)
		{
			LOG.error("Could not decrypt token", e);
			throw new SystemException(e.toString(), e);
		}
		catch (final GeneralSecurityException e)
		{
			LOG.warn("Could not decrypt token: " + e.toString());
			throw new IllegalArgumentException("Invalid token", e);
		}
	}

	protected byte[] decodeHexString(final String text) throws DecoderException
	{
		final char[] chars = new char[text.length()];
		text.getChars(0, text.length(), chars, 0);
		return Hex.decodeHex(chars);
	}

	protected int[] computePaddingLengths()
	{
		final int firstNumber = getRandom().nextInt(8);// rand 0 through 7
		final int windowAdjustment = 7 - firstNumber;
		final int secondNumber = windowAdjustment + getRandom().nextInt(8 - windowAdjustment);

		if (getRandom().nextBoolean())
		{
			return new int[] { firstNumber, secondNumber };
		}
		return new int[] { secondNumber, firstNumber };
	}

	protected byte[] generatePadding(final int length)
	{
		if (length < 0 || length > 7)
		{
			throw new IllegalArgumentException("length must be in range 0 to 7. Actual value: " + length);
		}

		final byte[] block = new byte[length + 1];

		// Fill with random data
		getRandom().nextBytes(block);

		// Overwrite the bottom 3 bits of the first byte with the length
		final byte firstByte = (byte) ((block[0] & 0x00F8) | (length & 0x0007));
		block[0] = firstByte;

		return block;
	}

	protected byte[] generateSignature(final byte[] data, final int offset, final int length, final byte[] signatureKeyBytes)
			throws NoSuchAlgorithmException
	{
		final MessageDigest md5Digest = MessageDigest.getInstance(MESSAGEDIGEST_ALGORITHM);

		// Secret key bytes first
		md5Digest.update(signatureKeyBytes);

		// Data second
		md5Digest.update(data, offset, length);

		// Generate the digest
		final byte[] md5SigBytes = md5Digest.digest();
		if (md5SigBytes.length != MD5_LENGTH)
		{
			throw new IllegalArgumentException("MD5 Signature incorrect length [" + md5SigBytes.length + "]");
		}

		return md5SigBytes;
	}

	protected String encrypt(final byte[] plainText, final byte[] encryptionKeyBytes) throws GeneralSecurityException
	{
		// Generate 16 random IV bytes
		final byte[] ivBytes = new byte[AESIV_LENGTH];
		getRandom().nextBytes(ivBytes);
		final IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

		// Setup cypher
		final Cipher cipher = Cipher.getInstance(ENCRYPTION_CIPHER);
		cipher.init(Cipher.ENCRYPT_MODE, buildSecretKey(encryptionKeyBytes), ivSpec);

		// Generate encrypted data
		final byte[] encryptedBytes = cipher.doFinal(plainText);

		// Prepend the IV
		final byte[] encryptedBytesPlusIV = new byte[ivBytes.length + encryptedBytes.length];
		System.arraycopy(ivBytes, 0, encryptedBytesPlusIV, 0, ivBytes.length);
		System.arraycopy(encryptedBytes, 0, encryptedBytesPlusIV, ivBytes.length, encryptedBytes.length);

		return convert(encryptedBytesPlusIV);
	}

	protected String convert(final byte[] data)
	{
		try
		{
			return new String(Base64.encodeBase64(data), CharEncoding.UTF_8);
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new SystemException("encoding not supported", e);
		}
	}

	protected SecretKeySpec buildSecretKey(final byte[] encryptionKeyBytes)
	{
		final byte[] keyBytes = new byte[ENCRYPT_KEY_LENGTH];
		Arrays.fill(keyBytes, (byte) 0);
		final int copyLen = Math.min(ENCRYPT_KEY_LENGTH, encryptionKeyBytes.length);
		System.arraycopy(encryptionKeyBytes, 0, keyBytes, 0, copyLen);
		return new SecretKeySpec(keyBytes, "AES");
	}

	protected void skipPadding(final DataInputStream dataInputStream) throws IOException
	{
		final int firstByte = dataInputStream.readUnsignedByte();

		// Find number of additional bytes to skip (stored in the bottom 3 bits)
		final int length = firstByte & 0x0007;

		for (int i = 0; i < length; i++)
		{
			dataInputStream.readByte();
		}
	}

	protected boolean validateSignature(final byte[] dataBytes, final int dataOffset, final int dataLength,
			final byte[] signatureBytes, final int signatureOffset, final byte[] signatureKeyBytes) throws NoSuchAlgorithmException
	{
		final byte[] computedSig = generateSignature(dataBytes, dataOffset, dataLength, signatureKeyBytes);
		return arrayEquals(signatureBytes, signatureOffset, computedSig, 0, MD5_LENGTH);
	}

	protected boolean arrayEquals(final byte[] array1, final int offset1, final byte[] array2, final int offset2, final int length)
	{
		if ((array1 == null || array2 == null) || (array1.length - offset1 < length) || (array2.length - offset2 < length))
		{
			return false;
		}

		for (int i = 0; i < length; i++)
		{
			if (array1[offset1 + i] != array2[offset2 + i])
			{
				return false;
			}
		}

		return true;
	}

	protected byte[] decrypt(final String encryptedText, final byte[] encryptionKeyBytes) throws GeneralSecurityException
	{
		// Decode base64 encoded string
		final byte[] encryptedBytes = Base64.decodeBase64(encryptedText.getBytes());

		if (encryptedBytes == null || encryptedBytes.length < AESIV_LENGTH)
		{
			throw new IllegalArgumentException("Encrypted data too short");
		}

		// Create the cypher
		final Cipher cipher = Cipher.getInstance(ENCRYPTION_CIPHER);

		// The IV is the first 16 bytes of the data
		final IvParameterSpec ivSpec = new IvParameterSpec(encryptedBytes, 0, AESIV_LENGTH);
		cipher.init(Cipher.DECRYPT_MODE, buildSecretKey(encryptionKeyBytes), ivSpec);

		return cipher.doFinal(encryptedBytes, AESIV_LENGTH, encryptedBytes.length - AESIV_LENGTH);
	}

	protected String createChecksum(final String data) throws IOException, NoSuchAlgorithmException
	{
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		dataOutputStream.writeUTF(data);
		dataOutputStream.flush();
		final byte[] unsignedDataBytes = byteArrayOutputStream.toByteArray();
		final byte[] md5SigBytes = generateSignature(unsignedDataBytes, 0, unsignedDataBytes.length, getSignatureKeyBytes());
		return convert(Base64.encodeBase64(md5SigBytes));
	}

	protected String getSignatureKeyHex()
	{
		return signatureKeyHex;
	}

	/**
	 * @param signatureKeyHex
	 *           the signatureKeyHex to set
	 */
	@Required
	public void setSignatureKeyHex(final String signatureKeyHex)
	{
		this.signatureKeyHex = signatureKeyHex;
	}

	protected String getEncryptionKeyHex()
	{
		return encryptionKeyHex;
	}

	/**
	 * @param encryptionKeyHex
	 *           the encryptionKeyHex to set
	 */
	@Required
	public void setEncryptionKeyHex(final String encryptionKeyHex)
	{
		this.encryptionKeyHex = encryptionKeyHex;
	}

}