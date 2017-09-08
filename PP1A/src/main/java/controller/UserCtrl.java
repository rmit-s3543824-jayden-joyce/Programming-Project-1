package controller;

import java.util.Map;

import model.User;

public class UserCtrl {

	public void initUserDetails(Map<String, Object> model, User user)
	{
		model.put("username", user.getID());
		model.put("fname", user.getFName());
		model.put("lname", user.getLName());
		model.put("age", user.getAge());
		model.put("password", user.getPassword());
	}
}
