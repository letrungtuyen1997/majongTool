package com.ss.gameLogic.config;

import com.badlogic.gdx.Gdx;
import com.ss.core.util.GStage;
public class Config {
    public static float ScreenW = GStage.getWorldWidth();
    public static float ScreenH = GStage.getWorldHeight();
    public static boolean       checkConnet         = true;
    public static boolean       checkWheel          = false;
    public static String        megaID              ="10001";
    public static String        token               ="";
    public static int           veloccity           =3;
    public static String        uri                 = Gdx.files.internal("uri/uri.txt").readString();
    public static boolean       remoteEffect        = Boolean.parseBoolean(Gdx.files.internal("uri/effect.txt").readString());
    public static int           condi_merge         = Integer.parseInt(Gdx.files.internal("uri/condi_merge.txt").readString());
    public static int           row                 = 10;
    public static int           col                 = 10;
    public static float         paddingX            = 3;
    public static float         paddingY            = 3;
    public static float         duraMove            = 1;
    public static float         TileW               = 93;
    public static float         TileH               = 120;


    public static int           quanLayer           = 10;
    public static int           quanAni             = 40;

    public static int           LEFTUP              = 1;
    public static int           CENTERUP            = 2;
    public static int           RIGHTUP             = 3;
    public static int           LEFTCENTER          = 4;
    public static int           CENTER              = 5;
    public static int           RIGHTCENTER         = 6;
    public static int           LEFTDOWN            = 7;
    public static int           CENTERDOWN          = 8;
    public static int           RIGHTDOWN           = 9;
}

