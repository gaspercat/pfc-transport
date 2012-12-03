package Data;

import java.io.Serializable;
import java.util.ArrayList;

import Type.TypeCoordinates;
import Type.TypeAddress;

public class DataLocationDepot extends DataLocation implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Integer> idProducts;    // ID of the products on the depot
	private ArrayList<Integer> qtProducts;    // Quantity of products on the depot
	private ArrayList<Integer> asProducts;    // Quantity of products assigned
	
	private ArrayList<Integer> idTransports;  // ID of the transports on the depot
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public DataLocationDepot(TypeCoordinates coordinates){
		super(coordinates);
		this.idProducts = new ArrayList<Integer>();
		this.qtProducts = new ArrayList<Integer>();
		this.asProducts = new ArrayList<Integer>();
		this.idTransports = new ArrayList<Integer>();
		this.setPersistenceData();
	}
	
	public DataLocationDepot(TypeAddress directions){
		super(directions);
		this.idProducts = new ArrayList<Integer>();
		this.qtProducts = new ArrayList<Integer>();
		this.asProducts = new ArrayList<Integer>();
		this.idTransports = new ArrayList<Integer>();
		this.setPersistenceData();
	}
	
	public DataLocationDepot(float latitude, float longitude){
		super(latitude, longitude);
		this.idProducts = new ArrayList<Integer>();
		this.qtProducts = new ArrayList<Integer>();
		this.asProducts = new ArrayList<Integer>();
		this.idTransports = new ArrayList<Integer>();
		this.setPersistenceData();
	}
	
	public DataLocationDepot(String state, String city, int postcode, String street, int number){
		super(state, city, postcode, street, number);
		this.idProducts = new ArrayList<Integer>();
		this.qtProducts = new ArrayList<Integer>();
		this.asProducts = new ArrayList<Integer>();
		this.idTransports = new ArrayList<Integer>();
		this.setPersistenceData();
	}
	
	public DataLocationDepot(String state, String city, int postcode, String street, int number, int floor, int door){
		super(state, city, postcode, street, number, floor, door);
		this.idProducts = new ArrayList<Integer>();
		this.qtProducts = new ArrayList<Integer>();
		this.asProducts = new ArrayList<Integer>();
		this.idTransports = new ArrayList<Integer>();
		this.setPersistenceData();
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTETS & SETTERS FOR PRODUCTS                 ** ** //
	// ** *********************************************************** ** //
	
	public int getNumProducts(){
		return this.idProducts.size();
	}
	
	public int getProductID(int idx){
		if(idx < 0 || idx >= this.idProducts.size()) return -1;
		return this.idProducts.get(idx).intValue();
	}
	
	public int getProductQuantity(int idx){
		if(idx < 0 || idx >= this.idProducts.size()) return -1;
		return this.qtProducts.get(idx).intValue();
	}
	
	public int getProductAssigned(int idx){
		if(idx < 0 || idx >= this.idProducts.size()) return -1;
		return this.asProducts.get(idx).intValue();
	}
	
	public int gerProductAvailable(int idx){
		if(idx < 0 || idx >= this.idProducts.size()) return -1;
		return this.qtProducts.get(idx).intValue() - this.asProducts.get(idx).intValue();
	}
	
	public void addProduct(int id, int quantity){
		// Check if product already exists and replace if so
		for(int i=0;i<this.idProducts.size();i++){
			if(this.idProducts.get(i).intValue() == id){
				this.qtProducts.remove(i).intValue();
				this.qtProducts.add(i, new Integer(quantity));
				return;
			}
		}
		
		// Add product
		this.idProducts.add(new Integer(id));
		this.qtProducts.add(new Integer(quantity));
	}
	
	public boolean delProduct(int id){
		for(int i=0;i<this.idProducts.size();i++){
			if(this.idProducts.get(i).intValue() == id){
				this.idProducts.remove(i);
				this.qtProducts.remove(i);
				this.asProducts.remove(i);
				return true;
			}
		}
		return false;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTETS & SETTERS FOR TRANSPORTS               ** ** //
	// ** *********************************************************** ** //
	
	public int getNumTransports(){
		return this.idTransports.size();
	}
	
	public int getTransportID(int idx){
		if(idx < 0 || idx >= this.idTransports.size()) return -1;
		return this.idTransports.get(idx).intValue();
	}
	
	public void addTransport(int id){
		// Check if transport already exists
		for(int i=0;i<this.idTransports.size();i++){
			if(this.idTransports.get(i).intValue() == id) return;
		}
		
		// Add transport
		this.idTransports.add(id);
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC PERSISTENCE METHODS                            ** ** //
	// ** *********************************************************** ** //
	
	protected static DataLocationDepot loadObject(int uid){
		Object obj = Persistence.loadJavaObject(DataLocationDepot.class.getName(), uid);
		if(obj != null){
			DataLocationDepot ret = (DataLocationDepot)obj;
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
