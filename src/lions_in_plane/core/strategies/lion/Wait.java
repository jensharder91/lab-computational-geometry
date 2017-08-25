package lions_in_plane.core.strategies.lion;

import lions_in_plane.core.plane.Lion;
import lions_in_plane.core.plane.Man;
import util.Point;

import java.util.ArrayList;

public class Wait extends Strategy {

    public Wait(StrategyEnumLion strategyEnumLion) {
        super(strategyEnumLion);
    }

    @Override
    public ArrayList<Point> getPath(Lion lion, Man man, ArrayList<Point> manPath, ArrayList<Point> prevPath) {
        ArrayList<Point> path = new ArrayList<>();
        for(int i = 0; i < 200; i++){
            path.add(lion.getPosition());
        }
        return path;
    }
}