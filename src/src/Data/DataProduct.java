package Data;

import java.io.Serializable;

public class DataProduct implements Serializable{
	private static final long serialVersionUID = 1L;

	private int uid;
	private String name;
	private float volume;
	private float weight;
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public DataProduct(String name, float volume, float weight){
		this.name = name;
		this.volume = volume;
		this.weight = weight;
		this.setPersistenceData();
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public int getID(){
		return this.uid;
	}
	
	public String getName(){
		return this.name;
	}
	
	public float getVolume(){
		return this.volume;
	}
	
	public float getWeight(){
		return this.weight;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setVolume(float volume){
		this.volume = volume;
	}
	
	public void setWeight(float weight){
		this.weight = weight;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC PERSISTENCE METHODS                            ** ** //
	// ** *********************************************************** ** //
	
	protected static DataProduct loadObject(int uid){
		Object obj = Persistence.loadJavaObject(DataProduct.class.getName(), uid);
		if(obj != null){
			DataProduct ret = (DataProduct)obj;
			ret.uid = uid;
			return ret;
		}
		
		return null;
	}
	
	protected boolean deleteObject(){
		if(this.uid == -1) return true;
		return Persistence.removeJavaObject(this.getClass().getName(), this.uid);
	}

	// ** *********************************************************** ** //
	// ** ** PRIVATE PERSISTENCE METHODS                           ** ** //
	// ** *********************************************************** ** //
	
	private boolean setPersistenceData(){
		int uid = Persistence.saveJavaObject(this.uid, this);
		if(uid != -1){
			this.uid = uid;
			return true;
		}
		
		return false;
	}
}
