package MapGame;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public GridPane mapGrid;
    public ImageView[] mapImageViews;
    public ImageView[] oxygenImageViews;
    public GridPane oxygenGrid;
   

    @Override

    public void initialize(URL url, ResourceBundle rb) {
        mapData = new MapData(21, 15);
        chara = new MoveChara(1, 1, mapData);
        mapImageViews = new ImageView[mapData.getHeight() * mapData.getWidth()];
        oxygenImageViews = new ImageView[63];
        for (int y = 0; y < mapData.getHeight(); y ++) {
            for (int x = 0; x < mapData.getWidth(); x ++) {
                int index = y * mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x, y);
            }
        }
        for (int y = 0; y < 3; y ++) {
            for (int x = 0; x < 21; x ++) {
                int index = y * 21 + x;
                oxygenImageViews[index] = mapData.getOxygenImageView(x, y);
            }
        }
        drawMap(chara, mapData);
        drawOxygen();
    }

    // Draw the map
    public void drawMap(MoveChara c, MapData m) {
        int cx = c.getPosX();
        int cy = c.getPosY();
        mapGrid.getChildren().clear();
        for (int y = 0; y < mapData.getHeight(); y ++) {
            for (int x = 0; x < mapData.getWidth(); x ++) {
                int index = y * mapData.getWidth() + x;
                if (x == cx && y == cy) {
                    mapGrid.add(c.getCharaImageView(), x, y);
                } else {
                    mapGrid.add(mapImageViews[index], x, y);
                }
            }
        }
        
    }
    public void drawOxygen(){
        for (int y = 0; y < 3; y ++) {
            for (int x = 0; x < 21; x ++) {
                int index = y * 21 + x;
                oxygenGrid.add(oxygenImageViews[index], x, y);
            }
        }
        
    }

    // Get users' key actions
    public void keyAction(KeyEvent event) {
        int posX = chara.getPosX();
        int posY = chara.getPosY();
        if(mapData.getMap(posX,posY) == MapData.TYPE_SHOE) {
            for(int count = 0; count < 5;) {  
        KeyCode key = event.getCode();
        System.out.println("keycode:" + key);
            if (key == KeyCode.A) {
            leftButtonAction();
                leftButtonAction();
                count = count + 1;
            } else if (key == KeyCode.S) {
            downButtonAction();
                downButtonAction();
                count = count + 1;
            } else if (key == KeyCode.W) {
            upButtonAction();
                upButtonAction();
                count = count + 1;
            } else if (key == KeyCode.D) {
                rightButtonAction();
                rightButtonAction();
                count = count + 1;
            }
          }
        } else {
        KeyCode key = event.getCode();
        System.out.println("keycode:" + key);
        if (key == KeyCode.A) {
            leftButtonAction();
        } else if (key == KeyCode.S) {
            downButtonAction();
        } else if (key == KeyCode.W) {
            upButtonAction();
        } else if (key == KeyCode.D) {
            rightButtonAction();
        }
    }
}

    // Operations for going the cat up
    public void upButtonAction() {
        printAction("UP");
        chara.setCharaDirection(MoveChara.TYPE_UP);
        if(MoveChara.isShoe){
            if(MoveChara.shoeCounter < 0){
                MoveChara.isShoe = false;
                chara.move(0,-1);
                drawMap(chara, mapData);
            }else{
                if(chara.getIsMoveable(0,-2) && chara.getIsMoveable(0,-1)){
                    chara.move(0,-2);
                    checkItem(0,1);
                    if(MoveChara.isLamp){
                        mapData.changeDark(chara.posX,chara.posY+2);
                        mapData.changeLight(chara.posX,chara.posY);
                        chara.updateDarkForWarpWide(chara.posX,chara.posY-2,chara.posX,chara.posY);
                    }else{
                        mapData.changeDark(chara.posX,chara.posY+2);
                        mapData.changeLight(chara.posX,chara.posY);
                        chara.updateDarkForWarp(chara.posX,chara.posY-2,chara.posX,chara.posY);
                    }
                    drawMap(chara, mapData);
                    MoveChara.shoeCounter--;
                }else{
                    chara.move(0,-1);
                    drawMap(chara, mapData);
                }
            }
            
        }else{
            chara.move(0, -1);
            drawMap(chara, mapData);
        }
        
    }

    // Operations for going the cat down
    public void downButtonAction() {
        printAction("DOWN");
        chara.setCharaDirection(MoveChara.TYPE_DOWN);
        if(MoveChara.isShoe){
            if(MoveChara.shoeCounter < 0){
                MoveChara.isShoe = false;
                chara.move(0,1);
                drawMap(chara, mapData);
            }else{
                if(chara.getIsMoveable(0,2) && chara.getIsMoveable(0,1)){
                    chara.move(0,2);
                    checkItem(0,-1);
                    if(MoveChara.isLamp){
                        mapData.changeDark(chara.posX,chara.posY-2);
                        mapData.changeLight(chara.posX,chara.posY);
                        chara.updateDarkForWarpWide(chara.posX,chara.posY-2,chara.posX,chara.posY);
                    }else{
                        mapData.changeDark(chara.posX,chara.posY-2);
                        mapData.changeLight(chara.posX,chara.posY);
                        chara.updateDarkForWarp(chara.posX,chara.posY-2,chara.posX,chara.posY);
                    }
                    MoveChara.shoeCounter--;
                    drawMap(chara, mapData);
                }else{
                    chara.move(0,1);
                    drawMap(chara, mapData);
                }
            }
            
        }else{
            chara.move(0, 1);
            drawMap(chara, mapData);
        }
    }

    // Operations for going the cat right
    public void leftButtonAction() {
        printAction("LEFT");
        chara.setCharaDirection(MoveChara.TYPE_LEFT);
        if(MoveChara.isShoe){
            if(MoveChara.shoeCounter < 0){
                MoveChara.isShoe = false;
                chara.move(-1,0);
                drawMap(chara, mapData);
            }else{
                if(chara.getIsMoveable(-1,0) && chara.getIsMoveable(-2,0)){
                    chara.move(-2,0);
                    checkItem(1,0);
                    if(MoveChara.isLamp){
                        mapData.changeDark(chara.posX+2,chara.posY);
                        mapData.changeLight(chara.posX,chara.posY);
                        chara.updateDarkForWarpWide(chara.posX+2,chara.posY,chara.posX,chara.posY);
                    }else{
                        mapData.changeDark(chara.posX+2,chara.posY);
                        mapData.changeLight(chara.posX,chara.posY);
                        chara.updateDarkForWarp(chara.posX+2,chara.posY,chara.posX,chara.posY);
                    }
                    MoveChara.shoeCounter--;
                    drawMap(chara, mapData);
                }else{
                    chara.move(-1,0);
                    drawMap(chara, mapData);
                }
            }
            
        }else{
            chara.move(-1, 0);
            drawMap(chara, mapData);
        }
    }

    // Operations for going the cat right
    public void rightButtonAction() {
        printAction("RIGHT");
        chara.setCharaDirection(MoveChara.TYPE_RIGHT);
        if(MoveChara.isShoe){
            if(MoveChara.shoeCounter < 0){
                MoveChara.isShoe = false;
                chara.move(1,0);
                drawMap(chara, mapData);
            }else{
                if(chara.getIsMoveable(1,0) && chara.getIsMoveable(2,0)){
                    chara.move(2,0);
                    checkItem(-1,0);
                    if(MoveChara.isLamp){
                        mapData.changeDark(chara.posX-2,chara.posY);
                        mapData.changeLight(chara.posX,chara.posY);
                        chara.updateDarkForWarpWide(chara.posX-2,chara.posY,chara.posX,chara.posY);
                    }else{
                        mapData.changeDark(chara.posX-2,chara.posY);
                        mapData.changeLight(chara.posX,chara.posY);
                        chara.updateDarkForWarp(chara.posX-2,chara.posY,chara.posX,chara.posY);
                    }
                    MoveChara.shoeCounter--;
                    drawMap(chara, mapData);
                }else{
                    chara.move(1,0);
                    drawMap(chara, mapData);
                }
            }
            
        }else{
            chara.move(1, 0);
            drawMap(chara, mapData);
        }
    }

    @FXML
    public void func1ButtonAction(ActionEvent event) {
        try {
            System.out.println("func1");
            StageDB.getMainStage().hide();
            StageDB.getMainSound().stop();
            StageDB.getGameOverStage().show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

	//Func2ボタンでマップ生成（12.22編集）
    @FXML
    public void func2ButtonAction(ActionEvent event) {
        System.out.println("func2");
        StageDB.getMainStage().hide();
        StageDB.setMainstage();
        StageDB.getMainStage().show();
    }

    @FXML
    public void func3ButtonAction(ActionEvent event) {
        System.out.println("func3: Nothing to do");
        MoveChara.isLamp = true;
        MoveChara.lampCounter = 10;
    }

    @FXML
    public void func4ButtonAction(ActionEvent event) {
        System.out.println("func4: Nothing to do");
        MoveChara.isShoe = true;
        MoveChara.shoeCounter = 5;
        mapData.showMap();
    }


    // Print actions of user inputs
    public void printAction(String actionString) {
        System.out.println("Action: " + actionString);
    }

    //必須機能4シューズ使用時のアイテム回収
    public void checkItem(int dx,int dy){
        if(mapData.getMap(chara.posX + dx,chara.posY + dy) == 8 ||mapData.getMap(chara.posX + dx,chara.posY + dy) == 12 ){
            MoveChara.isLamp = true;
            MoveChara.lampCounter = 10;
        }
        if(mapData.getMap(chara.posX + dx,chara.posY + dy) == 9 ||mapData.getMap(chara.posX + dx,chara.posY + dy) == 13 ){
            chara.plusAvailableOxygen(5);
            mapData.adjustOxygen(chara.getAvailableOxygen());
        }
        if(mapData.getMap(chara.posX + dx,chara.posY + dy) == 10 ||mapData.getMap(chara.posX + dx,chara.posY + dy) == 14 ){
            MoveChara.isShoe = true;
            MoveChara.shoeCounter = 10;
        }
        if(mapData.getMap(chara.posX + dx,chara.posY + dy) == 10 ||mapData.getMap(chara.posX + dx,chara.posY + dy) == 14 ){
            mapData.showMap();
        }
        if(mapData.getMap(chara.posX + dx,chara.posY + dy) == 8 || mapData.getMap(chara.posX + dx,chara.posY + dy) == 9 ||mapData.getMap(chara.posX + dx,chara.posY + dy) == 10 ||mapData.getMap(chara.posX + dx,chara.posY + dy) == 11){
            mapData.updateItem(chara.posX + dx,chara.posY + dy);
            mapData.updateItemView(chara.posX + dx,chara.posY + dy);
        }

    }

}
