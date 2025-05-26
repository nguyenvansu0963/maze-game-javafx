package MapGame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TitleController {

	@FXML
	void onStartAction(ActionEvent event) {
		try {
			System.out.println("move to gameMain window!");
			StageDB.getTitleStage().hide();
			//StageDB.getTitleSound().stop();
			StageDB.getMainStage().show();
			StageDB.getMainSound().play();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}