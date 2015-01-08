package edu.cmu.WakeUp;
/***
 * 
 * Stop Model
 * Contains information that comes back from the bus getstop API
 *
 */
public class Stop {
	private String stopId;
	private String stopName;
	private String latitude;
	private String longitude;
	
	Stop() {}
	
	Stop(String stopId, String stopName, String latitude, String longitude)
	{
		this.stopId = stopId;
		this.stopName = stopName;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	//stopId get and set
	public String getStopId() {
		return this.stopId;
	}
	public void setStopId(String stopId) {
		this.stopId = stopId;
	}
	
	//stopName get and set
	public String getStopName() {
		return this.stopName;
	}
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	
	//latitude get and set
	public String getLatitude() {
		return this.latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	//longitude get and set
	public String getLongitude() {
		return this.longitude;
	}
	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

}
