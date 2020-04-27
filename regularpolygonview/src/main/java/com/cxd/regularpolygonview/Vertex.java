package com.cxd.regularpolygonview;

import android.graphics.Point;

public class Vertex {
    private int order ; //次序
    private Point vertex ; //顶点
//    private Point center ; //圆角的圆心
    private Point vertexL ; //顶点左
    private Point vertexR ; //顶点右

    public Vertex(int order, Point vertex, Point vertexL, Point vertexR) {
        this.order = order;
        this.vertex = vertex;
        this.vertexL = vertexL;
        this.vertexR = vertexR;
    }

    public Point getVertex() {
        return vertex;
    }

    public void setVertex(Point vertex) {
        this.vertex = vertex;
    }

    public Point getVertexL() {
        return vertexL;
    }

    public void setVertexL(Point vertexL) {
        this.vertexL = vertexL;
    }

    public Point getVertexR() {
        return vertexR;
    }

    public void setVertexR(Point vertexR) {
        this.vertexR = vertexR;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
