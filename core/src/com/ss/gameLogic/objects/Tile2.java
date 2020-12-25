package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.action.exAction.GShakeAction;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.gameScene;
import com.ss.scenes.gameScene2;

public class Tile2 {
  public Image            block,OverLay;
  private gameScene2       gameScene;
  private float           SclW        = 0.4f;
  private float           SclH        = 0.4f;
  private int             Layer       = 0;
  private int             row,col;
  public  float           x,y;
  public  int             id;
  public  int             kind;
  private Label           IdLb;
  private Group           gr = new Group();
  public Tile2(Group group, gameScene2 gameScene){
    this.gameScene  = gameScene;
    group.addActor(gr);
    if(gameScene!=null){
      addevent();
    }

  }
  public void createID(int Id){
    this.id = Id;
    IdLb = new Label(""+id,new Label.LabelStyle(BitmapFontC.font_white, Color.BLUE));
    IdLb.setFontScale(0.6f);
    IdLb.setAlignment(Align.center);
    IdLb.setPosition(block.getX(Align.center),block.getY(Align.center),Align.center);
    gr.addActor(IdLb);
    OverLay = GUI.createImage(TextureAtlasC.uiAtlas,"titlebg");
    OverLay.setPosition(block.getX(),block.getY());
    gr.addActor(OverLay);
    select(false);

  }

  public void setPos(float x ,float y,int kind){
    block = GUI.createImage(TextureAtlasC.uiAtlas,"cucxilau");
//    block.setPosition();
    System.out.println("get Layer: "+getLayer());
    gr.setSize(block.getWidth(),block.getHeight());
    gr.setOrigin(Align.center);
    gr.addActor(block);
    gr.setPosition(x-block.getWidth()/2-((Layer-1)* Config.paddingX),y-block.getHeight()/2-((Layer-1)* Config.paddingY));
//    gr.debug();
    this.x=gr.getX();
    this.y=gr.getY();
    this.kind = kind;

  }
  public int getKind(){return kind;}
  public void setLayer(Array<Boolean> arrLayer){
    int dem=0;
    for (Boolean b: arrLayer){
      if(b)
        dem++;
    }
    Layer = dem;
    System.out.println("check Layer: "+Layer);
  }
  public void setLayer2(int layer){
    Layer = layer;
  }
  public int getLayer(){
    return Layer;
  }
  public void setRowCol(int r,int c){
    row=r;
    col=c;
  }
  public Vector2 getRowCol(){
    return new Vector2(row,col);
  }
  public Vector2 getXY(){
    return new Vector2(x,y);
  }
  public int getId(){return id;}
  public void setColor(Color set){
    block.setColor(set);
  }
  public void select(boolean set){
    OverLay.setVisible(set);
  }
  public boolean checkLock(){
    if(block.getColor()==Color.DARK_GRAY)
      return true;
    return false;
  }
  public void ActionSelect(){

    gr.addAction(Actions.sequence(
            GShakeAction.shakeAction(0.5f,5, Interpolation.linear)
    ));
  }
  public void dispose(){
    gr.clear();
    gr.remove();
  }
  public void changeId(int Id){
    gr.addAction(Actions.sequence(
            Actions.moveTo(GStage.getWorldWidth()/2,GStage.getWorldHeight()/2,0.5f),
            GSimpleAction.simpleAction((d,a)->{
              this.id = Id;
              IdLb.setText(""+id);
              return true;
            }),
            Actions.moveTo(x,y,0.5f)
    ));


  }

  private void addevent(){
    gr.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Array<Tile2> arrTile = gameScene.getLock(Tile2.this);
        Array<Tile2> arrTile2 = gameScene.getLockLayer(Tile2.this);
        if(arrTile!=null){
          for (Tile2 t : arrTile){
            t.ActionSelect();
          }

        }else if(gameScene.checkLayer(Tile2.this)==1) {
//          ActionSelect();
          Array<Tile2> arr = new Array<>();
          System.out.println("check size arrTile2: "+arrTile2.size);
          for (int i=0;i<arrTile2.size;i++){
            System.out.println(arrTile2.get(i));
            if(arrTile2.get(i)!=null){
              arr.add(arrTile2.get(i));
            }
          }
          System.out.println("check size arrTile2: "+arrTile2.size);
          if(arr.size>=2){
            for (Tile2 t : arr){
              System.out.println("tile orthe layer: "+t.getRowCol().x+"_"+t.getRowCol().y+"_"+t.getKind()+"_"+t.getLayer());
              t.ActionSelect();
            }
          }

        }else {
          select(true);
          gameScene.checkMatch(Tile2.this);
        }
        return super.touchDown(event, x, y, pointer, button);
      }
    });
  }


}
