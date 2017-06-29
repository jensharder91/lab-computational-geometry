package shapes;

import graph.CoreController;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import util.Point;

import static shapes.ShapeConstants.COLOR_EDGE;


public class ShapedEdge {
    private static Group shapeGroup = new Group();

    private CoreController coreController;
    private Line shape;

    public ShapedEdge(CoreController coreController, Point from, Point to) {

        this.coreController = coreController;

        shape = new Line(from.getX(), from.getY(), to.getX(), to.getY());
        shape.setStroke(COLOR_EDGE);
        shapeGroup.getChildren().add(shape);
    }

    public static void setShapeGroup(Group shapeGroup) {
        ShapedEdge.shapeGroup = shapeGroup;
    }

    public void relocate(Point from, Point to) {
        shape.setStartX(from.getX());
        shape.setStartY(from.getY());
        shape.setEndX(to.getX());
        shape.setEndY(to.getY());
    }

    public void delete() {
        shapeGroup.getChildren().remove(shape);
    }
}
