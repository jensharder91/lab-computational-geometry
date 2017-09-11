package lions_and_men.applet_one_graph.core.strategies;

import lions_and_men.applet_one_graph.core.CoreController;
import lions_and_men.applet_one_graph.core.entities.Lion;
import lions_and_men.applet_one_graph.core.graph.Connection;
import lions_and_men.applet_one_graph.core.graph.GraphHelper;
import lions_and_men.applet_one_graph.core.graph.Vertex;

import java.util.ArrayList;

/**
 * Created by Jens on 01.07.2017.
 */

public abstract class StrategyLion implements Strategy, Cloneable {


    protected CoreController coreController;
    protected Lion lion;
    protected GraphHelper helper;
    protected CoreController.LionStrategy strategyEnum;

    public StrategyLion(CoreController coreController, CoreController.LionStrategy strategyEnum) {
        this.coreController = coreController;
        this.helper = GraphHelper.createGraphHelper(coreController);
        this.strategyEnum = strategyEnum;
    }


    @Override
    public Vertex getNextPosition() {
        //we can implement here more conditions for the returned vertex

        for (Vertex vertex : calculatePossibleSteps()) {
            if (vertexIsValidStep(vertex)) {
                return vertex;
            }
        }

        //fallback
        return lion.getCurrentPosition();
    }

    public boolean vertexIsValidStep(Vertex vertex) {
        if (this.coreController.isLionOnVertex(vertex.getCoordinates())) {
            return false;
        }
        if (lion.getCurrentPosition().equals(vertex)) {
            return true;
        }
        for (Connection neighborConnection : lion.getCurrentPosition().getConnections())
            if (neighborConnection.getNeighbor(lion.getCurrentPosition()).equals(vertex)) {
                return true;
            }

        return false;//TODO
    }

    protected abstract ArrayList<Vertex> calculatePossibleSteps();

    public void setLion(Lion lion) {
        this.lion = lion;
    }

    public String getName() {
        return strategyEnum.name();
    }

}