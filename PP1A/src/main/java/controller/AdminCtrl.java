package controller;

import java.util.Map;

import model.Admin;
import model.FileTools;
import spark.Request;

public class AdminCtrl extends UserCtrl{

	public void initAdminDetails(Map<String, Object> model, Request req)
	{
		FileTools fileTool = new FileTools();
		Admin admin = (Admin) fileTool.LoadUser(req.session().attribute("username"));
		
		super.initUserDetails(model, req, admin);
	}
}
