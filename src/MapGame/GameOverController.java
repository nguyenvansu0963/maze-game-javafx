package MapGame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GameOverController {

	@FXML
	void onGameOverAction(ActionEvent event) {
		try {
			StageDB.getGameOverStage().hide();
			StageDB.getMainSound().stop();
			StageDB.getMainStage().show();
			StageDB.getMainSound().play();
			MoveChara.initialOxygenAmount = MoveChara.initialOxygenAmount + 5;
			MoveChara.isDead = true;

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}