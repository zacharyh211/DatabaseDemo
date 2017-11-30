import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
		primaryStage.setTitle("Demo Application");
		TabPane tabpane = new TabPane();
		tabpane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		//Delivery Calendar Tab
		Tab calTab = new Tab();
		CalendarPanel cal = new CalendarPanel(new cellPopup() {
			public Scene getPopupScene(LocalDate date) {
				{
					HBox hor = new HBox();
					SQLTable table = new SQLTable(DBInterface.getDeliveries(date));
					hor.getChildren().add(table);
					VBox vert = new VBox();
					Label label = new Label("  Functionality goes here  ");
					vert.getChildren().add(label);
					vert.setAlignment(Pos.CENTER);
					hor.getChildren().add(vert);
					return new Scene(hor);
				}
			}
		});
		calTab.setContent(cal);
		calTab.setText("Delivery Calendar");
		tabpane.getTabs().add(calTab);
		
		Scene scene = new Scene(tabpane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
