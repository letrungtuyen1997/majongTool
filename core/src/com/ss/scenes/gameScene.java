package com.ss.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.objects.Tile;
import com.ss.utils.Utils;


public class gameScene extends GScreen {
  private Array<Tile>                 arrTile;
  private Array<Tile>                 arrTileBoard       = new Array<>();
  private Array<Integer>              arrID              = new Array<>();
  private Group                       group              = new Group();
  private Array<Tile>                 arrCompare         = new Array<>();
  private Array<Integer>              arrTileShuffle     = new Array<>();
  private Array<Tile>                 arrTileHint        = new Array<>();
  private Array<Tile>                 arrTileCanHint     = new Array<>();
  private Array<Utils.JsonLevel>      arrJSLevel         = new Array<>();
  private Label                       lbShuffle;


  public gameScene(Array<Tile> arr){
    this.arrTile= arr;
  }
  @Override
  public void dispose() {

  }

  @Override
  public void init() {

    GStage.addToLayer(GLayer.ui,group);
    group.setSize(720,1280);
    group.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,Align.center);

    Image bg = GUI.createImage(TextureAtlasC.uiAtlas,"bg");
    bg.setSize(GStage.getWorldWidth(),GStage.getWorldHeight());
    group.addActor(bg);
    initId();
    createBoard();
    setLockTile();
    for (Tile t: arrTileBoard){
      createLv((int)t.getRowCol().x,(int)t.getRowCol().y,t.getLayer(),t.getXY().x+t.block.getWidth()*0.75f,t.getXY().y+t.block.getHeight()/2,t.getKind(),t.getId());
    }
    save(arrJSLevel,Gdx.files.local("data/data.txt"));

    showfps();
    Image btnShuffle = GUI.createImage(TextureAtlasC.uiAtlas,"btnShuffle");
    btnShuffle.setPosition(GStage.getWorldWidth()/2+btnShuffle.getWidth(),btnShuffle.getHeight()/2, Align.center);
    group.addActor(btnShuffle);
    btnShuffle.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        createlbShuffle();
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
        hint();
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    Image btnReturn = GUI.createImage(TextureAtlasC.uiAtlas,"btnReplay");
    btnReturn.setPosition(btnShuffle.getX(Align.left)+100,btnShuffle.getY());
    group.addActor(btnReturn);
    btnReturn.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        setScreen(new baseScene());
        return super.touchDown(event, x, y, pointer, button);
      }
    });


  }

  @Override
  public void run() {

  }
  private void createBoard(){
    for (Tile T : arrTile){
      Tile t = new Tile(group,this,null);
      t.setRowCol((int)T.getRowCol().x,(int)T.getRowCol().y);
      t.setLayer2(T.getLayer());
      t.setPos(T.getXY().x+T.block.getWidth()/2,T.getXY().y+T.block.getHeight()/2,T.getKind(),false);
      t.createID(arrID.get(arrTile.indexOf(T,true)));
      arrTileBoard.add(t);

    }
  }

  private void initId(){
    for (int i=0; i<arrTile.size/4; i++){
      arrID.add(i+1);
      arrID.add(i+1);
      arrID.add(i+1);
      arrID.add(i+1);
    }
    arrID.shuffle();
    for (int i=0 ;i<arrID.size;i++){
      System.out.println("check id: "+arrID.get(i));
    }
  }
  public void setLockTile(){
    setDefault();
    for (int i=0;i<arrTileBoard.size;i++){
      Tile tile = arrTileBoard.get(i);
      if(setLock(tile)){
        tile.setColor(Color.DARK_GRAY);
      }
      System.out.println("check: "+arrTileBoard.get(i).getRowCol().x+"___"+arrTileBoard.get(i).getRowCol().y+"__"+arrTileBoard.get(i).getKind());
    }

  }
  public void setDefault(){
    for (int i=0;i<arrTileBoard.size;i++){
      Tile tile = arrTileBoard.get(i);
      tile.setColor(Color.WHITE);

    }

  }
  private boolean setLock(Tile tile){
    if(checkLock(tile)==1||checkLayer(tile)==1)
      return true;
    return false;
  }
  private int checkLock(Tile t){
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
  public Array<Tile> getLock(Tile t){
    int row   = (int)t.getRowCol().x;
    int col   = (int)t.getRowCol().y;
    int Layer = t.getLayer();
    Array<Tile> arr = new Array<>();
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
  public int checkLayer(Tile t){
    int row   = (int)t.getRowCol().x;
    int col   = (int)t.getRowCol().y;
    int Layer = t.getLayer()+1;
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row,col+1,Layer,1)!=null
            ||findTile(row,col+1,Layer,4)!=null||findTile(row+1,col+1,Layer,1)!=null
            ||findTile(row+1,col,Layer,1)!=null||findTile(row+1,col,Layer,2)!=null)  && t.getKind()==5){
      return 1;
    }
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row-1,col-1,Layer,5)!=null
            ||findTile(row-1,col,Layer,4)!=null||findTile(row-1,col,Layer,5)!=null
            ||findTile(row,col-1,Layer,5)!=null ||findTile(row,col-1,Layer,2)!=null)&& t.getKind()==1){
      return 1;
    }
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row-1,col,Layer,4)!=null
            ||findTile(row-1,col,Layer,5)!=null||findTile(row-1,col+1,Layer,4)!=null
            ||findTile(row,col+1,Layer,1)!=null||findTile(row,col+1,Layer,4)!=null) && t.getKind()==2){
      return 1;
    }
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row,col-1,Layer,2)!=null
            ||findTile(row,col-1,Layer,5)!=null||findTile(row+1,col,Layer,1)!=null
            ||findTile(row+1,col,Layer,2)!=null||findTile(row+1,col-1,Layer,2)!=null) && t.getKind()==4){
      return 1;
    }
    return 0;
  }
  public Array<Tile> getLockLayer(Tile t){
    int row   = (int)t.getRowCol().x;
    int col   = (int)t.getRowCol().y;
    int Layer = t.getLayer()+1;
    Array<Tile> arr = new Array<>();
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row,col+1,Layer,1)!=null
            ||findTile(row,col+1,Layer,4)!=null||findTile(row+1,col+1,Layer,1)!=null
            ||findTile(row+1,col,Layer,1)!=null||findTile(row+1,col,Layer,2)!=null ) && t.getKind()==5){

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
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row-1,col-1,Layer,5)!=null
            ||findTile(row-1,col,Layer,4)!=null||findTile(row-1,col,Layer,5)!=null
            ||findTile(row,col-1,Layer,5)!=null ||findTile(row,col-1,Layer,2)!=null) && t.getKind()==1){
      arr.add(findTile(row,col,Layer,1));
      arr.add(findTile(row,col,Layer,2));
      arr.add(findTile(row,col,Layer,4));
      arr.add(findTile(row,col,Layer,5));
      arr.add(findTile(row-1,col-1,Layer,5));
      arr.add(findTile(row-1,col,Layer,4));
      arr.add(findTile(row-1,col,Layer,5));
      arr.add(findTile(row,col-1,Layer,5));
      arr.add(findTile(row,col-1,Layer,2));
      arr.add(t);
      return arr;
    }
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row-1,col,Layer,4)!=null
            ||findTile(row-1,col,Layer,5)!=null||findTile(row-1,col+1,Layer,4)!=null
            ||findTile(row,col+1,Layer,1)!=null||findTile(row,col+1,Layer,4)!=null )&& t.getKind()==2){
      arr.add(findTile(row,col,Layer,1));
      arr.add(findTile(row,col,Layer,2));
      arr.add(findTile(row,col,Layer,4));
      arr.add(findTile(row,col,Layer,5));
      arr.add(findTile(row-1,col,Layer,4));
      arr.add(findTile(row-1,col,Layer,5));
      arr.add(findTile(row-1,col+1,Layer,4));
      arr.add(findTile(row,col+1,Layer,1));
      arr.add(findTile(row,col+1,Layer,4));
      arr.add(t);
      return arr;
    }
    if((findTile(row,col,Layer,1)!=null
            ||findTile(row,col,Layer,2)!=null|| findTile(row,col,Layer,4)!=null
            ||findTile(row,col,Layer,5)!=null|| findTile(row,col-1,Layer,2)!=null
            ||findTile(row,col-1,Layer,5)!=null||findTile(row+1,col,Layer,1)!=null
            ||findTile(row+1,col,Layer,2)!=null||findTile(row+1,col-1,Layer,2)!=null) && t.getKind()==4){

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
  public void checkMatch(Tile t){
    if(arrCompare.size==0){
      arrCompare.add(t);
    }else if(arrCompare.size==1){
      int row   = (int)arrCompare.get(0).getRowCol().x;
      int col   = (int)arrCompare.get(0).getRowCol().y;
      int id    = (int)arrCompare.get(0).getId();
      int kind  = (int)arrCompare.get(0).getKind();
      if(row==(int)t.getRowCol().x && col == (int)t.getRowCol().y && id == t.getId() && kind== t.getKind()){
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
        Tile t1 = arrCompare.get(0);
        Tile t2 = arrCompare.get(1);
        removeTile((int)t1.getRowCol().x,(int)t1.getRowCol().y,t1.getKind(),t1.getId());
        removeTile((int)t2.getRowCol().x,(int)t2.getRowCol().y,t2.getKind(),t2.getId());
        arrCompare.clear();
        setLockTile();
      }else {
        arrCompare.get(0).select(false);
        arrCompare.get(1).select(false);
        arrCompare.clear();
      }
    }
  }

  private void removeTile(int row, int col,int kind,int id){
    for (Tile t : arrTileBoard){
      if(t.getRowCol().x==row && t.getRowCol().y==col && t.getKind()==kind && t.getId()==id){
        t.dispose();
        arrTileBoard.removeIndex(arrTileBoard.indexOf(t,true));
      }
    }

  }


  //Label: tim lien ket khoa tile
  private Tile findTile(int row, int col, int Layer, int kind){
    for (Tile tt : arrTileBoard){
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
  private void hint(){
    hintBoard();
    System.out.println("get size arrhint: "+ arrTileHint.size);
    int hint =0;
    for (Tile t: arrTileHint){
      hint++;
      if(hint>2)
        return;
      t.select(true);
    }
  }
  public void hintBoard(){
    arrTileHint.clear();
    arrTileCanHint.clear();
    for (Tile t : arrTileBoard){
      if(t.block.getColor().equals(Color.WHITE))
        arrTileCanHint.add(t);
    }
    System.out.println("arrTileCanhint: "+arrTileCanHint.size);
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
  }
  private void SkipHint(){
    for (Tile t : arrTileHint)
      t.select(false);
  }
  public void shuffleBoard(){
    arrTileShuffle.clear();
    for (Tile t : arrTileBoard){
      arrTileShuffle.add(t.getId());
    }
    arrTileShuffle.shuffle();
    if(arrTileShuffle.size==arrTileBoard.size){
      for (Tile t : arrTileBoard){
        t.changeId(arrTileShuffle.get(arrTileBoard.indexOf(t,true)));
      }
    }
    hintBoard();
    if(arrTileHint.size>=2){
      lbShuffle.remove();
      return;
    }else {
      shuffleBoard();
    }


  }
  private void createlbShuffle(){
    lbShuffle = new Label("dang shuffle!!!",new Label.LabelStyle(BitmapFontC.FontAlert,null));
    lbShuffle.setPosition(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,Align.center);
    group.addActor(lbShuffle);
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

    String txt = json.toJson(lv);
    file.writeString(txt+"\n", true);
  }


}
