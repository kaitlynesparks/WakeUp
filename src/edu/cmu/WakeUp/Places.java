package edu.cmu.WakeUp;

/**
 *
 * Model for the places results that are returned from the YahooProvider 
 * when making the Places API call
 * 
 * Holds WoeId which is a unique identifier for a place,
 * CityName, CountryName, StateName
 *
 */
public class Places {
	private String woeid;
	private String cityName;
	private String countryName;
	private String stateName;
	
	public Places() {}
	
	public Places(String woeid, String cityName, String countryName, String stateName) {
		this.woeid = woeid;
		this.cityName = cityName;
		this.countryName = countryName;
		this.stateName = stateName;
	}
	
	public String getWoeid() {
		return woeid;
	}
	public void setWoeid(String woeid) {
		this.woeid = woeid;
	}
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	public String readableLocation()
	{
		if (this.stateName != null) {
			return this.cityName + ", " + this.stateName;
		}
		else {
			return this.cityName + ", " + this.countryName;
		}
	}

}
