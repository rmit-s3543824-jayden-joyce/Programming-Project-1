package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import model.Admin;
import model.FileTools;
import spark.Request;

public class AdminCtrl extends UserCtrl{

	public void initAdminDetails(Map<String, Object> model, Request req)
	{
		Admin admin = (Admin) FileTools.LoadUser(req.session().attribute("username"));
		
		super.initUserDetails(model, req, admin);
	}
	
	public void listPlayers(Map<String, Object> model, Request req)
	{
		List<String[]> playerList = Admin.searchPlayer(req.session().attribute("input"), FileTools.USER_DATA_FILE);
		
		model.put("playerList", playerList);
	}
	
	public void listTrans(Map<String, Object> model, Request req)
	{
		List<String[]> transList = null;
		
		try {
			transList = Admin.searchTrans(req.session().attribute("input"), FileTools.USER_TRANSACTION_LOG);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.put("transList", transList);
	}
}
