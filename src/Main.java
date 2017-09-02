import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage stageOne;
    private Stage stageTwo;
    private boolean stageIsOne = false;

    @Override
    public void start(Stage stageMain) throws Exception {
        // stage one
        stageOne = new Stage();
        BorderPane rootOne = new BorderPane();
        Scene sceneOne = new Scene(rootOne, 1600, 900, Color.WHITE);
        rootOne.prefWidthProperty().bind(sceneOne.widthProperty());
        rootOne.prefHeightProperty().bind(sceneOne.heightProperty());
        rootOne.setMaxWidth(Region.USE_PREF_SIZE);
        rootOne.setMaxHeight(Region.USE_PREF_SIZE);
        stageOne.setScene(sceneOne);
        stageOne.setTitle("Lions On Graph");
        stageOne.setWidth(1600);
        stageOne.setHeight(900);
        stageOne.setResizable(true);
        new applet_one.Controller(stageOne, rootOne);

        // stage two
        stageTwo = new Stage();
        BorderPane rootTwo = new BorderPane();
        Scene sceneTwo = new Scene(rootTwo, 1600, 900, Color.WHITE);
        rootTwo.prefWidthProperty().bind(sceneTwo.widthProperty());
        rootTwo.prefHeightProperty().bind(sceneTwo.heightProperty());
        rootTwo.setMaxWidth(Region.USE_PREF_SIZE);
        rootTwo.setMaxHeight(Region.USE_PREF_SIZE);
        stageTwo.setScene(sceneTwo);
        stageTwo.setTitle("Lions In Plane");
        stageTwo.setWidth(1600);
        stageTwo.setHeight(900);
        stageTwo.setResizable(true);
        new applet_two.Controller(stageTwo, rootTwo);

//        stageOne.show();
        stageTwo.show();
        switchStage();
    }

    public void switchStage() {
        if (stageIsOne) {
            stageOne.hide();
            stageTwo.show();
        } else {
            stageTwo.hide();
            stageOne.show();
        }
    }
}
