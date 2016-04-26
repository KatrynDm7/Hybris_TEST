/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.eventtracking.services.converters;

import de.hybris.eventtracking.model.events.AbstractTrackingEvent;
import de.hybris.eventtracking.model.events.AddToCartEvent;
import de.hybris.eventtracking.model.events.BannerClickEvent;
import de.hybris.eventtracking.model.events.CartViewEvent;
import de.hybris.eventtracking.model.events.CategoryPageViewEvent;
import de.hybris.eventtracking.model.events.FindStoresNearMeEvent;
import de.hybris.eventtracking.model.events.PageThroughSearchResultsEvent;
import de.hybris.eventtracking.model.events.PageViewEvent;
import de.hybris.eventtracking.model.events.ProceedToCheckoutEvent;
import de.hybris.eventtracking.model.events.ProductDetailPageViewEvent;
import de.hybris.eventtracking.model.events.ProductMediaViewEvent;
import de.hybris.eventtracking.model.events.ProductReviewsViewEvent;
import de.hybris.eventtracking.model.events.RefineSearchEvent;
import de.hybris.eventtracking.model.events.RemoveFromCartEvent;
import de.hybris.eventtracking.model.events.SearchEvent;
import de.hybris.eventtracking.model.events.SearchNoResultsEvent;
import de.hybris.eventtracking.model.events.StoreLocationEnteredEvent;
import de.hybris.eventtracking.model.events.SuccessfulCheckoutEvent; 
import de.hybris.eventtracking.services.constants.TrackingEventJsonFields;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;
	

/**
 * @author stevo.slavic
 *
 */
public class TrackingEventTypeResolver implements TypeResolver<Map<String, Object>, AbstractTrackingEvent>
{

	private final ObjectMapper mapper;

	private static final Logger LOG = Logger.getLogger(TrackingEventTypeResolver.class);
	

	public TrackingEventTypeResolver(final ObjectMapper mapper)
	{
		this.mapper = mapper;
	}

	@Override
	public Class<? extends AbstractTrackingEvent> resolveType(final Map<String, Object> trackingEventData)
	{
		
		try
		{
			final String eventtype = (String) trackingEventData.get(TrackingEventJsonFields.COMMON_EVENT_TYPE.getKey());
			
			if (LOG.isDebugEnabled())
			{
					LOG.debug("Event type as found in COMMON_EVENT_TYPE: " + eventtype);
			}
		
			if (StringUtils.isBlank(eventtype))
			{
				if (LOG.isDebugEnabled())
				{
						LOG.debug("Event type as found in COMMON_EVENT_TYPE was blank, returning null.");
				}
				return null;
			}

			final String cvar = (String) trackingEventData.get(TrackingEventJsonFields.COMMON_CVAR_PAGE.getKey());
						
			Map<String, Object> customVariablesPageScoped = null;
			if (StringUtils.isNotBlank(cvar))
			{
				customVariablesPageScoped = mapper.readValue(cvar, Map.class);
				
			}

			switch (eventtype)
			{
				case "log":
				{
					String productSku = null;
					if (customVariablesPageScoped != null)
					{
						final List<String> pksData = (List) customVariablesPageScoped.get("3");
						if (pksData != null && pksData.size() > 0)
						{
							productSku = pksData.get(1);
						}
					}

					if (StringUtils.isNotBlank(productSku))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: ProductDetailPageViewEvent");
						}					
						return ProductDetailPageViewEvent.class;
					}
					else
					{
						String url = null;
						if (customVariablesPageScoped != null)
						{
							url = (String) customVariablesPageScoped.get(TrackingEventJsonFields.COMMON_URL.getKey());
						}
						if (StringUtils.isNotBlank(url) && url.endsWith("/cart"))
						{
							if (LOG.isDebugEnabled())
							{
								LOG.debug("Event type successfully resolved: CartViewEvent");
							}	
							return CartViewEvent.class;
						}
						
						if (LOG.isDebugEnabled())
						{
								LOG.debug("Event type successfully resolved: PageViewEvent");
						}						
						return PageViewEvent.class;
					}
				}

				case "sitesearch":
				{
					final String categoryInfo = (String) trackingEventData.get(TrackingEventJsonFields.SEARCH_CATEGORY.getKey());
					final String searchTerms = (String) trackingEventData.get(TrackingEventJsonFields.SEARCH_TERMS.getKey());
					final String searchCount = (String) trackingEventData.get(TrackingEventJsonFields.SEARCH_COUNT.getKey());
					String searchFacets = null;
					String searchResultsPage = null;
					if (customVariablesPageScoped != null)
					{
						searchFacets = (String) customVariablesPageScoped.get(TrackingEventJsonFields.SEARCH_FACETS.getKey());
						searchResultsPage = (String) customVariablesPageScoped
								.get(TrackingEventJsonFields.SEARCH_RESULTS_PAGE.getKey());
					}

					if ("0".equals(searchCount))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: SearchNoResultsEvent");
						}
						return SearchNoResultsEvent.class;
					}
					else if (StringUtils.isNotBlank(categoryInfo) && StringUtils.isBlank(searchTerms))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: CategoryPageViewEvent");
						}
						return CategoryPageViewEvent.class;
					}
					else if (StringUtils.isNotBlank(searchResultsPage))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: PageThroughSearchResultsEvent");
						}					
						return PageThroughSearchResultsEvent.class;
					}
					else if (StringUtils.isNotBlank(searchFacets))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: RefineSearchEvent");
						}					
						return RefineSearchEvent.class;
					}
					else
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: SearchEvent");
						}						
						return SearchEvent.class;
					}
				}
				case "event":
				{
					final String eventCategory = (String) trackingEventData.get(TrackingEventJsonFields.EVENT_CATEGORY.getKey());
					final String eventAction = (String) trackingEventData.get(TrackingEventJsonFields.EVENT_ACTION.getKey());
					
					if (LOG.isDebugEnabled())
						{
							LOG.debug("eventCategory = "+eventCategory);
							LOG.debug("eventAction = "+eventAction);
						}

					if ("video".equals(eventCategory) && "play".equals(eventAction))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: ProductMediaViewEvent");
						}					
						return ProductMediaViewEvent.class;
					}

					else if ("review".equals(eventCategory) && "view".equals(eventAction))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: ProductReviewsViewEvent");
						}					
					
						return ProductReviewsViewEvent.class;
					}

					else if ("store_location".equals(eventCategory) && "enter".equals(eventAction))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: StoreLocationEnteredEvent");
						}					
					
						return StoreLocationEnteredEvent.class;
					}

					else if ("store".equals(eventCategory) && "find".equals(eventAction))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: FindStoresNearMeEvent");
						}	
						return FindStoresNearMeEvent.class;
					}

					else if ("cart".equals(eventCategory) && "view".equals(eventAction))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: CartViewEvent");
						}	
					
						return CartViewEvent.class;
					}

					else if ("checkout".equals(eventCategory) && "proceed".equals(eventAction))
					{
					
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: ProceedToCheckoutEvent");
						}	
				
						return ProceedToCheckoutEvent.class;
					}
					
					else if ("checkout".equals(eventCategory) && "success".equals(eventAction))
					{
					
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: SuccessfulCheckoutEvent");
						}	
				
						return SuccessfulCheckoutEvent.class;
					}
						
					if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type cannot be resolved, returning null.");
						}	
					return null;
				}

				case "link":
				{
					final String banner = (String) trackingEventData.get(TrackingEventJsonFields.BANNER.getKey());

					if (StringUtils.isNotBlank(banner))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Event type successfully resolved: BannerClickEvent");
						}					
						return BannerClickEvent.class;
					}

					return null;
				}

				case "ecommerce":
				{

					String productSku = null;
					if (customVariablesPageScoped != null)
					{
						final List<String> pksData = (List) customVariablesPageScoped.get("3");
						if (pksData != null && pksData.size() > 0)
						{
							productSku = pksData.get(1);
						}
					}

					if (StringUtils.isNotBlank(productSku))
					{
						final String ecItems = (String) trackingEventData.get(TrackingEventJsonFields.COMMERCE_CART_ITEMS.getKey());
						final Map<String, CartItem> cartItems = new HashMap<String, CartItem>();
						List<List<Object>> cartItemsData = null;
						if (StringUtils.isNotBlank(ecItems))
						{
							cartItemsData = mapper.readValue(ecItems, List.class);
						}

						if (cartItemsData != null)
						{
							for (final List<Object> cartItemData : cartItemsData)
							{
								if (cartItemData != null && cartItemData.size() > 3)
								{
									final CartItem cartItem = new CartItem();
									cartItem.sku = (String) cartItemData.get(0);
									cartItem.name = (String) cartItemData.get(1);
									cartItem.categories = (List) cartItemData.get(2);
									cartItem.price = (String) cartItemData.get(3);
									cartItem.quantity = (String) cartItemData.get(4);

									cartItems.put(cartItem.sku, cartItem);
								}
							}
						}

						final CartItem cartItem = cartItems.get(productSku);
						if (cartItem != null)
						{
							if ("0".equals(cartItem.quantity))
							{
								final List<String> partOne = (List) customVariablesPageScoped.get("1");
								final String cartID = partOne.get(1);

								if (LOG.isDebugEnabled())
								{
									LOG.debug("Event type successfully resolved: RemoveFromCartEvent");
									LOG.debug("===== CART ID="+cartID);					
								}								
							return RemoveFromCartEvent.class;
							}
							else
							{

								final List<String> partOne = (List) customVariablesPageScoped.get("1");
								final String cartID = partOne.get(1);
											
								if (LOG.isDebugEnabled())
								{
									LOG.debug("Event type successfully resolved: AddToCartEvent");
									LOG.debug("===== CART ID="+cartID);	
								
								}	
																					
								return AddToCartEvent.class;
							}
						}
					}
					if (LOG.isDebugEnabled())
					{
							LOG.debug("Event type cannot be resolved, returning null.");
					}
					return null;
				}

				default:
				{
					if (LOG.isDebugEnabled())
					{
							LOG.debug("Event type cannot be resolved, returning null.");
					}				
					return null;
				}
			}
		}
		catch (final IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	private static class CartItem
	{
		private String sku;
		private String name;
		private List<String> categories;
		private String price;
		private String quantity;
	}
}
