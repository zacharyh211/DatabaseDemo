import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CalendarCell extends StackPane {
	LocalDate date;
	final private int dim = 100;
	
	public CalendarCell(LocalDate date, cellPopup func, determineColor detCol) {
		this.date = date;
		Rectangle rect = new Rectangle(dim,dim);
		rect.setStroke(Color.BLACK);
		rect.setStrokeWidth(0.2);
		getChildren().add(rect);
		if(date != null) {
			Label dateNum = new Label("" + date.getDayOfMonth());
			getChildren().add(dateNum);
			setAlignment(dateNum, Pos.TOP_LEFT);
			setMargin(dateNum, new Insets(4,0,0,4));
			rect.setFill(detCol.decide(date));
			rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent t) {
					Stage parent = (Stage) getScene().getWindow();
					Stage popup = new Stage();
					popup.setScene(func.getPopupScene(date));
					popup.initModality(Modality.WINDOW_MODAL);
					popup.initOwner(parent);
					popup.setTitle("Deliveries on " + date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
					popup.show();
				}
			});
		}
		else
			rect.setFill(Color.LIGHTGRAY);
	}
}
