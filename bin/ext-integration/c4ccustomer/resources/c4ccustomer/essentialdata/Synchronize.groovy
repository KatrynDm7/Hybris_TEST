import de.hybris.platform.servicelayer.search.FlexibleSearchQuery

def customerCronJob = findCronJob 'c4cSyncToDataHubCustomersCronJob'
cronJobService.performCronJob(customerCronJob, true)

def findCronJob(code) {
	def fQuery = new FlexibleSearchQuery('SELECT {PK} FROM {Y2YSyncCronJob} WHERE {code}=?code')
	fQuery.addQueryParameter('code', code)
	flexibleSearchService.searchUnique(fQuery)
}