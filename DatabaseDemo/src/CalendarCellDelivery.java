import java.time.LocalDate;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CalendarCellDelivery extends CalendarCell {
	
	public CalendarCellDelivery(LocalDate date) {
		super(date);
	}

	@Override
	public Scene popupScene() {
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
