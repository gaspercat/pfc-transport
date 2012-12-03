package Communication;

import java.util.ArrayList;

public class JsonParser {
	// ** *********************************************************** ** //
	// ** ** PUBLIC PARSED DATA CLASS                              ** ** //
	// ** *********************************************************** ** //
	
	public static class JsonNode{
		ArrayList<String> key;
		ArrayList<ArrayList<Object>> value;
	
		public JsonNode(){
			this.key = new ArrayList<String>();
			this.value = new ArrayList<ArrayList<Object>>();
		}
		
		public JsonNode(String key, Object value){
			this.key = new ArrayList<String>();
			this.key.add(key);
			this.value = new ArrayList<ArrayList<Object>>();
			this.value.add(new ArrayList<Object>());
			this.value.get(0).add(value);
		}
		
		public JsonNode(String key, ArrayList<Object> values){
			this.key = new ArrayList<String>();
			this.key.add(key);
			this.value.add(values);
		}
		
		public JsonNode(ArrayList<String> keys, ArrayList<ArrayList<Object>> values){
			this.key = keys;
			this.value = values;
		}
		
		public int countAttributes(){
			return this.key.size();
		}
		
		public int countAttributeValues(String key){
			for(int i=0;i<this.key.size();i++){
				if(this.key.get(i).compareTo(key) == 0){
					return this.value.get(i).size();
				}
			}
			return 0;
		}
		
		public String getStringAttribute(String key, int idxValue){
			if(idxValue<0) return null;
			
			for(int i=0;i<this.key.size();i++){
				if(this.key.get(i).compareTo(key) == 0){
					ArrayList<Object> vals = this.value.get(i);
					if(idxValue >= vals.size()) return null;
					return (String)vals.get(idxValue);
				}
			}
			return null;
		}
		
		public Integer getIntegerAttribute(String key, int idxValue){
			if(idxValue<0) return null;
			
			for(int i=0;i<this.key.size();i++){
				if(this.key.get(i).compareTo(key) == 0){
					ArrayList<Object> vals = this.value.get(i);
					if(idxValue >= vals.size()) return null;
					return (Integer)vals.get(idxValue);
				}
			}
			return null;
		}
		
		public Float getFloatAttribute(String key, int idxValue){
			if(idxValue<0) return null;
			
			for(int i=0;i<this.key.size();i++){
				if(this.key.get(i).compareTo(key) == 0){
					ArrayList<Object> vals = this.value.get(i);
					if(idxValue >= vals.size()) return null;
					return (Float)vals.get(idxValue);
				}
			}
			return null;
		}
		
		public JsonParser.JsonNode getNodeAttribute(String key, int idxValue){
			for(int i=0;i<this.key.size();i++){
				if(this.key.get(i).compareTo(key) == 0){
					ArrayList<Object> vals = this.value.get(i);
					if(idxValue >= vals.size()) return null;
					return (JsonNode)vals.get(idxValue);
				}
			}
			return null;
		}
		
		public void addAttribute(String key, Object value){
			for(int i=0;i<this.key.size();i++){
				if(this.key.get(i).compareTo(key) == 0){
					this.value.get(i).add(value);
					return;
				}
			}
			
			this.key.add(key);
			ArrayList<Object> values = new ArrayList<Object>();
			values.add(value);
			this.value.add(values);
		}
		
		public void addAttributes(String key, ArrayList<Object> values){
			for(int i=0;i<this.key.size();i++){
				if(this.key.get(i).compareTo(key) == 0){
					this.value.get(i).addAll(values);
					return;
				}
			}
			
			this.key.add(key);
			this.value.add(values);
		}
		
		public void delAttribute(String key){
			for(int i=0;i<this.key.size();i++){
				if(this.key.get(i).compareTo(key) == 0){
					this.key.remove(i);
					this.value.remove(i);
					return;
				}
			}
		}
		
		public void delAttributeVaule(String key, int idxValue){
			if(idxValue >= 1)
			for(int i=0;i<this.key.size();i++){
				if(this.key.get(i).compareTo(key) == 0){
					ArrayList<Object> vals = this.value.get(i);
					if(idxValue>=vals.size()) return;
					if(vals.size()==1){
						this.key.remove(i);
						this.value.remove(i);
						return;
					}
					vals.remove(idxValue);
				}
			}
		}
		
		public String getStringEncoded(){
			String ret = "{";
			
			for(int i=0;i<this.key.size();i++){
				// Print key
				ret = ret + "\"" + this.key.get(i) + "\":";
				
				// Print value
				ArrayList<Object> values = this.value.get(i);
				if(values.size() == 1){
					String type = this.getObjectClassName(values.get(0));
					if(type.equals("String")){
						String val = (String)values.get(0);
						ret = ret + "\"" + val + "\"";
					}else if(type.equals("Integer")){
						Integer val = (Integer)values.get(0);
						ret = ret + val.toString();
					}else if(type.equals("Float")){
						Float val = (Float)values.get(0);
						ret = ret + val.toString();
					}else if(type.equals("Boolean")){
						Boolean val = (Boolean)values.get(0);
						ret = ret + val.toString();
					}else{
						JsonNode node = (JsonNode)values.get(0);
						ret = ret + node.getStringEncoded();
					}
				}else{
					ret = ret + "[";
					for(int j=0;j<values.size();j++){
						String type = this.getObjectClassName(values.get(j));
						if(type.equals("String")){
							String val = (String)values.get(j);
							ret = ret + "\"" + val + "\"";
						}else if(type.equals("Integer")){
							Integer val = (Integer)values.get(j);
							ret = ret + val.toString();
						}else if(type.equals("Float")){
							Float val = (Float)values.get(j);
							ret = ret + val.toString();
						}else if(type.equals("Boolean")){
							Boolean val = (Boolean)values.get(j);
							ret = ret + val.toString();
						}else{
							JsonNode node = (JsonNode)values.get(j);
							ret = ret + node.getStringEncoded();
						}
						// Print separator if more values
						if(j<values.size()-1) ret = ret + ", ";
					}
					ret = ret + "]";
				}
				
				// Print separator if more attributes
				if(i<this.key.size()-1) ret = ret + ", ";
			}
			ret = ret + "}";
			
			return ret;
		}

		private String getObjectClassName(Object object) {
			String parts[] = object.getClass().getName().split("\\.");
			return parts[parts.length-1];
		}
	}

	String strng;
	int len;
	int pos;
	
	// ** *********************************************************** ** //
	// ** ** PUBLIC CHECK & PARSE METHODS                          ** ** //
	// ** *********************************************************** ** //
	
	public boolean checkFormat(String data){
		if(data.length() <= 1) return false;
		int count = 0;
		
		for(int i=0;i<data.length();i++){
			if(data.charAt(i) == '{'){
				count++;
			}else if(data.charAt(i) == '}'){
				count--;
			}
		}
		
		if(count==0) return true;
		return false;
	}
	
	public JsonParser.JsonNode parseString(String str){
		// Initialize values
		this.strng = str;
		this.len = str.length();
		this.pos = 0;
		
		// Jump white spaces
		jumpWhitespaces();
		if(this.pos == this.len) return null;
		
		// Return parse of the string
		if(this.strng.charAt(this.pos) != '{') return null;
		return this.parseNode();
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE PARSING METHODS                               ** ** //
	// ** *********************************************************** ** //
	
	private JsonParser.JsonNode parseNode(){
		JsonNode ret = new JsonNode();
		
		// Move to the beginning of first element
		this.pos++;
		this.jumpWhitespaces();
		if(this.pos == this.len) return null;
		
		while(this.pos != this.len && this.strng.charAt(this.pos) != '}'){
			// Parse key-value element
			if(this.parseKeyValue(ret) == false) return null;
			
			// If not key-value pairs separator or end of node, terminate
			if(this.strng.charAt(this.pos) == '}') break;
			if(this.strng.charAt(this.pos) != ',') return null;
			this.pos++;
		}
		
		// Return node
		this.pos++;
		return ret;
	}
	
	private boolean parseKeyValue(JsonNode curr){
		String key;
		Object value;
		ArrayList<Object> valueList;
		
		// Move to the beginning of the key
		this.jumpWhitespaces();
		if(this.pos == this.len) return false;
		
		// Read key string
		key = readString();
		if(key == null) return false;
		
		// Move past the separator
		this.jumpWhitespaces();
		if(this.pos == this.len) return false;
		if(this.strng.charAt(this.pos) != ':') return false;
		this.pos++;
		this.jumpWhitespaces();
		if(this.pos == this.len) return false;
		
		// Read node value
		char c = this.strng.charAt(this.pos);
		switch(c){
			case '"':
			case '\'':
				value = this.readString();
				if(value == null) return false;
				curr.addAttribute(key, value);
				break;
				
			case '0':	case '1':	case '2':	case '3':
			case '4':	case '5':	case '6':	case '7':
			case '8':	case '9':   case '+':	case '-':
				value = this.readNumber();
				if(value == null) return false;
				curr.addAttribute(key, value);
				break;
				
			case 't':
			case 'T':
				value = this.readTrue();
				if(value == null) return false;
				curr.addAttribute(key, value);
				break;
			
			case 'f':
			case 'F':
				value = this.readFalse();
				if(value == null) return false;
				curr.addAttribute(key, value);
				break;
				
			case '{':
				value = this.parseNode();
				if(value == null) return false;
				curr.addAttribute(key, value);
				break;
				
			case '[':
				valueList = this.parseList();
				if(valueList == null) return false;
				curr.addAttributes(key, valueList);
				break;
				
			default:
				return false;
		}
		
		// Jump white spaces
		this.jumpWhitespaces();
		if(this.pos == this.len) return false;
		
		return true;
	}
	
	private ArrayList<Object> parseList(){
		if(this.strng.charAt(this.pos) != '[') return null;
		
		ArrayList<Object> values = new ArrayList<Object>();
		Object value;
		
		// Jump white spaces
		this.pos++;
		this.jumpWhitespaces();
		if(this.pos == this.len) return null;
		
		while(this.pos != this.strng.length() && this.strng.charAt(this.pos) != ']'){
			// Read element value
			char c = this.strng.charAt(this.pos);
			switch(c){
				case '"':
				case '\'':
					value = this.readString();
					break;
					
				case '0':	case '1':	case '2':	case '3':
				case '4':	case '5':	case '6':	case '7':
				case '8':	case '9':	case '+':	case '-':
					value = this.readNumber();
					break;
					
				case 't':
				case 'T':
					value = this.readTrue();
					break;
				
				case 'f':
				case 'F':
					value = this.readFalse();
					break;
					
				case '{':
					value = this.parseNode();
					break;
					
				default:
					return null;
			}
			
			// If value is null, return null
			if(value == null) return null;
			values.add(value);
			
			// Jump white spaces
			this.jumpWhitespaces();
			if(this.pos == this.len) return null;
			
			// Check next character
			if(this.strng.charAt(this.pos) == ']') break;
			if(this.strng.charAt(this.pos) != ',') return null;
			
			// Jump white spaces
			this.pos++;
			this.jumpWhitespaces();
			if(this.pos == this.len) return null;
		}
		
		this.pos++;
		return values;
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE VALUES READOUT METHODS                        ** ** //
	// ** *********************************************************** ** //
	
	// Read string
	private String readString(){
		char c = this.strng.charAt(this.pos);
		if(c!='"' && c!='\'') return null;
		this.pos++;
		
		String value = "";
		while(this.strng.charAt(this.pos-1) == '\\' || this.strng.charAt(this.pos) != c){
			value = value + String.valueOf(this.strng.charAt(this.pos));
			this.pos++;
		}
		this.pos++;
		
		return value;
	}
	
	private Object readNumber(){
		String raw = "";
		boolean decimal = false;
		
		// If value has a sign
		char c = this.strng.charAt(this.pos);
		if(c == '+' || c == '-'){
			if(c == '-') raw = "-";
			this.pos++;
		}
		
		// Read 
		while(isNumeric(this.strng.charAt(this.pos))){
			if(this.strng.charAt(this.pos) == '.') decimal = true;
			raw = raw + String.valueOf(this.strng.charAt(this.pos));
			this.pos++;
		}
		
		if(decimal){
			return Float.parseFloat(raw);
		}else{
			return Integer.parseInt(raw);
		}
	}
	
	private Boolean readTrue(){
		if(this.pos+4 >= this.len) return null;
		if(this.strng.charAt(this.pos+0) != 't' && this.strng.charAt(this.pos+0) != 'T') return null;
		if(this.strng.charAt(this.pos+1) != 'r' && this.strng.charAt(this.pos+1) != 'R') return null;
		if(this.strng.charAt(this.pos+2) != 'u' && this.strng.charAt(this.pos+2) != 'U') return null;
		if(this.strng.charAt(this.pos+3) != 'e' && this.strng.charAt(this.pos+3) != 'E') return null;
		this.pos = this.pos + 4;
		return new Boolean(true);
	}
	
	private Boolean readFalse(){
		if(this.pos+5 >= this.len) return null;
		if(this.strng.charAt(this.pos+0) != 'f' && this.strng.charAt(this.pos+0) != 'F') return null;
		if(this.strng.charAt(this.pos+1) != 'a' && this.strng.charAt(this.pos+1) != 'A') return null;
		if(this.strng.charAt(this.pos+2) != 'l' && this.strng.charAt(this.pos+2) != 'L') return null;
		if(this.strng.charAt(this.pos+3) != 's' && this.strng.charAt(this.pos+3) != 'S') return null;
		if(this.strng.charAt(this.pos+4) != 'e' && this.strng.charAt(this.pos+4) != 'E') return null;
		this.pos = this.pos + 5;
		return new Boolean(false);
	}
	
	// ** *********************************************************** ** //
	// ** ** PRIVATE AUXILIAR METHODS                              ** ** //
	// ** *********************************************************** ** //
	
	private void jumpWhitespaces(){
		while(this.pos<this.len){
			if(this.strng.charAt(this.pos) != ' ') return;
			this.pos++;
		}
	}
	
	private boolean isNumeric(char num){
		if(num>='0' && num<='9') return true;
		if(num=='.') return true;
		return false;
	}
}
