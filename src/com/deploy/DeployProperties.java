package com.deploy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public abstract class DeployProperties {
	private static Properties props;
	private static InputStream in;
	private final static String propertiesFileName = "deploy.properties";
	 static{
        try {
       	 props = new Properties();
       	 in = DeployProperties.class.getResourceAsStream(propertiesFileName);
         props.load(in);
         in.close();
        } catch (FileNotFoundException e) {
             e.printStackTrace();
        } catch (IOException e) {
             e.printStackTrace();
        }
   }
	protected static Properties getProps() {
		return props;
	}
}
