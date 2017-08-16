package lions_in_plane.visualization;

import javafx.scene.paint.Color;
import lions_in_plane.core.CoreController;
import util.ConvexHull;
import util.Point;

import java.util.ArrayList;
import java.util.List;


public class VisualizedCoreController extends CoreController {
    private ArrayList<Man> men;
    private List<Lion> lions;
    private ConvexHull hull;
    private ArrayList<ArrayList<Point>> allPaths;
    private int pathCount;

    public VisualizedCoreController() {
        reset();
    }

    private void reset() {
        this.men = new ArrayList<>();
        this.lions = new ArrayList<>();
        this.allPaths = new ArrayList<>();
        this.pathCount = 0;
//        this.hull = new Point[0];
    }


    @Override
    public void setEmptyGraph() {
        reset();
    }

    @Override
    public void setDefaultGraph1() {
        super.setDefaultGraph1();
        calcAllPaths();
    }

    @Override
    public void setDefaultGraph2() {
        super.setDefaultGraph2();
        calcAllPaths();

    }

    @Override
    public void setDefaultGraph3() {
        super.setDefaultGraph3();
        calcAllPaths();
    }

    public void setRandomConfiguration() {
        super.setRandomConfiguration();
        lions.forEach(lion -> System.out.print(lion.getPosition() + ", "));
        System.out.println();
        calcAllPaths();
    }


    @Override
    public void createMan(Point coordinates) {
        super.createMan(coordinates);

        men.add(new Man(coordinates));
//        // TODO: total debug
//        ArrayList<Point> path = new ArrayList<>(Arrays.asList(hull));
//        new PolygonalPath(path, Color.BLACK);
    }

    @Override
    public void removeMan(Point coordinates) {
        super.removeMan(coordinates);

        men.stream().filter(man -> man.getPosition() == coordinates).forEach(Man::clear);
        men.removeIf(man -> man.getPosition() == coordinates);
    }

    @Override
    public void createLion(Point coordinates) {
        super.createLion(coordinates);
        lions.add(new Lion(coordinates));

        if (hull == null || !hull.insideHull(coordinates)) {
            Polygon.clear();
            hull = new ConvexHull(lions);
            new Polygon(hull.getPoints());
        }
    }

    @Override
    public void removeLion(Point coordinates) {
        super.removeLion(coordinates);

        lions.stream().filter(lion -> lion.getPosition() == coordinates).forEach(Lion::clear);
        lions.removeIf(lion -> lion.getPosition() == coordinates);

        if (hull == null || !hull.insideHull(coordinates)) {
            hull = new ConvexHull(lions);
            Polygon.clear();
            new Polygon(hull.getPoints());
        }
    }

    @Override
    public void relocateMan(Point from, Point to) {
        super.relocateMan(from, to);

        men.stream().filter(man -> man.getPosition() == from).forEach(man -> man.setPosition(to));
    }

    @Override
    public void relocateLion(Point from, Point to) {
        super.relocateLion(from, to);

        lions.stream().filter(e -> e.getPosition() == from).forEach(e -> e.setPosition(to));

        hull = new ConvexHull(lions);
        Polygon.clear();
        new Polygon(hull.getPoints());

    }

    @Override
    public void setLionRange(Point coordinates, double range) {
        // TODO: IMPLEMENT THIS
    }


    @Override
    public boolean simulateStep() {
        boolean res = super.simulateStep();

        if (pathCount >= allPaths.size()) {
            return res;
        }

        if (pathCount == 0) {
            new PolygonalPath(allPaths.get(pathCount), Color.BLUE);
            pathCount++;
        }

        new PolygonalPath(allPaths.get(pathCount), Color.RED);
        pathCount++;


        return res;
    }


    @Override
    protected ArrayList<ArrayList<Point>> calcAllPaths() {
        allPaths = super.calcAllPaths();
        pathCount = 0;

        Point[] newHull = new Point[allPaths.size() - 1];

        //draw lion paths (position >= 1 in list)
        if (allPaths.size() > 1) {
            for (int i = 1; i < allPaths.size(); i++) {
                newHull[i - 1] = allPaths.get(i).get(allPaths.get(i).size() - 1);
            }
        }

        hull = new ConvexHull(newHull);
        Polygon.clear();
        new Polygon(hull.getPoints());

        // draw man path (position == = in list)
        if (allPaths.size() > 0) {
            // new PolygonalPath(allPaths.get(0), Color.BLUE);
        }

        return allPaths;
    }
}
