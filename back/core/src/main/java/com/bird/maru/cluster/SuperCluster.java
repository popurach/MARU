/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bird.maru.cluster;

import com.bird.maru.cluster.geo.Cluster;
import com.bird.maru.cluster.geo.MainCluster;
import com.bird.maru.cluster.geo.Marker;
import com.bird.maru.cluster.geo.PointCluster;
import com.bird.maru.cluster.geo.Property;
import com.bird.maru.cluster.geo.SuperClusterParams;
import com.bird.maru.cluster.mapper.MarkerMapper;
import com.bird.maru.cluster.util.DistanceMeasure;
import com.bird.maru.cluster.util.PointConverter;
import com.bird.maru.domain.model.type.GeoType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import org.wololo.geojson.Feature;
import org.wololo.geojson.Point;

/**
 * @author yeozkaya@gmail.com
 */
public class SuperCluster {

    private final int minZoom;
    private final int maxZoom;
    private final int initZoom;
    private final int nodeSize;
    private final int radius;
    private final int extent;
    private final List<Marker> points;
    private final KDBush[] trees;
    private final PointConverter pointConverter;
    private final DistanceMeasure distanceMeasure;

    @Builder
    public SuperCluster(
            SuperClusterParams superClusterParams, List<Marker> points,
            PointConverter pointConverter, DistanceMeasure distanceMeasure
    ) {
        this.radius = superClusterParams.getRadius();
        this.extent = superClusterParams.getExtent();
        this.minZoom = superClusterParams.getMinZoom();
        this.maxZoom = superClusterParams.getMaxZoom();
        this.initZoom = superClusterParams.getInitZoom();
        this.nodeSize = superClusterParams.getNodeSize();
        this.pointConverter = pointConverter;
        this.distanceMeasure = distanceMeasure;
        this.trees = new KDBush[maxZoom + 2];
        this.points = points;

        List<MainCluster> clusters = new ArrayList<>();

        for (int i = 0; i < this.points.size(); i++) {
            clusters.add(createPointCluster(MarkerMapper.toRawPoint(this.points.get(i)), i));
        }

        trees[maxZoom + 1] = new KDBush(clusters, nodeSize);

        for (int z = maxZoom; z >= minZoom; z--) {
            clusters = this.initCluster(clusters, z);
            this.trees[z] = new KDBush(clusters, nodeSize);
        }

    }

    /**
     * SuperCluster 객체 생성 시 KDBush 초기화 진행
     */
    private List<MainCluster> initCluster(List<MainCluster> points, int zoom) {
        List<MainCluster> clusters = new ArrayList<>();

        double actualRadius = calculateActualRadius(zoom);

        for (int i = 0; i < points.size(); i++) {
            MainCluster p = points.get(i);

            if (p.getZoom() <= zoom) {
                continue;
            }
            p.setZoom(zoom);

            KDBush tree = this.trees[zoom + 1];
            int[] neighborIds = withIn(p.getX(), p.getY(), actualRadius, tree);

            Integer nowCount = p.getCount();
            nowCount = nowCount != null ? nowCount : 1;
            double weightedX = p.getX() * nowCount;
            double weightedY = p.getY() * nowCount;

            int id = (i << 5) + (zoom + 1);

            for (int neighborId : neighborIds) {
                MainCluster b = tree.getPoints().get(neighborId);

                if (b.getZoom() <= zoom) {
                    continue;
                }

                b.setZoom(zoom);

                Integer tempCount = b.getCount();
                tempCount = tempCount != null ? tempCount : 1;
                weightedX += b.getX() * tempCount;
                weightedY += b.getY() * tempCount;

                nowCount += tempCount;
                b.setParentId(id);
            }

            if (nowCount == 1) {
                clusters.add(p);
            } else {
                p.setParentId(id);
                clusters.add(createCluster(weightedX / nowCount, weightedY / nowCount, id, nowCount));
            }
        }

        return clusters;
    }

    private int[] withIn(double x, double y, double actualRadius, KDBush tree) {
        return withIn(tree.getIds(), tree.getCoords(), x, y, actualRadius, this.nodeSize);
    }


    private int[] withIn(int[] ids, double[] coordinates, double qx, double qy, double actualRadius, int nodeSize) {
        LinkedList<Integer> stack = new LinkedList<>();
        stack.push(0);
        stack.push(ids.length - 1);
        stack.push(0);

        List<Integer> result = new ArrayList<>();
        double r2 = actualRadius * actualRadius;

        while (!stack.isEmpty()) {
            int axis = stack.pop();
            int right = stack.pop();
            int left = stack.pop();

            if (right - left <= nodeSize) {
                for (int i = left; i <= right; i++) {
                    if (distanceMeasure.compute(coordinates[2 * i], coordinates[2 * i + 1], qx, qy) <= r2) {
                        result.add(ids[i]);
                    }
                }
                continue;
            }

            int mid = (left + right) / 2;

            double x = coordinates[2 * mid];
            double y = coordinates[2 * mid + 1];

            if (distanceMeasure.compute(x, y, qx, qy) <= r2) {
                result.add(ids[mid]);
            }

            int nextAxis = (axis + 1) % 2;

            if (axis == 0 ? qx - actualRadius <= x : qy - actualRadius <= y) {
                stack.push(left);
                stack.push(mid - 1);
                stack.push(nextAxis);
            }
            if (axis == 0 ? qx + actualRadius >= x : qy + actualRadius >= y) {
                stack.push(mid + 1);
                stack.push(right);
                stack.push(nextAxis);
            }
        }

        return result.stream().mapToInt(Integer::valueOf).toArray();
    }

    /**
     * 단일 클러스터(포인트) 생성
     */
    private PointCluster createPointCluster(double[] point, int id) {

        double x = pointConverter.convertLngToX(point[0]);
        double y = pointConverter.convertLatToY(point[1]);

        return new PointCluster(x, y, initZoom, id, -1);
    }

    /**
     * 클러스터(다중 포인트) 생성
     */
    private Cluster createCluster(double x, double y, int id, int count) {
        return new Cluster(x, y, id, count);
    }

    /**
     * 클러스터링 알고리즘 수행
     *
     * @param boundingBox : 현재 사용자의 지도 범위
     * @param zoom        : 현재 사용자의 지도 줌 레벨
     */
    public List<Feature> run(double[] boundingBox, int zoom) {
        double minLng = ((boundingBox[0] + 180) % 360 + 360) % 360 - 180;
        double minLat = Math.max(-90, Math.min(90, boundingBox[1]));
        double maxLng = boundingBox[2] == 180 ? 180 : ((boundingBox[2] + 180) % 360 + 360) % 360 - 180;
        double maxLat = Math.max(-90, Math.min(90, boundingBox[3]));

        if (boundingBox[2] - boundingBox[0] >= 360) {
            minLng = -180;
            maxLng = 180;
        } else if (minLng > maxLng) {
            List<Feature> easternHem = this.run(new double[] { minLng, minLat, 180, maxLat }, zoom);
            List<Feature> westernHem = this.run(new double[] { -180, minLat, maxLng, maxLat }, zoom);
            easternHem.addAll(westernHem);
            return easternHem;
        }

        KDBush tree = this.trees[limitZoom(zoom)];
        int[] ids = rangeInBoundingBox(pointConverter.convertLngToX(minLng), pointConverter.convertLatToY(maxLat),
                                       pointConverter.convertLngToX(maxLng), pointConverter.convertLatToY(minLat),
                                       tree);
        double actualRadius = calculateActualRadius(zoom);

        List<Feature> clusters = new ArrayList<>();
        for (int id : ids) {
            MainCluster c = tree.getPoints().get(id);
            clusters.add(
                    (c.getCount() != null) ? makeClusterFeature(c, actualRadius) : makePointFeature(this.points.get(c.getIndex().intValue()))
            );
        }
        return clusters;
    }


    /**
     * 안정성을 위해 maxZoom+1
     */
    private int limitZoom(int z) {
        return Math.max(this.minZoom, Math.min(z, this.maxZoom + 1));
    }

    private int[] rangeInBoundingBox(double minX, double minY, double maxX, double maxY, KDBush tree) {
        return range(tree.getIds(), tree.getCoords(), minX, minY, maxX, maxY, this.nodeSize);
    }

    private int[] range(int[] ids, double[] coordinates, double minX, double minY, double maxX, double maxY, int nodeSize) {
        LinkedList<Integer> stack = new LinkedList<>();
        stack.push(0);
        stack.push(ids.length - 1);
        stack.push(0);

        List<Integer> result = new ArrayList<>();

        double x;
        double y;

        while (!stack.isEmpty()) {
            int axis = stack.pop();
            int right = stack.pop();
            int left = stack.pop();

            if (right - left <= nodeSize) {
                for (int i = left; i <= right; i++) {
                    x = coordinates[2 * i];
                    y = coordinates[2 * i + 1];
                    if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
                        result.add(ids[i]);
                    }
                }
                continue;
            }

            int mid = (left + right) / 2;

            x = coordinates[2 * mid];
            y = coordinates[2 * mid + 1];

            if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
                result.add(ids[mid]);
            }

            int nextAxis = (axis + 1) % 2;

            if (axis == 0 ? minX <= x : minY <= y) {
                stack.push(left);
                stack.push(mid - 1);
                stack.push(nextAxis);
            }
            if (axis == 0 ? maxX >= x : maxY >= y) {
                stack.push(mid + 1);
                stack.push(right);
                stack.push(nextAxis);
            }
        }

        return result.stream().mapToInt(Integer::valueOf).toArray();
    }

    /**
     * 클러스터링 영역 Feature Geometry 정보 생성
     */
    private Feature makeClusterFeature(MainCluster cluster, double actualRadius) {

        Point point = new Point(new double[] { pointConverter.convertXToLng(cluster.getX()), pointConverter.convertYToLat(cluster.getY()) });
        return new Feature(point, getClusterProperties(cluster, actualRadius));

    }

    /**
     * 클러스터링 포인트 Feature Geometry 정보 생성
     */
    private Feature makePointFeature(Marker marker) {
        return MarkerMapper.toFeature(marker);
    }

    private Map<String, Object> getClusterProperties(MainCluster cluster, double actualRadius) {
        int count = cluster.getCount();
        Property property = Property.builder()
                                    .radius(actualRadius)
                                    .geoType(GeoType.CLUSTER)
                                    .abbrevCount(extractAbbrev(count))
                                    .count(count)
                                    .build();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(property, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * 클러스터링 영역에 포함된 마커의 수 축약어 생성
     * */
    private String extractAbbrev(int count) {
        String abbrev;
        if (count >= 1000000) {
            abbrev = (count / 1000000) + "M";
        } else if (count >= 10000) {
            abbrev = (count / 1000) + "K";
        } else if (count >= 1000) {
            abbrev = ((count / 100) / 10) + "K";
        } else {
            abbrev = Integer.toString(count);
        }
        return abbrev;
    }

    private double calculateActualRadius(int zoom) {
        return radius / (extent * Math.pow(2, zoom));
    }

}
