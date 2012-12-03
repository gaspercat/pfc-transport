package Communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import Type.TypeCoordinates;
import Type.TypeAddress;
import Type.TypeDirections;

public class GeofeedGoogleMaps extends Geofeed{
	private static final String QUERY_COORDINATES = "http://maps.googleapis.com/maps/api/geocode/json";
	private static final String QUERY_DIRECTIONS = "http://maps.googleapis.com/maps/api/directions/json";
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC QUERY METHODS                                  ** ** //
	// ** *********************************************************** ** //
	
	public TypeCoordinates getAddressCoordinates(TypeAddress directions){
		// Make request
		String request = QUERY_COORDINATES + "?address=" + this.encodeDirections(directions) + "&sensor=false";
        JsonParser.JsonNode parsed = this.retrieveRequestData(request);
        if(parsed == null){
        	// If couldn't retrieve data
        	System.out.println("ERROR! Data retrieval failed!");
        	return null;
        }
		
		// Obtain location coordinates
		parsed = parsed.getNodeAttribute("results", 0);
		parsed = parsed.getNodeAttribute("geometry", 0);
		parsed = parsed.getNodeAttribute("location", 0);
		float latitude = parsed.getFloatAttribute("lat", 0);
		float longitude = parsed.getFloatAttribute("lng", 0);
		
		// Return coordinates
		return new TypeCoordinates(latitude, longitude);
	}
	
	public TypeAddress getCoordinatesAddress(TypeCoordinates coordinates){
		// Make request
		String request = QUERY_COORDINATES + "?latlng=" + this.encodeCoordinates(coordinates) + "&sensor=false";
        JsonParser.JsonNode parsed = this.retrieveRequestData(request);
        if(parsed == null){
        	// If couldn't retrieve data
        	System.out.println("ERROR! Data retrieval failed!");
        	return null;
        }
		
		// Prepare return structure
		TypeAddress ret = new TypeAddress();
		
		// Obtain location address
		parsed = parsed.getNodeAttribute("results", 0);
		int num = parsed.countAttributeValues("address_components");
		for(int i=0;i<num;i++){
			JsonParser.JsonNode attr = parsed.getNodeAttribute("address_components", i);
			String value = attr.getStringAttribute("long_name", 0);
			String type = attr.getStringAttribute("types", 0);
			if(type.equals("street_number")){
				ret.setNumber(Integer.parseInt(value));
			}else if(type.equals("route")){
				ret.setStreet(value);
			}else if (type.equals("locality")){
				ret.setCity(value);
			}else if (type.equals("country")){
				ret.setState(value);
			}else if (type.equals("postal_code")){
				ret.setPostcode(Integer.parseInt(value));
			}
		}
		
		// Return coordinates
		return ret;
	}
	
	public TypeDirections getRoute(TypeCoordinates origin, TypeCoordinates destination){
		// Make request
		String request = QUERY_DIRECTIONS + "?origin=" + this.encodeCoordinates(origin) + "&destination=" + this.encodeCoordinates(destination) + "&mode=driving&units=metric&sensor=false";
        JsonParser.JsonNode parsed = this.retrieveRequestData(request);
        if(parsed == null){
        	// If couldn't retrieve data
        	System.out.println("ERROR! Data retrieval failed!");
        	return null;
        }
		
		// Prepare parsing variables
		JsonParser.JsonNode attr = null;
		JsonParser.JsonNode param = null;
		
		// Prepare return structure
		TypeDirections ret = new TypeDirections();
		
		// Obtain route
		parsed = parsed.getNodeAttribute("routes", 0);
		parsed = parsed.getNodeAttribute("legs", 0);
		
		// Add start point
		attr = parsed.getNodeAttribute("start_location", 0);
		float latitude = attr.getFloatAttribute("lat", 0);
		float longitude = attr.getFloatAttribute("lng", 0);
		ret.addWaypoint(new TypeCoordinates(latitude, longitude), 0, 0);
		
		// Add waypoints
		int num = parsed.countAttributeValues("steps");
		for(int i=0;i<num;i++){
			attr = parsed.getNodeAttribute("steps", i);
			
			// Get endpoint coordinates
			param = attr.getNodeAttribute("end_location", 0);
			float lat = param.getFloatAttribute("lat", 0).floatValue();
			float lng = param.getFloatAttribute("lng", 0).floatValue();
			TypeCoordinates coords = new TypeCoordinates(lat, lng);
			
			// Get step distance
			param = attr.getNodeAttribute("distance", 0);
			int distance = param.getIntegerAttribute("value", 0);
			
			// Get step time
			param = attr.getNodeAttribute("duration", 0);
			int duration = param.getIntegerAttribute("value", 0);
			
			// Add step to return structure
			ret.addWaypoint(coords, distance, duration);
		}
		
		// Return coordinates
		return ret;
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE FORMATTING METHODS                            ** ** //
	// ** *********************************************************** ** //
	
	private String encodeDirections(TypeAddress directions){
		try{
			String ret = directions.getStreet() + ", " + String.valueOf(directions.getNumber()) + ", " + String.valueOf(directions.getPostcode() + " " + String.valueOf(directions.getCity()) + ", " + String.valueOf(directions.getState()));
			return java.net.URLEncoder.encode(ret, "UTF-8");
		}catch(UnsupportedEncodingException e){
			return null;
		}
	}
	
	private String encodeCoordinates(TypeCoordinates coordinates){
		try{
			String ret = coordinates.getLatitude() + "," + coordinates.getLongitude();
			return java.net.URLEncoder.encode(ret, "UTF-8");
		}catch(UnsupportedEncodingException e){
			return null;
		}
	}
	
    private JsonParser.JsonNode retrieveRequestData(String request){
    	JsonParser.JsonNode ret = null;
    	
		// If not present or not updated, try to make the request
		if(GeofeedGoogleMaps.cache.checkTimedOut(request)){
			try{
				// Open connection
				URL url = new URL(request);
				URLConnection conn = (URLConnection)url.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				// Read response
				String response = "", line;
				while ((line = in.readLine()) != null){
					response = response + line;
				}
				in.close();
				
				// Parse Json object
				JsonParser parser = new JsonParser();
				ret = parser.parseString(response);
				GeofeedGoogleMaps.cache.cacheObject(request, ret);
			}catch(Exception e){}
		}
		
		// Retrieve object from cache if updated or request failed
		if(ret == null){
			ret = (JsonParser.JsonNode)GeofeedGoogleMaps.cache.retrieveObject(request);
		}
        
		return ret;
    }
}
