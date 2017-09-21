package controller;

import static spark.Spark.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

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

public class LeaderboardController {
	public static Route leaderboard = (req, res) -> {
		Map<String, Object> model = new HashMap<>();
		
		leaderboard();
		model.put("template", "/utils/leaderboard.vtl");
				
		return new VelocityTemplateEngine().render(new ModelAndView(model, "layout.vtl"));
	};
	
	public static void leaderboard(){
		//limit map to 100
		Map<String, Integer> board = new HashMap<>();
		FileTools ft = new FileTools();
		
		//only player with transaction account will be listed on leader board
		//get player from accountdata 
		try {
			List<String[]> accountData = FileTools.readCSV(ft.USER_ACC_FILE);
			List<String[]> toCSV = new ArrayList<>();
			String[] temp = new String[3];
			
			//merge data
			for(int i=1; i<accountData.size(); i++){
				String playerId, balance;
				int playerScore = 0;
				
				playerId = accountData.get(i)[0];
				balance = accountData.get(i)[1];
				
//				BigDecimal totalSharesValue = TradingAcc.showCurrStockVal(playerId);
//				
//				//calculating marks
//				if(totalSharesValue != null){
//					int tempValue = totalSharesValue.intValue();
//					playerScore += tempValue;
//				}
				
				int tempBalance = (int)Double.parseDouble(balance);
				
				playerScore += tempBalance;
				
				board.put(playerId, playerScore);
			}
			
			//sort the temporary board
			board = sortByValue(board);
			
			//columes for leaderboard
			temp[0] = "Rank";
			temp[1] = "User";
			temp[2] = "Points";
			
			toCSV.add(temp);
			
			//test printing board
			Map<String, Integer> reversedMap = new TreeMap<String, Integer>(board);
			int j=1;
			for (Map.Entry entry : reversedMap.entrySet()) {
				String[] temp2 = new String[3];
				temp2[0] = Integer.toString(j);
				temp2[1] = entry.getKey().toString();
				temp2[2] = entry.getValue().toString();
				
				toCSV.add(temp2);
//			    System.out.println(j+"," +entry.getKey() + ", " + entry.getValue());
			    j++;
			}
			
			ft.overwriteCSV(toCSV, ft.LEADERBOARD);
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
	}
	
	/* sorting hash map by value, altered from 
	 * https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
	 */
	public static <String, Integer extends Comparable<? super Integer>> Map<String, Integer> sortByValue( Map<String, Integer> map) {
		
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
		
		for (Map.Entry<String, Integer> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
