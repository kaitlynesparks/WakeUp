package edu.cmu.WakeUp;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/***
 * 
 * Provider class to call the Pittsburgh bus APIs and parse results into object Lists
 * Uses developer key provided
 * Calls 5 APIs: time (get server time), routes, directions, stops, predictions
 *
 */
public class BusProvider {
	public static String HOST_NAME = "http://realtime.portauthority.org/bustime/api/v2/";
	public static String key = "KRGhhZMdXX9hFSxbNUPMdSfcv";
	
	//make call to gettime API and parse the XML results into a string
	public String makeGetTimeCall()
	{
		HttpURLConnection busTimeConn = null;
		String time = "";
		try
		{
			String query = makeGetTimeQueryString();
			busTimeConn = (HttpURLConnection) (new URL(query)).openConnection();
			busTimeConn.connect();
			
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new InputStreamReader(busTimeConn.getInputStream()));
			int event = parser.getEventType();
			
			String tagName = null;
			String currentTag = null;
			while (event != XmlPullParser.END_DOCUMENT)
			{
				tagName = parser.getName();
				if (event == XmlPullParser.START_TAG) 
				{
					currentTag = tagName;
				}
				else if (event == XmlPullParser.TEXT)
				{
					if (currentTag.equals("tm"))
					{
						time = parser.getText();
					}
				}	
				event = parser.next();
			}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			try
			{
				busTimeConn.disconnect();
			}
			catch (Throwable ignore) {}
		}
		
		return time;
	}
	
	//make call to getroute API and parse the XML results into a list of routes
	public List<Routes> makeRoutesCall()
	{
		List<Routes> results = new ArrayList<Routes>();
		HttpURLConnection busTimeConn = null;
		
		try
		{
			String query = makeGetRoutesQueryString();
			busTimeConn = (HttpURLConnection) (new URL(query)).openConnection();
			busTimeConn.connect();
			
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new InputStreamReader(busTimeConn.getInputStream()));
			int event = parser.getEventType();
			
			String tagName = null;
			String currentTag = null;
			Routes route = null;
			boolean firstPass = true;
			
			while (event != XmlPullParser.END_DOCUMENT)
			{
				tagName = parser.getName();
				if (event == XmlPullParser.START_TAG) 
				{
					if (tagName.equals("route"))
					{
						route = new Routes();
					}
					currentTag = tagName;
				}
				else if (event == XmlPullParser.TEXT)
				{
					if ("rt".equals(currentTag))
					{
						if (firstPass)
						{
							String test = parser.getText();
							route.setRoute(test);
						}
						firstPass = !firstPass;
					}
					else if ("rtnm".equals(currentTag))
					{
						if (firstPass)
						{
							route.setRouteName(parser.getText());
						}
						firstPass = !firstPass;
					}
				}
				else if (event == XmlPullParser.END_TAG)
				{
					if ("route".equals(tagName))
					{
						results.add(route);
					}
				}
				
				event = parser.next();
			}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			try
			{
				busTimeConn.disconnect();
			}
			catch (Throwable ignore) {}
		}
		
		return results;	
	}
	
	//make call to getdirection API and parse the XML results into a list of strings
	public List<String> makeGetDirectionsCall(String route) {
		List<String> results = new ArrayList<String>();
		String direction = "";
		HttpURLConnection busTimeConn = null;
		try
		{
			String query = makeGetDirectionQueryString(route);
			busTimeConn = (HttpURLConnection) (new URL(query)).openConnection();
			busTimeConn.connect();
			
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new InputStreamReader(busTimeConn.getInputStream()));
			int event = parser.getEventType();
			
			String tagName = null;
			String currentTag = null;
			while (event != XmlPullParser.END_DOCUMENT)
			{
				tagName = parser.getName();
				if (event == XmlPullParser.START_TAG) 
				{
					currentTag = tagName;
				}
				else if (event == XmlPullParser.TEXT)
				{
					if (currentTag.equals("dir"))
					{
						direction = parser.getText();
					}
				}
				else if (event == XmlPullParser.END_TAG)
				{
					if ("dir".equals(tagName))
					{
						results.add(direction);
					}
				}
				event = parser.next();
			}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			try
			{
				busTimeConn.disconnect();
			}
			catch (Throwable ignore) {}
		}
		
		return results;
	}
	
	//make call to getstops API and parse the XML results into a list of stops
	public List<Stop> makeGetStopsCall(String route, String dir) {
		List<Stop> results = new ArrayList<Stop>();
		HttpURLConnection busTimeConn = null;
		
		try
		{
			String query = makeGetStopsQueryString(route, dir);
			busTimeConn = (HttpURLConnection) (new URL(query)).openConnection();
			busTimeConn.connect();
			
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new InputStreamReader(busTimeConn.getInputStream()));
			int event = parser.getEventType();
			
			String tagName = null;
			String currentTag = null;
			Stop stop = null;
			boolean firstPass = true;

			// original try that worked on stub but doesn't work now
			
			while (event != XmlPullParser.END_DOCUMENT)
			{
				tagName = parser.getName();
				if (event == XmlPullParser.START_TAG) 
				{
					if (tagName.equals("stop"))
					{
						stop = new Stop();
						firstPass = true;
					}
					currentTag = tagName;
				}
				else if (event == XmlPullParser.TEXT)
				{
					if ("stpid".equals(currentTag))
					{
						if (firstPass)
						{
							String test = parser.getText();
							stop.setStopId(test);
						}
						firstPass = !firstPass;
					}
					else if ("stpnm".equals(currentTag))
					{
						if (firstPass)
						{
							String test2 = parser.getText();
							stop.setStopName(test2);
						}
						firstPass = !firstPass;
					}
					else if ("lat".equals(currentTag))
					{
						if (firstPass)
						{
							stop.setLatitude(parser.getText());
						}
						firstPass = !firstPass;
						
					}
					else if ("lon".equals(currentTag))
					{
						if (firstPass)
						{
							stop.setLongitude(parser.getText());
						}
						firstPass = !firstPass;
					}
				}
				else if (event == XmlPullParser.END_TAG)
				{
					if ("stop".equals(tagName))
					{
						results.add(stop);
					}
				}
				
				event = parser.next();
			}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			try
			{
				busTimeConn.disconnect();
			}
			catch (Throwable ignore) {}
		}
		
		return results;	
	}
	
	//make call to getpredictions API and parse the XML results into a list of predictions
	public List<Prediction> makeGetPredictionCall(String route, String stopId) {
		List<Prediction> results = new ArrayList<Prediction>();
		HttpURLConnection busTimeConn = null;
		
		try
		{
			String query = makeGetPredictionsQueryString(route, stopId);
			busTimeConn = (HttpURLConnection) (new URL(query)).openConnection();
			busTimeConn.connect();
			
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new InputStreamReader(busTimeConn.getInputStream()));
			int event = parser.getEventType();
			
			String tagName = null;
			String currentTag = null;
			Prediction prediction = null;
			boolean firstPass = true;
			
			while (event != XmlPullParser.END_DOCUMENT)
			{
				tagName = parser.getName();
				if (event == XmlPullParser.START_TAG) 
				{
					if (tagName.equals("prd"))
					{
						prediction = new Prediction();
					}
					currentTag = tagName;
				}
				else if (event == XmlPullParser.TEXT)
				{
					if ("tmstmp".equals(currentTag)) {
						if (firstPass) {
							prediction.setTimeStamp(parser.getText());
						}
						firstPass = !firstPass;
					}
					else if ("typ".equals(currentTag)) {
						prediction.setType(parser.getText());
					}
					else if ("stpid".equals(currentTag)) {
						if(firstPass){
							prediction.setStopId(parser.getText());
						}
						firstPass = !firstPass;
						
					}
					else if ("stpnm".equals(currentTag)) {
						if (firstPass) {
							prediction.setStopName(parser.getText());
						}
						firstPass = !firstPass;
					}
					else if("vid".equals(currentTag)) {
						if (firstPass) {
							prediction.setVehicleId(parser.getText());
						}
						firstPass = !firstPass;
					}
					else if("dstp".equals(currentTag)) {
						if (firstPass) {
							prediction.setDistanceFromStop(parser.getText());
						}
						firstPass = !firstPass;
					}
					else if("rt".equals(currentTag)) {
						if (firstPass) {
							prediction.setRoute(parser.getText());
						}
						firstPass = !firstPass;
					}
					else if ("rtdir".equals(currentTag)) {
						if (firstPass) {
							prediction.setDirection(parser.getText());
						}
						firstPass = !firstPass;
					}
					else if("rtdst".equals(currentTag)) {
						if (firstPass) {
							prediction.setDestination(parser.getText());
						}
					}
					else if ("schdtm".equals(currentTag))
					{
						if (firstPass) {
							prediction.setScheduledTime(parser.getText());
						}
						firstPass = !firstPass;
					}
					else if("prdtm".equals(currentTag)) {
						if (firstPass) {
							prediction.setPredictedTime(parser.getText());
						}
						firstPass = !firstPass;
					}
					else if("prdctdn".equals(currentTag)) {
						if (firstPass) {
							prediction.setTimeUntilArrives(parser.getText());
						}
						firstPass = !firstPass;
					}
					
				}
				else if (event == XmlPullParser.END_TAG)
				{
					if ("prd".equals(tagName))
					{
						results.add(prediction);
					}
				}
				
				event = parser.next();
			}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			try
			{
				busTimeConn.disconnect();
			}
			catch (Throwable ignore) {}
		}
		
		return results;	
		
	}
	
	//get the url to call gettime API
	public String makeGetTimeQueryString() {
		return HOST_NAME + "gettime?key=" + key;
	}
	
	//get the url to call getroutes API
	public String makeGetRoutesQueryString() {
		//example: http://realtime.portauthority.org/bustime/api/v2/getroutes?key=KRGhhZMdXX9hFSxbNUPMdSfcv
		return HOST_NAME + "getroutes?key=" + key;
	}
	
	//get the url to call getdirections API
	public String makeGetDirectionQueryString(String route) {
		return HOST_NAME + "getdirections?key=" + key +"&rt=" + route;
		//example: http://realtime.portauthority.org/bustime/api/v2/getdirections?key=KRGhhZMdXX9hFSxbNUPMdSfcv&rt=75
		//return "http://portauthority.azurewebsites.net/bustime/api/v1/getdirections?key=89dj2he89d8j3j3ksjhdue93j&rt=71D";
	}
	
	//get the url to call getstops API
	public String makeGetStopsQueryString(String route, String dir) {
		// example: http://realtime.portauthority.org/bustime/api/v2/getstops?key=KRGhhZMdXX9hFSxbNUPMdSfcv&rt=71C&dir=INBOUND
		return HOST_NAME  + "getstops?key=" + key + "&rt=" + route +"&dir=" + dir;
		//return "http://portauthority.azurewebsites.net/bustime/api/v1/getstops?key=89dj2he89d8j3j3ksjhdue93j&rt=71D&dir=East%20Bound";
	}
	
	//get the url to call getpredictions API
	public String makeGetPredictionsQueryString(String route, String stopId) {
		return HOST_NAME + "getpredictions?key=" + key +"&rt=" + route + "&stpid=" + stopId;
		//return "http://portauthority.azurewebsites.net/bustime/api/v1/getpredictions?key=89dj2he89d8j3j3ksjhdue93j&rt=71d&stpid=456";
	}
}
