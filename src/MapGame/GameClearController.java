package MapGame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GameClearController {

	@FXML
	void onStartAction(ActionEvent event) {
		try {
			System.out.println("move to title window!");
			StageDB.getGameClearStage().hide();
			//StageDB.getTitleSound().stop();
			StageDB.getTitleStage().show();
			MoveChara.isDead = true;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}