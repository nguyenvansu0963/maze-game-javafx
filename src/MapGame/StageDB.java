package MapGame;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

class StageDB {

	static private Stage titleStage = null;
	static private Stage mainStage = null;
	static private Stage gameOverStage = null;
	static private Stage gameClearStage = null;
	//static private Media titleSound = null;  ゲームオーバーサウンド
	//static private MediaPlayer titleMp = null;
	static private MediaPlayer mainSound = null;
	static private MediaPlayer gameOverSound = null;
	static private Class mainClass;
	static private final String mainSoundFileName = "sound/ExploreTheCave.mp3"; // BGM by OtoLogic
	static private final String gameOverSoundFileName = "sound/GameOverSound.mp3";

	public static void setMainClass(Class mainClass) {
		StageDB.mainClass = mainClass;
	}

	/*static public MediaPlayer getTitleSound() {
	    if (titleSound == null) {
	        try {
	            titleSound = new Media(new File(mainSoundFileName).toURI().toString());
	            titleMp = new MediaPlayer(titleSound);
	            titleMp.setCycleCount(MediaPlayer.INDEFINITE); // loop play
	        } catch (Exception io) {
	            System.err.print(io.getMessage());
	        }
	    }
	    return titleMp;
	}*/

	public static MediaPlayer getMainSound() {
		if (mainSound == null) {
			try {
				Media m = new Media(new File(mainSoundFileName).toURI().toString());
				MediaPlayer mp = new MediaPlayer(m);
				mp.setCycleCount(MediaPlayer.INDEFINITE); // loop play
				mp.setRate(1.0); // 1.0 = normal speed
				mp.setVolume(0.5); // volume from 0.0 to 1.0
				mainSound = mp;
			} catch (Exception io) {
				System.err.print(io.getMessage());
			}
		}
		return mainSound;
	}

	public static MediaPlayer getGameOverSound() {
		if (gameOverSound == null) {
			try {
				Media m = new Media(new File(gameOverSoundFileName).toURI().toString());
				MediaPlayer mp = new MediaPlayer(m);
				mp.setCycleCount(MediaPlayer.INDEFINITE);
				mp.setRate(1.0);
				mp.setVolume(0.5);
				gameOverSound = mp;
			} catch (Exception io) {
				System.err.print(io.getMessage());
			}
		}
		return gameOverSound;
	}

	static public Stage getTitleStage() {
		if (titleStage == null) {
			try {
				FXMLLoader loader = new FXMLLoader(mainClass.getResource("title.fxml"));
				VBox root = loader.load();
				Scene scene = new Scene(root);
				titleStage = new Stage();
				titleStage.setScene(scene);
			} catch (IOException ioe) {
				System.err.println(ioe);
			}
		}
		return titleStage;
	}

	static public Stage getGameClearStage() {
		if (gameClearStage == null) {
			try {
				FXMLLoader loader = new FXMLLoader(mainClass.getResource("gameClear.fxml"));
				VBox root = loader.load();
				Scene scene = new Scene(root);
				gameClearStage = new Stage();
				gameClearStage.setScene(scene);
			} catch (IOException ioe) {
				System.err.println(ioe);
			}
		}
		return gameClearStage;
	}

	public static Stage getMainStage() {
		if (mainStage == null) {
			try {
				FXMLLoader loader = new FXMLLoader(mainClass.getResource("MapGame2.fxml"));
				VBox root = loader.load();
				Scene scene = new Scene(root);
				mainStage = new Stage();
				mainStage.setScene(scene);
			} catch (IOException ioe) {
				System.err.println("Error loading MapGame2.fxml: " + ioe.getMessage());
				ioe.printStackTrace();
			}
		}
		return mainStage;
	}

	//マップの更新(12.22編集)
	public static Stage setMainstage() {
		mainStage = null;
		return getMainStage();
	}

	public static Stage getGameOverStage() {
		if (gameOverStage == null) {
			try {
				System.out.println("StageDB:getGameOverStage()");
				FXMLLoader loader = new FXMLLoader(mainClass.getResource("MapGameOver.fxml"));
				VBox root = loader.load();
				Scene scene = new Scene(root);
				gameOverStage = new Stage();
				gameOverStage.setScene(scene);
			} catch (IOException ioe) {
				System.err.println(ioe);
			}
		}
		return gameOverStage;
	}
}
