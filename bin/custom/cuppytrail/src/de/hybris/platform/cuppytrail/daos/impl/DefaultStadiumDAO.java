package de.hybris.platform.cuppytrail.daos.impl;

import de.hybris.platform.cuppytrail.daos.StadiumDAO;
import de.hybris.platform.cuppytrail.model.StadiumModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "stadiumDAO")
public class DefaultStadiumDAO implements StadiumDAO
{
    /**
     * We use hybris' FlexibleSearchService for running queries against the database
     *
     * @see "https://wiki.hybris.com/display/release5/FlexibleSearch"
     */
    @Autowired
    private FlexibleSearchService flexibleSearchService;

    /**
     * Finds all Stadiums by performing a FlexibleSearch using the {@link FlexibleSearchService}.
     */
    @Override
    public List<StadiumModel> findStadiums()
    {
        // Build a query for the flexible search.
        final String queryString = //
                "SELECT {p:" + StadiumModel.PK + "} "//
                        + "FROM {" + StadiumModel._TYPECODE + " AS p} ";

        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

        // Note that we could specify paginating logic by providing a start and count variable (commented out below)
        // This can provide a safeguard against returning very large amounts of data, or hogging the database when there are
        // for example millions of items being returned.
        // As we know that there are only a few persisted stadiums in this use case we do not need to provide this.

        //query.setStart(start);
        //query.setCount(count);

        // Return the list of StadiumModels.
        return flexibleSearchService.<StadiumModel> search(query).getResult();
    }

    /**
     * Finds all Stadiums by given code by performing a FlexibleSearch using the {@link FlexibleSearchService}.
     */
    @Override
    public List<StadiumModel> findStadiumsByCode(final String code)
    {
        final String queryString = //
                "SELECT {p:" + StadiumModel.PK + "}" //
                        + "FROM {" + StadiumModel._TYPECODE + " AS p} "//
                        + "WHERE " + "{p:" + StadiumModel.CODE + "}=?code ";

        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.addQueryParameter("code", code);

        return flexibleSearchService.<StadiumModel> search(query).getResult();
    }
}