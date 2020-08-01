package pl.edu.agh.msm.dense.packing.model;

public class BinSphereMixer2D extends BinSphereMixer {

    public BinSphereMixer2D(Bin bin) {
        super(bin);
        collision = new SphereCollision2D();
    }

    @Override
    protected void updateSpherePosition(Sphere sphere) {
        updateSphereXPosition(sphere);
        updateSphereYPosition(sphere);
    }

    @Override
    protected void resolveCollisionsWithBoundaryPlanes(Sphere sphere) {
        resolveCollisionWithYZPlane(sphere);
        resolveCollisionWithXZPlane(sphere);
    }
}