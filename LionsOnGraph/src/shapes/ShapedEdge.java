package shapes;

import graph.CoreController;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.shape.Line;
import util.ContextMenuHolder;
import util.Point;

import java.util.Optional;

import static shapes.ShapeConstants.COLOR_EDGE;


public class ShapedEdge {
    private static Group shapeGroup = new Group();

    private CoreController coreController;
    private Line shape;
    private Point from;
    private Point to;

    public ShapedEdge(CoreController coreController, Point startFrom, Point startTo) {

        this.coreController = coreController;
        this.from = startFrom;
        this.to = startTo;

        shape = new Line(from.getX(), from.getY(), to.getX(), to.getY());
        shape.setStroke(COLOR_EDGE);
        shapeGroup.getChildren().add(shape);

        shape.setOnContextMenuRequested(event1 -> {
            event1.consume();

            if (!this.coreController.isEditMode())
                return;

            final ContextMenu contextMenu = ContextMenuHolder.getFreshContextMenu();
            MenuItem item0 = new MenuItem("Remove Edge");
            Menu edgeMenu = new Menu("Edge Weight");

            MenuItem item1 = new MenuItem("Increment");
            MenuItem item2 = new MenuItem("Decrement");
            MenuItem item3 = new MenuItem("Set");

            MenuItem closeItem = new MenuItem("Close");


            item0.setOnAction(event2 -> {
                coreController.removeEdge(from, to);
            });

            item1.setOnAction(event2 -> {
                int weight = coreController.getEdgeByVertices(coreController.getBigVertexByCoordinate(from), coreController.getBigVertexByCoordinate(to)).getEdgeWeight();
                coreController.changeEdgeWeight(from, to, weight + 1);
            });

            item2.setOnAction(event2 -> {
                int weight = coreController.getEdgeByVertices(coreController.getBigVertexByCoordinate(from), coreController.getBigVertexByCoordinate(to)).getEdgeWeight();
                coreController.changeEdgeWeight(from, to, weight - 1);
            });

            item3.setOnAction(event2 -> {
                int weight = coreController.getEdgeByVertices(coreController.getBigVertexByCoordinate(from), coreController.getBigVertexByCoordinate(to)).getEdgeWeight();

                TextInputDialog dialog = new TextInputDialog("" + weight);
                dialog.setTitle("Set Edge Weight");
                dialog.setHeaderText("Enter the new weight of the edge.");

                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    int newWeight = Integer.parseInt(result.get());
                    coreController.changeEdgeWeight(from, to, newWeight);
                }
            });

            edgeMenu.getItems().addAll(item1, item2, item3);
            contextMenu.getItems().addAll(item0, edgeMenu, new SeparatorMenuItem(), closeItem);
            contextMenu.show(shape, event1.getScreenX(), event1.getScreenY());
        });
    }

    public static void setShapeGroup(Group shapeGroup) {
        ShapedEdge.shapeGroup = shapeGroup;
    }

    public void relocate(Point from, Point to) {
        this.from = from;
        this.to = to;
        shape.setStartX(from.getX());
        shape.setStartY(from.getY());
        shape.setEndX(to.getX());
        shape.setEndY(to.getY());
    }

    public void delete() {
        shapeGroup.getChildren().remove(shape);
    }
}
