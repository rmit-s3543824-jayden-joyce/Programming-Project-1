package controller;

import static spark.Spark.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

public class UserController {
	public static Route userPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		model.put("userTemplate", "/users/user.vtl");
		model.put("username", req.session().attribute("username"));
		model.put("firstname", req.session().attribute("firstname"));
		model.put("lastname", req.session().attribute("lastname"));
		model.put("age", req.session().attribute("age"));
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Route editPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		model.put("userTemplate", "/users/editProfile.vtl");
		model.put("username", req.session().attribute("username"));
		model.put("firstname", req.session().attribute("firstname"));
		model.put("lastname", req.session().attribute("lastname"));
		model.put("age", req.session().attribute("age"));
		model.put("password", req.session().attribute("password"));
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Route confirmEditProfile = (req, res) -> {
		Map<String, Object> model = editProfile(req);
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public static Map<String, Object> editProfile(Request req) throws IOException{
		Map<String, Object> model = new HashMap<>();
		
		String oldId = req.session().attribute("username");
		String newId = req.queryParams("username");
		String firstName = req.queryParams("firstname");
		String lastName = req.queryParams("lastname");
		int age = Integer.parseInt(req.queryParams("age"));
		String password = req.queryParams("password");
		
		//this line is to workaround for the static error
		System.out.println("63-67: PP1A/src/main/java/controller/UserController.java");
		Player player = new Player(null, null, null, null, 0);
		
		//change the player corresponding to the workaround
		if(player.editProfile(oldId, newId, password, firstName, lastName, age) != null)
		{
			req.session().attribute("username", newId);
			req.session().attribute("firstname", firstName);
			req.session().attribute("lastname", lastName);
			req.session().attribute("age", age);
			req.session().attribute("password", password);
			
			model.put("userTemplate", "/users/ConfirmEditProfile.vtl");
		}
		else
			model.put("userTemplate", "/users/editProfile.vtl");
		
		return model;	
	}
}