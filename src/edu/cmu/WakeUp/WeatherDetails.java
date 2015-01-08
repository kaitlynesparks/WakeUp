package edu.cmu.WakeUp;

/**
 * 
 * This class defines a model to hold weather details 
 * that are returned from the YahooService class that
 * calls the Yahoo Weather API
 *
 * Stores high temperature, low temperature, and weather condition
 */
public class WeatherDetails {
	private int high;
	private int low;
	private int temperature;
	private String condition;
	private int conditionCode;
	
	WeatherDetails() {}
	
	WeatherDetails(int high, int low, int temperature, String condition, int conditionCode) {
		this.high = high;
		this.low = low;
		this.temperature = temperature;
		this.condition = condition;
		this.conditionCode = conditionCode;
	}
	
	public int getHigh() {
		return this.high;
	}
	public void setHigh(int high) {
		this.high = high;
	}
	
	public int getLow() {
		return this.low;
	}
	public void setLow(int low) {
		this.low = low;
	}
	
	public String getCondition() {
		return this.condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public int getTemperature() {
		return this.temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	
	public int getConditionCode() {
		return this.conditionCode;
	}
	public void setConditionCode(int conditionCode) {
		this.conditionCode = conditionCode;
	}
}
