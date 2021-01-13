package com.ss.gameLogic.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.baseScene;

public class tileBg {
//  public int id
  public Image          block;
  public Group          group;
  public Group          gr                      = new Group();
  public Rectangle      bodyLeftTop             = new Rectangle();
  public Rectangle      bodyCenterTop           = new Rectangle();
  public Rectangle      bodyRightTop            = new Rectangle();
  public Rectangle      bodyLeftCenter          = new Rectangle();
  public Rectangle      bodyCenter              = new Rectangle();
  public Rectangle      bodyRightCenter         = new Rectangle();
  public Rectangle      bodyLeftBottom          = new Rectangle();
  public Rectangle      bodyCenterBottom        = new Rectangle();
  public Rectangle      bodyRightBottom         = new Rectangle();
  public GShapeSprite   bodyLeftTop2            = new GShapeSprite();
  public GShapeSprite   bodyCenterTop2          = new GShapeSprite();
  public GShapeSprite   bodyRightTop2           = new GShapeSprite();
  public GShapeSprite   bodyLeftCenter2         = new GShapeSprite();
  public GShapeSprite   bodyCenter2             = new GShapeSprite();
  public GShapeSprite   bodyRightCenter2        = new GShapeSprite();
  public GShapeSprite   bodyLeftBottom2         = new GShapeSprite();
  public GShapeSprite   bodyCenterBottom2       = new GShapeSprite();
  public GShapeSprite   bodyRightBottom2        = new GShapeSprite();
  public Array<Boolean> arrLayer                = new Array<>();
  public  int           row,col;
//  private String
  private float         x,y;
  private float         sclW =0.2f;
  private float         sclH =0.2f;
  private float         alpha = 0.6f;
  private int           layer=0;
  private baseScene     baseScene;
  public tileBg(Group group, int  i, int j,baseScene baseScene){
    this.baseScene = baseScene;
    GStage.addToLayer(GLayer.top,gr);
    this.group = group;
    row=i;
    col=j;
    block = GUI.createImage(TextureAtlasC.uiAtlas,"titlebg");
    block.setPosition((GStage.getWorldWidth()/2- Config.col/2*block.getWidth())+j*block.getWidth(),(GStage.getWorldHeight()/2- Config.row/2*block.getHeight())+i*block.getHeight());
    group.addActor(block);
    //// lb ///////////
    Label lb = new Label("["+row+","+col+"]",new Label.LabelStyle(BitmapFontC.font_white,null));
    lb.setFontScale(0.4f);
    lb.setAlignment(Align.center);
    lb.setPosition(block.getX(Align.center),block.getY(Align.center)+10,Align.center);
    group.addActor(lb);
    lb.getColor().a=0.5f;

    //////// leftUp /////////
    bodyLeftTop.set(block.getX()-block.getWidth()*sclW/2,block.getY()-block.getHeight()*sclH/2,block.getWidth()*sclW,block.getHeight()*sclH);
    bodyLeftTop2.createRectangle(true, bodyLeftTop.getX(), bodyLeftTop.getY(), bodyLeftTop.width, bodyLeftTop.height);
    bodyLeftTop2.setColor(239/255f,11/255f,11/255f,alpha);
//    bodyLeftUp2.setC
    gr.addActor(bodyLeftTop2);

    //////// centerUp /////////
    bodyCenterTop.set(block.getX()+block.getWidth()/2-block.getWidth()*sclW/2,block.getY()-block.getHeight()*sclH/2,block.getWidth()*sclW,block.getHeight()*sclH);
    bodyCenterTop2.createRectangle(true, bodyCenterTop.getX(), bodyCenterTop.getY(), bodyCenterTop.width, bodyCenterTop.height);
    bodyCenterTop2.setColor(239/255f,11/255f,11/255f,alpha);
    gr.addActor(bodyCenterTop2);

    //////// rightUp /////////
    bodyRightTop.set(block.getX()+block.getWidth()-block.getWidth()*sclW/2,block.getY()-block.getHeight()*sclH/2,block.getWidth()*sclW,block.getHeight()*sclH);
    bodyRightTop2.createRectangle(true, bodyRightTop.getX(), bodyRightTop.getY(), bodyRightTop.width, bodyRightTop.height);
    bodyRightTop2.setColor(239/255f,11/255f,11/255f,alpha);
    gr.addActor(bodyRightTop2);

    //////// LeftCenter /////////
    bodyLeftCenter.set(block.getX()-block.getWidth()*sclW/2,block.getY()+block.getHeight()/2-block.getHeight()*sclH/2,block.getWidth()*sclW,block.getHeight()*sclH);
    bodyLeftCenter2.createRectangle(true,bodyLeftCenter.getX(),bodyLeftCenter.getY(),bodyLeftCenter.width,bodyLeftCenter.height);
    bodyLeftCenter2.setColor(239/255f,11/255f,11/255f,1f);
    gr.addActor(bodyLeftCenter2);

    //////// Center /////////
    bodyCenter.set(block.getX()+block.getWidth()/2-block.getWidth()*sclW/2,block.getY()+block.getHeight()/2-block.getHeight()*sclH/2,block.getWidth()*sclW,block.getHeight()*sclH);
    bodyCenter2.createRectangle(true,bodyCenter.getX(),bodyCenter.getY(),bodyCenter.width,bodyCenter.height);
    bodyCenter2.setColor(239/255f,11/255f,11/255f,alpha);
    gr.addActor(bodyCenter2);

    //////// RightCenter /////////
    bodyRightCenter.set(block.getX()+block.getWidth()-block.getWidth()*sclW/2,block.getY()+block.getHeight()/2-block.getHeight()*sclH/2,block.getWidth()*sclW,block.getHeight()*sclH);
    bodyRightCenter2.createRectangle(true,bodyRightCenter.getX(),bodyRightCenter.getY(),bodyRightCenter.width,bodyRightCenter.height);
    bodyRightCenter2.setColor(239/255f,11/255f,11/255f,alpha);
    gr.addActor(bodyRightCenter2);

    //////// leftdown /////////
    bodyLeftBottom.set(block.getX()-block.getWidth()*sclW/2,block.getY()+block.getHeight()-block.getHeight()*sclH/2,block.getWidth()*sclW,block.getHeight()*sclH);
    bodyLeftBottom2.createRectangle(true, bodyLeftBottom.getX(), bodyLeftBottom.getY(), bodyLeftBottom.width, bodyLeftBottom.height);
    bodyLeftBottom2.setColor(239/255f,11/255f,11/255f,alpha);
    gr.addActor(bodyLeftBottom2);

    //////// centerdown /////////
    bodyCenterBottom.set(block.getX()+block.getWidth()/2-block.getWidth()*sclW/2,block.getY()+block.getHeight()-block.getHeight()*sclH/2,block.getWidth()*sclW,block.getHeight()*sclH);
    bodyCenterBottom2.createRectangle(true, bodyCenterBottom.getX(), bodyCenterBottom.getY(), bodyCenterBottom.width, bodyCenterBottom.height);
    bodyCenterBottom2.setColor(239/255f,11/255f,11/255f,alpha);
    gr.addActor(bodyCenterBottom2);

    //////// rightdown /////////
    bodyRightBottom.set(block.getX()+block.getWidth()-block.getWidth()*sclW/2,block.getY()+block.getHeight()-block.getHeight()*sclH/2,block.getWidth()*sclW,block.getHeight()*sclH);
    bodyRightBottom2.createRectangle(true, bodyRightBottom.getX(), bodyRightBottom.getY(), bodyRightBottom.width, bodyRightBottom.height);
    bodyRightBottom2.setColor(239/255f,11/255f,11/255f,alpha);
    gr.addActor(bodyRightBottom2);






    x=block.getX()+block.getWidth()/2;
    y=block.getY()+block.getHeight()/2;
    initArrLayer();
    addEvent();
  }

  public int getRow() {
    return row;
  }
  public int getCol(){
    return col;
  }
  public float getX(){
    return x;
  }
  public float getY(){
    return y;
  }
  public Vector3 XYLeftUp(){
      return new Vector3(bodyLeftTop.x+ bodyLeftTop.width/2, bodyLeftTop.y+ bodyLeftTop.height/2,Config.LEFTUP);
  }
  public Vector3 XYCenterUp(){
      return new Vector3(bodyCenterTop.x+ bodyCenterTop.width/2, bodyCenterTop.y+ bodyCenterTop.height/2,Config.CENTERUP);
  }
  public Vector3 XYRightUp(){
      return new Vector3(bodyRightTop.x+ bodyRightTop.width/2, bodyRightTop.y+ bodyRightTop.height/2,Config.RIGHTUP);
  }
  public Vector3 XYLeftCenter(){
      return new Vector3(bodyLeftCenter.x+bodyLeftCenter.width/2,bodyLeftCenter.y+bodyLeftCenter.height/2,Config.LEFTCENTER);
  }
  public Vector3 XYCenter(){
      return new Vector3(bodyCenter.x+bodyCenter.width/2,bodyCenter.y+bodyCenter.height/2,Config.CENTER);
  }
  public Vector3 XYRightCenter(){
      return new Vector3(bodyRightCenter.x+bodyRightCenter.width/2,bodyRightCenter.y+bodyRightCenter.height/2,Config.RIGHTCENTER);
  }
  public Vector3 XYLefttDown(){
      return new Vector3(bodyLeftBottom.x+ bodyLeftBottom.width/2, bodyLeftBottom.y+ bodyLeftBottom.height/2,Config.LEFTDOWN);
  }
  public Vector3 XYCenterDown(){
      return new Vector3(bodyCenterBottom.x+ bodyCenterBottom.width/2, bodyCenterBottom.y+ bodyCenterBottom.height/2,Config.CENTERDOWN);
  }
  public Vector3 XYRightDown(){
      return new Vector3(bodyRightBottom.x+ bodyRightBottom.width/2, bodyRightBottom.y+ bodyRightBottom.height/2,Config.RIGHTDOWN);
  }
  private void initArrLayer(){
    for(int i=0;i<Config.quanLayer;i++){
      arrLayer.add(false);
    }
  }
  private int checkLayer(){
    for(Boolean bool :arrLayer){
      if(bool==false)
        return arrLayer.indexOf(bool,true);
    }
    return -1;
  }
  public void resetLayer(){
    arrLayer.clear();
    initArrLayer();
  }
  public int getLayer(){
    return layer;
  }
  public void setLayer(){
    int indexLayer = checkLayer();
    if(indexLayer!=-1){
      arrLayer.set(indexLayer,true);
    }
  }
  private void addEvent(){
    bodyLeftTop2.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        setLayer();
        Tile tile = new Tile(group,null,baseScene);
        tile.setRowCol(row,col);
        tile.setLayer(arrLayer);
        tile.setPos(XYLeftUp().x, XYLeftUp().y,(int)XYLeftUp().z,false);
        baseScene.setArrTile(tile);
        baseScene.setTextTile();
        return super.touchDown(event, x, y, pointer, button);
      }
    });
    bodyCenterTop2.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        setLayer();
        Tile tile = new Tile(group,null,baseScene);
        tile.setRowCol(row,col);
        tile.setLayer(arrLayer);
        tile.setPos(XYCenterUp().x, XYCenterUp().y,(int)XYCenterUp().z,false);
        baseScene.setArrTile(tile);
        baseScene.setTextTile();

        return super.touchDown(event, x, y, pointer, button);
      }
    });
    bodyRightTop2.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        setLayer();
        Tile tile = new Tile(group,null,baseScene);
        tile.setRowCol(row,col);
        tile.setLayer(arrLayer);
        tile.setPos(XYRightUp().x, XYRightUp().y,(int)XYRightUp().z,false);
        baseScene.setArrTile(tile);
        baseScene.setTextTile();

        return super.touchDown(event, x, y, pointer, button);
      }
    });
    bodyLeftCenter2.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        setLayer();
        Tile tile = new Tile(group,null,baseScene);
        tile.setRowCol(row,col);
        tile.setLayer(arrLayer);
        tile.setPos(XYLeftCenter().x, XYLeftCenter().y,(int)XYLeftCenter().z,false);
        baseScene.setArrTile(tile);
        baseScene.setTextTile();

        return super.touchDown(event, x, y, pointer, button);
      }
    });
    bodyCenter2.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        setLayer();
        Tile tile = new Tile(group,null,baseScene);
        tile.setRowCol(row,col);
        tile.setLayer(arrLayer);
        tile.setPos(XYCenter().x, XYCenter().y,(int)XYCenter().z,false);
        baseScene.setArrTile(tile);
        baseScene.setTextTile();

        return super.touchDown(event, x, y, pointer, button);
      }
    });
    bodyRightCenter2.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        setLayer();
        Tile tile = new Tile(group,null,baseScene);
        tile.setRowCol(row,col);
        tile.setLayer(arrLayer);
        tile.setPos(XYRightCenter().x, XYRightCenter().y,(int)XYRightCenter().z,false);
        baseScene.setArrTile(tile);
        baseScene.setTextTile();

        return super.touchDown(event, x, y, pointer, button);
      }
    });
    bodyLeftBottom2.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        setLayer();
        Tile tile = new Tile(group,null,baseScene);
        tile.setRowCol(row,col);
        tile.setLayer(arrLayer);
        tile.setPos(XYLefttDown().x, XYLefttDown().y,(int)XYLefttDown().z,false);
        baseScene.setArrTile(tile);
        baseScene.setTextTile();

        return super.touchDown(event, x, y, pointer, button);
      }
    });
    bodyCenterBottom2.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        setLayer();
        Tile tile = new Tile(group,null,baseScene);
        tile.setRowCol(row,col);
        tile.setLayer(arrLayer);
        tile.setPos(XYCenterDown().x, XYCenterDown().y,(int)XYCenterDown().z,false);
        baseScene.setArrTile(tile);
        baseScene.setTextTile();

        return super.touchDown(event, x, y, pointer, button);
      }
    });
    bodyRightBottom2.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        setLayer();
        Tile tile = new Tile(group,null,baseScene);
        tile.setRowCol(row,col);
        tile.setLayer(arrLayer);
        tile.setPos(XYRightDown().x, XYRightDown().y,(int)XYRightDown().z,false);
        baseScene.setArrTile(tile);
        baseScene.setTextTile();

        return super.touchDown(event, x, y, pointer, button);
      }
    });

  }


}
