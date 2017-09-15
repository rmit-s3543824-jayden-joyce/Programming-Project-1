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

public class CompanyPageController {
	public static Route companyPage = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		model.put("template", "/utils/CompanyDetails.vtl");
		model.put("chart", "/utils/chart.vtl");
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
}
