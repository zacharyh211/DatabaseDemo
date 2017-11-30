import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
					Button add_btn = new Button("  Add Delivery  ");
					add_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent e) {
							InputForm.open(table,date);
						}
					});
					vert.setPadding(new Insets(5,5,5,5));
					vert.getChildren().add(add_btn);
					hor.getChildren().add(vert);
					return new Scene(hor);
				}
			}
		}, new determineColor() {
			public Color decide(LocalDate date) {
				if(DBInterface.existDeliveries(date))
					return Color.LIGHTBLUE;
				else
					return Color.WHITE;
			}
		});
		calTab.setContent(cal);
		calTab.setText("Delivery Calendar");
		tabpane.getTabs().add(calTab);
		
		//Inventory Tab
		Tab inventTab = new Tab();
		inventTab.setText("Ingredient Inventory");
		inventTab.setContent(new SQLTable(DBInterface.getIntegredients()));
		tabpane.getTabs().add(inventTab);
		
		Scene scene = new Scene(tabpane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
