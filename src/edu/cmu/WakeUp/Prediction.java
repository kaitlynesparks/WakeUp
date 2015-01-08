package edu.cmu.WakeUp;
/***
 * 
 * Prediction Model
 * Holds information returned from the getpredictions bus API
 *
 */
public class Prediction {
	private String timeStamp;
	private String type;
	private String stopId;
	private String stopName;
	private String vehicleId;
	private String tripId;
	private String distanceFromStop;
	private String route;
	private String direction;
	private String destination;
	private String scheduledTime;
	private String predictedTime;
	private String timeUntilArrives;
	
	Prediction() {}
	Prediction(String timeStamp, String type, String stopId, String stopName, String vehicleId, String tripId,
			String distanceFromStop, String route, String direction, String destination, String scheduledTime, 
			String predictedTime, String timeUntilArrives)
			{
				this.timeStamp = timeStamp;
				this.type = type;
				this.stopId = stopId;
				this.stopName = stopName;
				this.vehicleId = vehicleId;
				this.tripId = tripId;
				this.distanceFromStop = distanceFromStop;
				this.route = route;
				this.direction = direction;
				this.destination = destination;
				this.scheduledTime = scheduledTime;
				this.predictedTime = predictedTime;
				this.timeUntilArrives = timeUntilArrives;
			}
	
	//timeStamp get and set
	public String getTimeStamp() {
		return this.timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	//type get and set
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
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
	
	//vehicleId get and set
	public String getVehicleId() {
		return this.vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	//tripId get and set
	public String getTripId() {
		return this.tripId;
	}
	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	
	//distanceFromStop get and set
	public String getDistanceFromStop() {
		return this.distanceFromStop;
	}
	public void setDistanceFromStop(String distanceFromStop) {
		this.distanceFromStop = distanceFromStop;
	}
	
	//route get and set
	public String getRoute() {
		return this.route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	
	//direction get and set
	public String getDirection() {
		return this.direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	//destination get and set
	public String getDestination() {
		return this.destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	//scheduledTime get and set
	public String getScheduledTime() {
		return this.scheduledTime;
	}
	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
	
	//predictedTime get and set
	public String getPredictedTime() {
		return this.predictedTime;
	}
	public void setPredictedTime(String predictedTime) {
		this.predictedTime = predictedTime;
	}
	
	//timeUntilArrives get and set
	public String getTimeUntilArrives() {
		return this.timeUntilArrives;
	}
	public void setTimeUntilArrives(String timeUntilArrives) {
		this.timeUntilArrives = timeUntilArrives;
	}
}
