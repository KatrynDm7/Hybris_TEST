package de.hybris.platform.cuppy.web.cockpit;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.TemplateListEntry;

import java.util.ArrayList;
import java.util.List;


public class CuppyAdminPerspective extends BaseUICockpitPerspective
{
	private List<TemplateListEntry> templateList;

	@Override
	public List<TemplateListEntry> getTemplateList()
	{
		if (this.templateList == null)
		{
			this.templateList = new ArrayList<TemplateListEntry>();
			this.templateList.add(new TemplateListEntry(UISessionUtils.getCurrentSession().getTypeService()
					.getObjectTemplate("News"), 0, 0));
		}
		return this.templateList;
	}
}
