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
package de.hybris.platform.commerceservices.security;

/**
 * Service to Encrypt SecureTokenData into a short encrypted token and decrypt a token.
 * 
 * The encrypted string is in Base64 format. The data is signed and encrypted using 2 separate keys. The keys are
 * specified as hex character strings. Instances of this class are thread safe. Unlimited strength cryptography is
 * required. The data block is calculated as: Signature key (not included in data block) Length prefixed padding
 * SecureTokenData.Data SecureTokenData.Checksum SecureTokenData.TimeStamp padding MD5 signature of the above. All the
 * above (except signature key) is AES encrypted using encryption key. Result is base64ed.
 * 
 * @spring.bean secureTokenService
 */
public interface SecureTokenService
{
	/**
	 * Encrypt the SecureToken parameter to a String
	 * 
	 * @param data The unencrypted token data
	 * @throws IllegalArgumentException
	 * @return encrypted token
	 */
	String encryptData(SecureToken data);

	/**
	 * Decrypt the token to a SecureToken.
	 * 
	 * @param encryptedToken the encrypted token data
	 * @throws IllegalArgumentException if the token cannot be decrypted
	 * @return decrypted SecureToken
	 */
	SecureToken decryptData(String encryptedToken);
}
