package map.shortestpath;

import exception.NoPathFoundExeception;
import map.MapFacade;
import map.heuristic.Heuristic;
import map.movingrule.MovingRule;
import util.Vector;
import util.Tuple2;
import util.Tuple3;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by paloka on 06.06.16.
 */
abstract class ShortestPath implements ExploreStrategy {

    private ShortestPathPreprocessing   preprocessing   = null;
    private ShortestPathPruning         pruning         = null;

    protected ShortestPath(ShortestPathPreprocessing preprocessing, ShortestPathPruning pruning){
        this.preprocessing  = preprocessing;
        this.pruning        = pruning;
    }

    /* ------- Preprocessing ------- */

    protected void doPreprocessing(MapFacade map, MovingRule movingRule){
        this.preprocessing.doPreprocessing(map,movingRule);
        this.pruning.doPreprocessing(map,movingRule);
    }

    protected boolean prune(Vector candidate, Vector direction, Vector goal){
        return pruning.prune(candidate,direction,goal);
    }


    /* ------- ShortestPath ------- */

    protected ShortestPathResult findShortestPath(MapFacade map, Vector start, Vector goal, Heuristic heuristic, MovingRule movingRule) throws NoPathFoundExeception {
        //Todo: System.out.println("start: "+start+"\t goal: "+goal);

        Map<Vector, Vector> pathPredecessors = new HashMap<>();

        PriorityQueue<Tuple3<Vector, Vector, Tuple2<Double,Double>>> openList = new PriorityQueue<>((p, q) -> {
            if (p.getArg3().getArg1() + p.getArg3().getArg2() > q.getArg3().getArg1() + q.getArg3().getArg2()) return 1;
            return -1;
        });

        openList.add(new Tuple3<>(start, null, new Tuple2<>(.0,heuristic.estimateDistance(start,goal))));
        while (!openList.isEmpty()) {
            Tuple3<Vector, Vector, Tuple2<Double, Double>> currentPath = openList.poll();
            Vector currentPoint         = currentPath.getArg1();
            Vector currentPredecessor   = currentPath.getArg2();
            double pathDistance         = currentPath.getArg3().getArg1();
            if (pathPredecessors.get(currentPoint) != null)     continue;
            pathPredecessors.put(currentPoint, currentPredecessor);
            if (currentPoint.equals(goal)) {
                return new ShortestPathResult(start, goal,
                        openList.stream().map(entry -> entry.getArg1()).collect(Collectors.toList()),
                        pathPredecessors,
                        pathDistance);
            }
            openList.addAll(getOpenListCandidates(map, currentPoint, currentPredecessor, goal, movingRule).stream().
                    filter(candidate -> pathPredecessors.get(candidate.getArg1()) == null).
                    map(candidate -> new Tuple3<>(candidate.getArg1(), currentPoint, new Tuple2<>(pathDistance + candidate.getArg2(),heuristic.estimateDistance(candidate.getArg1(),goal)))).
                    collect(Collectors.toList()));
        }
        throw new NoPathFoundExeception();
    }


    /* ------- Algorithm Substeps ------- */

    private Collection<Tuple2<Vector, Double>> getOpenListCandidates(MapFacade map, Vector currentPoint, Vector predecessor, Vector goal, MovingRule movingRule){
        Collection<Vector> directions = getDirectionsStrategy(map, currentPoint, predecessor, movingRule);

        Collection<Tuple2<Vector, Double>> candidates = new ArrayList<>();
        for(Vector dir:directions) {
            Tuple2<Vector, Double> candidate = exploreStrategy(map, currentPoint, dir, 0.0, goal, movingRule);
            //Todo: if(candidate!=null) System.out.print("current: "+currentPoint);
            if (candidate != null && !prune(candidate.getArg1(),dir,goal)) candidates.add(candidate);
        }
        return candidates;
    }
}