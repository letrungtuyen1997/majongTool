package com.ss.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.ss.GMain;

import java.util.Date;

public class Utils {
    public static String result ="";
    private Utils(){

    }
    public static JsonValue GetJsV(String s){
        JsonReader JReader= new JsonReader();
        return JReader.parse(s);
    }

    public static String ConvertDateTime(String s){
//        System.out.println("milisecond: "+s);
//        String result="";
        if(Gdx.app.getGraphics().getType() == Graphics.GraphicsType.WebGL) {
//            result = GMain.FormatDate(s);
        }else {
            Date date = new Date(Long.parseLong(s));
            result = date.toString();

        }
        return result;
    }
    public static class JsonLevel{
//        @Serialization
        public int row;

//        @Serialization
        public int col;

//        @Serialization
        public int layer;

//        @Serialization
        public float x;

//        @Serialization
        public float y;

//        @Serialization
        public int kind;

//        @Serialization
        public int id;

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        public int getLayer() {
            return layer;
        }

        public void setLayer(int layer) {
            this.layer = layer;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public int getKind() {
            return kind;
        }

        public void setKind(int kind) {
            this.kind = kind;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


    }
}
