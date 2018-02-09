package com.scott.honerv8loadingview.utils;

import android.graphics.Point;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2018-02-09 11:07</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class ViewMathUtils {

    /***
     * 获取圆上一点(x,y) 转过 degress 角度后，在圆周上的点
     * @param x0 圆心点x
     * @param y0 圆心点y
     * @param x  圆周上某点x
     * @param y  圆周上某点y
     * @param degress 转过的角度
     * @return
     */
    public static Point getPointTurnDegress(double x0,double y0,
                                            double x,double y,double degress) {
        double radian = (degress * Math.PI) / 180;
        double x2 = x0 + (x - x0) * Math.cos(radian) - (y - y0) * Math.sin(radian);
        double y2 = y0 + (x - x0) * Math.sin(radian) + (y - y0) * Math.cos(radian);
        return new Point((int)(x2),(int)(y2));
    }


    /***
     * 获取圆上一点(x,y) 转过 radian 弧度 后，在圆周上的点
     * @param x0 圆心点x
     * @param y0 圆心点y
     * @param x  圆周上某点x
     * @param y  圆周上某点y
     * @param degress 转过的弧度
     * @return
     */
    public static Point getPointTurnRadian(double x0,double y0,
                                           double x,double y,double degress) {
        double radian = (degress * Math.PI) / 180;
        return getPointTurnDegress(x0,y0,x,y,radian);
    }

    public static Point getPointTurnDegress(Point p0,Point p1,double degress) {
        return getPointTurnDegress(p0.x,p0.y,p1.x,p1.y,degress);
    }

    public static Point getMidPoint(Point p1,Point p2) {
        Point p3 = new Point();
        p3.x = (p1.x + p2.x) / 2;
        p3.y = (p1.y + p2.y) / 2;
        return p3;
    }
}
