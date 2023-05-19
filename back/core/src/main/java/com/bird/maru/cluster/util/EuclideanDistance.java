package com.bird.maru.cluster.util;

public class EuclideanDistance implements DistanceMeasure {

    @Override
    public double compute(double minX, double minY, double maxX, double maxY) {
        double dx = minX - maxX;
        double dy = minY - maxY;
        return dx * dx + dy * dy;
    }

}
