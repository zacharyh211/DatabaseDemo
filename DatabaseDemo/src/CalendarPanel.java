
import java.time.DayOfWeek;
import java.time.LocalDate;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CalendarPanel extends GridPane {
	int year;
	int month;
	LocalDate[][] day;
	
	public CalendarPanel(int year, int month, cellPopup popup) {
		this.year = year;
		this.month = month;
		day = new LocalDate[7][7];
		LocalDate date = LocalDate.of(year, month, 1);
		int numDays = date.lengthOfMonth();
		int startDay = date.getDayOfWeek().getValue() % 7;
		int row = 0;
		int col = startDay;
		for(int i = 1; i <= numDays; i++) {
			day[row][col] = LocalDate.of(year, month, i);
			col++;
			if(col >= 7) {
				row++;
				col = 0;
			}
		}
		for(int c = 0; c < 7; c++) {
			Label day = new Label(DayOfWeek.of(c == 0 ? 7 : c).toString());
			add(day, c, 0, 1, 1);
			setHalignment(day, HPos.CENTER);
		}
		for(int r = 1; r < 7; r++)
			for(int c = 0; c < 7; c++)
				add(new CalendarCell(day[r-1][c], popup), c, r, 1, 1);
	}
}
