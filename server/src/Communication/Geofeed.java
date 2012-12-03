package Communication;

import Type.TypeCoordinates;
import Type.TypeAddress;
import Type.TypeDirections;

public abstract class Geofeed {
	protected static final long CACHE_TIMEOUT = 1000 * 60 * 60 * 3;
	protected static final Cache cache = new Cache(CACHE_TIMEOUT);
	
	private static Geofeed instance = null;
	
	public static Geofeed getInstance(){
		if(Geofeed.instance == null) Geofeed.instance = (Geofeed)new GeofeedGoogleMaps();
		return Geofeed.instance;
	}

	public abstract TypeCoordinates getAddressCoordinates(TypeAddress directions);
	public abstract TypeAddress getCoordinatesAddress(TypeCoordinates coordinates);
	public abstract TypeDirections getRoute(TypeCoordinates origin, TypeCoordinates desitnation);
}
