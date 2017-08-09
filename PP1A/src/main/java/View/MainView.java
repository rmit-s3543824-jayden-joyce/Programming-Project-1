package View;

import static spark.Spark.get;

public class MainView {

	public static void registerPage() {
		// TODO Auto-generated method stub
		get("/register", (req, res) -> ""
        		+ "This is the registerPage");
	}

}
