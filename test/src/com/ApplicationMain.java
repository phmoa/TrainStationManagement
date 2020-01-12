package com;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;



public class ApplicationMain {

	public static void main(String args[]) {

//		File inFile = null;
//		if (0 < args.length) {
//			inFile = new File(args[0]);
//		} else {
//			System.err.println("Invalid argument");
//			System.exit(0);
//		}
		File inFile = new File("C:\\Users\\bborchani\\Desktop\\Work\\testIn.txt");

		List<Station> listStations = getAllStationsInformations(inFile);

		if (listStations != null) {
			Map<Integer, List<Station>> listStationsBycustomer = getStationsByCustomer(listStations);
			if (listStationsBycustomer != null) {
				Map<Integer, List<Trip>> listtrips = getListTripsByCustomer(listStationsBycustomer);
				if (listtrips != null) {
					List<Customer> listCustumers = new ArrayList<Customer>();
					Set<Integer> keyCustomer = listtrips.keySet();
					for (int key : keyCustomer) {
						List<Trip> listTrip = new ArrayList<Trip>();
						Customer c = new Customer(key, listTrip);
						int costTotal = 0;
						List<Trip> listTrips = listtrips.get(key);
						for (Trip t : listTrips) {
							c.getTrips().add(t);
							costTotal = costTotal + t.getConstInCents();
						}
						c.setTotalConstInCents(costTotal);
						listCustumers.add(c);
					}

					String result = createJsonResponse(listCustumers);

					// Create result file
					Path currentRelativePath = Paths.get("");
					StringBuilder s = new StringBuilder();
					s.append(currentRelativePath.toFile());
					s.append("result");
					URI uri;
					try {
						uri = new URI(s.toString());
						String fileSeparator = System.getProperty("file.separator");
						String absoluteFilePath = uri + fileSeparator + "resullt.txt";
						File file = new File(absoluteFilePath);
						if (file.exists()) {
							file.delete();
						}
						file.createNewFile();
						BufferedWriter writer = null;
						writer = new BufferedWriter(new FileWriter(file));
						writer.write(result);

						writer.close();
					} catch (URISyntaxException e1) {
						System.err.println("Problem occured while reading the file");
						System.exit(0);
					} catch (Exception e) {
						System.err.println(e.toString());
						System.exit(0);
					}

				} else {
					System.err.println("Problem occured while getting trips by customer");
					System.exit(0);
				}
			} else {
				System.err.println("Problem occured while getting stations");
				System.exit(0);
			}
		}

	}

	private static String createJsonResponse(List<Customer> listCustumers) {
		StringBuilder sb = new StringBuilder();
		sb.append(ProjectUtils.JSON_HEADER);
		for (int i = 0; i < listCustumers.size(); i++) {
			sb.append(ProjectUtils.JSON_CUSTOMER_ID).append(listCustumers.get(i).getCustomerId() + ProjectUtils.COMMA);
			sb.append(ProjectUtils.JSON_TOTAL_COST_IN_CENT)
					.append(listCustumers.get(i).getTotalConstInCents() + ProjectUtils.COMMA);
			sb.append(ProjectUtils.JSON_TRIPS);
			for (int j = 0; j < listCustumers.get(i).getTrips().size(); j++) {
				sb.append(ProjectUtils.JSON_STATION_START).append(ProjectUtils.QUOTE
						+ listCustumers.get(i).getTrips().get(j).getStationStart() + ProjectUtils.SLASH_COMMA);
				sb.append(ProjectUtils.JSON_STATION_END).append(ProjectUtils.QUOTE
						+ listCustumers.get(i).getTrips().get(j).getStationEnd() + ProjectUtils.SLASH_COMMA);
				sb.append(ProjectUtils.JSON_STARTED_JOURNEY_AT)
						.append(listCustumers.get(i).getTrips().get(j).getStartedJourneyAt() + ProjectUtils.COMMA);
				sb.append(ProjectUtils.JSON_COST_IN_CENTS)
						.append(listCustumers.get(i).getTrips().get(j).getConstInCents() + ProjectUtils.COMMA);
				sb.append(ProjectUtils.JSON_ZONE_FROM)
						.append(listCustumers.get(i).getTrips().get(j).getZoneFrom() + ProjectUtils.COMMA);
				sb.append(ProjectUtils.JSON_ZONE_TO)
						.append(listCustumers.get(i).getTrips().get(j).getZoneTo() + ProjectUtils.LINE_BREAK);
				if (j == listCustumers.get(i).getTrips().size() - 1) {
					sb.append(ProjectUtils.JSON_END_TRIP);
				} else {
					sb.append(ProjectUtils.JSON_NEXT_TRIP);

				}
			}
			if (i != listCustumers.size() - 1) {
				sb.append(ProjectUtils.JSON_NEXT_CUSTOMER);

			}
		}
		sb.append(ProjectUtils.JSON_END);
		return sb.toString();

	}

	// construct list trips by customer
	private static Map<Integer, List<Trip>> getListTripsByCustomer(Map<Integer, List<Station>> listStationsBycustomer) {

		List<Trip> listTrips = new ArrayList<Trip>();
		boolean isStationStartSetted = false;
		boolean isStationEndSetted = false;
		Set<Integer> keyCustomer = listStationsBycustomer.keySet();
		for (int key : keyCustomer) {
			List<Station> liststations = listStationsBycustomer.get(key);
			char stationStart = ' ';
			char stationEnd = ' ';
			char zoneStart = ' ';
			char zoneEnd = ' ';
			int tripCost = 0;
			String startedJourneyAt = "";

			for (int i = 0; i < liststations.size(); i++) {

				if (isStationStartSetted && !isStationEndSetted) {
					stationEnd = liststations.get(i).getIdStation();
					isStationEndSetted = true;
				}
				if (!isStationStartSetted && !isStationEndSetted) {
					stationStart = liststations.get(i).getIdStation();
					isStationStartSetted = true;
					startedJourneyAt = liststations.get(i).getTimeUnix();
				}
				if (isStationStartSetted && isStationEndSetted) {
					// get the Zone
					zoneStart = getZoneFromStation(stationStart, stationEnd);
					zoneEnd = getZoneFromStation(stationEnd, stationStart);
					try {
						if (zoneStart == ' ' || zoneEnd == ' ') {
							throw new Exception();
						}
					} catch (Exception e) {
						System.err.println("Station unknown");
						System.exit(0);
						return null;
					}

					// get the price of the trip
					tripCost = getTripCost(zoneStart, zoneEnd);
					try {
						if (tripCost == -1) {
							throw new Exception();
						}
					} catch (Exception e) {
						System.err.println("Cost unknown");
						System.exit(0);
						return null;
					}

					Trip t = new Trip(key, stationStart, stationEnd, startedJourneyAt, tripCost, zoneStart, zoneEnd);
					listTrips.add(t);
					isStationStartSetted = false;
					isStationEndSetted = false;
				}
			}
		}
		return listTrips.stream().collect(Collectors.groupingBy(Trip::getCustumerId));

	}

	// get list of all stations
	private static List<Station> getAllStationsInformations(File text) {
		List<Station> listStations = new ArrayList<Station>();
		Scanner scnr;
		try {
			scnr = new Scanner(text);
			int index = 0;
			String time = "";
			while (scnr.hasNextLine()) {
				String line = scnr.nextLine();
				if (line.contains(ProjectUtils.CUSTOMER_ID)) {
					String[] lineSplit = line.split(":");
					try {
						listStations.add(new Station(Integer.parseInt(lineSplit[1].replace(" ", "").replace(",", ""))));
					} catch (java.lang.NumberFormatException nbFormatExcep) {
						System.err.println("invalid customer id\\n");
						System.exit(0);
						return null;

					}
				}
				if (line.contains(ProjectUtils.UNIX_TIME_STAMP)) {
					String[] lineSplit = line.split(":");
					time = lineSplit[1].replaceAll(",", "");
					try {
						Integer.parseInt(time.trim());
					} catch (java.lang.NumberFormatException nbFormatExcep) {
						System.err.println("invalid time format \n");
						System.exit(0);
						return null;
					}
				}
				if (line.contains(ProjectUtils.STATION)) {
					String[] lineSplit = line.split(":");
					try {
						listStations.get(index).setIdStation(lineSplit[1].charAt(2));
					} catch (IndexOutOfBoundsException indexOutExcep) {
						System.err.println("invalid station value \n");
						System.exit(0);
						return null;
					}
					listStations.get(index).setTimeUnix(time);
					index++;
				}
			}

			return listStations;
		} catch (FileNotFoundException e) {
			System.err.println("Problem occured while reading the file");
			System.exit(0);
		}
		return null;
	}

	// get stations by customer
	private static Map<Integer, List<Station>> getStationsByCustomer(List<Station> listStations) {

		return listStations.stream().collect(Collectors.groupingBy(Station::getIdCutomer));

	}

	// get the zone from the station
	private static char getZoneFromStation(char station, char stationEndPoint) {
		switch (station) {
		case ProjectUtils.STATION_A:
			return '1';
		case ProjectUtils.STATION_B:
			return '1';
		case ProjectUtils.STATION_D:
			return '2';
		case ProjectUtils.STATION_G:
			return '4';
		case ProjectUtils.STATION_H:
			return '4';
		case ProjectUtils.STATION_I:
			return '4';
		default:
			return getZone(station, stationEndPoint);
		}
	}

	private static char getZone(char station, char stationEndPoint) {

		String zone1_2 = "ABD";
		String zone3 = "CEF"; // Stations C,E and F are considered in zone 3 for constraints of pricing
		String zone4 = "GHI";
		String stationF = Character.toString(ProjectUtils.STATION_F);
		if (zone4.contains(String.valueOf(stationEndPoint)) && String.valueOf(station).equals(stationF)) {
			return '4';
		}
		if (zone4.contains(String.valueOf(stationEndPoint)) && !String.valueOf(station).equals(stationF)) {
			return '3';
		}
		if (zone1_2.contains(String.valueOf(stationEndPoint)) && !String.valueOf(station).equals(stationF)) {
			return '2';
		}
		if (zone1_2.contains(String.valueOf(stationEndPoint)) && String.valueOf(station).equals(stationF)) {
			return '3';
		}
		if (zone3.contains(String.valueOf(stationEndPoint))) {
			return '3';
		}

		return ' ';
	}

	// get the price of the trip
	private static int getTripCost(char zoneStart, char zoneEnd) {

		String zone1_2 = "12";
		String zone3_4 = "34";
		String zone3 = "3";
		String zone4 = "4";

		if (zoneStart == zoneEnd) {
			return 0;
		}

		if (zone1_2.contains(String.valueOf(zoneStart)) && zone1_2.contains(String.valueOf(zoneEnd))) {
			return ProjectUtils.PRICE_FOR_ZONE_1_2;
		}
		if (zone3_4.contains(String.valueOf(zoneStart)) && zone3_4.contains(String.valueOf(zoneEnd))) {
			return ProjectUtils.PRICE_FOR_ZONE_3_4;
		}
		if ((zone1_2.contains(String.valueOf(zoneStart)) && zone3.contains(String.valueOf(zoneEnd)))
				|| (zone3.contains(String.valueOf(zoneStart)) && zone1_2.contains(String.valueOf(zoneEnd)))) {
			return ProjectUtils.PRICE_FOR_ZONE_1_2_FROM_TO_ZONE_3;
		}
		if ((zone1_2.contains(String.valueOf(zoneStart)) && zone4.contains(String.valueOf(zoneEnd)))
				|| (zone4.contains(String.valueOf(zoneStart)) && zone1_2.contains(String.valueOf(zoneEnd)))) {
			return ProjectUtils.PRICE_FOR_ZONE_1_2_FROM_TO_ZONE_4;
		}

		return -1;
	}

}
