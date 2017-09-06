package View;

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
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Mainpage {
	public static Menu menu;
	
	//this class is used to test for Velocity template
	public static void helloWorld(){
		staticFiles.location("/public");
		
		menu = new Menu();
		get("/", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			// put "value" inside "cardholder"
			model.put("template", "/mainpage/mainpage.vtl");
			
			// The vtl files are located under the resources directory
			// The line below are required to make the page works
            return new VelocityTemplateEngine().render(
            		new ModelAndView(model, "layout.vtl")
            		);
        });
		
		get("/login", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			model.put("template", "/mainpage/login.vtl");
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
		});
		
		get("/redirectUser", (req, res) -> {
			Map<String, Object> model = testLogin(req);
						
			return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
		});
		
		get("/register", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			model.put("template", "/mainpage/register.vtl");
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
		});
		
		get("/regSuccess", (req, res) -> {
			Map<String, Object> model = register(req);
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
		});
		
		get("/admin", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			model.put("userTemplate", "/users/admin.vtl");
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
		});
		
		//test
		get("/user", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			model.put("userTemplate", "/users/user.vtl");
			model.put("username", req.session().attribute("username"));
			model.put("firstname", req.session().attribute("firstname"));
			model.put("lastname", req.session().attribute("lastname"));
			model.put("age", req.session().attribute("age"));
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
		});
		
		get("/EditProfile", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			model.put("userTemplate", "/users/editProfile.vtl");
			model.put("username", req.session().attribute("username"));
			model.put("firstname", req.session().attribute("firstname"));
			model.put("lastname", req.session().attribute("lastname"));
			model.put("age", req.session().attribute("age"));
			model.put("password", req.session().attribute("password"));
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
		});
		
		get("/ConfirmEditProfile", (req, res) -> {
			Map<String, Object> model = editProfile(req);
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
		});
		
		get("/TransactionAccount", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			model.put("userTemplate", "/users/TransactionAccount.vtl");
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
		});
		
		
		//to test table
		get("/testTable", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			model.put("template", "/mainpage/testTable.vtl");
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
		});
	}
	
	//function for login
	public static Map<String, Object> testLogin(Request req){
		Map<String, Object> model = new HashMap<>();
		
		String username	= req.queryParams("username");
		String password	= req.queryParams("password");
		
		User user = FileTools.LoadPlayer(username);
		
		String firstName = user.getFName();
		String lastName = user.getLName();
		int age = user.getAge();
				
		if(menu.login(username, password))
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
	
	public static Map<String, Object> register(Request req) throws IOException{
		Map<String, Object> model = new HashMap<>();
		
		String userName = req.queryParams("username");
		String firstName = req.queryParams("firstname");
		String lastName = req.queryParams("lastname");
		int age = Integer.parseInt(req.queryParams("age"));
		String password = req.queryParams("password");
		String confirmPassword = req.queryParams("confirmpassword");
		
		if(menu.register(userName, firstName, lastName, age, password, confirmPassword))
		{	
			model.put("template", "/mainpage/regSuccess.vtl");
		}
		else
			model.put("template", "/mainpage/register.vtl");
		
		return model;	
	}
	
	public static Map<String, Object> editProfile(Request req) throws IOException{
		Map<String, Object> model = new HashMap<>();
		
		String oldId = req.session().attribute("username");
		String newId = req.queryParams("username");
		String firstName = req.queryParams("firstname");
		String lastName = req.queryParams("lastname");
		int age = Integer.parseInt(req.queryParams("age"));
		String password = req.queryParams("password");
		
		if(Player.editProfile(oldId, newId, password, firstName, lastName, age) != null)
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
