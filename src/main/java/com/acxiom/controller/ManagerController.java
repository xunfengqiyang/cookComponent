package com.acxiom.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import com.acxiom.helper.CCStringTool;
import com.acxiom.helper.FetchTool;
import com.acxiom.manager.MysqlManager;
import com.acxiom.model.CookModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


//import com.acxiom.manager.MysqlManager;

public class ManagerController {
	
	private static ManagerController singleton = null;      
	

    //构造方法被设为私有，防止外部使用new来创建对象，破坏单例
    private ManagerController(){
        //System.out.println("构造函数被调用");
    }    

    //公有的静态方法，供外部调用来获取单例对象
    public static ManagerController getInstance(){
        if(singleton == null){    //第一次调用该方法时，创建对象。
            singleton = new ManagerController();
            
        }
        return singleton;
    }

	public void startTestSql() {
		
		//MysqlManager newM = MysqlManager.getInstance();
		//newM.testSql();
	}

	
	
	
	//随机挑些菜单 3～4个。
	public JSONArray getServeralDishNames() {

		JSONArray array = new JSONArray();

		ArrayList<CookModel> listCook = MysqlManager.getInstance().getListCook();
		Random r = new Random();
		
		int size = r.nextInt(listCook.size());
		
		HashSet<Integer> set = new HashSet<Integer>();
		FetchTool.randomSet(0, size, 4, set);
		
		try {
			for (Integer index:set)
			{
				CookModel model = listCook.get(index);
				JSONObject object = new JSONObject();
				object.put("title", model.title);
				object.put("img", CCStringTool.handleAlbumStr(model.albums));
				array.add(object);
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return array;
	}
	
	
	//提供菜名，给出菜谱
	public JSONArray getDish(String uName) {
		
		JSONArray array = new JSONArray();
		ArrayList<CookModel> listCook = MysqlManager.getInstance().getListCook();
		
		for (int i = 0; i < listCook.size(); i++) {
			CookModel model = listCook.get(i);
			if (model.title.equals(uName)) {

				JSONObject modelObject = new JSONObject();
				modelObject.put("title", model.title);
				modelObject.put("tags", model.tags);
				modelObject.put("ingredients", model.ingredients);
				modelObject.put("burden", model.ingredients);
				modelObject.put("albums", CCStringTool.handleAlbumStr(model.albums));
				modelObject.put("steps", CCStringTool.handleStepStr(model.steps));
				
				array.add(modelObject);
			}
		}
		
		return array;
	}
	
	//根据某些类型，来挑选菜单
	public JSONArray getDishsWithTag(String tagName) {
		
		JSONArray array = new JSONArray();
		
		ArrayList<CookModel> listCook = MysqlManager.getInstance().getListCook();
		ArrayList<CookModel> listNeed = new ArrayList<CookModel>();
		
		for (int i = 0; i < listCook.size(); i++)
		{
			CookModel model = listCook.get(i);
			if( model.tags.indexOf(tagName) != -1)
			{
				listNeed.add(model);
			}
		}
		
		HashSet<Integer> set = new HashSet<Integer>();
		Integer count = (listNeed.size()>4) ? 4 : listNeed.size() ;
		FetchTool.randomSet(0, listNeed.size(), count, set);
		
		try {
			for (Integer index:set)
			{
				CookModel model = listNeed.get(index);
				JSONObject object = new JSONObject();
				object.put("title", model.title);
				object.put("img", CCStringTool.handleAlbumStr(model.albums));
				array.add(object);
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return array;
	}
	
	
}
