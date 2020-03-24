package pl.edu.agh.msm.dense.packing;

import pl.edu.agh.msm.core.Simulation;
import pl.edu.agh.msm.core.Space;


public class GreedyPackingSimulation extends Simulation {

    private Bin bin;
    private GreedyPacker packer;
    private int numberOfFilledCells;


    public GreedyPackingSimulation(Space space, GreedyPacker packer) {
        super(space);
        bin = packer.getBin();
        this.packer = packer;
        this.packer.createInitialConfiguration();
        numberOfFilledCells = 0;
    }


    @Override
    protected boolean performStep() {
        boolean packed = packer.tryToPackNextCircle();
        if (!packed) {
            SpaceFiller filler = new SpaceFiller(space);
            filler.fillWithAllCircles(bin);
            numberOfFilledCells = filler.getNumberOfFilledCells();
        }
        return packed;
    }


    public double computeVoxelDensityLevel() {
        return numberOfFilledCells / (double) (space.getXSize() * space.getYSize());
    }


    public double computeMathDensityLevel() {
        return bin.getCircles().stream()
                .mapToDouble(circle -> Math.PI * circle.getR() * circle.getR())
                .sum() / (double) (space.getXSize() * space.getYSize());
    }

}
