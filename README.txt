 Activites:
 1. HomeActivity
    - layout: activity_home.xml
    - Explanation: Main activity for the application. Displays the bus information and weather from the latest preferences. Defaults are used for the first time the app is opened. Pressing the gear in the top right corner will take you to the PreferencesActivity.  Pressing the edit button at the bottom in the alarm section will take you to the SetAlarmActivity.
 2. AlarmActivity
    - layout: activity_alarm.xml
    - Explanation: This is the activity that is triggered when the alarm goes off. Alarm sound plays on created. Bus and weather information are displayed.  Stop button will make the alarm stop and go back to the previous screen.
 3. SetAlarmActivity
    - layout: activity_set_alarm.xml
    - Explanation: This activity is where alarms are set and turned on/off. You get to it by pressing the edit button in the alarm clock section of the HomeActivity. Pressing the back arrow in the top left corner will return to the HomeActivity.
 4. PreferencesActivity
    - layout: activity_preferences.xml
    - Explanation: This activity is where you set preferences for bus information and location for weather. You get to it by pressing the gear on the top right of the HomeActivity. Pressing the back arrow in the top left corner will return to the HomeActivity.

Storage:
- All preferences are stored in their own files that can be accessed from any of the activities.  The class "FileManager.java" is used to read and write from this files.

Features:
- Alarm Clock with sound 
- Set alarm time, turn alarm on and off
- Weather Information on home page and shown when alarm goes off
- Bus Information on home page and shown when alarm goes off - up to the three next buses
- Pick Preferences for city (weather), set bus route, bus direction, bus stop

APIs Used:
- APIs for Yahoo called from the class YahooProvider.java
    - http://where.yahooapis.com/v1/places API is used to return the unique identifier for a location
    - http://weather.yahooapis.com/forecastrss API takes the unique id returned from the places call and returns the weather
- APIs for buses called from the class BusProvider.java
    - http://realtime.portauthority.org/bustime/api/v2/getroutes API returns all routes (later filtered by code)
    - http://realtime.portauthority.org/bustime/api/v2/getdirection API takes in the route from the route call and returns the possible directions
    - http://realtime.portauthority.org/bustime/api/v2/getstops takes a route and a direction and returns all stops on the route in that direction
    - http://realtime.portauthority.org/bustime/api/v2/getpredictions takes a route and a stop and returns predictions for when the bus will arrive at the stop

Know bugs (or possible bugs and known unahndled edge cases):
- Only buses with real time predictions are shown. If no bus predictions are available, display will say "Route Offline"
- If a bus route is changed but the stop is not, it will not return anything
- Not sure if the alarm repeats or not. I don't think it does.
- Not well designed for different size phones though main screens do have scrollable views 
- Alarm sound does not work on emulator
- if you set an alarm without changing the timepicker wheel, nothing will happen
- when you return to the set alarm page, timepicker displays current time instead of alarm time, even if an alarm is set. You can still turn off the set alarm with the switch though.
- if you search the location on the preferences page without changing the text, nothing will be returned.  The other searches will work without changing anything, however.

Requirements for app:
- internet for API calls
- mp3 for alarm sound

I created all of the code myself with one exception, but I used a lot of examples from the internet to figure things out.
- The piece of code not mine (it's also commented in the code itself) is used to set a TimePicker text color to white. It came from: http://stackoverflow.com/questions/26015798/change-timepicker-text-color
- Resoures used for alarm clocks:
    - http://www.steventrigg.com/detail-view-layout-create-an-alarm-clock-in-android-tutorial-part-2/
    - http://nerdwin15.com/2013/04/android-creating-an-alarm-with-alarmmanager/
- Resources used for Yahoo APIs
    - http://www.survivingwithandroid.com/2014/02/android-weather-app-tutorial-step-by.html
    - http://android-er.blogspot.com/2012/03/get-weather-info-from-yahoo-weather-rss.html
    - https://developer.yahoo.com/forum/Fantasy-Sports-API/Yahoo-API-for-Android-application/1325574236600-f1a6913c-07c4-4ef8-85d9-a33a6662ba54/