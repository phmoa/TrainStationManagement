package com.projet.train.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import com.projet.train.entities.Customer;
import com.projet.train.entities.Station;
import com.projet.train.entities.Trip;
import com.projet.train.entities.Zone;

public class ApplicationMain {

	public static void main(String args[]) throws FileNotFoundException {

		// Scanner input = new Scanner(System.in);
		// File text = new File(input.nextLine());
		File text = new File("C:\\Users\\bborchani\\Desktop\\Work\\testIn.txt"); // change dynamic !!!!!

		// Verification input (mod2) a devlopper

		//List<Customer> listCustumers = getAllCustomers(text);
		List<Station> listStations = getAllStationsInformations(text);

		if (listStations != null) {
			Map<Integer, List<Station>> listStationsBycustomer = getStationsByCustomer(listStations);
			if (listStationsBycustomer != null) {
				Map<Integer, List<Trip>> listtrips = getListTripsByCustomer(listStationsBycustomer);
			int a =0;
			a++;
			}
		}
	}

	//contruct list customer for the output
	
	
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

			for (int i = 0; i < liststations.size(); i++) {

				if (isStationStartSetted && !isStationEndSetted) {
					stationEnd = liststations.get(i).getIdStation();
					isStationEndSetted = true;
				}
				if (!isStationStartSetted && !isStationEndSetted) {
					stationStart = liststations.get(i).getIdStation();
					isStationStartSetted = true;
				}
				if (isStationStartSetted && isStationEndSetted) {
					// get the Zone
					zoneStart = getZoneFromStation(stationStart, stationEnd);
					zoneEnd = getZoneFromStation(stationEnd, stationStart);

					// get the price of the trip
					tripCost = getTripCost(zoneStart, zoneEnd);

					Trip t = new Trip(key, stationStart, stationEnd, tripCost, zoneStart, zoneEnd);
					listTrips.add(t);
					isStationStartSetted = false;
					isStationEndSetted = false;
				}
			}
		}
		return listTrips.stream().collect(Collectors.groupingBy(Trip::getCustumerId));

	}

	// construct list customers
	private static List<Customer> getAllCustomers(File text) throws FileNotFoundException {
		List<Customer> listCustumers = new ArrayList<Customer>();
		Scanner scnr = new Scanner(text);
		while (scnr.hasNextLine()) {
			String line = scnr.nextLine();
			if (line.contains("customerId")) {
				String[] lineSplit = line.split(":");
				listCustumers.add(new Customer(Integer.parseInt(lineSplit[1].replace(" ", "").replace(",", ""))));
			}
		}
		return listCustumers;
	}

	// get list of all stations
	private static List<Station> getAllStationsInformations(File text) throws FileNotFoundException {
		List<Station> listStations = new ArrayList<Station>();
		Scanner scnr = new Scanner(text);
		int index = 0;
		while (scnr.hasNextLine()) {
			String line = scnr.nextLine();

			// Verification input (mod2) a devlopper

			if (line.contains("customerId")) {
				String[] lineSplit = line.split(":");
				listStations.add(new Station(Integer.parseInt(lineSplit[1].replace(" ", "").replace(",", ""))));
			}
			if (line.contains("unixTimestamp")) {

			}
			if (line.contains("station")) {
				String[] lineSplit = line.split(":");
				listStations.get(index).setIdStation(lineSplit[1].charAt(2));
				index++;
			}
		}

		return listStations;
	}

	// get stations by customer
	private static Map<Integer, List<Station>> getStationsByCustomer(List<Station> listStations) {

		return listStations.stream().collect(Collectors.groupingBy(Station::getIdCutomer));

	}

	// get the zone from the station
	private static char getZoneFromStation(char station, char stationEndPoint) {
		switch (station) {
		case 'A':
			return '1';
		case 'B':
			return '1';
		case 'D':
			return '2';
		case 'G':
			return '4';
		case 'H':
			return '4';
		case 'I':
			return '4';
		default:
			return getZone(station, stationEndPoint);
		}
	}

	private static char getZone(char station, char stationEndPoint) {

		String zone1_2 = "ABD";
		String zone3 = "CEF"; // Stations C,E and F are considered in zone 3 for constraints of pricing
		String zone4 = "GHI";

		if (zone4.contains(String.valueOf(stationEndPoint)) && String.valueOf(station).equals("F")) {
			return '4';
		}
		if (zone4.contains(String.valueOf(stationEndPoint)) && !String.valueOf(station).equals("F")) {
			return '3';
		}
		if (zone1_2.contains(String.valueOf(stationEndPoint)) && !String.valueOf(station).equals("F")) {
			return '2';
		}
		if (zone1_2.contains(String.valueOf(stationEndPoint)) && String.valueOf(station).equals("F")) {
			return '3';
		}
		if (zone3.contains(String.valueOf(stationEndPoint))) {
			return '3';
		}

		return '?'; // for the testing to know is there is a condition not treated
	}

	// get the price of the trip
	private static int getTripCost(char zoneStart, char zoneEnd) {

		String zone1_2 = "12";
		String zone3_4 = "34";
		String zone3 = "3";
		String zone4 = "4";

		if (zone1_2.contains(String.valueOf(zoneStart)) && zone1_2.contains(String.valueOf(zoneEnd))) {
			return 240;
		}
		if (zone3_4.contains(String.valueOf(zoneStart)) && zone3_4.contains(String.valueOf(zoneEnd))) {
			return 200;
		}
		if ((zone1_2.contains(String.valueOf(zoneStart)) && zone3.contains(String.valueOf(zoneEnd)))
				|| (zone3.contains(String.valueOf(zoneStart)) && zone1_2.contains(String.valueOf(zoneEnd)))) {
			return 280;
		}
		if ((zone1_2.contains(String.valueOf(zoneStart)) && zone4.contains(String.valueOf(zoneEnd)))
				|| (zone4.contains(String.valueOf(zoneStart)) && zone1_2.contains(String.valueOf(zoneEnd)))) {
			return 300;
		}
		return 0;
	}
}
