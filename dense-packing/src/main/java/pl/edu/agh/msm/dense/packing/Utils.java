package pl.edu.agh.msm.dense.packing;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


public class Utils {

    public static boolean isCircleAbleToBePlacedInBin(Sphere sphere, Bin bin) {
        if (!isSphereNotOverlappingBin(sphere, bin)) {
            return false;
        }
        for (Sphere s : bin.getSpheres()) {
            if (!Utils.areSpheresNotOverlapping(sphere, s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean areSpheresNotOverlapping(Sphere s1, Sphere s2) {
        return computeDistanceBetweenCircuits(s1, s2) >= 0.0;
    }


    public static boolean isSphereNotOverlappingBin(Sphere c1, Bin bin) {
        boolean notOverlappingWidth = isNotOverlappingSize(c1.getCoords().getX(), c1.getR(), bin.getXSize());
        boolean notOverlappingHeight = isNotOverlappingSize(c1.getCoords().getY(), c1.getR(), bin.getYSize());
        boolean notOverlappingDepth = bin.getZSize() == 1 || isNotOverlappingSize(c1.getCoords().getZ(), c1.getR(), bin.getZSize());


        return notOverlappingWidth && notOverlappingHeight && notOverlappingDepth;
    }


    public static double computeDistanceBetweenCircuits(Sphere c1, Sphere c2) {
        return computeDistanceBetweenMiddles(c1, c2) - c1.getR() - c2.getR();
    }


    public static double computeDistanceBetweenMiddles(Sphere c1, Sphere c2) {
        double distance = sqrt(computeSquaredDistance(c1.getCoords(), c2.getCoords()));
        return BigDecimal.valueOf(distance).setScale(10, RoundingMode.HALF_UP).doubleValue();
    }


    public static double computeSquaredDistance(Coords c1, Coords c2) {
        return pow(c1.getX() - c2.getX(), 2) + pow(c1.getY() - c2.getY(), 2) + pow(c1.getZ() - c2.getZ(), 2);
    }


    private static boolean isNotOverlappingSize(double coordsPart, int r, int size) {
        coordsPart = BigDecimal.valueOf(coordsPart).setScale(10, RoundingMode.HALF_UP).doubleValue();
        return coordsPart - r >= 0 && size - coordsPart - r >= 0.0;
    }

}
