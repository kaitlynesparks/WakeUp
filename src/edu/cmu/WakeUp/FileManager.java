package edu.cmu.WakeUp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.util.Log;


public class FileManager {
	
	public enum FileName { LocationPreference, WoeIdPreference, RoutePreference, 
		StopNamePreference, StopIdPreference, DirectionPreference, AlarmPreference, AlarmStatusPreference};
	
	private static String getFileNameFromEnum (FileName file) {
		String fileName;
		switch (file) {
			case LocationPreference :
				fileName = "LocationPreference";
				break;
			case WoeIdPreference: 
				fileName = "WoeIdPreference";
				break;
			case RoutePreference:
				fileName = "RoutePreference";
				break;
			case StopNamePreference:
				fileName = "StopNamePreference";
				break;
			case StopIdPreference:
				fileName = "StopIdPreference";
				break;
			case DirectionPreference:
				fileName = "DirectionPreference";
				break;
			case AlarmPreference:
				fileName = "AlarmPreference";
				break;
			case AlarmStatusPreference:
				fileName = "AlarmStatusPreference";
				break;
			default:
				fileName = "";
		}
		return fileName;
	}
	
	public static String readFromFile(Activity fileActivity, FileName name) {
		String returnValue = readFromFile(fileActivity, getFileNameFromEnum(name));
		if (returnValue == "") {
			returnValue = getDefault(name);
		}
		return returnValue;
	}
	
	public static void writeToFile(String input, Activity fileActivity, FileName name) {
		writeToFile(input, fileActivity, getFileNameFromEnum(name));
	}

	public static void writeToFile(String input, Activity fileActivity, String fileName)
	{
		try {
			File file = fileActivity.getBaseContext().getFileStreamPath(fileName);
			if(!file.exists()) {
				file = new File(fileActivity.getFilesDir(), fileName);
			}
			//FileOutputStream locationFOut = openFileOutput(fileName, Context.MODE_PRIVATE);
			FileOutputStream locationFOut = fileActivity.openFileOutput(fileName, Context.MODE_PRIVATE);
			locationFOut.write(input.getBytes());
			locationFOut.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public static String readFromFile(Activity fileActivity, String fileName)
	{
		String result = "";
    	try {
    		File file = fileActivity.getBaseContext().getFileStreamPath(fileName);
    		if (file.exists()) {
    			FileInputStream fin = fileActivity.openFileInput(fileName);
    			int i;
    			while ((i = fin.read()) != -1) {
    				result = result + Character.toString((char) i);
    			}
    			fin.close();
    		}
    	} catch (Throwable t) {
    		Log.d("Error", "File read error");
    	}
    	return result;
	}
	
	public static String readFromFile(Context context, String fileName)
	{
		String result = "";
    	try {
    		File file = context.getFileStreamPath(fileName);
    		if (file.exists()) {
    			FileInputStream fin = context.openFileInput(fileName);
    			int i;
    			while ((i = fin.read()) != -1) {
    				result = result + Character.toString((char) i);
    			}
    			fin.close();
    		}
    	} catch (Throwable t) {
    		Log.d("Error", "File read error");
    	}
    	return result;
	}

	private static String getDefault(FileName file){
		String defaultValue;
		switch (file) {
			case LocationPreference :
				defaultValue = "Pittsburgh, Pennsylvania";
				break;
			case WoeIdPreference: 
				defaultValue = "2473224";
				break;
			case RoutePreference:
				defaultValue = "71D";
				break;
			case StopNamePreference:
				defaultValue = "Beechwood opp 5th";
				break;
			case StopIdPreference:
				defaultValue = "8310";
				break;
			case DirectionPreference:
				defaultValue = "Inbound";
				break;
			case AlarmPreference:
				defaultValue = "8:00";
				break;
			case AlarmStatusPreference:
				defaultValue = "Off";
				break;
			default:
				defaultValue = "";
				break;
		}
		return defaultValue;
	}
}
