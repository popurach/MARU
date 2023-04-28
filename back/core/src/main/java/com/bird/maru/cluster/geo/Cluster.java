/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bird.maru.cluster.geo;

import java.util.Properties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yeozkaya@gmail.com
 */
@Getter
@Setter
public class Cluster extends MainCluster {

    private Properties properties;

    public Cluster(double x, double y, long id, int count) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.count = count;
        this.zoom = 24; // Init zoom
        this.parentId = -1L;
    }

}
