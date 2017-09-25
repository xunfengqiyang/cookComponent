package com.acxiom.helper;

import java.io.UnsupportedEncodingException;

import com.acxiom.configer.Configuration;
import com.acxiom.model.StepModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CCStringTool {
	
	
    public static String getUTF8String(String srcStr) throws UnsupportedEncodingException {
  	  
  	  return new String(srcStr.getBytes("ISO-8859-1"), "UTF-8");
	}
    
    public static String handleAlbumStr(String albumStr)
    {
    	JSONArray returnArray = new JSONArray();
    	JSONArray jsonArray = JSONArray.fromObject(albumStr);
    	for(int i=0; i<jsonArray.size(); i++)
    	{
    		String tmp = jsonArray.get(i).toString();
    		tmp = Configuration.getUrlPre() + tmp;
    		returnArray.add(tmp);
    	}
    	
    	return returnArray.toString();
    }
    
    public static String handleStepStr(String stepStr) {
		
    	JSONArray returnArray = new JSONArray();
    	JSONArray jsonArray = JSONArray.fromObject(stepStr);
    	for(int i=0; i<jsonArray.size(); i++)
    	{
    		Object o=jsonArray.get(i);
    		JSONObject jsonObject=JSONObject.fromObject(o);
    		StepModel step =(StepModel)JSONObject.toBean(jsonObject, StepModel.class);
    		step.setImg(Configuration.getUrlPre() + step.getImg());
    		returnArray.add(step);
    	}
    	
    	return returnArray.toString();
	}

}
