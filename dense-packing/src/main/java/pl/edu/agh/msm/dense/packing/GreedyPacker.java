package pl.edu.agh.msm.dense.packing;


import java.util.List;

public class GreedyPacker {

    private InitialConfiguration initialConfiguration;
    private Bin bin;
    private CircleGenerator circleGenerator;
    private HolesFinder holesFinder;


    public GreedyPacker(InitialConfiguration initialConfiguration, HolesFinder holesFinder) {
        this.initialConfiguration = initialConfiguration;
        this.bin = initialConfiguration.getBin();
        this.circleGenerator = initialConfiguration.getCircleGenerator();
        this.holesFinder = holesFinder;
    }


    public void createInitialConfiguration() {
        initialConfiguration.init();
    }


    public Bin getBin() {
        return bin;
    }


    public boolean tryToPackNextCircle() {
        Circle circle = circleGenerator.generateNewCircle();
        List<Coords> coordsList = holesFinder.findForCircle(circle);

        if (coordsList.isEmpty()) {
            return tryToGenerateAndPackNewCircleWithDiminishedRadius(circle);
        }

        Coords bestCoords = holesFinder.findCoordsWithMaximumHoleDegree();
        circle.setCoords(bestCoords);
        return bin.addCircle(circle);
    }


    private boolean tryToGenerateAndPackNewCircleWithDiminishedRadius(Circle circle) {
        boolean diminished = circleGenerator.setLowerRadiusIfPossible(circle.getR());
        if (diminished) {
            return tryToPackNextCircle();
        }
        return false;
    }

}