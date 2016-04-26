/**
 *
 */
package de.hybris.platform.sap.productconfig.frontend.controllers;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.yacceleratorstorefront.controllers.cms.DefaultCMSComponentController;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller("ListAddToCartActionController")
@Scope("tenant")
@RequestMapping(value = "/view/ListAddToCartActionController")
public class ListAddToCartActionController extends DefaultCMSComponentController
{

	@Override
	protected String getView(final AbstractCMSComponentModel component)
	{
		// TODO: use some constants, for ext name, default cms folder, ...
		return "/../addons/ysapproductconfigb2baddon/desktop/cms/configureproductaction";
	}

}
