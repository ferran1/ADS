package graphalgorithms;

import model.TransportGraph;

import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstPath extends AbstractPathSearch {
    public BreadthFirstPath(TransportGraph graph, String start, String end) {
        super(graph, start, end);
    }

    @Override
    public void search() {
        Queue<Integer> queue = new LinkedList<>();
        marked[startIndex] = true;          // Mark the source
        queue.add(startIndex);              // and put it on the queue.
        while (!queue.isEmpty()) {
            int v = queue.poll();           // Remove next vertex from the queue.
            if (v != endIndex) {
                nodesVisited.add(graph.getStation(v));
                for (int w : graph.getAdjacentVertices(v))
                    if (!marked[w]) {           // For every unmarked adjacent vertex,
                        edgeTo[w] = v;          // Save last edge on a shortest path,
                        marked[w] = true;       // mark it because path is known,
                        if (w == endIndex) {
                            pathTo(endIndex);
                        }
                        queue.add(w);           // and add it to the queue.

                    }
            }else {
                nodesVisited.add(graph.getStation(v));
                break;                          // If target is found stop the search
            }
        }
    }
}
