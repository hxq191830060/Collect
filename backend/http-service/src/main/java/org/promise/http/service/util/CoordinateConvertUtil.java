package org.promise.http.service.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author moqi
 * @date 2022/5/27 19:49
 */
public class CoordinateConvertUtil {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RectangularCoordinate {

        private double x;

        private double y;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PolarCoordinate {

        private double x;

        private double y;

        private double angle;

        private double distance;

        public PolarCoordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }

    }

    public static RectangularCoordinate polar2Rectangular(PolarCoordinate polarCoordinate) {
        double x = polarCoordinate.distance * Math.cos(polarCoordinate.angle);
        double y = polarCoordinate.distance * Math.sin(polarCoordinate.angle);
        RectangularCoordinate rectangularCoordinate = new RectangularCoordinate();
        rectangularCoordinate.setX(x + polarCoordinate.x);
        rectangularCoordinate.setY(y + polarCoordinate.y);
        return rectangularCoordinate;
    }

    public static PolarCoordinate rectangular2Polar(RectangularCoordinate rectangularCoordinate, double x, double y) {
        double tmpX = rectangularCoordinate.x - x;
        double tmpY = rectangularCoordinate.y - y;
        PolarCoordinate polarCoordinate = new PolarCoordinate();
        polarCoordinate.setAngle(Math.atan2(tmpY, tmpX));
        polarCoordinate.setDistance(Math.sqrt(tmpX * tmpX + tmpY * tmpY));
        polarCoordinate.setX(x);
        polarCoordinate.setY(y);
        return polarCoordinate;
    }

}
