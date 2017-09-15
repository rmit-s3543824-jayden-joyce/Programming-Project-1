package controller;

import java.util.Map;

import spark.Request;
import model.User;

public class UserCtrl {

	public void initUserDetails(Map<String, Object> model, Request req, User user)
	{
		if (user.getPassword().equals(req.session().attribute("password")))
		{
			model.put("username", user.getID());
			model.put("firstname", user.getFName());
			model.put("lastname", user.getLName());
			model.put("age", user.getAge());
			model.put("password", user.getPassword());
		}
	}
}
