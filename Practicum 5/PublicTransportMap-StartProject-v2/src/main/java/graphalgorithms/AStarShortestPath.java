package graphalgorithms;

import model.Connection;
import model.IndexMinPQ;
import model.TransportGraph;

public class AStarShortestPath extends AbstractPathSearch {
    private double[] distTo;
    private IndexMinPQ<Double> pq;

    public AStarShortestPath(TransportGraph graph, String start, String end) {
        super(graph, start, end);
        distTo = new double[graph.getNumberOfStations()];
        pq = new IndexMinPQ<>(graph.getNumberOfStations());
    }

    @Override
    public void search() {
        for (int v = 0; v < graph.getNumberOfStations(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[startIndex] = 0.0;
        pq.insert(startIndex, 0.0);
        while (!pq.isEmpty()) {
            int nextPq = pq.delMin();
            if (nextPq != endIndex) {
                relax(nextPq);
                nodesVisited.add(graph.getStation(nextPq));
            } else {
                break;
            }
        }
        pathTo(endIndex);
    }


    private void relax(int v) {


        // For each adjacent station check if the distance to adj station is
        for (int w : graph.getAdjacentVertices(v)) {
            Connection connection = graph.getConnection(v, w);
            double estimatedTravelTimeW = graph.getStation(w).getLocation().travelTime(graph.getStation(endIndex).getLocation());
            double estimatedTravelTimeV = graph.getStation(v).getLocation().travelTime(graph.getStation(endIndex).getLocation());

            if (distTo[v] + connection.getWeight() < distTo[w]) { // distTo[v] is the distance between v and startIndex
                if (v == startIndex && w == endIndex) {
                    distTo[w] = distTo[v] + connection.getWeight() + estimatedTravelTimeV;
                } else if (v == startIndex) {
                    distTo[w] = distTo[v] + connection.getWeight() + estimatedTravelTimeW;
                } else {
                    distTo[w] = (distTo[v] - estimatedTravelTimeV) + connection.getWeight() + estimatedTravelTimeW;
                }
                edgeTo[w] = v; // set this stations parent to be the station (v) because weight + travelTime is lower with current v for this w

                // If the station already is in the PQ update it's distance (which is now lower)
                if (pq.contains(w)) {
                    pq.changeKey(w, distTo[w]);
                } else { // If the station is not yet in the PQ, insert the new station
                    pq.insert(w, distTo[w]);
                }
            }
        }
    }

    @Override
    public boolean hasPathTo(int vertex) {
        return distTo[vertex] < Double.POSITIVE_INFINITY;
    }
}
