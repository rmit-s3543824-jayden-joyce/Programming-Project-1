package controller;

import static spark.Spark.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import app.Application;
import model.Admin;
import model.FileTools;
import model.Menu;
import model.Player;
import model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class LoginController {
	public static Route mainPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		// put "value" inside "cardholder"
		model.put("template", "/mainpage/mainpage.vtl");
		model.put("table", "utils/companyTable.vtl");
		
		// The vtl files are located under the resources directory
		// The line below are required to make the page works
        return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	public static Route loginPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		model.put("template", "/mainpage/login.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	public static Route redirectUser = (req, res) -> {
		Map<String, Object> model = login(req);

		if(model.containsKey("userTemplate")){
			//redirect to user page
			
			return null;
		}
		else
			return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	public static Map<String, Object> login(Request req){
		Map<String, Object> model = new HashMap<>();
		
		Admin admin = null;
		Player player = null;
		
		String username	= req.queryParams("username");
		String password	= req.queryParams("password");
		
		if(username.contains("admin"))
		{
			admin = (Admin)FileTools.LoadUser(username);
			
			if(admin != null)
			{
				String firstName = admin.getFName();
				String lastName = admin.getLName();
				int age = admin.getAge();
						
				//need to check if return val is true or false later or else any pw will log you in
				app.Application.menu.login(username, password);
						
				req.session().attribute("adminObj", admin);
				req.session().attribute("username", username);
				req.session().attribute("password", password);
				req.session().attribute("firstname", firstName);
				req.session().attribute("lastname", lastName);
				req.session().attribute("age", age);
				
				loadToModel(model, req);
				model.put("userTemplate", "/users/admin.vtl");
			}
			else
			{
				model.put("authenticationFailed", true);
				model.put("template", "/mainpage/login.vtl");
			}
		}
		else
		{
			player = (Player)FileTools.LoadUser(username);
			
			if(player != null)
			{
				String firstName = player.getFName();
				String lastName = player.getLName();
				int age = player.getAge();
						
				//need to check if return val is true or false later or else any pw will log you in
				if(app.Application.menu.login(username, password))
				{		
					req.session().attribute("playerObj", player);
					req.session().attribute("username", username);
					req.session().attribute("password", password);
					req.session().attribute("firstname", firstName);
					req.session().attribute("lastname", lastName);
					req.session().attribute("age", age);
				
					player.loadTrAcc();
					//returns error when player hasn't opened a trading account yet
					//req.session().attribute("currBal", player.getTradingAcc().getCurrBal());
					//req.session().attribute("sharesOwned", player.getTradingAcc().getSharesOwned());
				
					loadToModel(model, req);
					model.put("userTemplate", "/users/user.vtl");
				}	
			}
			else
			{
				model.put("authenticationFailed", true);
				model.put("template", "/mainpage/login.vtl");
			}
		}
		
		return model;		
	}
	
	public static void loadToModel(Map<String, Object> model, Request req)
	{		
		if (req.session().attribute("username") != null && req.session().attribute("firstname") != null)
		{
			model.put("username", req.session().attribute("username"));
			model.put("firstname", req.session().attribute("firstname"));
			model.put("lastname", req.session().attribute("lastname"));
			model.put("age", req.session().attribute("age"));
			model.put("password", req.session().attribute("password"));
		}
		//TODO returns null always
		if (req.session().attribute("currBal") != null)
		{
			System.out.println("Has trading account");
			model.put("tradingAcc", true);
			model.put("currBal", req.session().attribute("currBal"));
			model.put("sharesOwned", req.session().attribute("sharesOwned"));
		}
		else
			System.out.println("No trading account");
			model.put("tradingAcc", false);
	}
}