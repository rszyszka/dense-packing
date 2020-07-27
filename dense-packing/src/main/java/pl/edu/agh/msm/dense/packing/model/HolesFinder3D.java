package pl.edu.agh.msm.dense.packing.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static pl.edu.agh.msm.dense.packing.model.Coords.coords;
import static pl.edu.agh.msm.dense.packing.model.Utils.*;


public class HolesFinder3D extends HolesFinder {

    private BoundaryHoleFinderHelper3D helper;

    public HolesFinder3D(Bin bin) {
        super(bin);
    }


    @Override
    protected void determineCornerHolesIfExist() {
        Plane yz1 = bin.getPlanes().get(0);
        Plane yz2 = bin.getPlanes().get(1);
        Plane xz1 = bin.getPlanes().get(2);
        Plane xz2 = bin.getPlanes().get(3);
        Plane xy1 = bin.getPlanes().get(4);
        Plane xy2 = bin.getPlanes().get(5);
        int x1 = sphere.getR();
        int y1 = sphere.getR();
        int z1 = sphere.getR();
        int x2 = bin.getXSize() - x1;
        int y2 = bin.getYSize() - y1;
        int z2 = bin.getYSize() - z1;

        List<Hole> possibleHoles = new ArrayList<>();
        possibleHoles.add(new Hole(Arrays.asList(yz1, xz1, xy1), coords(x1, y1, z1), sphere.getR()));
        possibleHoles.add(new Hole(Arrays.asList(yz1, xz1, xy2), coords(x1, y1, z2), sphere.getR()));
        possibleHoles.add(new Hole(Arrays.asList(yz1, xz2, xy1), coords(x1, y2, z1), sphere.getR()));
        possibleHoles.add(new Hole(Arrays.asList(yz1, xz2, xy2), coords(x1, y2, z2), sphere.getR()));
        possibleHoles.add(new Hole(Arrays.asList(yz2, xz1, xy1), coords(x2, y1, z1), sphere.getR()));
        possibleHoles.add(new Hole(Arrays.asList(yz2, xz1, xy2), coords(x2, y1, z2), sphere.getR()));
        possibleHoles.add(new Hole(Arrays.asList(yz2, xz2, xy1), coords(x2, y2, z1), sphere.getR()));
        possibleHoles.add(new Hole(Arrays.asList(yz2, xz2, xy2), coords(x2, y2, z2), sphere.getR()));

        addNotOverlappingHolesToSolutionHolesList(possibleHoles);
    }


    @Override
    protected void determineBoundaryHolesIfExists() {
        helper = new BoundaryHoleFinderHelper3D(sphere);
        determineTwoPlanesAndSphereBoundaryHoles();
        determinePlaneAndTwoSpheresBoundaryHoles();
    }


    private void determineTwoPlanesAndSphereBoundaryHoles() {
        int numberOfPlanes = bin.getPlanes().size();
        for (int i = 0; i < numberOfPlanes - 1; i++) {
            Plane p1 = bin.getPlanes().get(i);
            for (int j = i + 1; j < numberOfPlanes; j++) {
                Plane p2 = bin.getPlanes().get(j);
                for (Sphere s : bin.getSpheres()) {
                    determineNextBoundaryHole(s, p1, p2);
                }
            }
        }
    }


    private void determineNextBoundaryHole(Sphere s, Plane p1, Plane p2) {
        if (possibleBoundaryCoordsExist(p1, s) && possibleBoundaryCoordsExist(p2, s)) {
            List<Hole> possibleHoles = helper.determineAllPossibleHoles(s, p1, p2);
            addNotOverlappingHolesToSolutionHolesList(possibleHoles);
        }
    }


    private void determinePlaneAndTwoSpheresBoundaryHoles() {
        int numberOfSpheresInBin = bin.getSpheres().size();
        for (Plane p : bin.getPlanes()) {
            for (int i = 0; i < numberOfSpheresInBin - 1; i++) {
                Sphere s1 = bin.getSpheres().get(i);
                for (int j = i + 1; j < numberOfSpheresInBin; j++) {
                    Sphere s2 = bin.getSpheres().get(j);
                    determineNextBoundaryHole(p, s1, s2);
                }
            }
        }
    }


    private void determineNextBoundaryHole(Plane p, Sphere s1, Sphere s2) {
        if (possibleCoordsFromSpheresExist(s1, s2) && possibleBoundaryCoordsExist(p, s1) && possibleBoundaryCoordsExist(p, s2)) {
            List<Hole> possibleHoles = helper.determineAllPossibleHoles(p, s1, s2);
            addNotOverlappingHolesToSolutionHolesList(possibleHoles);
        }
    }


    @Override
    protected void determineHolesFromSpheresIfExist() {
        int numberOfSpheresInBin = bin.getSpheres().size();
        for (int i = 0; i < numberOfSpheresInBin - 2; i++) {
            Sphere s1 = bin.getSpheres().get(i);
            for (int j = i + 1; j < numberOfSpheresInBin - 1; j++) {
                Sphere s2 = bin.getSpheres().get(j);
                for (int k = j + 1; k < numberOfSpheresInBin; k++) {
                    Sphere s3 = bin.getSpheres().get(k);
                    determineNextHoleIfExist(s1, s2, s3);
                }
            }
        }
    }


    private void determineNextHoleIfExist(Sphere s1, Sphere s2, Sphere s3) {
        if (possibleCoordsFromSpheresExist(s1, s2) && possibleCoordsFromSpheresExist(s2, s3) && possibleCoordsFromSpheresExist(s1, s3)) {
            List<Hole> possibleHoles = determineAllPossibleHoles(s1, s2, s3);
            addNotOverlappingHolesToSolutionHolesList(possibleHoles);
        }
    }


    private List<Hole> determineAllPossibleHoles(Sphere s1, Sphere s2, Sphere s3) {
        int r1 = s1.getR() + sphere.getR();
        int r2 = s2.getR() + sphere.getR();
        int r3 = s3.getR() + sphere.getR();

        Coords temp1 = vectorsSubtraction(s2.getCoords(), s1.getCoords());
        double d = norm(temp1);
        Coords e_x = coords(temp1.getX() / d, temp1.getY() / d, temp1.getZ() / d);

        Coords temp2 = vectorsSubtraction(s3.getCoords(), s1.getCoords());
        double i = dot(e_x, temp2);
        Coords temp3 = vectorsSubtraction(temp2, coords(i * e_x.getX(), i * e_x.getY(), i * e_x.getZ()));
        Coords e_y = coords(temp3.getX() / norm(temp3), temp3.getY() / norm(temp3), temp3.getZ() / norm(temp3));
        Coords e_z = cross(e_x, e_y);
        double j = dot(e_y, temp2);

        double x = (pow(r1, 2) - pow(r2, 2) + pow(d, 2)) / (2 * d);
        double y = (pow(r1, 2) - pow(r3, 2) + pow(i, 2) + pow(j, 2) - 2 * i * x) / (2 * j);
        double z = sqrt(pow(r1, 2) - pow(x, 2) - pow(y, 2));

        double x1 = s1.getCoords().getX() + x * e_x.getX() + y * e_y.getX() + z * e_z.getX();
        double y1 = s1.getCoords().getY() + x * e_x.getY() + y * e_y.getY() + z * e_z.getY();
        double z1 = s1.getCoords().getZ() + x * e_x.getZ() + y * e_y.getZ() + z * e_z.getZ();

        double x2 = s1.getCoords().getX() + x * e_x.getX() + y * e_y.getX() - z * e_z.getX();
        double y2 = s1.getCoords().getY() + x * e_x.getY() + y * e_y.getY() - z * e_z.getY();
        double z2 = s1.getCoords().getZ() + x * e_x.getZ() + y * e_y.getZ() - z * e_z.getZ();

        List<Hole> possibleHoles = new ArrayList<>();
        possibleHoles.add(new Hole(Arrays.asList(s1, s2, s3), coords(x1, y1, z1), sphere.getR()));
        possibleHoles.add(new Hole(Arrays.asList(s1, s2, s3), coords(x2, y2, z2), sphere.getR()));

        return possibleHoles;
    }

}
