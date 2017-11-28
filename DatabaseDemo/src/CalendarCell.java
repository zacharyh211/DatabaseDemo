import java.time.LocalDate;

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

public abstract class CalendarCell extends StackPane {
	DBInterface db;
	LocalDate date;
	final private int dim = 100;
	
	public CalendarCell(DBInterface db, LocalDate date) {
		this.db = db;
		this.date = date;
		Rectangle rect = new Rectangle(dim,dim,Color.WHITE);
		rect.setStroke(Color.BLACK);
		rect.setStrokeWidth(0.2);
		getChildren().add(rect);
		if(date != null) {
			Label dateNum = new Label("" + date.getDayOfMonth());
			getChildren().add(dateNum);
			setAlignment(dateNum, Pos.TOP_LEFT);
			setMargin(dateNum, new Insets(4,0,0,4));
			rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent t) {
					Stage parent = (Stage) getScene().getWindow();
					Stage popup = new Stage();
					popup.setScene(popupScene());
					popup.initModality(Modality.WINDOW_MODAL);
					popup.initOwner(parent);
					
					popup.show();
				}
			});
		}
		else
			rect.setFill(Color.LIGHTGRAY);
	}
	
	public abstract Scene popupScene();
}