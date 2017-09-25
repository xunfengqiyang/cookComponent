package com.acxiom.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.stereotype.Controller;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acxiom.helper.CCStringTool;
import com.acxiom.manager.MysqlManager;
import com.acxiom.model.CookModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;  


@Controller 


/*There must be a Controller annotation or the application will doesn't work .*/
//类级别的RequestMapping，告诉DispatcherServlet由这个类负责处理URL。  
//HandlerMapping依靠这个标签来工作  
@RequestMapping(value="/cook")  
public class CookController {

      
	@RequestMapping(value = "/menu", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public void dishName(@RequestParam("dishName") String dishName, HttpServletResponse response, HttpSession session)
			throws IOException {
  	  ArrayList<CookModel> listCook = MysqlManager.getInstance().getListCook();
  	  System.out.println(listCook.size());
  	  
  	  String uName = new String(dishName.getBytes("ISO-8859-1"), "UTF-8");
  	  
  	  System.out.println(uName);
  	  
  	  Boolean bGetDish = false;
  	  JSONObject returnObject = new JSONObject();
  	  
  	  for(int i = 0; i < listCook.size();i++)
       {             
  		  CookModel model = listCook.get(i);
  		  if (model.title.equals(uName)) {
  			  
  			  JSONObject modelObject = new JSONObject();
  			  modelObject.put("title", model.title);
  			  modelObject.put("tags", model.tags);
  			  modelObject.put("ingredients", model.ingredients);
  			  modelObject.put("burden", model.ingredients);
  			  modelObject.put("albums", CCStringTool.handleAlbumStr(model.albums));
  			  modelObject.put("steps", CCStringTool.handleStepStr(model.steps));
  			  
			  bGetDish = true;
			  returnObject.put("result", modelObject.toString());
			  break;
  		  }
        }

		returnObject.put("success", bGetDish);

		response.setContentType("application/json;charset=UTF-8");
		OutputStream outputStream = response.getOutputStream();

		byte[] dataByteArr = returnObject.toString().getBytes("UTF-8");
		outputStream.write(dataByteArr);

	}

	@RequestMapping(value = "/statement", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public void statement(@RequestParam("s") String sentence, HttpServletResponse response, HttpSession session)
			throws IOException {
		
		JSONObject returnObject = new JSONObject();
		
		String uSentence = new String(sentence.getBytes("ISO-8859-1"), "UTF-8");
		
		Result result = ToAnalysis.parse(uSentence);
		System.out.println(result);
		
		returnObject.put("success", "false");
		
		
		//输入即菜名
		JSONArray arrayDish = ManagerController.getInstance().getDish(uSentence);
		if (arrayDish.size() != 0) {
			returnObject.put("result", arrayDish.toString());
			returnObject.put("success", "true");
			returnObject.put("type", "name");
		}
		
		
		//先直接通过菜名去找
		if (returnObject.get("success").equals("false")) {
			for (Integer integer = 0; integer < result.size(); integer++) {
				Term term = result.get(integer);

				if (term.getNatureStr().equals("n") || term.getNatureStr().equals("r")) {
					
					System.out.println("search name :" + term.getName());
					arrayDish = ManagerController.getInstance().getDish(term.getName());
					if (arrayDish.size() != 0) {
						returnObject.put("result", arrayDish.toString());
						returnObject.put("success", "true");
						returnObject.put("type", "name");
						break;
					}
				}
			}
		}

		//然后通过口味tag去找
		if (returnObject.get("success").equals("false")) {

			for (Integer integer = 0; integer < result.size(); integer++) {
				Term term = result.get(integer);

				if (term.getNatureStr().equals("n") || term.getNatureStr().equals("r")) {
					System.out.println("search tag :" + term.getName());
					arrayDish = ManagerController.getInstance().getDishsWithTag(term.getName());
					if (arrayDish.size() != 0) {
						returnObject.put("result", arrayDish.toString());
						returnObject.put("success", "true");
						returnObject.put("type", "tag");
						returnObject.put("tagName", term.getName());
						break;
					}
				}
			}
		}

		//不能分析用户想要的，察觉用户是询问，建议时，随机提供一些
		if (returnObject.get("success").equals("false")) {
			if (uSentence.indexOf("什么")!=-1 || uSentence.indexOf("推荐")!=-1 || uSentence.indexOf("推介")!=-1) {

				JSONArray dishNames = ManagerController.getInstance().getServeralDishNames();
				returnObject.put("result", dishNames.toString());
				returnObject.put("success", "true");
				returnObject.put("type", "random");
			}
		}
		
		//不在设计功能内
		if (returnObject.get("success").equals("false")) {
			
			returnObject.put("result", "对不起，不能找到您想要的信息。");
			returnObject.put("success", "true");
			returnObject.put("type", "none");
		}
		
		response.setContentType("application/json;charset=UTF-8");
		OutputStream outputStream = response.getOutputStream();
		byte[] dataByteArr = returnObject.toString().getBytes("UTF-8");
		outputStream.write(dataByteArr);
		
	}
      
}


