package com.projet.train.entities;

public class Station {

	//fields
	int idCutomer;
	char idStation;

	//constructors
	public Station() {
		super();
	}

	public Station(char idStation) {
		super();
		this.idStation = idStation;
	}


	public Station(int idCutomer) {
		super();
		this.idCutomer = idCutomer;
	}
    //getters & setters
	public int getIdCutomer() {
		return idCutomer;
	}

	public void setIdCutomer(int idCutomer) {
		this.idCutomer = idCutomer;
	}

	public char getIdStation() {
		return idStation;
	}

	public void setIdStation(char idStation) {
		this.idStation = idStation;
	}
	
	

}
