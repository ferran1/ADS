package graphalgorithms;

import model.Connection;
import model.IndexMinPQ;
import model.TransportGraph;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DijkstraShortesPath extends AbstractPathSearch {
    private double[] distTo;
    private IndexMinPQ<Double> pq;

    public DijkstraShortesPath(TransportGraph graph, String start, String end) {
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
            if (distTo[v] + connection.getWeight() < distTo[w]) {
                distTo[w] = distTo[v] + connection.getWeight(); // set the new distance to the w from the startIndex
                edgeTo[w] = v; // set this stations parent to be the station (v) because weight is lower with current v for this w
                // If the station already is in the PQ update its distance (which is now lower)
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

    public double distTo(int v) {
        return distTo[v];
    }

    public double getTotalWeight() {
        BigDecimal bd = BigDecimal.valueOf(distTo(endIndex));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
