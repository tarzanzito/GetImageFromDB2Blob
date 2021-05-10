package candal.app.business;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ConfigFile {

	private final String _propFileName = "app.properties";
	private Properties _properties = null;
    
	private void writeInitial() throws Exception {
		
		_properties = new Properties();
		OutputStream output = null;

		try {

			output = new FileOutputStream(_propFileName);
			
			// set the properties value
			_properties.setProperty("url", "localhost");
			_properties.setProperty("schema", "schema");
			_properties.setProperty("userId", "userid");
			_properties.setProperty("userPwd", "password");

			// save properties to project root folder
			_properties.store(output, "Properties for BD2 access." );

			output.flush();
			output.close();

		} catch (Exception e) {
			throw new Exception(_propFileName + " error writting. : " + e.getMessage());
		} 

	}
	
   	public void read() throws Exception {
   		
		InputStream input = null;

		try {
			input = new FileInputStream(_propFileName);
		} catch (Exception e) {
			writeInitial();
			throw new Exception(_propFileName + " not found. : " + e.getMessage(), e);
		}
		
		_properties = new Properties();
		_properties.load(input);
		
		input.close();
		
   	}    
   	
   	public String getUrl() {
   		
   		if (_properties == null)
   			return "";
   		
   		return _properties.getProperty("url");
   	}

   	public String getSchema() {
   		
   		if (_properties == null)
   			return "";
   		
   		return _properties.getProperty("schema");
   	}
   	
   	public String getUserId() {
   		
   		if (_properties == null)
   			return "";
   		
   		return _properties.getProperty("userId");
   	}

   	public String getUserPwd() {
   		
   		if (_properties == null)
   			return "";
   		
   		return _properties.getProperty("userPwd");
   	}
   	
}
