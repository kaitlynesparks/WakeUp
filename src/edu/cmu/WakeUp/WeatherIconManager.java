package edu.cmu.WakeUp;

/***
 * this class just takes in a condition code that is returned from the yahoo weather api and returns the correct drawable 
 * for the weather condition
 *
 */

public class WeatherIconManager {
	
	public static int getWeatherIcon(int conditionCode) {
		int iconCode;
		switch (conditionCode) {
			case 0:
				iconCode = R.drawable.a0;
				break;
			case 1:
				iconCode = R.drawable.a1;
				break;
			case 2:
				iconCode = R.drawable.a2;
				break;
			case 3:
				iconCode = R.drawable.a3;
				break;
			case 4:
				iconCode = R.drawable.a4;
				break;
			case 5:
				iconCode = R.drawable.a5;
				break;
			case 6:
				iconCode = R.drawable.a6;
				break;
			case 7:
				iconCode = R.drawable.a7;
				break;
			case 8:
				iconCode = R.drawable.a8;
				break;
			case 9:
				iconCode = R.drawable.a9;
				break;
			case 10:
				iconCode = R.drawable.a10;
				break;
			case 11:
				iconCode = R.drawable.a11;
				break;
			case 12:
				iconCode = R.drawable.a12;
				break;
			case 13:
				iconCode = R.drawable.a13;
				break;
			case 14:
				iconCode = R.drawable.a14;
				break;
			case 15:
				iconCode = R.drawable.a15;
				break;
			case 16:
				iconCode = R.drawable.a16;
				break;
			case 17:
				iconCode = R.drawable.a17;
				break;
			case 18:
				iconCode = R.drawable.a18;
				break;
			case 19:
				iconCode = R.drawable.a19;
				break;
			case 20:
				iconCode = R.drawable.a20;
				break;
			case 21:
				iconCode = R.drawable.a21;
				break;
			case 22:
				iconCode = R.drawable.a22;
				break;
			case 23:
				iconCode = R.drawable.a23;
				break;
			case 24:
				iconCode = R.drawable.a24;
				break;
			case 25:
				iconCode = R.drawable.a25;
				break;
			case 26:
				iconCode = R.drawable.a6;
				break;
			case 27:
				iconCode = R.drawable.a27;
				break;
			case 28:
				iconCode = R.drawable.a28;
				break;
			case 29:
				iconCode = R.drawable.a29;
				break;
			case 30:
				iconCode = R.drawable.a30;
				break;
			case 31:
				iconCode = R.drawable.a31;
				break;
			case 32:
				iconCode = R.drawable.a32;
				break;
			case 33:
				iconCode = R.drawable.a33;
				break;
			case 34:
				iconCode = R.drawable.a34;
				break;
			case 35:
				iconCode = R.drawable.a35;
				break;
			case 36:
				iconCode = R.drawable.a36;
				break;
			case 37:
				iconCode = R.drawable.a37;
				break;
			case 38:
				iconCode = R.drawable.a38;
				break;
			case 39:
				iconCode = R.drawable.a39;
				break;
			case 40:
				iconCode = R.drawable.a40;
				break;
			case 41:
				iconCode = R.drawable.a41;
				break;
			case 42:
				iconCode = R.drawable.a43;
				break;
			case 43:
				iconCode = R.drawable.a43;
				break;
			case 44:
				iconCode = R.drawable.a44;
				break;
			case 45:
				iconCode = R.drawable.a45;
				break;
			case 46:
				iconCode = R.drawable.a46;
				break;
			case 47:
				iconCode = R.drawable.a47;
				break;
			default:
				iconCode = R.drawable.na;
				break;
		}
		return iconCode;
	}
}
