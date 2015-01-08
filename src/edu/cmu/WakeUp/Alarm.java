package edu.cmu.WakeUp;

/***
 * 
 * Data model for alarm 
 * Attributes: Hour, Minute, TimeOfDay
 * 
 */
public class Alarm {
	private int hour;
	private int minute;
	private TimeOfDay timeOfDay;
	
	public enum TimeOfDay { AM, PM, FULL };
	
	Alarm() {};
	Alarm(int hour, int minute, TimeOfDay timeOfDay) {
		this.hour = hour;
		this.minute = minute;
		this.timeOfDay = timeOfDay;
	}
	Alarm(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
		this.timeOfDay = TimeOfDay.FULL;
	}
	
	public int getHour() {
		return this.hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public int getMinute() {
		return this.minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	public TimeOfDay getTimeOfDay() {
		return this.timeOfDay;
	}
	public void setTimeOfDay(TimeOfDay timeOfDay) {
		this.timeOfDay = timeOfDay;
	}
}
