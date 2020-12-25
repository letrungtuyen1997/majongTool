package com.ss.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.objects.Tile;
import com.ss.gameLogic.objects.Tile2;
import com.ss.utils.Utils;


public class gameScene2 extends GScreen {
  private Array<Tile2>                 arrTile;
  private Array<Tile2>                 arrTileBoard       = new Array<>();
  private Array<Integer>              arrID              = new Array<>();
  private Group                       group              = new Group();
  private Array<Tile2>                 arrCompare         = new Array<>();
  private Array<Integer>              arrTileShuffle     = new Array<>();
  private Array<Tile2>                 arrTileHint        = new Array<>();
  private Array<Tile2>                 arrTileCanHint     = new Array<>();
  private Array<Utils.JsonLevel>      arrJSLevel         = new Array<>();


  @Override
  public void dispose() {

  }

  @Override
  public void init() {

    GStage.addToLayer(GLayer.ui,group);
    Image bg = GUI.createImage(TextureAtlasC.uiAtlas,"bg");
    bg.setSize(GStage.getWorldWidth(),GStage.getWorldHeight());
    group.addActor(bg);
    createBoard(2);
    setLockTile();
    showfps();
    Image btnShuffle = GUI.createImage(TextureAtlasC.uiAtlas,"btnShuffle");
    btnShuffle.setPosition(GStage.getWorldWidth()/2+btnShuffle.getWidth(),btnShuffle.getHeight()/2, Align.center);
    group.addActor(btnShuffle);
    btnShuffle.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        SkipHint();
        shuffleBoard();
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    Image btnHint = GUI.createImage(TextureAtlasC.uiAtlas,"btnHint");
    btnHint.setPosition(GStage.getWorldWidth()/2-btnHint.getWidth(),btnHint.getHeight()/2, Align.center);
    group.addActor(btnHint);
    btnHint.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        hintBoard();
        return super.touchDown(event, x, y, pointer, button);
      }
    });

//    createLv(11,11,11,11,11,11,11);

  }

  @Override
  public void run() {

  }
  private void createBoard(int lv){
      String StrLv = Gdx.files.internal("data/data.txt").readString();
      String ArrStrLv[] = StrLv.split("\n");
//      String getLv[]    = ArrStrLv[lv-1].split();

      JsonValue Arrjv = Utils.GetJsV(ArrStrLv[(lv-1)]);
//      System.out.println("test: "+jv);
//      for (JsonValue jv : Arrjv){
//        Tile t = new Tile(group,this);
//        t.setRowCol(jv.get("row").asInt(),jv.get("col").asInt());
//        t.setLayer2(jv.get("layer").asInt());
//        t.setPos(jv.get("x").asFloat(),jv.get("y").asFloat(),jv.get("kind").asInt());
//        t.createID(jv.get("id").asInt());
//        arrTileBoard.add(t);
//      }
  }
  public void setLockTile(){
    setDefault();
    for (int i=0;i<arrTileBoard.size;i++){
      Tile2 tile = arrTileBoard.get(i);
      if(setLock(tile)){
        tile.setColor(Color.DARK_GRAY);
      }
      System.out.println("check: "+arrTileBoard.get(i).getRowCol().x+"___"+arrTileBoard.get(i).getRowCol().y+"__"+arrTileBoard.get(i).getKind());
    }

  }
  public void setDefault(){
    for (int i=0;i<arrTileBoard.size;i++){
      Tile2 tile = arrTileBoard.get(i);
      tile.setColor(Color.WHITE);

    }

  }
  private boolean setLock(Tile2 tile){
    if(checkLock(tile)==1||checkLayer(tile)==1)
      return true;
    return false;
  }
  private int checkLock(Tile2 t){
    int row   = (int)t.getRowCol().x;
    int col   = (int)t.getRowCol().y;
    int Layer = t.getLayer();
    //Label TH1:
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==5 )
      return 1;

    //Label TH2:
    if(findTile(row-1,col-1,Layer,4)!=null && findTile(row-1,col+1,Layer,4)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row-1,col-1,Layer,5)!=null && findTile(row-1,col+1,Layer,5)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==5 )
      return 1;

    //Label TH3:
    if(findTile(row+1,col-1,Layer,2)!=null && findTile(row+1,col+1,Layer,2)!=null && t.getKind()==5 )
      return 1;
    if(findTile(row+1,col-1,Layer,1)!=null && findTile(row+1,col+1,Layer,1)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==2 )
      return 1;

    //Label TH4:
    if(findTile(row-1,col-1,Layer,4)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row-1,col-1,Layer,5)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row,col-1,Layer,1)!=null && findTile(row+1,col+1,Layer,1)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,2)!=null && findTile(row+1,col+1,Layer,2)!=null && t.getKind()==5 )
      return 1;

    //Label TH5:
    if(findTile(row,col-1,Layer,4)!=null && findTile(row-1,col+1,Layer,4)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row,col-1,Layer,5)!=null && findTile(row-1,col+1,Layer,5)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row+1,col-1,Layer,1)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row+1,col-1,Layer,2)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==5 )
      return 1;

    //Label TH6:
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row,col-1,Layer,4)!=null && findTile(row+1,col+1,Layer,1)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,5)!=null && findTile(row+1,col+1,Layer,2)!=null && t.getKind()==5 )
      return 1;

    //Label TH7:
    if(findTile(row,col-1,Layer,1)!=null && findTile(row-1,col+1,Layer,4)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row,col-1,Layer,2)!=null && findTile(row-1,col+1,Layer,5)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==5 )
      return 1;

    //Label TH8:
    if(findTile(row-1,col-1,Layer,4)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row-1,col-1,Layer,1)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==5 )
      return 1;

    //Label TH9:
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==1 )
      return 1;
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==2 )
      return 1;
    if(findTile(row+1,col-1,Layer,1)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==4 )
      return 1;
    if(findTile(row+1,col-1,Layer,2)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==5 )
      return 1;
    return 0;
  }
  public Array<Tile2> getLock(Tile2 t){
    int row   = (int)t.getRowCol().x;
    int col   = (int)t.getRowCol().y;
    int Layer = t.getLayer();
    Array<Tile2> arr = new Array<>();
    //Label TH1:
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==1 ){
      arr.add(findTile(row,col-1,Layer,1),findTile(row,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==2 ){
      arr.add(findTile(row,col-1,Layer,2),findTile(row,col+1,Layer,2),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==4 ){
      arr.add(findTile(row,col-1,Layer,4),findTile(row,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==5 ){
      arr.add(findTile(row,col-1,Layer,5),findTile(row,col+1,Layer,5),t);
      return arr;
    }

    //Label TH2:
    if(findTile(row-1,col-1,Layer,4)!=null && findTile(row-1,col+1,Layer,4)!=null && t.getKind()==1 ){
      arr.add(findTile(row-1,col-1,Layer,4),findTile(row-1,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row-1,col-1,Layer,5)!=null && findTile(row-1,col+1,Layer,5)!=null && t.getKind()==2 ){
      arr.add(findTile(row-1,col-1,Layer,5),findTile(row-1,col+1,Layer,5),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==4 ){
      arr.add(findTile(row,col-1,Layer,1),findTile(row,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==5 ){
      arr.add(findTile(row,col-1,Layer,2),findTile(row,col+1,Layer,2),t);
      return arr;
    }

    //Label TH3:
    if(findTile(row+1,col-1,Layer,2)!=null && findTile(row+1,col+1,Layer,2)!=null && t.getKind()==5 ){
      arr.add(findTile(row+1,col-1,Layer,2),findTile(row+1,col+1,Layer,2),t);
      return arr;
    }
    if(findTile(row+1,col-1,Layer,1)!=null && findTile(row+1,col+1,Layer,1)!=null && t.getKind()==4 ){
      arr.add(findTile(row+1,col-1,Layer,1),findTile(row+1,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==1 ){
      arr.add(findTile(row,col-1,Layer,4),findTile(row,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==2 ){
      arr.add(findTile(row,col-1,Layer,5),findTile(row,col+1,Layer,5),t);
      return arr;
    }

    //Label TH4:
    if(findTile(row-1,col-1,Layer,4)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==1 ){
      arr.add(findTile(row-1,col-1,Layer,4),findTile(row,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row-1,col-1,Layer,5)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==2 ){
      arr.add(findTile(row-1,col-1,Layer,5),findTile(row,col+1,Layer,5),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,1)!=null && findTile(row+1,col+1,Layer,1)!=null && t.getKind()==4 ){
      arr.add(findTile(row,col-1,Layer,1),findTile(row+1,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,2)!=null && findTile(row+1,col+1,Layer,2)!=null && t.getKind()==5 ){
      arr.add(findTile(row,col-1,Layer,2),findTile(row+1,col+1,Layer,2),t);
      return arr;
    }

    //Label TH5:
    if(findTile(row,col-1,Layer,4)!=null && findTile(row-1,col+1,Layer,4)!=null && t.getKind()==1 ){
      arr.add(findTile(row,col-1,Layer,4),findTile(row-1,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,5)!=null && findTile(row-1,col+1,Layer,5)!=null && t.getKind()==2 ){
      arr.add(findTile(row,col-1,Layer,5),findTile(row-1,col+1,Layer,5),t);
      return arr;
    }
    if(findTile(row+1,col-1,Layer,1)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==4 ){
      arr.add(findTile(row+1,col-1,Layer,1),findTile(row,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row+1,col-1,Layer,2)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==5 ){
      arr.add(findTile(row+1,col-1,Layer,2),findTile(row,col+1,Layer,2),t);
      return arr;
    }

    //Label TH6:
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==1 ){
      arr.add(findTile(row,col-1,Layer,1),findTile(row,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==2 ){
      arr.add(findTile(row,col-1,Layer,2),findTile(row,col+1,Layer,5),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,4)!=null && findTile(row+1,col+1,Layer,1)!=null && t.getKind()==4 ){
      arr.add(findTile(row,col-1,Layer,4),findTile(row+1,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,5)!=null && findTile(row+1,col+1,Layer,2)!=null && t.getKind()==5 ){
      arr.add(findTile(row,col-1,Layer,5),findTile(row+1,col+1,Layer,2),t);
      return arr;
    }

    //Label TH7:
    if(findTile(row,col-1,Layer,1)!=null && findTile(row-1,col+1,Layer,4)!=null && t.getKind()==1 ){
      arr.add(findTile(row,col-1,Layer,1),findTile(row-1,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,2)!=null && findTile(row-1,col+1,Layer,5)!=null && t.getKind()==2 ){
      arr.add(findTile(row,col-1,Layer,2),findTile(row-1,col+1,Layer,5),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==4 ){
      arr.add(findTile(row,col-1,Layer,4),findTile(row,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==5 ){
      arr.add(findTile(row,col-1,Layer,5),findTile(row,col+1,Layer,2),t);
      return arr;
    }

    //Label TH8:
    if(findTile(row-1,col-1,Layer,4)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==1 ){
      arr.add(findTile(row-1,col-1,Layer,4), findTile(row,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row-1,col-1,Layer,1)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==2 ){
      arr.add(findTile(row-1,col-1,Layer,1), findTile(row,col+1,Layer,2),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,1)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==4 ){
      arr.add(findTile(row,col-1,Layer,1), findTile(row,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,2)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==5 ){
      arr.add(findTile(row,col-1,Layer,2), findTile(row,col+1,Layer,5),t);
      return arr;
    }

    //Label TH9:
    if(findTile(row,col-1,Layer,4)!=null && findTile(row,col+1,Layer,1)!=null && t.getKind()==1 ){
      arr.add(findTile(row,col-1,Layer,4), findTile(row,col+1,Layer,1),t);
      return arr;
    }
    if(findTile(row,col-1,Layer,5)!=null && findTile(row,col+1,Layer,2)!=null && t.getKind()==2 ){
      arr.add(findTile(row,col-1,Layer,5),findTile(row,col+1,Layer,2),t);
      return arr;
    }
    if(findTile(row+1,col-1,Layer,1)!=null && findTile(row,col+1,Layer,4)!=null && t.getKind()==4 ){
      arr.add(findTile(row+1,col-1,Layer,1), findTile(row,col+1,Layer,4),t);
      return arr;
    }
    if(findTile(row+1,col-1,Layer,2)!=null && findTile(row,col+1,Layer,5)!=null && t.getKind()==5 ){
      arr.add(findTile(row+1,col-1,Layer,2),findTile(row,col+1,Layer,5),t);
      return arr;
    }
    return null;
  }
  public int checkLayer(Tile2 t){
    int row   = (int)t.getRowCol().x;
    int col   = (int)t.getRowCol().y;
    int Layer = t.getLayer()+1;
    if(findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row,col+1,Layer,1)!=null
            ||findTile(row,col+1,Layer,4)!=null||findTile(row+1,col+1,Layer,1)!=null
            ||findTile(row+1,col,Layer,1)!=null||findTile(row+1,col,Layer,2)!=null  && t.getKind()==5){
      return 1;
    }
    if(findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row-1,col-1,Layer,5)!=null
            ||findTile(row-1,col,Layer,4)!=null||findTile(row-1,col,Layer,5)!=null
            ||findTile(row,col-1,Layer,5)!=null && t.getKind()==1){
      return 1;
    }
    if(findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row-1,col-1,Layer,4)!=null
            ||findTile(row-1,col,Layer,5)!=null||findTile(row-1,col+1,Layer,4)!=null
            ||findTile(row,col+1,Layer,1)!=null||findTile(row,col+1,Layer,4)!=null && t.getKind()==2){
      return 1;
    }
    if(findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row,col-1,Layer,2)!=null
            ||findTile(row,col-1,Layer,5)!=null||findTile(row+1,col,Layer,1)!=null
            ||findTile(row+1,col,Layer,2)!=null||findTile(row+1,col-1,Layer,2)!=null && t.getKind()==4){
      return 1;
    }
    return 0;
  }
  public Array<Tile2> getLockLayer(Tile2 t){
    int row   = (int)t.getRowCol().x;
    int col   = (int)t.getRowCol().y;
    int Layer = t.getLayer()+1;
    Array<Tile2> arr = new Array<>();
    if(findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row,col+1,Layer,1)!=null
            ||findTile(row,col+1,Layer,4)!=null||findTile(row+1,col+1,Layer,1)!=null
            ||findTile(row+1,col,Layer,1)!=null||findTile(row+1,col,Layer,2)!=null  && t.getKind()==5){

      arr.add(findTile(row,col,Layer,1));
      arr.add(findTile(row,col,Layer,2));
      arr.add(findTile(row,col,Layer,4));
      arr.add(findTile(row,col,Layer,5));
      arr.add(findTile(row,col+1,Layer,1));
      arr.add(findTile(row,col+1,Layer,4));
      arr.add(findTile(row+1,col+1,Layer,1));
      arr.add(findTile(row+1,col,Layer,1));
      arr.add(findTile(row+1,col,Layer,2));
      arr.add(t);
      return arr;
    }
    if(findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row-1,col-1,Layer,5)!=null
            ||findTile(row-1,col,Layer,4)!=null||findTile(row-1,col,Layer,5)!=null
            ||findTile(row,col-1,Layer,5)!=null && t.getKind()==1){
      arr.add(findTile(row,col,Layer,1));
      arr.add(findTile(row,col,Layer,2));
      arr.add(findTile(row,col,Layer,4));
      arr.add(findTile(row,col,Layer,5));
      arr.add(findTile(row-1,col-1,Layer,5));
      arr.add(findTile(row-1,col,Layer,4));
      arr.add(findTile(row-1,col,Layer,5));
      arr.add(findTile(row,col-1,Layer,5));
      arr.add(t);
      return arr;
    }
    if(findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row-1,col-1,Layer,4)!=null
            ||findTile(row-1,col,Layer,5)!=null||findTile(row-1,col+1,Layer,4)!=null
            ||findTile(row,col+1,Layer,1)!=null||findTile(row,col+1,Layer,4)!=null && t.getKind()==2){
      arr.add(findTile(row,col,Layer,1));
      arr.add(findTile(row,col,Layer,2));
      arr.add(findTile(row,col,Layer,4));
      arr.add(findTile(row,col,Layer,5));
      arr.add(findTile(row-1,col-1,Layer,4));
      arr.add(findTile(row-1,col,Layer,5));
      arr.add(findTile(row-1,col+1,Layer,4));
      arr.add(findTile(row,col+1,Layer,1));
      arr.add(findTile(row,col+1,Layer,4));
      arr.add(t);
      return arr;
    }
    if(findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row,col-1,Layer,2)!=null
            ||findTile(row,col-1,Layer,5)!=null||findTile(row+1,col,Layer,1)!=null
            ||findTile(row+1,col,Layer,2)!=null||findTile(row+1,col-1,Layer,2)!=null && t.getKind()==4){
      arr.add(findTile(row,col,Layer,1));
      arr.add(findTile(row,col,Layer,2));
      arr.add(findTile(row,col,Layer,4));
      arr.add(findTile(row,col,Layer,5));
      arr.add(findTile(row,col-1,Layer,2));
      arr.add(findTile(row,col-1,Layer,5));
      arr.add(findTile(row+1,col,Layer,1));
      arr.add(findTile(row+1,col,Layer,2));
      arr.add(findTile(row+1,col-1,Layer,2));
      arr.add(t);
      return arr;
    }
    return null;
  }
  public void checkMatch(Tile2 t){
    if(arrCompare.size==0){
      arrCompare.add(t);
    }else if(arrCompare.size==1){
      int row = (int)arrCompare.get(0).getRowCol().x;
      int col = (int)arrCompare.get(0).getRowCol().y;
      int id  = (int)arrCompare.get(0).getId();
      if(row==(int)t.getRowCol().x && col == (int)t.getRowCol().y && id == t.getId()){
        System.out.println("trùng ");
        arrCompare.get(0).select(false);
        arrCompare.clear();
      }else {
        System.out.println("them tile mới!!");
        arrCompare.add(t);
      }
    }

    if(arrCompare.size==2){
      System.out.println("compare here!!");
      if(arrCompare.get(0).getId()==arrCompare.get(1).getId()){
        Tile2 t1 = arrCompare.get(0);
        Tile2 t2 = arrCompare.get(1);
        removeTile((int)t1.getRowCol().x,(int)t1.getRowCol().y,t1.getKind());
        removeTile((int)t2.getRowCol().x,(int)t2.getRowCol().y,t2.getKind());
        arrCompare.clear();
        setLockTile();
      }else {
        arrCompare.get(0).select(false);
        arrCompare.get(1).select(false);
        arrCompare.clear();
      }
    }
  }

  private void removeTile(int row, int col,int kind){
    for (Tile2 t : arrTileBoard){
      if(t.getRowCol().x==row && t.getRowCol().y==col && t.getKind()==kind){
        t.dispose();
        arrTileBoard.removeIndex(arrTileBoard.indexOf(t,true));
      }
    }

  }


  //Label: tim lien ket khoa tile
  private Tile2 findTile(int row, int col, int Layer, int kind){
    for (Tile2 tt : arrTileBoard){
      if(row==tt.getRowCol().x && col == tt.getRowCol().y && Layer ==tt.getLayer() && kind== tt.getKind() )
        return tt;
    }
    return null;
  }
  int s;
  private void showfps(){
    s= Gdx.graphics.getFramesPerSecond();
    Label fps = new Label("fps: "+s,new Label.LabelStyle(BitmapFontC.font_white,null));
    fps.setPosition(200,200);
    group.addActor(fps);
    group.addAction(GSimpleAction.simpleAction((d,a)->{
      s= Gdx.graphics.getFramesPerSecond();
      fps.setText("fps: "+s);
      return false;
    }));
  }
  public void hintBoard(){
    arrTileHint.clear();
    arrTileCanHint.clear();
    for (Tile2 t : arrTileBoard){
      if(t.block.getColor().equals(Color.WHITE))
        arrTileCanHint.add(t);
    }
    int counthint=0;
    for (int i=0;i<arrTileCanHint.size; i++){
      for (int j=i+1 ;j<arrTileCanHint.size;j++){
        if(arrTileCanHint.get(i).getId()==arrTileCanHint.get(j).getId()){
          counthint++;
          arrTileHint.add(arrTileCanHint.get(j));
        }
      }
      if(counthint>0){
        arrTileHint.add(arrTileCanHint.get(i));
        break;
      }else {
        arrTileHint.clear();
      }

    }
    System.out.println("get size arrhint: "+ arrTileHint.size);
    int hint =0;
    for (Tile2 t: arrTileHint){
      hint++;
      if(hint>2)
        return;
      t.select(true);
    }
  }
  private void SkipHint(){
    for (Tile2 t : arrTileHint)
      t.select(false);
  }
  public void shuffleBoard(){
    arrTileShuffle.clear();
    for (Tile2 t : arrTileBoard){
      arrTileShuffle.add(t.getId());
    }
    arrTileShuffle.shuffle();
    if(arrTileShuffle.size==arrTileBoard.size){
      for (Tile2 t : arrTileBoard){
        t.changeId(arrTileShuffle.get(arrTileBoard.indexOf(t,true)));
      }
    }

  }

  private void createLv(int row, int col, int layer, float x, float y, int kind, int id ){
    Utils.JsonLevel level = new Utils.JsonLevel();
    level.setRow(row);
    level.setCol(col);
    level.setLayer(layer);
    level.setX(x);
    level.setY(y);
    level.setKind(kind);
    level.setId(id);
    arrJSLevel.add(level);
  }
  private void save(Array<Utils.JsonLevel> lv, FileHandle file) {
    Json json = new Json();
    json.setTypeName(null);
    json.setUsePrototypes(false);
    json.setIgnoreUnknownFields(true);
    json.setOutputType(JsonWriter.OutputType.json);

    int level = Integer.parseInt(Gdx.files.internal("data/Level.txt").readString());
    String txt = json.toJson(lv);
    file.writeString("Level"+level+"_"+txt+"\n", true);
    level++;
    FileHandle f = Gdx.files.local("data/Level.txt");
    f.writeString(""+level,false);

  }


}
