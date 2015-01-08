package edu.cmu.WakeUp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 
 * Activity to set your preferences for location and bus. You get to this activity by pressing the gear in the top right of the Home Activity.
 * You can return to the Home activity by pressing the back arrow in the top left corner. 
 * When this activity loads it gets all previous presets from files. When a new preference is selected, it is saved to a file. 
 *
 */

public class PreferencesActivity extends Activity {
	
	//hold saved preferences for other calls
	String city;
	String direction;
	String route;
	
	//global variables needed for city preference
	List<Places> placesResults;
	ArrayList<String> cityNamesList = new ArrayList<String>();
	ArrayAdapter<String> arrayAdapter;
	ListView locationList;
	
	//global variables needed for routes preference
	ArrayList<String> routesList = new ArrayList<String>();
	ArrayAdapter<String> routesArrayAdapter;
	ListView routesListView;
	List<Routes> routesResults;
	
	//global variables needed for stop preferences
	ArrayList<String> stopsList = new ArrayList<String>();
	ArrayAdapter<String> stopsArrayAdapter;
	ListView stopsListView;
	List<Stop> stopsResults;
	
	//global variables needed for direction preferences
	ArrayList<String> directionList = new ArrayList<String>();
	ArrayAdapter<String> directionArrayAdapter;
	ListView directionListView;
	List<String> directionResults;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		
		//set up back arrow on action bar
		getActionBar().setTitle("Preferences");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//instantiate ListView, but hide them so they don't take up screen space
		locationList = (ListView) findViewById(R.id.searchResultsList);
		routesListView = (ListView) findViewById(R.id.routesResultsList);
		stopsListView = (ListView) findViewById(R.id.stopResultsList);
		directionListView = (ListView) findViewById(R.id.directionResultsList);
		locationList.setVisibility(View.GONE);
		routesListView.setVisibility(View.GONE);
		stopsListView.setVisibility(View.GONE);
		directionListView.setVisibility(View.GONE);

		//check for previous preferences and populate text fields
		EditText locationET = (EditText) findViewById(R.id.cityInput);
		locationET.setText(getLocationPreference());
		
		EditText routeET = (EditText) findViewById(R.id.routeEditText);
		routeET.setText(getRoutePreference());
		route = getRoutePreference();
		
		EditText stopET = (EditText) findViewById(R.id.stopEditText);
		stopET.setText(getStopPreference());
		
		TextView dirTV = (TextView) findViewById(R.id.directionTextView);
		dirTV.setText(getDirectionPreference());
		direction = getDirectionPreference();
		
		//city search button listener
		Button searchBtn = (Button) findViewById(R.id.searchButton);
        searchBtn.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		hideKeyboard();
        		
        		EditText et = (EditText) findViewById(R.id.cityInput);
        		city = et.getText().toString();
        		
        		cityNamesList.clear(); //clear previous lists before populating a new one
        		
        		//execute async task to get official place with that city
        		PopulateCityList runner = new PopulateCityList();
        		runner.execute(city);
        		
        		locationList.setVisibility(View.VISIBLE);
        	}
        });
        
        //route search button listener
        Button routeSearchBtn = (Button) findViewById(R.id.routeSearchBtn);
        routeSearchBtn.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		hideKeyboard();
        		
        		EditText et = (EditText) findViewById(R.id.routeEditText);
        		String route = et.getText().toString();
        		
        		routesList.clear(); //clear previous lists before populating a new one
        		
        		//execute async task to get routes back
        		PopulateRouteList runner = new PopulateRouteList();
        		runner.execute(route);
        		
        		routesListView.setVisibility(View.VISIBLE);
        	}	
        });
        
        //stops search button listener
        Button stopsSearchBtn = (Button) findViewById(R.id.stopSearchBtn);
        stopsSearchBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideKeyboard();
				
				EditText et = (EditText) findViewById(R.id.stopEditText);
				String stop = et.getText().toString();
				
				stopsList.clear(); //clear previous lists before populating a new one
				
				//execute async task to get stops back
				PopulateStopsList runner = new PopulateStopsList();
				runner.execute(stop);
				
				stopsListView.setVisibility(View.VISIBLE);
			}
		});
        
        //direction search button listener
        Button directionSearchBtn = (Button) findViewById(R.id.directionSearchBtn);
        directionSearchBtn.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick (View v) {
        		directionList.clear(); //clear previous lists before populating a new one
        		
        		PopulateDirectionsList runner = new PopulateDirectionsList();
        		runner.execute();
        		
        		directionListView.setVisibility(View.VISIBLE);
        	}
        });
        
        //select location from list, save it, and add it to the text view
        locationList.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
        		String selectedWOEID = placesResults.get(position).getWoeid();
        		String selectedLocation = placesResults.get(position).readableLocation();
        		
        		try {
        			//save woeid to file - need WOEID to call actual weather api
					FileManager.writeToFile(selectedWOEID, PreferencesActivity.this, FileManager.FileName.WoeIdPreference);
        			
        			//save location string to file
					FileManager.writeToFile(selectedLocation, PreferencesActivity.this, FileManager.FileName.LocationPreference);
        			
        			//set the editable view to the selected string
        			EditText et = (EditText) findViewById(R.id.cityInput);
        			et.setText(placesResults.get(position).readableLocation());
        			
        			//hide the list view once an item has been selected
        			locationList.setVisibility(View.GONE);
        			
        		} catch(Throwable t) {
        			Log.d("Error", "File write error");
        		}
        	}
        });
	
        //select bus route from list, save it, and add it to the text view
		routesListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				String selectedRoute = routesResults.get(position).getRoute();
				
				try {
					FileManager.writeToFile(selectedRoute, PreferencesActivity.this, FileManager.FileName.RoutePreference);
					
					//set the editable view to the selected string
					EditText et = (EditText) findViewById(R.id.routeEditText);
					et.setText(routesResults.get(position).getRoute());
					
					//hide the list view once an item has been selected
					routesListView.setVisibility(View.GONE);
					
					//set global variable for selected route
					route = selectedRoute;
				} catch(Throwable t) {
					Log.d("Error", "File write error");
				}
			}
		});
		
		//select stop from list, save it, and add it to the text view
		stopsListView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				String selectedStopName = stopsResults.get(position).getStopName();
				String selectedStopId = stopsResults.get(position).getStopId();
				
				try {
					//save stop name to file
					FileManager.writeToFile(selectedStopName, PreferencesActivity.this, FileManager.FileName.StopNamePreference);
					
					//save stopid string to file - need stop id to pass to other APIs
					FileManager.writeToFile(selectedStopId, PreferencesActivity.this, FileManager.FileName.StopIdPreference);
						
					//set the editable view to the selected string
					EditText et = (EditText) findViewById(R.id.stopEditText);
					et.setText(stopsResults.get(position).getStopName());
					
					//hide the list view once an item has been selected
					stopsListView.setVisibility(View.GONE);
					
				} catch(Throwable t) {
					Log.d("Error", "File write error");
				}
			}
		});
		
		//select direction route from list, save it, and add it to the text view
		directionListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				String selectedDirection = directionResults.get(position);
				try {
					FileManager.writeToFile(selectedDirection, PreferencesActivity.this, FileManager.FileName.DirectionPreference);
					
					//set the view to the selected string
					TextView tv = (TextView) findViewById(R.id.directionTextView);
					tv.setText(selectedDirection);
					
					//hide the list view once an item has been selected
					directionListView.setVisibility(View.GONE);
					
					//set global variable for selected direction
					direction = selectedDirection;
				} catch(Throwable t) {
					Log.d("Error", "File write error");
				}
			}
		});

	}
	
	//go back to the HomeScreen, pass reload so the screen will reload based on new preferences
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
			{
				Intent homePage = new Intent(getApplicationContext(), HomeActivity.class);
				homePage.putExtra("reload", true);
				setResult(RESULT_OK, homePage);
				finish();
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	//async task to populate the possible cities from the given input string
	private class PopulateCityList extends AsyncTask<String, Integer, List<Places>> 
    {    	
    	protected List<Places> doInBackground(String... city) {
    		try 
    		{
    			//Makes Places Call
	    		YahooProvider provider = new YahooProvider();
	    		String citystring = city[0];
	    		placesResults = provider.makePlacesCall(citystring);
	    		return placesResults;
    		}
    		catch (Throwable t)
    		{
    			t.printStackTrace();
    		}
    		return null;
    	}
    	
    	protected void onProgressUpdate(Integer... progress)
    	{
    		System.out.println("progress");
    	}
    	
    	protected void onPostExecute(List<Places> result)
    	{
    		Log.d("debug","Completed");
    		
    		//build arrayList of city names, states 
    		
    		for (int i = 0; i < result.size(); i++)
    		{
    			String location = result.get(i).readableLocation();
    			cityNamesList.add(location);
    		}
    		
    		//initialize array
    		arrayAdapter = new ArrayAdapter<String>(PreferencesActivity.this, android.R.layout.simple_list_item_1, cityNamesList);
    		locationList.setAdapter(arrayAdapter);
    		Log.d("debug", "Array populated");
    	}
    }
	
	//async task to populate the list of possible routes
	private class PopulateRouteList extends AsyncTask<String, Integer, List<Routes>>
	{
		protected List<Routes> doInBackground(String...route) {
			try {
				//Make Routes Call
				BusProvider provider = new BusProvider();
				String routeString = route[0];
				routesResults = provider.makeRoutesCall();
				List<Routes> filteredResults = new ArrayList<Routes>();
				
				//only add results that contain the input string
				for (int i = 0; i< routesResults.size(); i++) {
					String compare = routesResults.get(i).getRoute().toLowerCase();
					if (compare.contains(routeString.toLowerCase())) {
						filteredResults.add(routesResults.get(i));
					}
				}
				
				//If results match, just return those. Otherwise return all results
				if (filteredResults.size() > 0) {
					routesResults = filteredResults;
				}
				return routesResults;
				
			} catch (Throwable t) {
				t.printStackTrace();
			}
			return null;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			System.out.println("progress");
		}
		
		protected void onPostExecute(List<Routes> results){
			for (int i = 0; i < results.size(); i++) {
				String route = results.get(i).getRoute();
				routesList.add(route);
			}
			routesArrayAdapter = new ArrayAdapter<String>(PreferencesActivity.this, android.R.layout.simple_list_item_1, routesList);
			routesListView.setAdapter(routesArrayAdapter);
		}
	}
	
	//async task to populate the list of possible directions for a route
	private class PopulateDirectionsList extends AsyncTask<String, Integer, List<String>> {
		protected List<String> doInBackground(String...strings) {
			try {
				//Make direction call
				BusProvider provider = new BusProvider();
				directionResults = provider.makeGetDirectionsCall(route);
				
				return directionResults;
				
			} catch (Throwable t) {
				t.printStackTrace();
			}
			return null;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			System.out.println("progress");
		}
		
		protected void onPostExecute(List<String> results) {
			directionArrayAdapter = new ArrayAdapter<String>(PreferencesActivity.this, android.R.layout.simple_list_item_1, results);
			directionListView.setAdapter(directionArrayAdapter);
		}
		
	}
	
	//async task populate the list of stops and filter out the ones requested 
	private class PopulateStopsList extends AsyncTask<String, Integer, List<Stop>> {
		protected List<Stop> doInBackground(String... stop) {
			try {
				//make Stops Call
				BusProvider provider = new BusProvider();
				String stopString = stop[0];
				stopsResults = provider.makeGetStopsCall(route, direction);
				List<Stop> filteredResults = new ArrayList<Stop>();
				
				//only add results that contain the input string
				for (int i = 0; i < stopsResults.size(); i++) {
					String compare = stopsResults.get(i).getStopName().toLowerCase();
					if(compare.contains(stopString.toLowerCase())) {
						filteredResults.add(stopsResults.get(i));
					}
				}
				
				//if results match, just return those. Otherwise return all results
				if(filteredResults.size() > 0) {
					stopsResults = filteredResults;
				}
				
				return stopsResults;
				
			} catch(Throwable t) {
				t.printStackTrace();
			}
			return null;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			System.out.println("progress");
		}
		
		protected void onPostExecute(List<Stop> results) {
			for (int i = 0; i< results.size(); i++) {
				String stop = results.get(i).getStopName();
				stopsList.add(stop);
			}
			
			stopsArrayAdapter = new ArrayAdapter<String>(PreferencesActivity.this, android.R.layout.simple_list_item_1, stopsList);
			stopsListView.setAdapter(stopsArrayAdapter);
		}
	}

	//hide keyboard when an edit text field is not selected so the whole screen is viewable
	private void hideKeyboard() {   
	    // Check if no view has focus:
	    View view = this.getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}

    private String getLocationPreference() {
    	return FileManager.readFromFile(this, FileManager.FileName.LocationPreference);
    }
    
    private String getRoutePreference() {
    	return FileManager.readFromFile(this, FileManager.FileName.RoutePreference);
    }

    private String getStopPreference() {
    	return FileManager.readFromFile(this, FileManager.FileName.StopNamePreference);
    }
    
    private String getDirectionPreference() {
    	return FileManager.readFromFile(this, FileManager.FileName.DirectionPreference);
    }
}
