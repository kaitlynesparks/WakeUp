package edu.cmu.WakeUp;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * Main activity for application. Displays weather and bus information. Gear in top right corner will take you to preference page to 
 * set bus and location info. When you come back, the info will update. Edit button next to alarm will take you to activity to 
 * set alarm and turn it on/off. When you come back, the info will update
 *
 */
public class HomeActivity extends Activity {
	
	//values sent to other activities to know where results came from
	private final int SET_PREFERENCES = 1;
	private final int SET_ALARM = 2;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        //call SetAlarmActivity when alarm edit button is pressed
		Button searchBtn = (Button) findViewById(R.id.editButton);
        searchBtn.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		Intent intent = new Intent(getApplicationContext(), SetAlarmActivity.class);
            	startActivityForResult(intent, SET_ALARM);
        	}
        });
        
        //update screen information
        updateWeatherInfo();
        updateBusInfo();
        updateAlarmInfo();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	switch (requestCode) {
	    	case SET_ALARM :
	    		updateAlarmInfo();
	    		break;
	    	case SET_PREFERENCES :
	    	    updateWeatherInfo();
	    	    updateBusInfo();
	    		break;
	    	default:
	    		break;
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_set_preferences) {
        	Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
        	startActivityForResult(intent, SET_PREFERENCES);
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * 
     * AsyncTask used to call weather API and set results on screen upon 
     * completion. Used so main thread is not making external calls
     * 
     * Takes in a cityCode which corresponds to the WoeId in the Places model
     * indicating the city preference for weather results
     *
     */
    private class CallWeatherService extends AsyncTask<String, Integer, WeatherDetails>
    {
    	
    	protected WeatherDetails doInBackground(String...city) {
    		
    		WeatherDetails results = null;
    		try {
    			YahooProvider provider = new YahooProvider();
    			String cityCode = city[0];
    			results = provider.makeForecastCall(cityCode);
    		} catch (Throwable t) {
    			t.printStackTrace();
    		}
    		return results;
    	}
    	
    	protected void onProgressUpdate(Integer...progress) {
    		System.out.println("progress");
    	}
    	
    	protected void onPostExecute(WeatherDetails results) {
    		TextView tv_currentTemp = (TextView) findViewById(R.id.currentTemp);
    		tv_currentTemp.setText(String.valueOf(results.getTemperature()));
    		
    		TextView tv_high = (TextView) findViewById(R.id.high);
    		tv_high.setText(results.getHigh() + " F");
    		
    		TextView tv_low = (TextView) findViewById(R.id.low);
    		tv_low.setText(results.getLow() + " F");
    		
    		TextView tv_condition = (TextView) findViewById(R.id.conditionText);
    		tv_condition.setText(results.getCondition());
    		
    		ImageView iv_weatherIcon = (ImageView) findViewById(R.id.weatherConditionIcon);
    		int drawable = WeatherIconManager.getWeatherIcon(results.getConditionCode());
    		iv_weatherIcon.setImageResource(drawable);
    	}
    }
    
    private class CallBusPredictionService extends AsyncTask<String, Integer, List<Prediction>>
    {
    	protected List<Prediction> doInBackground(String...params) {
    		try {
    			//make predictions call
    			BusProvider provider = new BusProvider();
    			String route = params[0];
    			String stop = params[1];
    			List<Prediction> predictionsResults = provider.makeGetPredictionCall(route, stop);
    			
    			return predictionsResults;
    		} catch (Throwable t) {
    			t.printStackTrace();
    		}
    		return null;
    	}
    	
    	protected void onProgressUpdate(Integer...integers) {
    		System.out.println("progress");
    	}
    	
    	protected void onPostExecute(List<Prediction> predictions) {
    		//get the textfields for the bus times
    		TextView tv1 = (TextView) findViewById(R.id.busTime1);
    		TextView tv2 = (TextView) findViewById(R.id.busTime2);
    		TextView tv3 = (TextView) findViewById(R.id.busTime3);
    		
    		if (predictions.size() > 2) {
    			//split time
    			String[] split = predictions.get(2).getPredictedTime().split(" ");
    			tv3.setText(convert24hrTo12hr(split[1]));
    		} else {
    			tv3.setText("");
    		}
    		if (predictions.size() > 1) {
    			String[] split = predictions.get(1).getPredictedTime().split(" ");
    			tv2.setText(convert24hrTo12hr(split[1]));
    		} else {
    			tv2.setText("");
    		}
    		if (predictions.size() > 0) {
    			String[] split = predictions.get(0).getPredictedTime().split(" ");
    			tv1.setText(convert24hrTo12hr(split[1]));
    		} else {
    			tv1.setText("Route Offline");
    		}
    	}
    }
        
    private Calendar parseTimeStringToCalendar(String time) {
    	Calendar cal = Calendar.getInstance();
    	String[] timeParts = time.split(":");
    	String[] timeParts2 = timeParts[1].split(" ");
    	// timeParts[0] = hour, timeParts2[0] = min, timeParts2[1] = AM/PM
    	int hour = Integer.parseInt(timeParts[0]);
    	int min = Integer.parseInt(timeParts2[0]);
    
		if (timeParts2[1] == "PM") {
			hour = hour + 12;
		}
		
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.SECOND, 0);
		return cal;
    }
    
    public void setNewAlarm()
   	{
    	//read time from file and convert to calendar from string
		String time = FileManager.readFromFile(this, "AlarmPreference");
		Calendar cal = parseTimeStringToCalendar(time);
			
		//call alarm clock
		Intent intent = new Intent(HomeActivity.this, AlarmActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(HomeActivity.this, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
   	}
    
    private String convert24hrTo12hr(String time) {
    	String result = "";
    	//get hour and minute from string
    	String[] parts = time.split(":");
    	String hour = parts[0];
    	String minute = parts[1];
    	
    	//check if single digit and append 0 to front
    	int minInt = Integer.parseInt(minute);
    	if (minInt < 10 && minute.length() < 2) {
    		minute = "0" + minute;
    	}
    	
    	
    	if (Integer.parseInt(hour) == 0) {
    		result = "12:"+ minute + " AM";
    	}
    	else if (Integer.parseInt(hour) >= 1 && Integer.parseInt(hour) <= 11) {
    		result = hour + ":" + minute + " AM";
    	}
    	else if (Integer.parseInt(hour)  == 12) {
    		result = hour + ":" + minute + " PM";
    	}
    	else {
    		result = (Integer.parseInt(hour) -12) + ":" + minute + " PM";
    	}
    	return result;
    }
    
    private void updateWeatherInfo() {
		String cityName = FileManager.readFromFile(this, FileManager.FileName.LocationPreference);
        String woeId = FileManager.readFromFile(this, FileManager.FileName.WoeIdPreference);
        
        if (woeId != "") {
	        CallWeatherService runner = new CallWeatherService();
	        runner.execute(woeId);
        }
        
        TextView location_tv = (TextView) findViewById(R.id.locationText);
        String[] parts = cityName.split(",");
        location_tv.setText(parts[0]);
        
    }
    
    private void updateBusInfo() {
        String route = FileManager.readFromFile(this, FileManager.FileName.RoutePreference);
        String direction = FileManager.readFromFile(this, FileManager.FileName.DirectionPreference);
        String stopName = FileManager.readFromFile(this,FileManager.FileName.StopNamePreference);
        String stopId = FileManager.readFromFile(this, FileManager.FileName.StopIdPreference);
        
        if (stopId != "" && route !="") {
        	CallBusPredictionService predictionRunner = new CallBusPredictionService();
            predictionRunner.execute(route, stopId);
        }
        
        TextView route_tv = (TextView) findViewById(R.id.busRoute);
        route_tv.setText(route);
        
        TextView direction_tv = (TextView) findViewById(R.id.direction);
        direction_tv.setText(direction);
        
        TextView stop_tv = (TextView) findViewById(R.id.busStop);
        stop_tv.setText(stopName);  
    }
    
    private void updateAlarmInfo() {
    	String alarmTime = FileManager.readFromFile(this, FileManager.FileName.AlarmPreference);
    	String alarmStatusTime = FileManager.readFromFile(this, FileManager.FileName.AlarmStatusPreference);

        TextView alarmTime_tv = (TextView) findViewById(R.id.alarmTime);
        alarmTime_tv.setText(convert24hrTo12hr(alarmTime)); 
        
        TextView alarmStatus_tv = (TextView) findViewById(R.id.alarmStatus);
        alarmStatus_tv.setText(alarmStatusTime);
    }
     
 }
