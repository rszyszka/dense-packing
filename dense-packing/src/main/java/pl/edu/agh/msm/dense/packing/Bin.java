package pl.edu.agh.msm.dense.packing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Bin {
    private int xSize;
    private int ySize;
    private List<Circle> circles;


    public Bin(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        circles = new ArrayList<>();
    }

    public boolean addCircle(Circle circle) {
        if (Utils.isCircleAbleToBePlacedInBin(circle, this)) {
            circles.add(circle);
            return true;
        }
        return false;
    }

    public int getNumberOfCirclesPacked() {
        return circles.size();
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public List<Circle> getCircles() {
        return Collections.unmodifiableList(circles);
    }
}