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
	
	public void loadToModel(Map<String, Object> model, Request req)
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