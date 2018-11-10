package parameters;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class DROParamManager {
	
	private static final String CONFIG_FILE_NAME = "conf/default.xml";
	private static final String EXPORT_DEFAULT_LOCATION = "data/export/";
	private static final String IMPORT_DEFAULT_LOCATION = "data/import/";
	
	private static final String VERSION = "JFlickr Data Request Organizer v0.1";
	
	private static Properties p;
	
	public DROParamManager() {
		p = new Properties();
		try {
			p.loadFromXML(new FileInputStream(CONFIG_FILE_NAME));
		} catch (IOException e) {
			initDefaultParameters();
		}
	}
	
	public void initDefaultParameters() {
		p.setProperty(DROParamNames.IMPORT_LOCATION.getKey(), IMPORT_DEFAULT_LOCATION);
		p.setProperty(DROParamNames.EXPORT_LOCATION.getKey(), EXPORT_DEFAULT_LOCATION);
	}
	
	public void saveParameters() throws FileNotFoundException, IOException {
		p.storeToXML(new FileOutputStream(CONFIG_FILE_NAME), VERSION);
	}
	
	public static void setDefaultParameters() {
		DROParamManager p = new DROParamManager();
		try {
			p.saveParameters();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @return import raw data location
	 */
	public String getImportLocation() {
		return p.getProperty(DROParamNames.IMPORT_LOCATION.getKey());
	}
	
	/**
	 * 
	 * @return export human readable data location
	 */
	public String getExportLocation() {
		return p.getProperty(DROParamNames.EXPORT_LOCATION.getKey());
	}

}
