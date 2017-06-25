package shapes;

import graph.BigVertex;
import graph.GraphController;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.shape.Circle;
import util.Point;

import static shapes.ShapeConstants.BIG_VERTEX_RADIUS;
import static shapes.ShapeConstants.COLOR_NODE;

public class ShapedBigVertex {
    private static Group mainGroup;
    private static Group shapeGroup;

    private Circle shape;
    private BigVertex vertex;
    private GraphController graphController;


    public ShapedBigVertex(GraphController graphController, BigVertex vertex) {
        this.vertex = vertex;
        this.graphController = graphController;

        shape = new Circle(vertex.getCoordinates().getX(), vertex.getCoordinates().getY(), BIG_VERTEX_RADIUS, COLOR_NODE);
        shapeGroup.getChildren().add(shape);

        shape.setOnContextMenuRequested(event1 -> {
            final ContextMenu contextMenu = new ContextMenu();
            MenuItem item0 = new MenuItem("Create Edge");
            MenuItem item1 = new MenuItem("Remove Edge");
            MenuItem item2 = new MenuItem("Relocate Node");
            MenuItem item5 = new MenuItem("Remove Node");
            MenuItem item3 = new MenuItem("Add Man");
            MenuItem item4 = new MenuItem("Add Lion");
            MenuItem closeItem = new MenuItem("Close");

            item0.setOnAction(event2 -> {
                mainGroup.setOnMouseClicked(event3 -> {

                    mainGroup.setOnMouseClicked(null);

                    graphController.createEdge(vertex, graphController.getBigVertexByCoordinate(new Point((int) event3.getX(), (int) event3.getY())));

                });
            });

            item1.setOnAction(event2 -> {
                mainGroup.setOnMouseClicked(event3 -> {

                    mainGroup.setOnMouseClicked(null);

                    graphController.removeEdge(vertex, graphController.getBigVertexByCoordinate(new Point((int) event3.getX(), (int) event3.getY())));

                });
            });

            item2.setOnAction(event2 -> {
                mainGroup.setOnMouseClicked(event3 -> {

                    mainGroup.setOnMouseClicked(null);

                    graphController.relocateVertex(vertex, new Point((int) event3.getX(), (int) event3.getY()));

                });
            });

            item5.setOnAction(event2 -> {
                graphController.deleteVertex(vertex);
//                shapeGroup.getChildren().remove(shape);
            });

            item3.setOnAction(event2 -> {
                System.out.println("Add Man");
                // TODO: something like new Man(vertex.getCoordinates());
            });

            item4.setOnAction(event2 -> {
                System.out.println("Add Lion");
                // TODO: something like new Lion(vertex.getCoordinates());
            });

            contextMenu.getItems().addAll(item0, item1, new SeparatorMenuItem(), item2, item5, new SeparatorMenuItem(), item3, item4, new SeparatorMenuItem(), closeItem);
            contextMenu.show(shape, event1.getScreenX(), event1.getScreenY());
        });

    }

    public void relocate() {
        shape.relocate(vertex.getCoordinates().getX()-BIG_VERTEX_RADIUS, vertex.getCoordinates().getY()-BIG_VERTEX_RADIUS);
    }

    public void delete() {
        shapeGroup.getChildren().remove(shape);
    }


    public static void setMainGroup(Group mainGroup) {
        ShapedBigVertex.mainGroup = mainGroup;
    }

    public static void setShapeGroup(Group shapeGroup) {
        ShapedBigVertex.shapeGroup = shapeGroup;
    }
}