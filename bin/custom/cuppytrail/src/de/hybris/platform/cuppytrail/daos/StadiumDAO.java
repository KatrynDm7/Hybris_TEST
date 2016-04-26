package de.hybris.platform.cuppytrail.daos;

import de.hybris.platform.cuppytrail.model.StadiumModel;
import java.util.List;
/**
 * This interface belongs to the Source Code Trail documented at https://wiki.hybris.com/display/pm/Source+Code+Tutorial
 * An interface for the Stadium DAO. This incorporates the CRUD functionality we require for our DAO tests to pass.
 */
public interface StadiumDAO
{
    List<StadiumModel> findStadiums();
    List<StadiumModel> findStadiumsByCode(String code);
}
