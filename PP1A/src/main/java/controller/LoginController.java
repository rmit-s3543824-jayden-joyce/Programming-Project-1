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
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	public static Map<String, Object> login(Request req){
		Map<String, Object> model = new HashMap<>();
		
		String username	= req.queryParams("username");
		String password	= req.queryParams("password");
		
		User user = FileTools.LoadPlayer(username);
		
		String firstName = user.getFName();
		String lastName = user.getLName();
		int age = user.getAge();
				
		if(app.Application.menu.login(username, password))
		{			
			req.session().attribute("username", username);
			req.session().attribute("password", password);
			req.session().attribute("firstname", firstName);
			req.session().attribute("lastname", lastName);
			req.session().attribute("age", age);
			
			model.put("username", req.session().attribute("username"));
			model.put("firstname", req.session().attribute("firstname"));
			model.put("lastname", req.session().attribute("lastname"));
			model.put("age", req.session().attribute("age"));
			model.put("password", req.session().attribute("password"));
			model.put("template", "/users/user.vtl");
		}
		else
			model.put("template", "/mainpage/login.vtl");
		
		return model;		
	}
}