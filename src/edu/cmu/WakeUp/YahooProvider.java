package edu.cmu.WakeUp;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * 
 * This is the class that calls Yahoo APIs.  It will call and parse the XML for the places API and the forecast api.
 * The forecast api requires a woeId that is returned by the places api for a specific location.
 *
 */
public class YahooProvider {
	
	public static String YAHOO_GEO_URL = "http://where.yahooapis.com/v1/places";
	public static String YAHOO_FORECAST_URL = "http://weather.yahooapis.com/forecastrss";
	public static String APP_ID = "dj0yJmk9bTU0NGI0NXQ4dmpvJmQ9WVdrOVNYaHFPR3gwTTJNbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD05Mw--";
	
	public List<Places> makePlacesCall(String cityName)
	{
		List<Places> result = new ArrayList<Places>();
		HttpURLConnection yahooHttpConn = null;
		try
		{
			String query = makePlacesQueryString(cityName);
			yahooHttpConn = (HttpURLConnection) (new URL(query)).openConnection();
			yahooHttpConn.connect();
			
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new InputStreamReader(yahooHttpConn.getInputStream()));
			int event = parser.getEventType();
			
			Places place = null;
			String tagName = null;
			String currentTag = null;
			
			while (event != XmlPullParser.END_DOCUMENT)
			{
				tagName = parser.getName();
				if (event == XmlPullParser.START_TAG)
				{
					if (tagName.equals("place"))
					{
						place = new Places();
					}
					currentTag = tagName;
				}
				else if (event == XmlPullParser.TEXT) 
				{
					if ("woeid".equals(currentTag))
					{
						place.setWoeid(parser.getText());
					}
					else if ("name".equals(currentTag))
					{
						place.setCityName(parser.getText());
					}
					else if ("country".equals(currentTag))
					{
						place.setCountryName(parser.getText());
					}
					else if ("admin1".equals(currentTag))
					{
						place.setStateName(parser.getText());
					}
				}
				else if (event == XmlPullParser.END_TAG)
				{
					if ("place".equals(tagName))
					{
						result.add(place);
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
				yahooHttpConn.disconnect();
			}
			catch (Throwable ignore) {}
		}
		
		return result;
	}
	
	public WeatherDetails makeForecastCall(String cityCode)
	{
		boolean firstPass = true;
		WeatherDetails result = new WeatherDetails();
		HttpURLConnection yahooHttpConn = null;
		try 
		{
			String query = makeForecastQueryString(cityCode);
			yahooHttpConn = (HttpURLConnection) (new URL(query)).openConnection();
			yahooHttpConn.connect();
			
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new InputStreamReader(yahooHttpConn.getInputStream()));
			int event = parser.getEventType();
			
			String tagName = null;
			
			while (event != XmlPullParser.END_DOCUMENT)
			{
				tagName = parser.getName();
				if (event == XmlPullParser.START_TAG)
				{
					if (tagName.equals("rss"))
					{
						result = new WeatherDetails();
					}
					else if (tagName.equals("yweather:forecast"))
					{
						if (firstPass)
						{
							firstPass = false;
							String high = parser.getAttributeValue(null, "high");
							result.setHigh(Integer.parseInt(high));
							result.setLow(Integer.parseInt(parser.getAttributeValue(null, "low")));
							result.setCondition(parser.getAttributeValue(null, "text"));
							result.setConditionCode(Integer.parseInt(parser.getAttributeValue(null,"code")));
						}
					}
					else if (tagName.equals("yweather:condition")) {
						result.setTemperature(Integer.parseInt(parser.getAttributeValue(null, "temp")));
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
				yahooHttpConn.disconnect();
			}
			catch (Throwable ignore) {}
		}
		
		return result;
	}
	
	private static String makePlacesQueryString(String cityName) {
		return YAHOO_GEO_URL +".q(" + cityName +"%2A);count=10?appid=" + APP_ID;
	}
	
	private static String makeForecastQueryString(String cityCode)
	{
		return YAHOO_FORECAST_URL +"?w=" + cityCode;
		
	}


}
