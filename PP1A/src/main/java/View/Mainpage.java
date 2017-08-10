package View;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import model.Menu;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class Mainpage {
	
	
	//this class is used to test for Velocity template
	public static void helloWorld(){
		get("/", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			//put "value" inside "cardholder"
			model.put("template", "hello.vtl");
			
			// The vtl files are located under the resources directory
			//The line below are required to make the page works
            return new VelocityTemplateEngine().render(
            		new ModelAndView(model, "layout.vtl")
            		);
        });
		
		get("/login", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			model.put("template", "login.vtl");
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
		});
		
		get("/redirectUser", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			String username	= req.queryParams("username");
			String password	= req.queryParams("password");
			
			if(Menu.login(username, password))
				model.put("template", "user.vtl");
			else
				model.put("template", "login.vtl");
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
		});
		
		get("/register", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			model.put("template", "register.vtl");
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
		});
		
		get("/admin", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			model.put("template", "admin.vtl");
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
		});
		
		get("/user", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			model.put("template", "user.vtl");
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
		});
	}
}
