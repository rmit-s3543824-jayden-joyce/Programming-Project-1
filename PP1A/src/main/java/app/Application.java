package app;

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

public class Application {
	public static Menu menu;
	
	//this class is used to test for Velocity template
	public static void main(String[] args){
		staticFiles.location("/public");
		port(getHerokuAssignedPort());
		menu = new Menu();
		
		get("/",                       controller.LoginController.mainPage);
		get("/login",                  controller.LoginController.loginPage);
		get("/redirectUser",           controller.LoginController.redirectUser);
		get("/register",               controller.RegisterController.registerPage);
		get("/regSuccess",             controller.RegisterController.regSuccess);
		get("/user",                   controller.UserController.userPage);		
		get("/EditProfile",            controller.UserController.editPage);
		get("/ConfirmEditProfile",     controller.UserController.confirmEditProfile);
		get("/admin",                  controller.AdminController.adminPage);
		get("/TransactionAccount",     controller.TransactionController.transactionAccount);
		
		//to test table
		get("/testTable", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			
			model.put("template", "/mainpage/testTable.vtl");
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
		});
	}
	
	static int getHerokuAssignedPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if(processBuilder.environment().get("PORT") != null)
		{
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 4567;
	}
}
