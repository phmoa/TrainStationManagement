package com.projet.train.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestApp {

	public static void main(String args[]) throws FileNotFoundException {

		//System.out.print("Enter the file name with its location and extension : ");
		//Scanner input = new Scanner(System.in);
		//File text = new File(input.nextLine());
		File text = new File("C:\\Users\\bborchani\\Desktop\\Work\\testIn.txt");


		// Creating Scanner instnace to read File in Java
		Scanner scnr = new Scanner(text);

		// Reading each line of file using Scanner class
		int lineNumber = 1;
		int i =0;
		while (scnr.hasNextLine()) {
			String line = scnr.nextLine();
//			System.out.println("line " + lineNumber + " :" + line);
//			lineNumber++;
			if(line.contains("customerId")) {
				//i++;
				//System.out.println("line " + lineNumber + " :" + line);	
			}
			if(line.contains("unixTimestamp")) {
				//i++;
				//System.out.println("line " + lineNumber + " :" + line);	
			}
			if(line.contains("station")) {
				//i++;
				//System.out.println("line " + lineNumber + " :" + line);	
			}
		}
		System.out.println(i);
	}
}
