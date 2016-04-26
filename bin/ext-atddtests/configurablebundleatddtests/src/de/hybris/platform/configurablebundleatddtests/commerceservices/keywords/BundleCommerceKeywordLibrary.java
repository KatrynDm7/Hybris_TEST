package de.hybris.platform.configurablebundleatddtests.commerceservices.keywords;


import static de.hybris.platform.atddengine.xml.XmlAssertions.assertXPathEvaluatesTo;
import static org.junit.Assert.fail;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.configurablebundlefacades.converters.BundleXStreamConverter;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.subscriptionfacades.converters.SubscriptionXStreamAliasConverter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public class BundleCommerceKeywordLibrary extends AbstractKeywordLibrary
{

	private static final Logger LOG = Logger.getLogger(BundleCommerceKeywordLibrary.class);

	@Autowired
	@Qualifier("configurableBundleProductFacade")
	private ProductFacade productFacade;

	@Autowired
	private SubscriptionXStreamAliasConverter xStreamAliasConverter;

	@Autowired
	private BundleXStreamConverter bundleXStreamConverter;

	@Autowired
	private CartFacade cartFacade;

	@Autowired
	private CartService cartService;

	@Autowired
	private CommerceCartService commerceCartService;

	@Autowired
	private UserService userService;

	@Autowired
	private Converter<CartData, CartModel> cartConverter;

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>verify product xml</i>
	 * <p>
	 *
	 * @param xpath
	 *           the XPath expression to evaluate
	 *
	 * @param expectedXml
	 *           the expected XML
	 *
	 * @param productCode
	 *           code the code of the product to verify
	 *
	 * @param option
	 *           ProductOption
	 */
	public void verifyProductXmlWithOption(final String xpath, final String expectedXml, final String productCode,
			final String... option)
	{
		try
		{
			final ProductData product = productFacade.getProductForCodeAndOptions(productCode, getProductOptions(option));
			final String productXml = xStreamAliasConverter.getXStreamXmlFromSubscriptionProductData(product);

			assertXPathEvaluatesTo("The product XML does not match the expectations:", productXml, xpath, expectedXml,
					"transformation/removeElements.xsl");
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error("Product with code " + productCode + " does not exist", e);
			fail("Product with code " + productCode + " does not exist");
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error("Either the expected XML is malformed or the product code is null", e);
			fail("Either the expected XML is malformed or the product code is null");
		}
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>verify Cart xml</i>
	 * <p>
	 *
	 * @param xpath
	 *           the XPath expression to evaluate
	 *
	 * @param expectedXml
	 *           the expected XML
	 *
	 */
	public void verifyCartXml(final String xpath, final String expectedXml)
	{
		try
		{
			final CartData cartData = cartFacade.getSessionCartWithEntryOrdering(true);
			buildProductFromReference(cartData);

			final String cartXml = bundleXStreamConverter.getXStreamXmlFromCartData(cartData);

			assertXPathEvaluatesTo("The Cart XML does not match the expectations:", cartXml, xpath, expectedXml,
					"transformation/removeCartElements.xsl");
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error("Either the expected XML is malformed or the Card is null", e);
			fail("Either the expected XML is malformed or the Card is null");
		}
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>verify last modified entries is empty</i>
	 * <p>
	 *
	 * @param cartCode
	 *           the card code to verify
	 *
	 */
	public void verifyLastModifiedEntriesIsEmpty(final String cartCode)
	{
		final List<CartEntryModel> lastModified = (List<CartEntryModel>) commerceCartService.getCartForCodeAndUser(cartCode,
				userService.getCurrentUser()).getLastModifiedEntries();
		Assert.assertTrue(CollectionUtils.isEmpty(lastModified));
	}

	private void buildProductFromReference(final CartData cartData)
	{
		if (cartData != null && CollectionUtils.isNotEmpty(cartData.getEntries()))
		{
			for (final OrderEntryData entryData : cartData.getEntries())
			{
				entryData.setProduct(createProductData(entryData.getProduct()));
				entryData.setComponent(createComponentData(entryData.getComponent()));
			}
		}
	}

	/**
	 * Helper method to re-create ProductData
	 *
	 * @param productData
	 * @return
	 */
	private ProductData createProductData(final ProductData productData)
	{
		final ProductData newProduct = new ProductData();
		newProduct.setCode(productData.getCode());
		newProduct.setDisabled(productData.isDisabled());
		return newProduct;
	}

	/**
	 * Helper method to re-create BundleTemplateData
	 *
	 * @param bundleTemplateData
	 * @return
	 */
	private BundleTemplateData createComponentData(final BundleTemplateData bundleTemplateData)
	{
		final BundleTemplateData bundleData = new BundleTemplateData();

		bundleData.setId(bundleTemplateData.getId());
		bundleData.setName(bundleData.getName());

		return bundleData;
	}

	/**
	 * Helper method to convert from String array to List of ProductOption.
	 *
	 * @param option
	 * @return
	 */
	private List<ProductOption> getProductOptions(final String[] option)
	{
		final List<ProductOption> productOptionList = new ArrayList<ProductOption>();

		for (final String name : option)
		{
			productOptionList.add(ProductOption.valueOf(name));
		}

		return productOptionList;
	}
}
