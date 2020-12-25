package com.ss.scenes;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.commons.BitmapFontC;
import com.ss.commons.TextureAtlasC;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.objects.Tile;
import com.ss.gameLogic.objects.tileBg;

public class baseScene extends GScreen {
  private Group        group          = new Group();
  public Array<tileBg> arrTileBg      = new Array<>();
  public Array<Tile>   arrTile        = new Array<>();
  public Label         text;
  public int           countTile      = 0;
  @Override
  public void dispose() {

  }

  @Override
  public void init() {
    GStage.addToLayer(GLayer.ui,group);
    Image bg = GUI.createImage(TextureAtlasC.uiAtlas,"bg");
    bg.setSize(GStage.getWorldWidth(),GStage.getWorldHeight());
    group.addActor(bg);
    createBg();

    Button btnExport = GUI.createTextButton(TextureAtlasC.uiAtlas.findRegion("btnYellow"),BitmapFontC.font_white,"xuất");
    btnExport.setPosition(GStage.getWorldWidth()/2-btnExport.getWidth()/2,GStage.getWorldHeight()-btnExport.getHeight(), Align.center);
    group.addActor(btnExport);
    btnExport.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setScreen(new gameScene(arrTile));
        return super.touchDown(event, x, y, pointer, button);
      }
    });
    Button btnReset = GUI.createTextButton(TextureAtlasC.uiAtlas.findRegion("btnYellow"),BitmapFontC.font_white,"reset");
    btnReset.setPosition(GStage.getWorldWidth()/2+btnReset.getWidth()/2,GStage.getWorldHeight()-btnReset.getHeight(), Align.center);
    group.addActor(btnReset);
    btnReset.addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        reset();
        return super.touchDown(event, x, y, pointer, button);
      }
    });

    text = new Label(""+countTile,new Label.LabelStyle(BitmapFontC.FontAlert,null));
    text.setPosition(50,50);
    group.addActor(text);
  }

  @Override
  public void run() {

  }
  private void createBg(){
    for (int i = 0; i< Config.row; i++){
      for (int j=0;j<Config.col;j++){
        tileBg obj =new tileBg(group,i,j,this);
        arrTileBg.add(obj);
      }
    }
  }
  private void reset(){
    for (Tile t : arrTile)
      t.dispose();
    arrTile.clear();
    countTile=0;
    text.setText(countTile+"");
    for (tileBg tbg : arrTileBg)
      tbg.resetLayer();
  }

  public void setArrTile(Tile tile){
    arrTile.add(tile);
  }
  public void setTextTile(){
    countTile++;
    float s = (float)countTile/4;
    text.setText(countTile+" số cặp: "+s);
  }


}
