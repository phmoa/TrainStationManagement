package com.projet.train.test;

import java.awt.Choice;
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

public class TestApp {

	public static void main(String args[]) throws FileNotFoundException {

		// Scanner input = new Scanner(System.in);
		// File text = new File(input.nextLine());
		File text = new File("C:\\Users\\bborchani\\Desktop\\Work\\testIn.txt"); // change dynamic !!!!!

		// Verification input (mod2) a devlopper

		List<Customer> listCustumers = getAllCustomers(text);
		List<Station> listStations = getAllStationsInformations(text);

		List<Trip> listTrips = new ArrayList<Trip>();
		Map<Integer, List<Station>> listStationsBycustomer = getStationsByCustomer(listStations);
		// construct all trips by custumer

		boolean isStationStartSetted = false;
		boolean isStationEndSetted = false;
		Set<Integer> keyCustomer = listStationsBycustomer.keySet();
		for (int key : keyCustomer) {
			List<Station> liststations = listStationsBycustomer.get(key);
			char stationStart = ' ';
			char stationEnd = ' ';
			for (int i = 0; i < liststations.size(); i++) {

				if (isStationStartSetted && !isStationEndSetted) {
					stationStart = liststations.get(i).getIdStation();
					isStationEndSetted = true;
				}
				if (!isStationStartSetted && !isStationEndSetted) {
					stationEnd = liststations.get(i).getIdStation();
					isStationStartSetted = true;
				}
				if (isStationStartSetted && isStationEndSetted) {
					Trip t = new Trip(key, stationStart, stationEnd);
					listTrips.add(t);
					isStationStartSetted = false;
					isStationEndSetted = false;
				}
			}
		}
		Map<Integer, List<Trip>> listtrips = listTrips.stream().collect(Collectors.groupingBy(Trip::getCustumerId));

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

	// get all customers stations
	
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

	// get stations By Zone
}
