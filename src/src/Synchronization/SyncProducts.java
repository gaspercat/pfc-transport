package Synchronization;

import java.util.ArrayList;

import Communication.JsonParser;
import Data.DataProduct;
import Data.HandlerProduct;

public class SyncProducts {
	static private final int					maxDiff = 20;
	
	static private SyncProducts					instance = null;
	
	private ArrayList<JsonParser.JsonNode>		diffs;
	private int 								version;
	
	private SyncProducts(){
		JsonParser.JsonNode syn;
		
		// Initialize version differences array
		this.diffs = new ArrayList<JsonParser.JsonNode>();
		for(int i=0;i<SyncProducts.maxDiff;i++){
			 syn = new JsonParser.JsonNode("elems", new ArrayList<Object>());
			 this.diffs.add(syn);
		}
		
		// Initialize version counter
		version = 1;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC :: INSTANCE RETRIEVAL METHOD                   ** ** //
	// ** *********************************************************** ** //
	
	public static synchronized SyncProducts getInstance(){
		if(SyncProducts.instance == null) SyncProducts.instance = new SyncProducts();
		return SyncProducts.instance;
	}
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC :: SYNCHRONIZATION METHODS                     ** ** //
	// ** *********************************************************** ** //
	
	public synchronized JsonParser.JsonNode syncUp(JsonParser.JsonNode req){
		JsonParser.JsonNode ret = new JsonParser.JsonNode();
		HandlerProduct hdlr = HandlerProduct.getInstance();
		
		// Loop though objects to synchronize
		int num = req.countAttributeValues("elems");
		for(int i=0;i<num;i++){
			JsonParser.JsonNode elem = req.getNodeAttribute("elems", i);
			String act = req.getStringAttribute("act", 0);
			
			// If request to create a new product
			if(act.compareTo("add") == 0){
				// 
				
		    // If request to delete a product
			}else if(act.compareTo("del") == 0){
				// Remove product if present
				DataProduct prod = hdlr.getProductByID(elem.getIntegerAttribute("id", 0).intValue());
				if(prod != null) hdlr.deleteProduct(prod);
				
		    // If request to update a product
			}else if(act.compareTo("upd") == 0){
				// Update product if present
				DataProduct prod = hdlr.getProductByID(elem.getIntegerAttribute("id", 0).intValue());
				if(prod != null){
					prod.setName(new String(elem.getStringAttribute("name", 0)));
					prod.setVolume(new Float(elem.getFloatAttribute("volume", 0)));
					prod.setWeight(new Float(elem.getFloatAttribute("weight", 0)));
				}
			}
		}
		
		return this.buildSynuResponse(ret);
	}
	
	public synchronized JsonParser.JsonNode syncDown(int cver){
		if(cver == -1 || cver > this.version || cver < this.version - SyncProducts.maxDiff){
			JsonParser.JsonNode list = this.listAll();
			return this.buildSyndResponse(list);
		}
		
		JsonParser.JsonNode list = this.listDiffs(cver);
		return this.buildSyndResponse(list);
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE :: RESPONSE BUILDING METHODS                  ** ** //
	// ** *********************************************************** ** //
	
	private synchronized JsonParser.JsonNode buildSyndResponse(JsonParser.JsonNode list){
		list.addAttribute("status", new String("ok"));
		list.addAttribute("ver", new Integer(this.version));
		return list;
	}
	
	private synchronized JsonParser.JsonNode buildSynuResponse(JsonParser.JsonNode list){
		list.addAttribute("status", new String("ok"));
		return list;
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE :: ELEMENTS LISTING METHODS                   ** ** //
	// ** *********************************************************** ** //
	
	private synchronized JsonParser.JsonNode listAll(){
		JsonParser.JsonNode ret = new JsonParser.JsonNode();
		HandlerProduct hdlr = HandlerProduct.getInstance();
		
		int count = hdlr.getNumProducts();
		for(int i=0;i<count;i++){
			DataProduct product = hdlr.getProduct(i);
			
			JsonParser.JsonNode tprod = new JsonParser.JsonNode();
			tprod.addAttribute("id", new Integer(product.getID()));
			tprod.addAttribute("name", product.getName());
			tprod.addAttribute("volume", new Float(product.getVolume()));
			tprod.addAttribute("weight", new Float(product.getWeight()));
			
			ret.addAttribute("elems", tprod);
		}
		
		return ret;
	}
	
	private synchronized JsonParser.JsonNode listDiffs(int cver){
		int first = SyncProducts.maxDiff - (this.version - cver);
		JsonParser.JsonNode ret = new JsonParser.JsonNode("elems", new ArrayList<Object>());
		
		for(int i=first;i<SyncProducts.maxDiff;i++){
			JsonParser.JsonNode diff = this.diffs.get(i);
			
			int nelems = diff.countAttributeValues("elems");
			for(int j=0;j<nelems;j++){
				ret.addAttribute("elems", diff.getNodeAttribute("elems", j));
			}
		}
		
		return ret;
	}
}
