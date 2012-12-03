package Communication;

import java.util.ArrayList;

public class Cache {
	// ** *********************************************************** ** //
	// ** ** PRIVATE CACHE ENTRY CLASS                             ** ** //
	// ** *********************************************************** ** //
	
	private class CachedObject{
		private String key;
		private Object obj;
		private long time;
		
		CachedObject(String key, Object obj){
			 java.util.Date date = new java.util.Date();
			 this.key = key;
			 this.obj = obj;
			 this.time = date.getTime();
		}
		
		public boolean checkKey(String key){
			return this.key.equals(key);
		}
		
		public boolean checkTimedout(long timeout){
			 java.util.Date date = new java.util.Date();
			 long interval = date.getTime() - this.time;
			 if(interval > timeout) return true;
			 return false;
		}
		
		public Object getObject(){
			return this.obj;
		}
	}
	
	private ArrayList<CachedObject> cachedObjects;
	private long timeout;
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public Cache(long timeout){
		this.timeout = timeout;
		this.cachedObjects = new ArrayList<CachedObject>();
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CACHE MANAGEMENT METHODS                       ** ** //
	// ** *********************************************************** ** //
	
	public void cacheObject(String key, Object obj){
		for(int i=0;i<this.cachedObjects.size();i++){
			if(this.cachedObjects.get(i).checkKey(key)){
				this.cachedObjects.remove(i);
				break;
			}
		}
		this.cachedObjects.add(new CachedObject(key, obj));
	}
	
	public void uncacheObject(String key){
		for(int i=0;i<this.cachedObjects.size();i++){
			if(this.cachedObjects.get(i).checkKey(key)){
				this.cachedObjects.remove(i);
				return;
			}
		}
	}
	
	public Object retrieveObject(String key){
		for(int i=0;i<this.cachedObjects.size();i++){
			CachedObject obj = this.cachedObjects.get(i);
			if(obj.checkKey(key)){
				return this.cachedObjects.get(i).getObject();
			}
		}
		return null;
	}
	
	public boolean checkTimedOut(String key){
		for(int i=0;i<this.cachedObjects.size();i++){
			CachedObject obj = this.cachedObjects.get(i);
			if(obj.checkKey(key)){
				return obj.checkTimedout(this.timeout);
			}
		}
		return true;
	}
}
