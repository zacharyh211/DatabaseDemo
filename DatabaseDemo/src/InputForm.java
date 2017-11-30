import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InputForm {
	public static void open(SQLTable table, LocalDate date) {
		Stage popup = new Stage();
		ObservableList<TableColumn<ObservableList<String>, ?>> cols = table.getColumns();
		List<String> headers = new ArrayList<String>();
		for(TableColumn<ObservableList<String>,?> tc : cols)
			headers.add(tc.getText());
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(5,5,5,5));
		List<TextField> inputs = new ArrayList<TextField>();
		for(int i = 0; i < headers.size(); i++) {
			Label label = new Label(headers.get(i));
			grid.add(label, i, 0);
			TextField input = new TextField();
			grid.add(input, i, 1);	
			inputs.add(input);
		}
		HBox buttonRow = new HBox();
		Button submit = new Button("Add");
		submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				String s = "(";
				for(TextField t : inputs)
					s = s + t.getText() + ",";
				if(s.length() > 1)
					s = s.substring(0, s.length() - 1);
				s = s + ")";
				System.out.println("I'm not doing anything, but I got this input: " + s);
				popup.close();
			}
		});
		buttonRow.getChildren().add(submit);
		buttonRow.setAlignment(Pos.BOTTOM_RIGHT);
		grid.add(buttonRow, 0, 2, headers.size(), 1);
		Scene scene = new Scene(grid);
		popup.setScene(scene);
		popup.initModality(Modality.WINDOW_MODAL);
		popup.initOwner((Stage)table.getScene().getWindow());
		popup.show();
	}
}
