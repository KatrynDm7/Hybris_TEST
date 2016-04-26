/**
 * 
 */
package de.hybris.platform.cuppy.data;

import de.hybris.platform.cuppy.data.BetGeneratorHelper.Mode;
import de.hybris.platform.cuppy.data.BetGeneratorHelper.Type;

import java.util.Random;

import org.junit.Ignore;


/**
 * @author andreas.thaler
 * 
 */
@Ignore
public class BetGenerator
{
	private final static Mode MODE = Mode.L12011_NULL;
	private static Random rand = new Random();

	public static void main(final String[] args)
	{
		System.out.println("INSERT_UPDATE MatchBet;player(uid)[unique=true];match(id,group(code,competition(code[default=" //NOPMD
				+ MODE.name + "])))[unique=true];guestGoals;homeGoals");
		for (int i = 0; i < MODE.players.length; i++)
		{
			if (MODE.type == Type.TOURNAMENT)
			{
				for (int j = MODE.start; j < MODE.start + MODE.count; j++)
				{
					if (placedBet())
					{
						final int matchnumber = j + 1;
						System.out.println(";" + MODE.players[i] + ";" + matchnumber + ":" + MODE.groups[j - MODE.start] + ";"
								+ getGoals() //NOPMD
								+ ";" + getGoals() + ";"); //NOPMD
					}
				}
			}
			else
			{
				for (int j = MODE.start; j < MODE.start + MODE.count; j++)
				{
					if (placedBet())
					{
						final int matchnumber = j + 1;
						System.out.println(";" + MODE.players[i] + ";" + matchnumber + ":"
								+ MODE.groups[j / (MODE.count / MODE.groups.length)] + ";" + getGoals() //NOPMD
								+ ";" + getGoals() + ";"); //NOPMD
					}
				}
			}
		}
	}

	private static int getGoals()
	{
		if (isNullPlayer())
		{
			return 0;
		}
		return rand.nextInt(6);
	}

	private static boolean placedBet()
	{
		if (isNullPlayer())
		{
			return true;
		}

		final int hit = rand.nextInt(20);
		return hit != 5;
	}

	private static boolean isNullPlayer()
	{
		return MODE.players.length == 1 && MODE.players[0].equals("null");
	}
}
