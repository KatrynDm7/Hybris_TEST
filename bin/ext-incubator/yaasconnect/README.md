# Yaas Connector
 
## Usage
### spring.xml
```xml
    <!-- this config provider makes use of ConfigurationService 
    -->    

	<bean id="platformPropertyResolver" class="de.hybris.platform.yaasconnect.config.PlatformPropertyResolver">
		<constructor-arg value="yaas" /> <!-- example configuration prefix -->
		<constructor-arg ref="configurationService" />
	</bean>

	<bean id="wishlistClient" class="com.hybris.charon.HttpClientFactoryBean">
		<property name="propertyResolver" ref="platformPropertyResolver" />
		<property name="clientId" value="wishlist" />
		<property name="type" value="de.hybris.platform.yaasservice.client.WishlistClient" />
	</bean>
```

### local.properties
```properties
yaas.wishlist.oauth.url=...

yaas.wishlist.oauth.clientId=...

yaas.wishlist.oauth.clientSecret=...

yaas.wishlist.oauth.scope=...

yaas.wishlist.url=...
```
### Java client interface
```java
@OAuth
public interface WishlistClient
{
	@GET
	@Path("/{id}")
	Observable<Wishlist> get(@PathParam("id") String id);

	@GET
	@Path("/")
	Collection<Wishlist> getAll();

	@POST
	@Path("/")
	@Consumes("application/json")
	void createWishlist(Wishlist wishlist);
}
```




