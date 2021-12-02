package com.nineclock.common.utils;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * 距离计算工具类
 */
public class DistanceUtil {


    /**
     * 计算两个地点的距离
     * @param latFrom
     * @param lonFrom
     * @param latTo
     * @param lonTo
     * @return
     */
    public static Integer getDistanceMeter(Double latFrom, Double lonFrom, Double latTo, Double lonTo) {
        GlobalCoordinates source = new GlobalCoordinates(latFrom, lonFrom);
        GlobalCoordinates target = new GlobalCoordinates(latTo, lonTo);
        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target);
        return new Double(geoCurve.getEllipsoidalDistance()).intValue();
    }

}
