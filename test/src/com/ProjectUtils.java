package com;

public class ProjectUtils {

	// inputs keys
	public final static String CUSTOMER_ID = "customerId";
	public final static String UNIX_TIME_STAMP = "unixTimestamp";
	public final static String STATION = "station";

	// stations
	public final static char STATION_A = 'A';
	public final static char STATION_B = 'B';
	public final static char STATION_C = 'C';
	public final static char STATION_D = 'D';
	public final static char STATION_E = 'E';
	public final static char STATION_F = 'F';
	public final static char STATION_G = 'G';
	public final static char STATION_H = 'H';
	public final static char STATION_I = 'I';

	//prices
	public final static int PRICE_FOR_ZONE_1_2 = 240;
	public final static int PRICE_FOR_ZONE_3_4 = 200;
	public final static int PRICE_FOR_ZONE_1_2_FROM_TO_ZONE_3 = 280;
	public final static int PRICE_FOR_ZONE_1_2_FROM_TO_ZONE_4 = 300;
	
	//json structor response
	public final static String COMMA = ",";
	public final static String QUOTE = "\"";
	public final static String SLASH_COMMA = "\",";
	public final static String LINE_BREAK = "\n";
	
	public final static String JSON_HEADER = "{{\n\n \"customerSummaries\" : [ {";
	public final static String JSON_CUSTOMER_ID = "\n\n    \"customerId\" : ";
	public final static String JSON_TOTAL_COST_IN_CENT = "\n\n    \"totalCostInCents\" : ";
	public final static String JSON_TRIPS = "\n\n   \"trips\" : [ {\n";
	public final static String JSON_STATION_START = "\n     \"stationStart\" : ";
	public final static String JSON_STATION_END = "\n\n     \"stationEnd\" : ";
	public final static String JSON_STARTED_JOURNEY_AT = "\n\n     \"startedJourneyAt\" :";
	public final static String JSON_COST_IN_CENTS = "\n\n     \"costInCents\" : ";
	public final static String JSON_ZONE_FROM = "\n\n     \"zoneFrom\" : ";
	public final static String JSON_ZONE_TO = "\n\n     \"zoneTo\" : ";
	public final static String JSON_END_TRIP = "\n   } ]";
	public final static String JSON_NEXT_TRIP = "\n   }, { \n";
	public final static String JSON_NEXT_CUSTOMER = "\n\n }, {";
	public final static String JSON_END= "\n\n } ]\n\n}";



}
