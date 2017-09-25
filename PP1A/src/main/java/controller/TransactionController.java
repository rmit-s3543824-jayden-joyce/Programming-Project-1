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
import model.TradingAcc;
import model.Transaction;
import model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class TransactionController {
	
	//Initialise transactionAccount page and details
	public static Route transactionAccount = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		Player player = req.session().attribute("playerObj");
		if (player != null)
		{
			TradingAcc trAcc = player.getTradingAcc();
			Transaction lastTrans = req.session().attribute("lastTrans");
			
			if (trAcc == null)
			{
				player.loadTrAcc();
			}
			model.put("userId", trAcc.getUser_ID());
			model.put("currBal", trAcc.getCurrBal());
			model.put("stockVal", trAcc.showCurrStockVal());
			
			//load last trans if attribute is null
			if (lastTrans == null)
			{
				lastTrans = Transaction.loadLastTrans(trAcc.getUser_ID());
				if (lastTrans != null)
				{
					req.session().attribute("lastTrans", lastTrans);
				}
			}
			
			putTransToModel(model, lastTrans);
			
			}
			
			model.put("userTemplate", "/users/TransactionAccount.vtl");
			
			return new VelocityTemplateEngine().render(new ModelAndView(model, "users/samplePlayerProfile.vtl"));
	};
	
	//page to show user transaction right after buying/selling
	public static Route ConfirmTransaction = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		Player player = req.session().attribute("playerObj");
		Transaction.TransType transType = Transaction.TransType.valueOf(req.queryParams("transType"));
		int amtShares = Integer.parseInt(req.queryParams("amtShares"));
		Shares share;
		Transaction transaction = null;
		
		try {
			share = FileTools.loadShare(req.queryParams("ASXCode"));
			
			//do buy/sell transaction depending on transType
			if (transType == Transaction.TransType.BUYING)
			{
				transaction = player.getTradingAcc().buyShares(share, amtShares);
			}
			else
			{
				transaction = player.getTradingAcc().sellShares(share, amtShares);
			}
			
			req.session().attribute("lastTrans", transaction);
			model.put("currBal", player.getTradingAcc().getCurrBal());
			model.put("stockVal", player.getTradingAcc().showCurrStockVal());
		} catch (InsufficientFundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		putTransToModel(model, transaction);
		
		return new VelocityTemplateEngine().render(new ModelAndView(model, "users/ConfirmTransaction.vtl"));
	};
	
	//loads transaction in model, used by both buy and sell in controller
	public static void putTransToModel(Map<String, Object> model, Transaction transaction)
	{
		if (transaction != null)
		{
			model.put("transUserId", transaction.getID());
			model.put("ASXcode", transaction.getASXcode());
			model.put("compName", transaction.getCompName());
			model.put("shareVal", transaction.getShareVal());
			model.put("numShares", transaction.getNumShares());
			model.put("totalPrice", transaction.getTotalPrice());
			model.put("transType", transaction.getTransType());
			model.put("transDate", transaction.getDateTime());
		}
	}
}