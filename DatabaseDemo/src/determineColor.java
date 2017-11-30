import java.time.LocalDate;

import javafx.scene.paint.Color;

@FunctionalInterface
public interface determineColor {
	Color decide(LocalDate date);
}
