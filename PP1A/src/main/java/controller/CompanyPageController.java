package controller;

import static spark.Spark.*;

import java.io.IOException;
import java.util.ArrayList;
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

public class CompanyPageController {
	private TradingAcc ta;
	
	public static Route companyPage = (req, res) -> {		
		Map<String, Object> model = new HashMap<>();
		CompanyPageController controller = new CompanyPageController();
		
		model.put("template", "/utils/CompanyDetails.vtl");
		model.put("chart", "/utils/chart.vtl");
		
		//if player exist
		if(req.session().attribute("username") != null){
			String username = req.session().attribute("username");
			model.put("username", req.session().attribute("username"));
			controller.userDetails(model, username);
		}
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	public void userDetails(Map<String, Object> model, String username){
		this.ta = new TradingAcc(username);
		
		ArrayList<String[]> shareList = ta.getSharesOwned();
		
		//list user shares
		if(shareList != null){
			model.put("shareList", shareList);
		}		
	}
}
