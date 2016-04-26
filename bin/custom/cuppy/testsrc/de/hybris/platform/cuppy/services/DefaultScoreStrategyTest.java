/**
 * 
 */
package de.hybris.platform.cuppy.services;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.cuppy.services.impl.DefaultScoreStrategy;

import org.junit.Before;
import org.junit.Test;


/**
 * @author andreas.thaler
 * 
 */
public class DefaultScoreStrategyTest
{
	private ScoreStrategy scoreStrategy;

	@Before
	public void setUp()
	{
		scoreStrategy = new DefaultScoreStrategy();
	}

	@Test
	public void test3Final()
	{
		assertEquals(6, scoreStrategy.getScore(0, 0, 0, 0, 2));
		assertEquals(6, scoreStrategy.getScore(1, 1, 2, 2, 2));
		assertEquals(6, scoreStrategy.getScore(3, 3, 0, 0, 2));
	}

	@Test
	public void test3NonFinal()
	{
		assertEquals(3, scoreStrategy.getScore(0, 0, 0, 0, 1));
		assertEquals(3, scoreStrategy.getScore(1, 1, 2, 2, 1));
		assertEquals(3, scoreStrategy.getScore(3, 3, 0, 0, 1));
	}

	@Test
	public void test2Final()
	{
		assertEquals(4, scoreStrategy.getScore(0, 2, 4, 6, 2));
		assertEquals(4, scoreStrategy.getScore(1, 6, 0, 5, 2));
		assertEquals(4, scoreStrategy.getScore(2, 0, 2, 0, 2));
	}

	@Test
	public void test2NonFinal()
	{
		assertEquals(2, scoreStrategy.getScore(0, 2, 4, 6, 1));
		assertEquals(2, scoreStrategy.getScore(1, 6, 0, 5, 1));
		assertEquals(2, scoreStrategy.getScore(2, 0, 2, 0, 1));
	}

	@Test
	public void test1Final()
	{
		assertEquals(2, scoreStrategy.getScore(0, 0, 4, 1, 2));
		assertEquals(2, scoreStrategy.getScore(1, 3, 0, 1, 2));
		assertEquals(2, scoreStrategy.getScore(5, 2, 2, 1, 2));
	}

	@Test
	public void test1NonFinal()
	{
		assertEquals(1, scoreStrategy.getScore(0, 0, 4, 1, 1));
		assertEquals(1, scoreStrategy.getScore(1, 3, 0, 1, 1));
		assertEquals(1, scoreStrategy.getScore(5, 2, 2, 1, 1));
	}

	@Test
	public void test0Final()
	{
		assertEquals(0, scoreStrategy.getScore(0, 1, 4, 0, 2));
		assertEquals(0, scoreStrategy.getScore(1, 2, 0, 6, 2));
		assertEquals(0, scoreStrategy.getScore(5, 2, 2, 3, 2));
	}

	@Test
	public void test0NonFinal()
	{
		assertEquals(0, scoreStrategy.getScore(0, 1, 4, 0, 1));
		assertEquals(0, scoreStrategy.getScore(1, 2, 0, 6, 1));
		assertEquals(0, scoreStrategy.getScore(5, 2, 2, 3, 1));
	}
}
