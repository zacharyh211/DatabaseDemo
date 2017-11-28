import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String [] args) throws Exception {
		Application.launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/restaurant_schema?user=root&autoReconnect=true&useSSL=false");
		Statement statement = connect.createStatement();
		DBInterface.setStatement(statement);
		primaryStage.setTitle("Testing Calendar");
		CalendarPanel cal = new CalendarPanel(2018,12);
		Scene scene = new Scene(cal);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
