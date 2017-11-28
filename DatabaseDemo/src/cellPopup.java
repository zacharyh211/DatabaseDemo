import java.time.LocalDate;

import javafx.scene.Scene;


@FunctionalInterface
public interface cellPopup {
	public Scene getPopupScene(LocalDate date);
}
