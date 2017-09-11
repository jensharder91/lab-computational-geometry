package lions_and_men.applet_one_graph.visualization;

import lions_and_men.applet_one_graph.core.CoreController;
import lions_and_men.applet_one_graph.core.graph.Connection;
import lions_and_men.util.Point;

import java.util.ArrayList;

import static lions_and_men.applet_one_graph.visualization.Constants.*;

public class VisualCoreController extends CoreController {

    private ArrayList<Vertex> verticesBig = new ArrayList<>();
    private ArrayList<Vertex> verticesSmall = new ArrayList<>();
    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Range> rangesMan = new ArrayList<>();
    private ArrayList<Range> rangesLion = new ArrayList<>();
    private ArrayList<Vertex> stepPreviews = new ArrayList<>();

    public VisualCoreController() {
        super();
    }

    public void cleanUp() {
        verticesBig.forEach(Vertex::delete);
        verticesBig.clear();

        verticesSmall.forEach(Vertex::delete);
        verticesSmall.clear();

        entities.forEach(Entity::delete);
        entities.clear();

        edges.forEach(Edge::delete);
        edges.clear();

        rangesMan.forEach(Range::delete);
        rangesMan.clear();

        rangesLion.forEach(Range::delete);
        rangesLion.clear();

        stepPreviews.forEach(Vertex::delete);
        stepPreviews.clear();
    }

    /* ****************************
     *
     *   Graph Shapes
     *
     * ****************************/

    public void relocateVertex(Point vertexCoordinates, Point newCoordinate) {

        super.relocateVertex(vertexCoordinates, newCoordinate);

        verticesBig.stream().filter(vertex -> vertex.getPosition().equals(vertexCoordinates)).forEach(vertex -> vertex.relocate(newCoordinate));

        edges.stream().filter(edge -> edge.getPositionFrom().equals(vertexCoordinates)).forEach(edge -> edge.relocate(newCoordinate, edge.getPositionTo()));
        edges.stream().filter(edge -> edge.getPositionTo().equals(vertexCoordinates)).forEach(edge -> edge.relocate(edge.getPositionFrom(), newCoordinate));

        verticesSmall.forEach(Vertex::delete);
        graph.getSmallVertices().forEach(smallVertex -> verticesSmall.add(new SmallVertex(this, smallVertex.getCoordinates())));

        entities.forEach(Entity::delete);
        entities.clear();
        getMen().forEach(man -> entities.add(new Man(this, man.getCoordinates())));
        getLions().forEach(lion -> entities.add(new Lion(this, lion.getCoordinates())));

        entities.stream().filter(entity -> entity.getPosition().equals(vertexCoordinates)).forEach(entity -> entity.relocate(newCoordinate));
    }

    public void createVertex(Point coordinate) {
        super.createVertex(coordinate);

        verticesBig.add(new BigVertex(this, coordinate));
    }

    public void deleteVertex(Point vertexCoordinates) {


        super.deleteVertex(vertexCoordinates);

        verticesBig.stream().filter(vertex -> vertex.getPosition().equals(vertexCoordinates)).forEach(Vertex::delete);
        verticesBig.removeIf(vertex -> vertex.getPosition().equals(vertexCoordinates));

        edges.stream().filter(edge -> (edge.getPositionTo().equals(vertexCoordinates)
                || edge.getPositionFrom().equals(vertexCoordinates))).forEach(Edge::delete);
        edges.removeIf(edge -> (edge.getPositionTo().equals(vertexCoordinates)
                || edge.getPositionFrom().equals(vertexCoordinates)));


        verticesSmall.forEach(Vertex::delete);
        graph.getSmallVertices().forEach(smallVertex -> verticesSmall.add(new SmallVertex(this, smallVertex.getCoordinates())));


        updateManRanges();
        updateLionRanges();
    }

    public void createEdge(Point vertex1Coordinates, Point vertex2Coordinates) {
        createEdge(vertex1Coordinates, vertex2Coordinates, getDefaultEdgeWeight());
    }

    public void createEdge(Point vertex1Coordinates, Point vertex2Coordinates, int weight) {

        super.createEdge(vertex1Coordinates, vertex2Coordinates, weight);

        lions_and_men.applet_one_graph.core.graph.Edge edge = getEdgeByPoints(vertex1Coordinates, vertex2Coordinates);
        if (edge == null) {
            return;
        }
        edges.add(new Edge(this, edge.getStartCoordinates(), edge.getEndCoordinates()));
        edge.getEdgeVertices().forEach(smallVertex -> verticesSmall.add(new SmallVertex(this, smallVertex.getCoordinates())));


        updateManRanges();
        updateLionRanges();
    }

    public void removeEdge(Point vertex1Coordinates, Point vertex2Coordinates) {

        super.removeEdge(vertex1Coordinates, vertex2Coordinates);

        edges.stream().filter
                (edge ->
                        ((edge.getPositionTo().equals(vertex1Coordinates) && edge.getPositionFrom().equals(vertex2Coordinates))
                                || (edge.getPositionTo().equals(vertex2Coordinates) && edge.getPositionFrom().equals(vertex1Coordinates))))
                .forEach(Edge::delete);

        edges.removeIf(edge ->
                ((edge.getPositionTo().equals(vertex1Coordinates) && edge.getPositionFrom().equals(vertex2Coordinates))
                        || (edge.getPositionTo().equals(vertex2Coordinates) && edge.getPositionFrom().equals(vertex1Coordinates))));

        for (lions_and_men.applet_one_graph.core.graph.SmallVertex smallVertex : getEdgeByPoints(vertex1Coordinates, vertex2Coordinates).getEdgeVertices()) {
            verticesSmall.stream().filter(vertex -> vertex.equals(smallVertex)).forEach(Vertex::delete);
            verticesSmall.removeIf(vertex -> vertex.equals(smallVertex));
        }


        updateManRanges();
        updateLionRanges();
    }

    @Override
    public void changeEdgeWeight(Point vertex1Coordinates, Point vertex2Coordinates, int weight) {

        super.changeEdgeWeight(vertex1Coordinates, vertex2Coordinates, weight);

        verticesSmall.forEach(Vertex::delete);
        graph.getSmallVertices().forEach(smallVertex -> verticesSmall.add(new SmallVertex(this, smallVertex.getCoordinates())));

        entities.forEach(Entity::delete);
        entities.clear();
        getMen().forEach(man -> entities.add(new Man(this, man.getCoordinates())));
        getLions().forEach(lion -> entities.add(new Lion(this, lion.getCoordinates())));

        updateManRanges();
        updateLionRanges();
    }

    /* ****************************
     *
     *   ENTITY Shapes
     *
     * ****************************/

    public void setMan(Point vertexCoorinate) {

        super.setMan(vertexCoorinate);

        entities.add(new Man(this, vertexCoorinate));
        updateManRanges();

        updateStepPreviewsAndChoicePoints();
    }

    public void setLion(Point vertexCoorinate) {

        super.setLion(vertexCoorinate);

        entities.add(new Lion(this, vertexCoorinate));
        updateLionRanges();

        updateStepPreviewsAndChoicePoints();
    }

    public void relocateMan(Point manCoordinate, Point vertexCoordinate) {

        super.relocateMan(manCoordinate, vertexCoordinate);

        entities.stream().filter(entity -> entity.getPosition().equals(manCoordinate)).forEach(entity -> entity.relocate(vertexCoordinate));
        updateManRanges();


        updateStepPreviewsAndChoicePoints();
    }

    public void relocateLion(Point lionCoordinate, Point vertexCoordinate) {

        super.relocateLion(lionCoordinate, vertexCoordinate);

        entities.stream().filter(entity -> entity.getPosition().equals(lionCoordinate)).forEach(entity -> entity.relocate(vertexCoordinate));
        updateLionRanges();
        updateStepPreviewsAndChoicePoints();
    }

    public void removeMan(Point manCoordinate) {

        entities.stream().filter(entity -> entity.getPosition().equals(manCoordinate)).forEach(Entity::delete);
        entities.removeIf(entity -> entity.getPosition().equals(manCoordinate));

        super.removeMan(manCoordinate);
        updateStepPreviewsAndChoicePoints();
    }

    public void removeLion(Point lionCoordinate) {

        entities.stream().filter(entity -> entity.getPosition().equals(lionCoordinate)).forEach(Entity::delete);
        entities.removeIf(entity -> entity.getPosition().equals(lionCoordinate));

        super.removeLion(lionCoordinate);
        updateStepPreviewsAndChoicePoints();
    }

    @Override
    public void setManStrategy(Point manCoordinate, ManStrategy strategy) {
        super.setManStrategy(manCoordinate, strategy);

        updateStepPreviewsAndChoicePoints();
    }

    @Override
    public void setLionStrategy(Point lionCoordinate, LionStrategy strategy) {
        super.setLionStrategy(lionCoordinate, strategy);

        updateStepPreviewsAndChoicePoints();
    }

    @Override
    public void setAllManStrategy(ManStrategy strategy) {
        super.setAllManStrategy(strategy);

        updateStepPreviewsAndChoicePoints();
    }

    @Override
    public void setAllLionStrategy(LionStrategy strategy) {
        super.setAllLionStrategy(strategy);

        updateStepPreviewsAndChoicePoints();
    }

    @Override
    public void setManRange(Point manCoordinate, int range) {
        super.setManRange(manCoordinate, range);

        updateManRanges();
    }

    private void updateManRanges() {

        rangesMan.forEach(Range::delete);
        rangesMan.clear();

        getMen().forEach(man -> man.getRangeVertices().forEach(vertex -> rangesMan.add(new Range(this, vertex.getCoordinates(), true))));
    }

    @Override
    public void setLionRange(Point lionCoordinate, int range) {
        super.setLionRange(lionCoordinate, range);

        updateLionRanges();
    }

    private void updateLionRanges() {

        rangesLion.forEach(Range::delete);
        rangesLion.clear();

        getLions().forEach(lion -> lion.getRangeVertices().forEach(vertex -> rangesLion.add(new Range(this, vertex.getCoordinates()))));
    }


    void updateStepPreviewsAndChoicePoints() {

        //TODO flush and create new....
        // step previews
        stepPreviews.forEach(Vertex::delete);
        stepPreviews.clear();

        getMen().forEach(man -> stepPreviews.add(new StepPreview(this, man.getNextPosition().getCoordinates())));
        getLions().forEach(lion -> stepPreviews.add(new StepPreview(this, lion.getNextPosition().getCoordinates())));


        // choice points
        ChoicePoint.clear();
        for (lions_and_men.applet_one_graph.core.entities.Man man : getMenWithManualInput()) {
            new ChoicePoint(this, man, man.getCoordinates(), COLOR_MAN);
            for (Connection con : man.getCurrentPosition().getConnections()) {
                lions_and_men.applet_one_graph.core.graph.Vertex choicePoint = con.getNeighbor(man.getCurrentPosition());
                new ChoicePoint(this, man, choicePoint.getCoordinates(), COLOR_CHOICEPOINT);
            }
        }

        for (lions_and_men.applet_one_graph.core.entities.Lion lion : getLionsWithManualInput()) {
            new ChoicePoint(this, lion, lion.getCoordinates(), COLOR_LION);
            for (Connection con : lion.getCurrentPosition().getConnections()) {
                lions_and_men.applet_one_graph.core.graph.Vertex choicePoint = con.getNeighbor(lion.getCurrentPosition());
                new ChoicePoint(this, lion, choicePoint.getCoordinates(), COLOR_CHOICEPOINT);
            }
        }
    }
}