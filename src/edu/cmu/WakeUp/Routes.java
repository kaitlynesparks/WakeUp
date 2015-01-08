package edu.cmu.WakeUp;

/***
 * 
 * Route Model
 * Holds information returned from the getroute bus api
 * route, routeName
 *
 */
public class Routes {
	private String route;
	private String routeName;
	
	Routes() {}
	
	Routes(String route, String routeName) {
		this.route = route;
		this.routeName = route;
	}
	
	//route get and set
	public String getRoute(){
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	
	//routeName get and set
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
}
