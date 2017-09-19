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

public class TransactionController {
	public static Route transactionAccount = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		model.put("userTemplate", "/users/TransactionAccount.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	public void buy(Request req)
	{
		
	}
	
	public void sell(Request req)
	{
		
	}
}