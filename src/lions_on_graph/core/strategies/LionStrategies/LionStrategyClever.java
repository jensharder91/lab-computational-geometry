package lions_on_graph.core.strategies.LionStrategies;

import lions_on_graph.core.CoreController;
import lions_on_graph.core.entities.Lion;
import lions_on_graph.core.entities.Man;
import lions_on_graph.core.graph.Vertex;
import lions_on_graph.core.strategies.StrategyLion;

import java.util.ArrayList;

/**
 * Created by Jens on 18.07.2017.
 */
public class LionStrategyClever extends StrategyLion {

    public LionStrategyClever(CoreController coreController, CoreController.LionStrategy strategyEnum) {
        super(coreController, strategyEnum);
    }

    @Override
    protected ArrayList<Vertex> calculatePossibleSteps() {
        System.out.println("### "+lion);
        Vertex currentPosition = lion.getCurrentPosition();
        int steps = Integer.MAX_VALUE;
        int stepsToBigVertex = Integer.MAX_VALUE;
        ArrayList<Vertex> result = new ArrayList<>();
        for (Vertex possibleTarget : helper.getNeighborBigVertices(currentPosition)) {
            int calculatedSteps = helper.BFSToMen(possibleTarget) + helper.getDistanceBetween(currentPosition, possibleTarget);
            if (calculatedSteps < steps) {
                boolean checkLions = true;
                for(Lion otherLion : coreController.getLions()){
                    //other lion
                    if(! lion.equals(otherLion)){
                        if(helper.getDistanceBetween(possibleTarget, otherLion.getCurrentPosition()) <  helper.getDistanceBetween(currentPosition, possibleTarget)){
                            checkLions = false;
                        } else if(helper.getDistanceBetween(possibleTarget, otherLion.getCurrentPosition()) ==  helper.getDistanceBetween(currentPosition, possibleTarget)){
                            Vertex calculatedPosition = otherLion.getCalculatedPosition();
                            if(calculatedPosition != null && helper.getDistanceBetween(possibleTarget, calculatedPosition) < helper.getDistanceBetween(calculatedPosition, otherLion.getCurrentPosition())){
                                checkLions = false;
                            }
                        }
                    }

                }
                if(checkLions){
                    result.add(0, helper.getPathBetween(currentPosition, possibleTarget).get(0));
                    steps = calculatedSteps;
                    stepsToBigVertex = helper.getDistanceBetween(currentPosition, possibleTarget);
                }
            }
        }

        for(Man man : coreController.getMen()){
            if(helper.getDistanceBetween(currentPosition, man.getCurrentPosition()) < stepsToBigVertex){
                result.add(0, helper.getPathBetween(currentPosition, man.getCurrentPosition()).get(0));
            }
        }


        return result;
    }
}