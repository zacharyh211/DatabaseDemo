
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String [] args) throws Exception {
		Application.launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/restaurant_schema?user=root&autoReconnect=true&useSSL=false");
		//Connection connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/musketeers?user=root&password=beyblade&autoReconnect=true&useSSL=false");
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
		cal.setPadding(new Insets(10,10,10,10));
		calTab.setContent(cal);
		calTab.setText("Delivery Calendar");
		tabpane.getTabs().add(calTab);
		
		//Inventory Tab
		Tab inventTab = new Tab();
		inventTab.setText("Ingredient Inventory");
		inventTab.setContent(new SQLTable(DBInterface.getIntegredients()));
		tabpane.getTabs().add(inventTab);
		
		//Menu Tab
		Tab menuTab = new Tab();
		ScrollPane menu = generateMenu(DBInterface.getMenuItems());
		menuTab.setText("Restaurant Menu");
		menuTab.setContent(menu);
		tabpane.getTabs().add(menuTab);
		
		tabpane.setMaxWidth(1400);
		tabpane.setMaxHeight(800);
		
		//Shift Calendar Tab
		Tab shiftTab = new Tab();
		CalendarPanel shiftCal = new CalendarPanel(new cellPopup() {
			public Scene getPopupScene(LocalDate date) {
				{
					HBox hor = new HBox();
					SQLTable table = new SQLTable(DBInterface.getWaiterShifts(date, "Breakfast"));
					hor.getChildren().add(table);
					VBox vert = new VBox();
					
					ComboBox box = new ComboBox();
				    box.getItems().addAll("Breakfast", "Lunch", "Dinner");
				    box.setValue("Breakfast");    
				    box.valueProperty().addListener(new ChangeListener<String>() {
				        @Override public void changed(ObservableValue<? extends String> ov, String old, String newStr) {
				        	SQLTable temp = new SQLTable(DBInterface.getWaiterShifts(date, newStr));
				        	hor.getChildren().set(0,temp);
				        }   
				    });
					vert.setPadding(new Insets(5,5,5,5));
					vert.getChildren().add(box);
					hor.getChildren().add(vert);
					return new Scene(hor);
				}
			}
		}, new determineColor() {
			public Color decide(LocalDate date) {
				return Color.LIGHTBLUE;
			}
		});
		shiftTab.setContent(shiftCal);
		shiftTab.setText("Shift Calendar");
		tabpane.getTabs().add(shiftTab);
		
		Scene scene = new Scene(tabpane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private static ScrollPane generateMenu(ResultSet rs) {
		try {
			List<String> name = new ArrayList<String>();
			List<String> inLists = new ArrayList<String>();
			List<Double> price = new ArrayList<Double>();
			
			String prev = "";
			List<String> list = new ArrayList<String>();
			while(rs.next()) {
				String cur = rs.getString(1);
				if(!prev.equals(cur)) {
					if(!prev.equals("")) {
						Collections.sort(list);
						inLists.add(list.toString().substring(1,list.toString().length()-1));
						list.clear();
					}
					name.add(cur);
					price.add(rs.getDouble(2));
					prev = cur;
				}
				list.add(rs.getString(3));
			}
			Collections.sort(list);
			inLists.add(list.toString().substring(1,list.toString().length()-1));
			VBox menu = new VBox();
			menu.setStyle("-fx-background-color: WHITE");
			
			BorderPane header = new BorderPane();
			Text centerText = new Text("Menu");
			centerText.setFont(Font.font("Verdana", 30));
			header.setCenter(centerText);
			menu.getChildren().add(header);
			for(int i = 0; i < name.size(); i++) {
				Text nameText = new Text(name.get(i));
				Text ingredText = new Text(inLists.get(i));
				Text priceText = new Text(String.format("%.2f", price.get(i)));
				
				nameText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
				ingredText.setFont(Font.font("Verdana", FontPosture.ITALIC, 10));
				ingredText.setWrappingWidth(500);
				BorderPane line = new BorderPane();
				line.setPrefWidth(800);
				line.setLeft(nameText);
				line.setRight(priceText);
				
				line.setPadding(new Insets(30,0,2,0));
				menu.setPadding(new Insets(5,5,5,5));
				
				menu.getChildren().add(line);
				menu.getChildren().add(ingredText);
			}
			ScrollPane scroll = new ScrollPane();
			scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
			scroll.setContent(menu);
			return scroll;
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
