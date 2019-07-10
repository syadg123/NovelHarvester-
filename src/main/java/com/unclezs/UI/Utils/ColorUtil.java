package com.unclezs.UI.Utils;

/*
 *@author unclezs.com
 *@date 2019.06.24 18:50
 */
public class ColorUtil {
    /**
     * 根据RGB值判断 深色与浅色
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static boolean isDark(Double r,Double g,Double b){
        System.out.println(r);
        System.out.println(g);
        System.out.println(b);
        if((r*0.299 + g*0.578 + b*0.114) >= 192){ //浅色
            return false;
        }else{  //深色
            return true;
        }
    }
}
