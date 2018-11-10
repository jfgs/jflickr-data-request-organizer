package parameters;

public enum DROParamNames {
	
	EXPORT_LOCATION("export_location"),
	IMPORT_LOCATION("import_location");
	
	private String key; 
	
	DROParamNames(String key) {
		this.key = key; 
	}
	
	public String getKey() {
		return this.key;
	}

}
