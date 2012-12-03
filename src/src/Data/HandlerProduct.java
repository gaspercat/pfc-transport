package Data;

import java.util.ArrayList;

public class HandlerProduct {
	private static HandlerProduct instance = null;
	
	private ArrayList<DataProduct> products;
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE CONSTRUCTORS                                  ** ** //
	// ** *********************************************************** ** //
	
	private HandlerProduct(){
		// Initialize products array
		this.products = new ArrayList<DataProduct>();
		
		// Get list of products
		String className = DataProduct.class.getName();
		ArrayList<Integer> products = Persistence.listJavaObjects(className);
		if(products == null) return;
		
		// Load listed depots
		for(int i=0;i<products.size();i++){
			DataProduct product = DataProduct.loadObject(products.get(i));
			if(product != null){
				this.products.add(product);
			}
		}
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CONSTRUCTORS                                   ** ** //
	// ** *********************************************************** ** //
	
	public static HandlerProduct getInstance(){
		if(HandlerProduct.instance == null) HandlerProduct.instance = new HandlerProduct();
		return HandlerProduct.instance;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC GETTERS & SETTERS                              ** ** //
	// ** *********************************************************** ** //
	
	public int getNumProducts(){
		return this.products.size();
	}
	
	public DataProduct getProduct(int idx){
		if(idx<0 || idx>=this.products.size()) return null;
		return this.products.get(idx);
	}
	
	public DataProduct getProductByID(int id){
		for(int i=0;i<this.products.size();i++){
			DataProduct product = this.products.get(i);
			if(product.getID() == id) return product;
		}
		
		return null;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC DEPOT CONSTRUCTORS                             ** ** //
	// ** *********************************************************** ** //
	
	public DataProduct newProduct(String name, float volume, float weight){
		DataProduct ret = new DataProduct(name, volume, weight);
		this.products.add(ret);
		return ret;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC DEPOT DESTRUCTORS                              ** ** //
	// ** *********************************************************** ** //
	
	public boolean deleteProduct(int idx){
		if(idx<0 || idx>=this.products.size()) return false;
		if(this.products.get(idx).deleteObject()){
			this.products.remove(idx);
			return true;
		}
		
		return false;
	}
	
	public boolean deleteProduct(DataProduct product){
		if(product == null) return false;
		if(product.deleteObject()){
			this.products.remove(product);
			return true;
		}
		
		return false;
	}
}
