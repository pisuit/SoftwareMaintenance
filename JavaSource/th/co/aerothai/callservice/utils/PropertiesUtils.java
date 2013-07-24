package th.co.aerothai.callservice.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesUtils {

	public static ResourceBundle getSVNProperties() {
		ResourceBundle props = ResourceBundle.getBundle("SVN");
		return props;
	}
	
	public static ResourceBundle getGrowlProperties() {
		ResourceBundle props = ResourceBundle.getBundle("Growl");
		return props;
	}
	
	public static ResourceBundle getIPProperties() {
		ResourceBundle props = ResourceBundle.getBundle("IP");
		return props;
	}
}
