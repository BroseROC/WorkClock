package com.broseadventures.workclock;

public class Job {
	private String name;
	private double hourlyRate;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}

	public void setHourlyRate(double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

	public Job(String name, double hourlyRate) {
		this.name = name;
		this.hourlyRate = hourlyRate;
	}

	public String toString() {
		return name + " @ " + hourlyRate;
	}

}
