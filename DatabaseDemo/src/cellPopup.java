import java.time.LocalDate;

import javafx.stage.Stage;


@FunctionalInterface
public interface cellPopup {
	public Stage getPopupStage(LocalDate date);
}
