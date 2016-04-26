/**
 * 
 */
package de.hybris.platform.cuppy.data;

import org.junit.Ignore;


/**
 * @author andreas.thaler
 * 
 */
@Ignore
public class BetGeneratorHelper
{
	private final static String[] DEFAULT_PLAYERS =
	{ "sternthaler",//
			"ppetersonson_cuppy",//
			"demo_cuppy",//
			"hweaving_cuppy",//
			"sbrueck_cuppy",//
			"hpneumann_cuppy",//
			"ahertz_cuppy",//
			"abrode_cuppy",//
			"hschweiger_cuppy",//
			"dkaufmann_cuppy",//
			"kvitali_cuppy",//
			"bpoweronoff_cuppy",//
			"tbullet_cuppy",//
			"hjaehnig_cuppy",//
			"mdigit_cuppy",//
			"ovh_cuppy",//
			"nvp_cuppy",//
			"ariel_cuppy",//
			"pp"//
	};

	private final static String[] GROUPS_EC_PREL =
	{ "A",//
			"A",//
			"B",//
			"B",//
			"C",//
			"C",//
			"D",//
			"D",//
			"A",//
			"A",//
			"B",//
			"B",//
			"C",//
			"C",//
			"D",//
			"D",//
			"A",//
			"A",//
			"B",//
			"B",//
			"C",//
			"C",//
			"D",//
			"D" };

	private final static String[] GROUPS_EC_FINAL =
	{ "QUARTER",//
			"QUARTER",//
			"QUARTER",//
			"QUARTER",//
			"SEMI",//
			"SEMI",//
			"FINAL" };

	private final static String[] GROUPS_WC2002_PREL =
	{ "A"//
			, "E"//
			, "A"//
			, "E"//
			, "F"//
			, "B"//
			, "F"//
			, "B"//
			, "G"//
			, "C"//
			, "G"//
			, "C"//
			, "H"//
			, "D"//
			, "H"//
			, "D"//
			, "E"//
			, "A"//
			, "E"//
			, "A"//
			, "F"//
			, "B"//
			, "F"//
			, "B"//
			, "G"//
			, "C"//
			, "G"//
			, "C"//
			, "H"//
			, "D"//
			, "H"//
			, "D"//
			, "A"//
			, "A"//
			, "E"//
			, "E"//
			, "F"//
			, "F"//
			, "B"//
			, "B"//
			, "C"//
			, "C"//
			, "G"//
			, "G"//
			, "H"//
			, "H"//
			, "D"//
			, "D"//
	};

	private final static String[] GROUPS_WC_FINAL =
	{ "LAST16",//
			"LAST16",//
			"LAST16",//
			"LAST16",//
			"LAST16",//
			"LAST16",//
			"LAST16",//
			"LAST16",//
			"QUARTER",//
			"QUARTER",//
			"QUARTER",//
			"QUARTER",//
			"SEMI",//
			"SEMI",//
			"THIRD",//
			"FINAL" };

	private final static String[] GROUPS_WC2010_PREL =
	{ "A",//
			"A",//
			"B",//
			"B",//
			"C",//
			"C",//
			"D",//
			"D",//
			"E",//
			"E",//
			"F",//
			"F",//
			"G",//
			"G",//
			"H",//
			"H",//
			"A",//
			"B",//
			"B",//
			"A",//
			"D",//
			"C",//
			"C",//
			"E",//
			"D",//
			"E",//
			"F",//
			"F",//
			"G",//
			"G",//
			"H",//
			"H",//
			"A",//
			"A",//
			"B",//
			"B",//
			"C",//
			"C",//
			"D",//
			"D",//
			"F",//
			"F",//
			"E",//
			"E",//
			"G",//
			"G",//
			"H",//
			"H" };

	private final static String[] GROUPS_WC2006_PREL =
	{ "A",//
			"A",//
			"B",//
			"B",//
			"C",//
			"C",//
			"D",//
			"D",//
			"E",//
			"E",//
			"F",//
			"F",//
			"G",//
			"G",//
			"H",//
			"H",//
			"A",//
			"A",//
			"B",//
			"B",//
			"C",//
			"C",//
			"D",//
			"D",//
			"E",//
			"E",//
			"F",//
			"F",//
			"G",//
			"G",//
			"H",//
			"H",//
			"A",//
			"A",//
			"B",//
			"B",//
			"C",//
			"C",//
			"D",//
			"D",//
			"E",//
			"E",//
			"F",//
			"F",//
			"G",//
			"G",//
			"H",//
			"H" };

	private final static String[] GROUPS_1L =
	{ "round1",//
			"round2"//
	};

	static enum Type
	{
		TOURNAMENT, LEAGUE;
	}

	public static enum Mode
	{
		WC2002_PREL(0, GROUPS_WC2002_PREL.length, "wc2002", GROUPS_WC2002_PREL, DEFAULT_PLAYERS, Type.TOURNAMENT), //
		WC2002_FINAL(GROUPS_WC2002_PREL.length, GROUPS_WC_FINAL.length, "wc2002", GROUPS_WC_FINAL, DEFAULT_PLAYERS, Type.TOURNAMENT), //
		EC2004_PREL(0, GROUPS_EC_PREL.length, "ec2004", GROUPS_EC_PREL, DEFAULT_PLAYERS, Type.TOURNAMENT), //
		EC2004_FINAL(GROUPS_EC_PREL.length, GROUPS_EC_FINAL.length, "ec2004", GROUPS_EC_FINAL, DEFAULT_PLAYERS, Type.TOURNAMENT), //
		EC2008_PREL(0, GROUPS_EC_PREL.length, "ec2008", GROUPS_EC_PREL, DEFAULT_PLAYERS, Type.TOURNAMENT), //
		EC2008_FINAL(GROUPS_EC_PREL.length, GROUPS_EC_FINAL.length, "ec2008", GROUPS_EC_FINAL, DEFAULT_PLAYERS, Type.TOURNAMENT), //
		WC2006_PREL(0, GROUPS_WC2006_PREL.length, "wc2006", GROUPS_WC2006_PREL, DEFAULT_PLAYERS, Type.TOURNAMENT), //
		WC2006_FINAL(GROUPS_WC2006_PREL.length, GROUPS_WC_FINAL.length, "wc2006", GROUPS_WC_FINAL, DEFAULT_PLAYERS, Type.TOURNAMENT), //
		WC2010_PREL(0, GROUPS_WC2010_PREL.length, "wc2010", GROUPS_WC2010_PREL, DEFAULT_PLAYERS, Type.TOURNAMENT), //
		WC2010_FINAL(GROUPS_WC2010_PREL.length, GROUPS_WC_FINAL.length, "wc2010", GROUPS_WC_FINAL, DEFAULT_PLAYERS, Type.TOURNAMENT), //
		L12010(0, GROUPS_1L.length, "1lger2010", GROUPS_1L, DEFAULT_PLAYERS, Type.LEAGUE), //
		L12011_NULL(0, 306, "1lger2011", GROUPS_1L, new String[]
		{ "null" }, Type.LEAGUE);


		public int count;
		public int start;
		public String name;
		public String[] groups;
		public String[] players;
		public Type type;

		private Mode(final int start, final int count, final String name, final String[] groups, final String[] players,
				final Type type) //NOPMD
		{
			this.start = start;
			this.count = count;
			this.name = name;
			this.groups = groups;
			this.players = players;
			this.type = type;
		}
	}
}
