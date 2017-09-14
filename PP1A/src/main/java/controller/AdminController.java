package controller;

import static spark.Spark.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

public class AdminController {
	public static Route adminPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		model.put("userTemplate", "/users/admin.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public void loadToSession(Map<String, Object> model, Request req)
	{
		Admin admin = (Admin) FileTools.LoadUser(req.session().attribute("username"));
		
		if (admin.getPassword().equals(req.session().attribute("password")))
		{
			model.put("username", admin.getID());
			model.put("firstname", admin.getFName());
			model.put("lastname", admin.getLName());
			model.put("age", admin.getAge());
			model.put("password", admin.getPassword());
		}
	}
	
	public void deletePlayer(Request req)
	{
		String delUserId = req.session().attribute("");
		
		try {
			Admin.delUser(delUserId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}