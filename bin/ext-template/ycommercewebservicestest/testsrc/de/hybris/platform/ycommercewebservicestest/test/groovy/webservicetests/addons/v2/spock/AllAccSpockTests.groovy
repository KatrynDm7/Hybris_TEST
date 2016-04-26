package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.addons.v2.spock

import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite.class)
@Suite.SuiteClasses([SopTest, ExtendedCartV2Tests])
class AllAccSpockTests {
	@BeforeClass
	public static void setUpClass() {
		//dummy setup class, if its not provided parent class is not created
	}
}
