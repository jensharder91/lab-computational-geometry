package lions_and_men.applet_graph.algorithm.strategies;

import lions_and_men.applet_graph.algorithm.CoreController;
import lions_and_men.applet_graph.algorithm.entities.Entity;
import lions_and_men.applet_graph.algorithm.graph.Connection;
import lions_and_men.applet_graph.algorithm.graph.Vertex;

import java.util.ArrayList;

public class RandomChoice<T extends Entity> extends Strategy<T> {


    public RandomChoice(CoreController coreController) {
        super(coreController);
    }

    @Override
    protected ArrayList<Vertex> calculatePossibleSteps() {
        ArrayList<Vertex> result = new ArrayList<>();
        ArrayList<Connection> connections = entity.getCurrentPosition().getConnections();
        if (connections.size() > 0) {
            int rndInt = lions_and_men.util.Random.getRandomInteger(connections.size());
            result.add(connections.get(rndInt).getNeighbor(entity.getCurrentPosition()));
        }
        return result;
    }
}