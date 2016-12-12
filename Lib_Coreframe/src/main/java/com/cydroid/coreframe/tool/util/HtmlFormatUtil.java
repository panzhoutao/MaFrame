package com.cydroid.coreframe.tool.util;

/**
 * Created by yaocui on 16/2/21.
 */
public class HtmlFormatUtil {
    public static final String black="#000000";
    public static final String transblue="#6042a6e5";

    public static final String txt_blue="#274dab";
    public static final String txt_gray="#a1a6a9";
    public static final String txt_dark_gray="#656565";
    public static final String txt_press_blue="#66ccff";
    public static final String txt_green="#4ba47d";
    public static final String divider_color="#ffd5d9df";
    public static final String txt_orange="#d97b11";
    public static final String progress_bg="#EFEFEF";
    public static final String progress_febg="#1BA2D7";
    public static final String grayline="#E3E4E5";
    public static final String background="#EEEEEE";
    public static final String orangeline="#ef8435";
    public static final String radio_bg_green="#4ca47f";
    public static final String txt_red="#D53C27";

    public static final String fontEnd="</font>";
    public static final String smallStart="<small>";
    public static final String smallEnd="</small>";
    public static final String newLine="<br></br>";

    public static String getColorBegin(String color){
        return "<font color="+color+">";

    }
    public static String getColorSizeBegin(String color,int size){
        return "<font color="+color+" size="+size+">";
    }

}
