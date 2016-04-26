import de.hybris.platform.servicelayer.search.FlexibleSearchQuery

def fQuery = new FlexibleSearchQuery('SELECT {PK} FROM {ItemVersionMarker}')
def result = flexibleSearchService.search(fQuery)

result.getResult().forEach {
	modelService.remove(it)
}