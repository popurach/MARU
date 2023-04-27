package com.bird.maru.cluster.util;

public class HaversineDistance implements DistanceMeasure {

    private static final double EARTH_RADIUS = 6371;

    @Override
    public double compute(double minX, double minY, double maxX, double maxY) {

        double dLong = Math.toRadians((maxX - minX));
        double dLat = Math.toRadians((maxY - minY));

        double startLat = Math.toRadians(minY);
        double endLat = Math.toRadians(maxY);

        double t = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(t), Math.sqrt(1 - t));

        return EARTH_RADIUS * c; // <-- d
    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

}
