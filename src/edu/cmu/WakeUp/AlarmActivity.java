package edu.cmu.WakeUp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/***
 * 
 * This activity is triggered when the alarm goes off. 
 * It starts the alarm sound and calls the weather and bus apis.
 * When the stop button is pressed it goes back to the previous activity
 * and stops the alarm sound.
 *
 */
public class AlarmActivity extends Activity {
	
	private MediaPlayer _mediaPlayer; //used to play music when alarm goes off
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //make it full screen with no title 
        setContentView(R.layout.activity_alarm);
        
        //set alarm to off in the file
        FileManager.writeToFile("Off", this, FileManager.FileName.AlarmStatusPreference);
        //play alarm sound
        playAlarmSound(this, getAlarmUri());
        
        //update information from files and APIs
        updateWeatherInfo();
        updateBusInfo();
        
        //update information other than weather and bus info
        String alarmTime = FileManager.readFromFile(this, FileManager.FileName.AlarmPreference);
        TextView alarmTime_tv = (TextView) findViewById(R.id.alarm_alarmTime);
        alarmTime_tv.setText(convert24hrTo12hr(alarmTime));

        //set button listener to turn off alarm
        Button stopAlarm = (Button) findViewById(R.id.alarm_stop);
        stopAlarm.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		_mediaPlayer.stop(); //stop the sound
        		finish();
        	}
        });
	}
	
	//Async task to call weather API and then set the results on the screen
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
    		TextView tv_currentTemp = (TextView) findViewById(R.id.alarm_currentTemp);
    		tv_currentTemp.setText(String.valueOf(results.getTemperature()));
    		
    		TextView tv_high = (TextView) findViewById(R.id.alarm_high);
    		tv_high.setText(results.getHigh() + "F");
    		
    		TextView tv_low = (TextView) findViewById(R.id.alarm_low);
    		tv_low.setText(results.getLow() + "F");
    		
    		TextView tv_condition = (TextView) findViewById(R.id.alarm_conditionText);
    		tv_condition.setText(results.getCondition());
    		
    		ImageView iv_weatherIcon = (ImageView) findViewById(R.id.alarm_weatherConditionIcon);
			int drawable = WeatherIconManager.getWeatherIcon(results.getConditionCode());
			iv_weatherIcon.setImageResource(drawable);
    	}
    }
	
	//Async task to call Bus API and then set the results on the screen
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
    		TextView tv1 = (TextView) findViewById(R.id.alarm_busTime1);
    		TextView tv2 = (TextView) findViewById(R.id.alarm_busTime2);
    		TextView tv3 = (TextView) findViewById(R.id.alarm_busTime3);
    		
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
	
	//play alarm sound - does not work on emulator
	private void playAlarmSound(Context context, Uri alert) {
		_mediaPlayer = new MediaPlayer();
		try {
			_mediaPlayer.setDataSource(context, alert);
			AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			//example checks for volume here
			_mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			_mediaPlayer.prepare();
			_mediaPlayer.start();
		} catch (IOException e) {
			Log.e("Error", "Error with playing audio for alarm");
		}
	}
	
	//get uri for ringtone first from alarm, then notification, then ringtone
	private Uri getAlarmUri() {
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if (alert == null)
		{
			alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if (alert == null) 
			{
				alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			}
		}
		return alert;
	}

	//APIs return 24 hour time and time picker returns 24 hour time, want to display to user as 12
	private String convert24hrTo12hr(String time) {
		String result = "";
    	//get hour and minute from string
    	String[] parts = time.split(":");
    	String hour = parts[0];
    	String minute = parts[1];
    	
    	//check if single digit and append 0 to front
    	int minInt = Integer.parseInt(minute);
    	if (minInt < 10) {
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
        
        //call service to get weather details
        if (woeId != "") {
	        CallWeatherService runner = new CallWeatherService();
	        runner.execute(woeId);
        }
        
        TextView city_tv = (TextView) findViewById(R.id.alarm_locationText);
        city_tv.setText(cityName);
	}
	
	private void updateBusInfo() {
		String route = FileManager.readFromFile(this, FileManager.FileName.RoutePreference);
        String direction = FileManager.readFromFile(this, FileManager.FileName.DirectionPreference);
        String stopName = FileManager.readFromFile(this,FileManager.FileName.StopNamePreference);
        String stopId = FileManager.readFromFile(this, FileManager.FileName.StopIdPreference);
       
        
        //call service to get bus details
        if (stopId != "" && route !="") {
        	CallBusPredictionService predictionRunner = new CallBusPredictionService();
            predictionRunner.execute(route, stopId);
        }
        
        //populate fields on interface that come from preferences
        
        TextView route_tv = (TextView) findViewById(R.id.alarm_busRoute);
        route_tv.setText(route);
        
        TextView direction_tv = (TextView) findViewById(R.id.alarm_direction);
        direction_tv.setText(direction);
                
        TextView stop_tv = (TextView) findViewById(R.id.alarm_busStop);
        stop_tv.setText(stopName);
        
	}
}
