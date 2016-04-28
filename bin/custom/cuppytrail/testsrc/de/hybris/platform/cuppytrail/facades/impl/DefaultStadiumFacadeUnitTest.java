package de.hybris.platform.cuppytrail.facades.impl;

/**
 * @author Ekaterina Dmitruk (Ekaterina.Dmitruk@returnonintelligence.com)
 */
import de.hybris.platform.cuppytrail.StadiumService;
import de.hybris.platform.cuppytrail.data.StadiumData;
import de.hybris.platform.cuppytrail.model.StadiumModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class DefaultStadiumFacadeUnitTest
{
    private DefaultStadiumFacade stadiumFacade;

    private StadiumService stadiumService;

    private final static String STADIUM_NAME = "wembley";
    private final static Integer STADIUM_CAPACITY = Integer.valueOf(12345);

    // Convenience method for returning a list of Stadium
    private List<StadiumModel> dummyDataStadiumList()
    {
        final StadiumModel wembley = new StadiumModel();
        wembley.setCode(STADIUM_NAME);
        wembley.setCapacity(STADIUM_CAPACITY);
        final List<StadiumModel> stadiums = new ArrayList<StadiumModel>();
        stadiums.add(wembley);
        return stadiums;
    }

    // Convenience method for returning a Stadium with code and capacity set for wembley
    private StadiumModel dummyDataStadium()
    {
        final StadiumModel wembley = new StadiumModel();
        wembley.setCode(STADIUM_NAME);
        wembley.setCapacity(STADIUM_CAPACITY);
        return wembley;
    }

    @Before
    public void setUp()
    {
        // We will be testing the POJO DefaultStadiumFacade - the implementation of the StadiumFacade interface.
        stadiumFacade = new DefaultStadiumFacade();

        /**
         * The facade is expected to make calls to an implementation of StadiumService but in this test we want to verify
         * the correct behaviour of DefaultStadiumFacade itself and not also implicitly test the behaviour of a
         * StadiumService. In fact as of writing this class, we do only have the interface StadiumService and no
         * implementation. This requires that we mock out the StadiumService interface. There are several strong arguments
         * for following this practice:
         *
         * If we were to include a real implementation of StadiumService rather than mocking it out..
         *
         * 1) we will not get "false failures" in DefaultStadiumFacade due to errors in the StadiumService implementation.
         * Such errors should be caught in tests that are focusing on StadiumService instead.
         *
         * 2) The condition could arise where an error in the facade gets hidden by a complimentary error in the
         * StadiumService implementation - resulting in a "false positive".
         *
         * By mocking out the interface StadiumService..
         *
         * 3) we do not actually need an implementation of it. This therefore helps us to focus our tests on this POJO
         * before having to implement other POJOs on which it depends - allowing us to write tests early.
         *
         * 4) by focusing on the behaviour of the facade and the interfaces it uses, we are forced to focus also on the
         * those interface, improving them before writing their implementation.
         *
         *
         * Therefore we create a mock of the StadiumService in the next line.
         */
        stadiumService = mock(StadiumService.class);
        // We then wire this service into the StadiumFacade implementation.
        stadiumFacade.setStadiumService(stadiumService);
    }

    /**
     * The aim of this test is to test that:
     *
     * 1) The facade's method getStadiums makes a call to the StadiumService's method getStadiums
     *
     * 2) The facade then correctly wraps StadiumModels that are returned to it from the StadiumService's getStadiums
     * into Data Transfer Objects of type StadiumData.
     */
    @Test
    public void testGetAllStadiums()
    {
        /**
         * We instantiate an object that we would like to be returned to StadiumFacade when the mocked out
         * StadiumService's method getStadiums is called. This will be a list of two StadiumModels.
         */
        final List<StadiumModel> stadiums = dummyDataStadiumList();
        // create wembley stadium for the assert comparison
        final StadiumModel wembley = dummyDataStadium();
        // We tell Mockito we expect StadiumService's method getStadiums to be called, and that when it is, stadiums should be returned
        when(stadiumService.getStadiums()).thenReturn(stadiums);

        /**
         * We now make the call to StadiumFacade's getStadiums. If within this method a call is made to StadiumService's
         * getStadiums, Mockito will return the stadiums instance to it. Mockito will also remember that the call was
         * made.
         */
        final List<StadiumData> dto = stadiumFacade.getStadiums();
        // We now check that dto is a DTO version of stadiums..
        assertNotNull(dto);
        assertEquals(stadiums.size(), dto.size());
        assertEquals(wembley.getCode(), dto.get(0).getName());
        assertEquals(wembley.getCapacity().toString(), dto.get(0).getCapacity());
    }

    @Test
    public void testGetStadium()
    {
        /**
         * We instantiate an object that we would like to be returned to StadiumFacade when the mocked out
         * StadiumService's method getStadium is called. This will be the StadiumModel for wembley stadium.
         */
        // create wembley stadium
        final StadiumModel wembley = dummyDataStadium();

        // We tell Mockito we expect StadiumService's method getStadiumForCode to be called, and that when it is, wembley should be returned
        when(stadiumService.getStadiumForCode("wembley")).thenReturn(wembley);

        /**
         * We now make the call to StadiumFacade's getStadium. If within this method a call is made to StadiumService's
         * getStadium, Mockito will return the wembley instance to it. Mockito will also remember that the call was made.
         */
        final StadiumData stadium = stadiumFacade.getStadium("wembley");
        // We now check that stadium is a correct DTO representation of the ServiceModel wembley
        assertEquals(wembley.getCode(), stadium.getName());
        assertEquals(wembley.getCapacity().toString(), stadium.getCapacity());
    }
}
