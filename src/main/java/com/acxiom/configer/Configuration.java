package com.acxiom.configer;

import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
import java.util.Properties;

/**
  * Created by Jie Tan on 17-9-16.
 */
/*There must be a Controller annotation or the application will doesn't work .*/

public class Configuration {
	
	private static String urlPre;
	private static String mysqlUser;
	private static String mysqlPwd;
	
	public static String filename1= "/Users/gavintan/local/configer/cookComponent/cookConfiger.properties";
	
	static  {    

		try {
			FileInputStream inputFile = new FileInputStream(filename1);
			Properties properties = new Properties();
			properties.load(inputFile);
			inputFile.close();

			urlPre = properties.getProperty("imageUrlPrefix");
			mysqlUser = properties.getProperty("mysqlUser");
			mysqlPwd = properties.getProperty("mysqlPwd");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
         
         
    }    
	
	  
    /**    
    * 私有构造方法，不需要创建对象    
    */     
    private  Configuration() {    
    	
    }    
 
    public static String getUrlPre() {    
        return  urlPre;    
    }

	public static String getMysqlUser() {
		return mysqlUser;
	}

	public static String getMysqlPwd() {
		return mysqlPwd;
	}
	
}
