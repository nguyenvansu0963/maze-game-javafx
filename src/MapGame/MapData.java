package MapGame;

//必須機能5用に追加
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapData {
    public static final int TYPE_SPACE = 0;
    public static final int TYPE_WALL = 1;
    public static final int TYPE_OTHERS = 2;
    public static final int TYPE_GOAL = 3;

    //独自機能1暗闇の定義(24/1/12)
    public static final int TYPE_DARK_SPACE = 4;
    public static final int TYPE_DARK_WALL = 5;
    public static final int TYPE_DARK_OTHERS = 6;
    public static final int TYPE_DARK_GOAL = 7;

    public static final int TYPE_LAMP = 8;
    public static final int TYPE_GAS = 9;
    public static final int TYPE_SHOE = 10;
    public static final int TYPE_CHIZU = 11;

    public static final int TYPE_DARK_LAMP = 12;
    public static final int TYPE_DARK_GAS = 13;
    public static final int TYPE_DARK_SHOE = 14;
    public static final int TYPE_DARK_CHIZU = 15;

    //独自機能2
    public static final int TYPE_OXYGEN_EMPTY = 0;
    public static final int TYPE_OXYGEN_FULL = 1;

    private static final String mapImageFiles[] = {
            "png/CaveSpace.png",
            "png/CaveWall.png",
            "png/CaveSpace.png",
            "png/goal.png",
            "png/Dark.png",
            "png/Dark.png",
            "png/Dark.png",
            "png/Dark.png",
            "png/lamp.png",
            "png/gas.png",
            "png/shoe.png",
            "png/chizu.png",
            "png/Dark.png",
            "png/Dark.png",
            "png/Dark.png",
            "png/Dark.png"
            
    };
    //独自機能2
    private static final String oxygenImageFiles[] = {
            "png/oxygenEmpty.png",
            "png/oxygenFull.png"
    };

    //独自機能1実装に伴いmapImagesとmapImageViewsをpublicに変更
    public Image[] mapImages;
    public ImageView[][] mapImageViews;
    private int[][] maps;
    private int width; // width of the map
    private int height; // height of the map
    private int oxygenWidth;
    private int oxygenHeight;

     //独自機能2酸素用グリッド定義
     private Image[] oxygenImages;
     private ImageView[][] oxygenImageViews;
     private int[][] oxygenMaps;

    //必須機能5ゴール座標の記録(2024/1/11)
    private static int goalX;
    private static int goalY;

    //必須機能4アイテム位置を保存
    public static int[] itemposX;
    public static int[] itemposY;
    public static int[] itemNum;
    public static int NumberOfGas;

    MapData(int x, int y) {
        mapImages = new Image[16];
        mapImageViews = new ImageView[y][x];
        for (int i = 0; i < 16; i ++) {
            mapImages[i] = new Image(mapImageFiles[i]);
        }
        //独自機能2
        oxygenImages = new Image[2];
        oxygenImageViews = new ImageView[3][21];

        width = x;
        height = y;
        maps = new int[y][x];

        oxygenWidth = 21;
        oxygenHeight = 3;
        oxygenMaps = new int[3][21];

        oxygenImages[0] = new Image(oxygenImageFiles[0]);
        oxygenImages[1] = new Image(oxygenImageFiles[1]);

        fillMap(MapData.TYPE_WALL);
        //独自2zs
        fillOxygen(MapData.TYPE_OXYGEN_EMPTY);
        digMap(1, 3);
        //必須機能4アイテムの設置
        placeItems();
        placeGas();

        //必須機能5ゴールの設定
        int goalNum;
        goalNum = selectGoal(6);
        setGoal(goalNum);
        saveItemPlace();
        setImageViews();
        setOxygenViews();
    }

     // 必須機能４　アイテムをTYPE_SPACE上にランダムに設置
     private void placeItems() {
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            int randomX;
            int randomY;

                randomX = random.nextInt(width);
                randomY = random.nextInt(height);
            
                if(maps[randomY][randomX] == TYPE_SPACE) {
                  
                  setMap(randomX,randomY,i + 8);
                } else {
                    i = i - 1;
                }

        }
    }

    // 必須機能4 & 独自機能2 酸素のみいっぱい配置
    private void placeGas() {
        Random random = new Random();
        NumberOfGas = 9;
        for(int i = 0; i < NumberOfGas ; i++){
            int randomX;
            int randomY;

                randomX = random.nextInt(width);
                randomY = random.nextInt(height);
            
                if(maps[randomY][randomX] == TYPE_SPACE) {
                  
                  setMap(randomX,randomY,9);
                } else {
                    i = i - 1;
                }

        }


    }
    //必須機能4アイテム座標を記録
    public void saveItemPlace(){
        itemposX = new int[ 4 + NumberOfGas ];
        itemposY = new int[ 4 + NumberOfGas ];
        itemNum  = new int[ 4 + NumberOfGas ];
        int index = 0;
        for(int y = 0; y < height;y++){
            for(int x = 0;x < width;x++){
                if(getMap(x,y) == 8 ||getMap(x,y) == 9 ||getMap(x,y) == 10 ||getMap(x,y) == 11){
                    itemposX[index] = x;
                    itemposY[index] = y;
                    itemNum[index] = getMap(x,y);
                    index++;
                }
            }
        }
    }

    //必須機能4アイテム取得後にアイテムを消す
    public void updateItem(int x,int y){
        setMap(x,y,0);

    }
    public void updateItemView(int x,int y){
        mapImageViews[y][x].setImage(mapImages[0]);
    }

    // fill two-dimentional arrays with a given number (maps[y][x])
    private void fillMap(int type) {
        for (int y = 0; y < height; y ++) {
            for (int x = 0; x < width; x++) {
                maps[y][x] = type;
            }
        }
    }
    //独自機能2
    private void fillOxygen(int type){
        for (int y = 0; y < oxygenHeight;y++){
            for(int x = 0;x < oxygenWidth;x++){
                oxygenMaps[y][x] = type;
            }
        }
    }


    // dig walls for making roads
    private void digMap(int x, int y) {
        setMap(x, y, MapData.TYPE_SPACE);
        int[][] dl = { { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 0 } };
        int[] tmp;

        for (int i = 0; i < dl.length; i ++) {
            int r = (int) (Math.random() * dl.length);
            tmp = dl[i];
            dl[i] = dl[r];
            dl[r] = tmp;
        }

        for (int i = 0; i < dl.length; i ++) {
            int dx = dl[i][0];
            int dy = dl[i][1];
            if (getMap(x + dx * 2, y + dy * 2) == MapData.TYPE_WALL) {
                setMap(x + dx, y + dy, MapData.TYPE_SPACE);
                digMap(x + dx * 2, y + dy * 2);
            }
        }
    }

    //必須機能5ゴールの設定(24/1/11)
    private int selectGoal(int i){
        Random rand = new Random();
        int num = rand.nextInt(i);
        return num;
    }

    private void setGoal(int i){
        if(i == 0 && getMap(19,13) == MapData.TYPE_SPACE){
            setMap(19, 13, MapData.TYPE_GOAL);
            goalX = 19;
            goalY = 13;
        }
        if(i == 0 && getMap(19,13) == MapData.TYPE_WALL){
            i = 1;
        }
        if(i == 1 && getMap(19,1) == MapData.TYPE_SPACE){
            setMap(19, 1, MapData.TYPE_GOAL);
            goalX = 19;
            goalY = 1;
        }
        if(i == 1 && getMap(19,1) == MapData.TYPE_WALL){
            i = 2;
        }
        if(i == 2 && getMap(1,13) == MapData.TYPE_SPACE){
            setMap(1, 13, MapData.TYPE_GOAL);
            goalX = 1;
            goalY = 13;
        }
        if(i == 2 && getMap(1,13) == MapData.TYPE_WALL){
            i = 3;
        }
        if(i == 3 && getMap(18,13) == MapData.TYPE_SPACE){
            setMap(18, 13, MapData.TYPE_GOAL);
            goalX = 18;
            goalY = 13;
        }
        if(i == 3 && getMap(18,13) == MapData.TYPE_WALL){
            i = 4;
        }
        if(i == 4 && getMap(19,7) == MapData.TYPE_SPACE){
            setMap(19, 7, MapData.TYPE_GOAL);
            goalX = 19;
            goalY = 7;
        }
        if(i == 4 && getMap(19,7) == MapData.TYPE_WALL){
            i = 5;
        }
        if(i == 5 && getMap(11,1) == MapData.TYPE_SPACE){
            setMap(11, 1, MapData.TYPE_GOAL);
            goalX = 11;
            goalY = 1;
        }
        if(i == 5 && getMap(11,1) == MapData.TYPE_WALL){
            i = 0;
        }
        
    }

    public int getMap(int x, int y) {
        if (x < 0 || width <= x || y < 0 || height <= y) {
            return -1;
        }
        return maps[y][x];
    }

    public void setMap(int x, int y, int type) {
        if (x < 1 || width <= x - 1 || y < 1 || height <= y - 1) {
            return;
        }
        maps[y][x] = type;
    }

    public int getOxygenMap(int x,int y){
        if (x < 0 || 21 <= x || y < 0 || 3 <= y) {
            return -1;
        }
        return oxygenMaps[y][x];
    }

    public void setOxygenMap(int x, int y, int type) {
        if (x < 1 || width <= x - 1 || y < 1 || height <= y - 1) {
            return;
        }
        oxygenMaps[y][x] = type;
    }

    public ImageView getImageView(int x, int y) {
        return mapImageViews[y][x];
    }
    public ImageView getOxygenImageView(int x, int y) {
        return oxygenImageViews[y][x];
    }

    public void setImageViews() {
        for (int y = 0; y < height; y ++) {
            for (int x = 0; x < width; x++) {
                mapImageViews[y][x] = new ImageView(mapImages[getDarkMaps(x,y)]);
            }
        }
    }
    public void setOxygenViews() {
        for ( int y = 0;y < 3; y++){
            for(int x = 0;x < 21;x++){
                oxygenImageViews[y][x] = new ImageView(oxygenImages[getOxygenMap(x,y)]);
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    //必須機能5ゴール座標の取得(2024/1/11)
    public int getGoalX(){
        return goalX;
    }

    public int getGoalY(){
        return goalY;
    }

    //独自機能1暗闇への変換(24/1/12)
    public int getDarkMaps(int x,int y){
        if(getMap(x,y) == 0){
            return 4;
        }else if(getMap(x,y) == 1){
            return 5;
        }else if(getMap(x,y) == 2){
            return 6;
        }else if(getMap(x,y) == 3){
            return 7;
        }else if(getMap(x,y) == 8){
            return 12;
        }else if(getMap(x,y) == 9){
            return 13;
        }else if(getMap(x,y) == 10){
            return 14;
        }else if(getMap(x,y) == 11){
            return 15;
        }
        return 4;
    }

    //独自機能1暗闇を通常に変更(24/1/12)
    public void changeLight(int x,int y){
        if(getMap(x,y) == 0){
            setImageViewsForDark(x,y,0);
        }else if(getMap(x,y) == 1){
            setImageViewsForDark(x,y,1);
        }else if(getMap(x,y) == 2){
            setImageViewsForDark(x,y,2);
        }else if(getMap(x,y) == 3){
            setImageViewsForDark(x,y,3);
        }else if(getMap(x,y) == 8){
            setImageViewsForDark(x,y,8);
        }else if(getMap(x,y) == 9){
            setImageViewsForDark(x,y,9);
        }else if(getMap(x,y) == 10){
            setImageViewsForDark(x,y,10);
        }else if(getMap(x,y) == 11){
            setImageViewsForDark(x,y,11);
        }
    }
    //通常を暗闇に変更
    public void changeDark(int x,int y){
        if(getMap(x,y) == 0){
            setImageViewsForDark(x,y,4);
        }else if(getMap(x,y) == 1){
            setImageViewsForDark(x,y,5);
        }else if(getMap(x,y) == 2){
            setImageViewsForDark(x,y,6);
        }else if(getMap(x,y) == 3){
            setImageViewsForDark(x,y,7);
        }else if(getMap(x,y) == 8){
            setImageViewsForDark(x,y,12);
        }else if(getMap(x,y) == 9){
            setImageViewsForDark(x,y,13);
        }else if(getMap(x,y) == 10){
            setImageViewsForDark(x,y,14);
        }else if(getMap(x,y) == 11){
            setImageViewsForDark(x,y,15);
        }
    }
    public void setImageViewsForDark(int x,int y,int num){
        mapImageViews[y][x].setImage(mapImages[num]);
    }
    //周囲1マスを通常に変更する
    public void changeLightCircle(int x,int y){
        
        if(0 <= x && x <= 20 && 0 <= y-1 && y-1 <= 14){
            changeLight(x,y-1);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y-1 && y-1 <= 14){
            changeLight(x-1,y-1);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y && y <= 14){
            changeLight(x-1,y);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y+1 && y+1 <= 14){
            changeLight(x-1,y+1);
        }
        if(0 <= x && x <= 20 && 0 <= y+1 && y+1 <= 14){
            changeLight(x,y+1);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y+1 && y+1 <= 14){
            changeLight(x+1,y+1);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y && y <= 14){
            changeLight(x+1,y);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y-1 && y-1 <= 14){
            changeLight(x+1,y-1);
        }
    }
    //周辺2マスを通常に変更する
    public void changeLightCircleWide(int x,int y){

        if(0 <= x && x <= 20 && 0 <= y-1 && y-1 <= 14){
            changeLight(x,y-1);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y-1 && y-1 <= 14){
            changeLight(x-1,y-1);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y && y <= 14){
            changeLight(x-1,y);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y+1 && y+1 <= 14){
            changeLight(x-1,y+1);
        }
        if(0 <= x && x <= 20 && 0 <= y+1 && y+1 <= 14){
            changeLight(x,y+1);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y+1 && y+1 <= 14){
            changeLight(x+1,y+1);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y && y <= 14){
            changeLight(x+1,y);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y-1 && y-1 <= 14){
            changeLight(x+1,y-1);
        }

        if(0 <= x && x <= 20 && 0 <= y-2 && y-2 <= 14){
            changeLight(x,y-2);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y-2 && y-2 <= 14){
            changeLight(x-1,y-2);
        }
        if(0 <= x-2 && x-2 <= 20 && 0 <= y-2 && y-2 <= 14){
            changeLight(x-2,y-2);
        }
        if(0 <= x-2 && x-2 <= 20 && 0 <= y-1 && y-1 <= 14){
            changeLight(x-2,y-1);
        }
        if(0 <= x-2 && x-2 <= 20 && 0 <= y && y <= 14){
            changeLight(x-2,y);
        }
        if(0 <= x-2 && x-2 <= 20 && 0 <= y+1 && y+1 <= 14){
            changeLight(x-2,y+1);
        }
        if(0 <= x-2 && x-2 <= 20 && 0 <= y+2 && y+2 <= 14){
            changeLight(x-2,y+2);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y+2 && y+2 <= 14){
            changeLight(x-1,y+2);
        }
        if(0 <= x && x <= 20 && 0 <= y+2 && y+2 <= 14){
            changeLight(x,y+2);
        }        
        if(0 <= x && x <= 20 && 0 <= y+2 && y+2 <= 14){
            changeLight(x+1,y+2);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y+2 && y+2 <= 14){
            changeLight(x+1,y+2);
        }
        if(0 <= x+2 && x+2 <= 20 && 0 <= y+2 && y+2 <= 14){
            changeLight(x+2,y+2);
        }
        if(0 <= x+2 && x+2 <= 20 && 0 <= y+1&& y+1 <= 14){
            changeLight(x+2,y+1);
        }
        if(0 <= x+2 && x+2 <= 20 && 0 <= y && y <= 14){
            changeLight(x+2,y);
        }
        if(0 <= x+2 && x+2 <= 20 && 0 <= y-1 && y-1 <= 14){
            changeLight(x+2,y-1);
        }
        if(0 <= x+2 && x+2 <= 20 && 0 <= y-2 && y-2 <= 14){
            changeLight(x+2,y-2);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y-2 && y-2 <= 14){
            changeLight(x+1,y-2);
        }
    }
    //周辺1マスを暗闇にする
    public  void changeDarkCircle(int x,int y){
        
        if(0 <= x && x <= 20 && 0 <= y-1 && y-1 <= 14){
            changeDark(x,y-1);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y-1 && y-1 <= 14){
            changeDark(x-1,y-1);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y && y <= 14){
            changeDark(x-1,y);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y+1 && y+1 <= 14){
            changeDark(x-1,y+1);
        }
        if(0 <= x && x <= 20 && 0 <= y+1 && y+1 <= 14){
            changeDark(x,y+1);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y+1 && y+1 <= 14){
            changeDark(x+1,y+1);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y && y <= 14){
            changeDark(x+1,y);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y-1 && y-1 <= 14){
            changeDark(x+1,y-1);
        }
    }
    //周辺2マスを暗闇にする(アイテム用)
    public void changeDarkCircleWide(int x,int y){

        if(0 <= x && x <= 20 && 0 <= y-1 && y-1 <= 14){
            changeDark(x,y-1);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y-1 && y-1 <= 14){
            changeDark(x-1,y-1);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y && y <= 14){
            changeDark(x-1,y);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y+1 && y+1 <= 14){
            changeDark(x-1,y+1);
        }
        if(0 <= x && x <= 20 && 0 <= y+1 && y+1 <= 14){
            changeDark(x,y+1);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y+1 && y+1 <= 14){
            changeDark(x+1,y+1);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y && y <= 14){
            changeDark(x+1,y);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y-1 && y-1 <= 14){
            changeDark(x+1,y-1);
        }

        if(0 <= x && x <= 20 && 0 <= y-2 && y-2 <= 14){
            changeDark(x,y-2);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y-2 && y-2 <= 14){
            changeDark(x-1,y-2);
        }
        if(0 <= x-2 && x-2 <= 20 && 0 <= y-2 && y-2 <= 14){
            changeDark(x-2,y-2);
        }
        if(0 <= x-2 && x-2 <= 20 && 0 <= y-1 && y-1 <= 14){
            changeDark(x-2,y-1);
        }
        if(0 <= x-2 && x-2 <= 20 && 0 <= y && y <= 14){
            changeDark(x-2,y);
        }
        if(0 <= x-2 && x-2 <= 20 && 0 <= y+1 && y+1 <= 14){
            changeDark(x-2,y+1);
        }
        if(0 <= x-2 && x-2 <= 20 && 0 <= y+2 && y+2 <= 14){
            changeDark(x-2,y+2);
        }
        if(0 <= x-1 && x-1 <= 20 && 0 <= y+2 && y+2 <= 14){
            changeDark(x-1,y+2);
        }
        if(0 <= x && x <= 20 && 0 <= y+2 && y+2 <= 14){
            changeDark(x,y+2);
        }
        if(0 <= x && x <= 20 && 0 <= y+2 && y+2 <= 14){
            changeDark(x+1,y+2);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y+2 && y+2 <= 14){
            changeDark(x+1,y+2);
        }
        if(0 <= x+2 && x+2 <= 20 && 0 <= y+2 && y+2 <= 14){
            changeDark(x+2,y+2);
        }
        if(0 <= x+2 && x+2 <= 20 && 0 <= y+1&& y+1 <= 14){
            changeDark(x+2,y+1);
        }
        if(0 <= x+2 && x+2 <= 20 && 0 <= y && y <= 14){
            changeDark(x+2,y);
        }
        if(0 <= x+2 && x+2 <= 20 && 0 <= y-1 && y-1 <= 14){
            changeDark(x+2,y-1);
        }
        if(0 <= x+2 && x+2 <= 20 && 0 <= y-2 && y-2 <= 14){
            changeDark(x+2,y-2);
        }
        if(0 <= x+1 && x+1 <= 20 && 0 <= y-2 && y-2 <= 14){
            changeDark(x+1,y-2);
        }
    }

    //必須機能4アイテム「地図」用の一時暗闇全解除メソッド
    public void showMap(){
        for(int y = 0;y < height;y++){
            for(int x = 0;x < width;x++){
                changeLight(x,y);
            }
        }
        MoveChara.isMap = true;
    }
    //地図を閉じるメソッド
    public void closeMap(int posX,int posY){
        for(int y = 0;y < height;y++){
            for(int x = 0;x < width;x++){
                changeDark(x,y);
            }
        }
        if(MoveChara.isLamp){
            changeLightCircleWide(posX,posY);
            MoveChara.isMap = false;
        }else{
            changeLightCircle(posX,posY);
            MoveChara.isMap = false;
        }
    }
    //独自機能2酸素残量の表示
    public void setImageViewsForOxygen(int x,int y,int num){
        oxygenImageViews[y][x].setImage(oxygenImages[num]);
    }
    
    public void adjustOxygen(int num){
        for(int i = 0;i < 3;i++){
            for(int j = 0;j < 21;j++){
                setImageViewsForOxygen(j,i,MapData.TYPE_OXYGEN_EMPTY);
            }
        }
        int y;
        int x;
        if(num > 21){
            y = num / 21;
            x = num % 21;
        }else{
            y = 0;
            x = num;
        }
        if(y == 0){
            for(int j = 0;j < x;j++){
                setImageViewsForOxygen(j,0,MapData.TYPE_OXYGEN_FULL);
            }
        }else if(y == 1){
            for(int j = 0;j < 21;j++){
                setImageViewsForOxygen(j,0,MapData.TYPE_OXYGEN_FULL);
            }
            for(int j = 0;j < x;j++){
                setImageViewsForOxygen(j,1,MapData.TYPE_OXYGEN_FULL);
            }
        }else if(y == 2){
            for(int j = 0;j < 21;j++){
                setImageViewsForOxygen(j,0,MapData.TYPE_OXYGEN_FULL);
            }
            for(int j = 0;j < 21;j++){
                setImageViewsForOxygen(j,1,MapData.TYPE_OXYGEN_FULL);
            }
            for(int j = 0;j < x;j++){
                setImageViewsForOxygen(j,2,MapData.TYPE_OXYGEN_FULL);
            }
        }
    }
    
}

