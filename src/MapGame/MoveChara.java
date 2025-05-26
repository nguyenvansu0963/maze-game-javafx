package MapGame;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MoveChara {
    public static final int TYPE_DOWN = 0;
    public static final int TYPE_LEFT = 1;
    public static final int TYPE_RIGHT = 2;
    public static final int TYPE_UP = 3;

    //独自機能2酸素残量の宣言
    public static int availableOxygen = 0;
    public static int initialOxygenAmount = 30;

    private final String[] directions = { "Down", "Left", "Right", "Up" };
    private final String[] animationNumbers = { "1", "2", "3" };
    private final String pngPathPre = "png/cat";
    private final String pngPathSuf = ".png";

    public static int posX;
    public static int posY;
    public static boolean isDead = false;
    public static boolean isLamp = false;
    public static boolean isShoe = false;
    public static boolean isMap  = false;
    public static int lampCounter = 0;
    public static int shoeCounter = 0;

    private MapData mapData;

    private Image[][] charaImages;
    private ImageView[] charaImageViews;
    private ImageAnimation[] charaImageAnimations;

    //独自機能1暗闇の切り替えように変更するマップの読みこみ
    private Image[] images;
    private ImageView[][] imageViews;

    private int charaDirection;

    MoveChara(int startX, int startY, MapData mapData) {
        this.mapData = mapData;
        

        charaImages = new Image[4][3];
        charaImageViews = new ImageView[4];
        charaImageAnimations = new ImageAnimation[4];

        for (int i = 0; i < 4; i++) {
            charaImages[i] = new Image[3];
            for (int j = 0; j < 3; j++) {
                charaImages[i][j] = new Image(
                        pngPathPre + directions[i] + animationNumbers[j] + pngPathSuf);
            }
            charaImageViews[i] = new ImageView(charaImages[i][0]);
            charaImageAnimations[i] = new ImageAnimation(
                    charaImageViews[i], charaImages[i]);
        }

        

        posX = startX;
        posY = startY;
        //独自機能1初期位置を明るくする
        mapData.changeLightCircle(posX,posY);

        //独自機能2酸素残量を設定する
        setAvailableOxygen(initialOxygenAmount);

        setCharaDirection(TYPE_RIGHT); // start with right-direction
    }

    // set the cat's direction
    public void setCharaDirection(int cd) {
        charaDirection = cd;
        for (int i = 0; i < 4; i++) {
            if (i == charaDirection) {
                charaImageAnimations[i].start();
            } else {
                charaImageAnimations[i].stop();
            }
        }
    }

    // check whether the cat can move on    必須機能4,5のアイテムとゴールに進めるようにした
    private boolean isMovable(int dx, int dy) {
        if (mapData.getMap(posX + dx, posY + dy) == MapData.TYPE_WALL) {
            return false;
        } else if (mapData.getMap(posX + dx, posY + dy) == MapData.TYPE_SPACE) {
            return true;
        } else if (mapData.getMap(posX + dx, posY + dy) == MapData.TYPE_GOAL) {
            return true;
        } else if(mapData.getMap(posX + dx, posY + dy) == MapData.TYPE_SHOE) {
            return true;
        } else if(mapData.getMap(posX + dx, posY + dy) == MapData.TYPE_GAS) {
            return true;
        } else if(mapData.getMap(posX + dx, posY + dy) == MapData.TYPE_LAMP) {
            return true;
        } else if(mapData.getMap(posX + dx, posY + dy) == MapData.TYPE_CHIZU) {
            return true;
        }
        return false;
    }
    public boolean getIsMoveable(int dx,int dy){
        return isMovable(dx,dy);
    }


    // move the cat
    public boolean move(int dx, int dy) {
        if(isDead){
            if(isLamp){
                mapData.changeDark(posX,posY);
                mapData.changeDarkCircleWide(posX,posY);
                mapData.changeLightCircle(1,1);
            }else{
                updateDarkForWarp(posX,posY,1,1);
                mapData.changeDark(posX,posY);
            }
            posX = 1;
            posY = 1;
            isDead = false;
            isLamp = false;
            isShoe = false;
            setAvailableOxygen(initialOxygenAmount);
            for(int index = 0;index < 4 + MapData.NumberOfGas;index++ ){
                mapData.setMap(mapData.itemposX[index],mapData.itemposY[index],mapData.itemNum[index]);
            }
        }
        if (isMovable(dx, dy)) {
            if(isMap){
                mapData.closeMap(posX,posY);
            }

            if(isLamp){
                if(lampCounter < 0){
                    isLamp = false;
                    mapData.changeDarkCircleWide(posX,posY);
                    updateDark(posX,posY,dx,dy);
                }else{
                    updateDarkWide(posX,posY,dx,dy);
                    lampCounter--;
                }
            }else{
                //独自機能1プレイヤーの周囲1マスを明るくする(24/1/12)
                updateDark(posX,posY,dx,dy);

            }
            
            //独自機能2酸素を使う
            useOxygen();

            //酸素残量ゲージを更新
            mapData.adjustOxygen(getAvailableOxygen());

            posX += dx;
            posY += dy;
	    System.out.println("chara[X,Y]:" + posX + "," + posY);

        //必須機能4&独自機能2,GASを取得したら酸素を増やす
        if(mapData.getMap(posX,posY) == 9 || mapData.getMap(posX,posY) == 13){
            plusAvailableOxygen(5);
            mapData.adjustOxygen(getAvailableOxygen());
        }
        //必須機能4&独自機能1,Lampを取得したらより明るくなる
        if(mapData.getMap(posX,posY) == 8 || mapData.getMap(posX,posY) == 12){
            isLamp = true;
            lampCounter = 10;
        }
        //必須機能4Shoeを取得したら足が速くなる
        if(mapData.getMap(posX,posY) == 10 || mapData.getMap(posX,posY) == 14){
            isShoe = true;
            shoeCounter = 10;
        }
        //必須機能4chizuを取得したら暗闇解除
        if(mapData.getMap(posX,posY) == 11 || mapData.getMap(posX,posY) == 15){
            mapData.showMap();
        }
        //必須機能5ゴールについたらゲームクリア(2024/01/11)
        if(posX == mapData.getGoalX() && posY == mapData.getGoalY()){
            System.out.println("Game Clear!");
            StageDB.getMainStage().hide();
            StageDB.getMainSound().stop();
            StageDB.getGameClearStage().show();
        }

         //必須機能4アイテムを取得したらアイテムを迷路上から消す
         if(mapData.getMap(posX,posY) == 8 || mapData.getMap(posX,posY) == 9 ||mapData.getMap(posX,posY) == 10 ||mapData.getMap(posX,posY) == 11){
            mapData.updateItem(posX,posY);
            mapData.updateItemView(posX,posY);
        }

        //独自機能2酸素が尽きたらゲームオーバー
        if(availableOxygen <= 0){
            try {
                System.out.println("Run out of oxygen!");
                StageDB.getMainStage().hide();
                StageDB.getMainSound().stop();
                StageDB.getGameOverStage().show();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
            return true;
        } else {
            return false;
        }
    }

    //独自機能1元いた場所を暗くし、新しい場所を明るくする(24/1/12)
    public void updateDark(int x,int y,int dx,int dy){
        mapData.changeDarkCircle(x,y);
        mapData.changeLightCircle(x + dx,y + dy);
    }

    public void updateDarkForWarp(int x,int y,int dx,int dy){
        mapData.changeDarkCircle(x,y);
        mapData.changeLightCircle(dx,dy);
    }
    public void updateDarkWide(int x,int y,int dx,int dy){
        mapData.changeDarkCircleWide(x,y);
        mapData.changeLightCircleWide(x + dx,y + dy);
    }
    public void updateDarkForWarpWide(int x,int y,int dx,int dy){
        mapData.changeDarkCircleWide(x,y);
        mapData.changeLightCircleWide(dx,dy);
    }

    // getter: direction of the cat
    public ImageView getCharaImageView() {
        return charaImageViews[charaDirection];
    }

    // getter: x-positon of the cat
    public int getPosX() {
        return posX;
    }

    // getter: y-positon of the cat
    public int getPosY() {
        return posY;
    }
    public static void setPosX(int x){
        posX = x;
    }
    public static void setPosY(int y){
        posY = y;
    }

    // Show the cat animation
    private class ImageAnimation extends AnimationTimer {

        private ImageView charaView = null;
        private Image[] charaImages = null;
        private int index = 0;

        private long duration = 500 * 1000000L; // 500[ms]
        private long startTime = 0;

        private long count = 0L;
        private long preCount;
        private boolean isPlus = true;

        public ImageAnimation(ImageView charaView, Image[] images) {
            this.charaView = charaView;
            this.charaImages = images;
            this.index = 0;
        }

        @Override
        public void handle(long now) {
            if (startTime == 0) {
                startTime = now;
            }

            preCount = count;
            count = (now - startTime) / duration;
            if (preCount != count) {
                if (isPlus) {
                    index++;
                } else {
                    index--;
                }
                if (index < 0 || 2 < index) {
                    index = 1;
                    isPlus = !isPlus; // true == !false, false == !true
                }
                charaView.setImage(charaImages[index]);
            }
        }
    }

    //独自機能2酸素残量を設定
    public static void setAvailableOxygen(int num){
        if(availableOxygen + num > 63){
            availableOxygen = 63;
        }else{
            availableOxygen = num;   
        }
    }

    //酸素残量を追加する
    public static void plusAvailableOxygen(int num){
        if(availableOxygen + num > 63){
            availableOxygen = 63;
        }else{
            availableOxygen = availableOxygen + num;   
        }
    }

    //酸素残量を1減らす
    public static void useOxygen(){
        availableOxygen = availableOxygen - 1;
    }

    //酸素残量を任意の値減らす
    public static void reduceAvailableOxygen(int num){
        if(availableOxygen - num < 0){
            availableOxygen = 0;
        }else{
            availableOxygen = availableOxygen - num;    
        }   
    }

    //酸素残量を取得する
    public static int getAvailableOxygen(){
        return availableOxygen;
    }
}
