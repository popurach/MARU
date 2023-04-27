/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bird.maru.cluster.geo;

import lombok.Getter;
import lombok.ToString;

/**
 * @author yeozkaya@gmail.com
 */
@Getter
@ToString
public class MainCluster {

    protected double x;
    protected double y;
    protected int zoom;
    protected Integer count;
    protected Long parentId;
    protected Long index;
    protected Long id;


    public void setZoom(int zoom) {
        this.zoom = zoom;
    }
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

}
