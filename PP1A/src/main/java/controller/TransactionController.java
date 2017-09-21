package controller;

import static spark.Spark.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.FileTools;
import model.InsufficientFundsException;
import model.Menu;
import model.NoSharesException;
import model.Player;
import model.Shares;
import model.Transaction;
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
	
	public static Route buyShareConfirm = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		Player player = req.session().attribute("playerObj");
		Shares buyingShare;
		int buyAmt = Integer.parseInt(req.queryParams("buyAmt"));
		Transaction transaction = null;
		
		try {
			buyingShare = FileTools.loadShare(req.queryParams("ASXCode"));
			transaction = player.getTradingAcc().buyShares(buyingShare, buyAmt);
		} catch (InsufficientFundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		putTransToModel(model, transaction);
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/buyShareConfirm.vtl"));
	};
	
	public static Route sellShareConfirm = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		Player player = req.session().attribute("playerObj");
		Shares sellingShare;
		int sellAmt = Integer.parseInt(req.queryParams("sellAmt"));
		Transaction transaction = null;
		
		try {
			sellingShare = FileTools.loadShare(req.queryParams("ASXCode"));
			transaction = player.getTradingAcc().sellShares(sellingShare, sellAmt);
		} catch (NoSharesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		putTransToModel(model, transaction);
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/buyShareConfirm.vtl"));
	};
	
	//loads transaction in model, used by both buy and sell in controller
	public static void putTransToModel(Map<String, Object> model, Transaction transaction)
	{
		model.put("transUserId", transaction.getID());
		model.put("ASXcode", transaction.getASXcode());
		model.put("shareVal", transaction.getShareVal());
		model.put("numShares", transaction.getNumShares());
		model.put("totalPrice", transaction.getTotalPrice());
		model.put("transType", transaction.getTransType());
		model.put("transDate", transaction.getDateTime());
	}
}