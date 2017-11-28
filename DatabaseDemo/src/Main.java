import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
		primaryStage.setTitle("Testing Calendar");
		CalendarPanel cal = new CalendarPanel(2018,12, new cellPopup() {
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
		Scene scene = new Scene(cal);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
