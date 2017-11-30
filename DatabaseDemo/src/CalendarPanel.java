
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class CalendarPanel extends GridPane {
	YearMonth ym;
	LocalDate[][] day;
	cellPopup popup;
	Label label;
	
	public CalendarPanel(cellPopup popup) {
		label = new Label();
		this.popup = popup;
		update(YearMonth.now());
		
		//Set month changer
		BorderPane top = new BorderPane();
		top.setPadding(new Insets(5,5,5,5));
		Button left = new Button(" << ");
		Button right = new Button(" >> ");
		left.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				YearMonth prevMonth = ym.minusMonths(1);
				update(prevMonth);
			}
		});
		right.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				YearMonth prevMonth = ym.plusMonths(1);
				update(prevMonth);
			}
		});
		
		left.setFocusTraversable(false);
		right.setFocusTraversable(false);
		
		top.setLeft(left);
		top.setRight(right);
		top.setCenter(label);
		add(top,0,0,7,1);
		
		//Set day of week labels
		for(int c = 0; c < 7; c++) {
			Label day = new Label(DayOfWeek.of(c == 0 ? 7 : c).toString());
			add(day, c, 1, 1, 1);
			setHalignment(day, HPos.CENTER);
		}
	}
	
	public void update(YearMonth newYM) {
		ym = newYM;
		day = new LocalDate[6][7];
		label.setText(ym.getMonth() + " " + ym.getYear());
		LocalDate date = ym.atDay(1);
		int numDays = date.lengthOfMonth();
		int startDay = date.getDayOfWeek().getValue() % 7;
		int row = 0;
		int col = startDay;
		for(int i = 1; i <= numDays; i++) {
			day[row][col] = ym.atDay(i);
			col++;
			if(col >= 7) {
				row++;
				col = 0;
			}
		}
		for(int r = 0; r < 6; r++)
			for(int c = 0; c < 7; c++)
				add(new CalendarCell(day[r][c], popup), c, r+2, 1, 1);
	}
}
