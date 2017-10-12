package controller;

import static spark.Spark.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.FileTools;
import model.Menu;
import model.Player;
import model.TradingAcc;
import model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class UserController {
	public static Route userPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		LoginController.loadToModel(model, req);
		model.put("userTemplate", "/users/user.vtl");
		model.put("admin", req.session().attribute("adminObj"));
		
		//load trading account
		Player player = (Player) FileTools.LoadUser(req.session().attribute("username"));
		if(player.getTradingAcc() != null){
			model.put("tradingAcc", true);
			model.put("tradingAccSuccess", true);
			model.put("currBal", player.getTradingAcc().getCurrBal());
			model.put("sharesOwned", player.getTradingAcc().getSharesOwned());
		}
		else
			model.put("tradingAcc", false);
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Route confirmEditProfile = (req, res) -> {
		Map<String, Object> model = editProfile(req);
		
		res.redirect("/userPage");
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Route openTradingAcc = (req, res) -> {
		Map<String, Object> model = openTradingAcc(req.session().attribute("username"), req);
		LoginController.loadToModel(model, req);
		
		res.redirect("/userPage");
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Route deleteAccount = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		deleteAccount(req.session().attribute("username"), req);
		model.put("template", "users/deleteAccount.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	public static Map<String, Object> editProfile(Request req) throws IOException{
		Map<String, Object> model = new HashMap<>();
		
		String oldId = req.session().attribute("username");
		String newId = req.queryParams("newID");
		String firstName = req.queryParams("newFname");
		String lastName = req.queryParams("newLname");
		int age = Integer.parseInt(req.queryParams("newAge"));
		String password = req.queryParams("newPassword");
		
		Player player = req.session().attribute("playerObj");
		
		if(player.editProfile(oldId, newId, password, firstName, lastName, age) != null)
		{
			req.session().attribute("playerObj", player);
			req.session().attribute("username", newId);
			req.session().attribute("firstname", firstName);
			req.session().attribute("lastname", lastName);
			req.session().attribute("age", age);
			req.session().attribute("password", password);
			
			model.put("success", "/users/ConfirmEditProfile.vtl");
		}
		else
			model.put("failed", "/users/editProfile.vtl");
		
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
	}
	
	public static void loadTradingAccToSession(Request req)
	{
		Player player = (Player) FileTools.LoadUser(req.session().attribute("username"));
		
		if (player.getPassword().equals(req.session().attribute("password")) && player.getTradingAcc() != null)
		{
			req.session().attribute("currBal", player.getTradingAcc().getCurrBal());
			req.session().attribute("sharesOwned", player.getTradingAcc().getSharesOwned());
		}
	}
	
	public static void removeTradAccfromSession(Request req)
	{
		Player player = (Player) FileTools.LoadUser(req.session().attribute("username"));
		
		if (player.getPassword().equals(req.session().attribute("password")) && player.getTradingAcc() != null)
		{
			req.session().removeAttribute("currBal");
			req.session().removeAttribute("sharesOwned");
		}
	}
	
	public static void deleteAccount(String username, Request req)
	{
		Player player = req.session().attribute("playerObj");
		
		try {
			LoginController.logoutUser(req);
			player.deleteAcc();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Map<String, Object> openTradingAcc(String username, Request req)
	{
		Map<String, Object> model = new HashMap<>();
		Player player = (Player) FileTools.LoadUser(req.session().attribute("username"));
		//Player player = req.session().attribute("playerObj");
		TradingAcc trAcc = null;
		
		if (player.getTradingAcc() == null)
		{
			try {
				trAcc = Player.openTradeAcc(player.getID());
				player.setTradingAcc(trAcc);
				
				model.put("tradingAcc", true);
				model.put("tradingAccSuccess", true);
				loadTradingAccToSession(req);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return model;
	}
}