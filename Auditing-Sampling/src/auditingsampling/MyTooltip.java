package auditingsampling;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class MyTooltip{

	public MyTooltip() {
		create_tooltip("");
	}
	
	public MyTooltip(String msg) {
		create_tooltip(msg);
	}

	public Tooltip create_tooltip(String msg) {
		Tooltip tooltip = new Tooltip();
		tooltip.setShowDelay(Duration.millis(100));
		tooltip.setShowDuration(Duration.minutes(5));
		tooltip.setStyle("-fx-font-size: 12");
		tooltip.setWrapText(true);
		tooltip.setText(msg);
		return tooltip;		
	}
}
