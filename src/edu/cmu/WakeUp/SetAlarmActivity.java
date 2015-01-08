package edu.cmu.WakeUp;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TimePicker;

/***
 * 
 * This activity is used to set an alarm and turn alarms on and off. You get to this activity by pressing the edit button next to alarms on the home screen.
 * You can return to the Home Activity by pressing the back arrow in the upper left corner.
 *
 */

public class SetAlarmActivity extends Activity {
	
	 FileManager fileManager = new FileManager();
	 private int _hourOfDay = -1;
	 private int _minute = -1;
	 
	 Intent _intent;
	 PendingIntent _pi;
	 
	//used for setting text of timePicker to white
	TimePicker time_picker; //Instantiated in onCreate()
	Resources system;
	 
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        
        //set time picker color to white
        time_picker = (TimePicker) findViewById(R.id.alarmTimePicker);
        set_timepicker_text_colour();
        
		//set up back arrow on action bar
		getActionBar().setTitle("Set Alarm Time");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//set state of switch
		String alarmStatus = FileManager.readFromFile(SetAlarmActivity.this, FileManager.FileName.AlarmStatusPreference);
		if ("On".equals(alarmStatus)) {
			Switch alarmSwitch = (Switch) findViewById(R.id.alarmSwitch);
			alarmSwitch.setChecked(true);
		}
		else {
			Switch alarmSwitch = (Switch) findViewById(R.id.alarmSwitch);
			alarmSwitch.setChecked(false);
			setNewAlarm(alarmSwitch);
		}
		
        TimePicker tp = (TimePicker) findViewById(R.id.alarmTimePicker);
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
        	public void onTimeChanged(TimePicker view, int hourOfDay, int minute ) {
        		_hourOfDay = hourOfDay;
        		_minute = minute;
        	}
        });
	}
	
	//go back to the HomeScreen.  Send reload key so that the screen will update with new alarm info
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
			{
				//if time has changed, write it to file before leaving page
				if (_hourOfDay != -1 || _minute != -1) {
					FileManager.writeToFile(_hourOfDay + ":" + _minute, this, "AlarmPreference");
				}
				Intent homePage = new Intent(getApplicationContext(), HomeActivity.class);
        		homePage.putExtra("Reload", true);
        		setResult(RESULT_OK, homePage);
				finish();
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	//this method is called when the switch is clicked. It sends the intents to turn alarms on and off
	public void setNewAlarm(View view) {
		Context context = this.getApplicationContext();
		boolean checked = ((Switch) view).isChecked(); 
		if (checked) {	
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, _hourOfDay);
			calendar.set(Calendar.MINUTE, _minute);
			calendar.set(Calendar.SECOND, 00);
			
			//convert time to string
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			String timeString = timeFormat.format(calendar.getTime());
			
			FileManager.writeToFile(timeString, SetAlarmActivity.this, FileManager.FileName.AlarmPreference);
			FileManager.writeToFile("On", SetAlarmActivity.this, FileManager.FileName.AlarmStatusPreference);
			
			_intent = new Intent(this, AlarmActivity.class);
			_pi = PendingIntent.getActivity(this, 12345, _intent, PendingIntent.FLAG_CANCEL_CURRENT);
			 AlarmManager _am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
			_am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), _pi);
		} 
		else {
			FileManager.writeToFile("Off", SetAlarmActivity.this, FileManager.FileName.AlarmStatusPreference);
			_intent = new Intent(this, AlarmActivity.class);
			_pi = PendingIntent.getActivity(this, 12345, _intent, PendingIntent.FLAG_UPDATE_CURRENT);
			_pi.cancel();
			AlarmManager _am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
			_am.cancel(_pi);
		}
	}

	//Below code to change color of timepicker was copied from
	//http://stackoverflow.com/questions/26015798/change-timepicker-text-color
	private void set_timepicker_text_colour(){
	    system = Resources.getSystem();
	    int hour_numberpicker_id = system.getIdentifier("hour", "id", "android");
	    int minute_numberpicker_id = system.getIdentifier("minute", "id", "android");
	    int ampm_numberpicker_id = system.getIdentifier("amPm", "id", "android");

	    NumberPicker hour_numberpicker = (NumberPicker) time_picker.findViewById(hour_numberpicker_id);
	    NumberPicker minute_numberpicker = (NumberPicker) time_picker.findViewById(minute_numberpicker_id);
	    NumberPicker ampm_numberpicker = (NumberPicker) time_picker.findViewById(ampm_numberpicker_id);

	    set_numberpicker_text_colour(hour_numberpicker);
	    set_numberpicker_text_colour(minute_numberpicker);
	    set_numberpicker_text_colour(ampm_numberpicker);
	}

	private void set_numberpicker_text_colour(NumberPicker number_picker){
	    final int count = number_picker.getChildCount();
	    final int color = getResources().getColor(R.color.text);

	    for(int i = 0; i < count; i++){
	        View child = number_picker.getChildAt(i);

	        try{
	            Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
	            wheelpaint_field.setAccessible(true);

	            ((Paint)wheelpaint_field.get(number_picker)).setColor(color);
	            ((EditText)child).setTextColor(color);
	            number_picker.invalidate();
	        }
	        catch(NoSuchFieldException e){
	            Log.w("setNumberPickerTextColor", e);
	        }
	        catch(IllegalAccessException e){
	            Log.w("setNumberPickerTextColor", e);
	        }
	        catch(IllegalArgumentException e){
	            Log.w("setNumberPickerTextColor", e);
	        }
	    }
	}

}
