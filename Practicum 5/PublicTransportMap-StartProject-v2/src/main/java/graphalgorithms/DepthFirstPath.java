package graphalgorithms;

import model.TransportGraph;

public class DepthFirstPath extends AbstractPathSearch {
    public DepthFirstPath(TransportGraph graph, String start, String end) {
        super(graph, start, end);
    }

    @Override
    public void search() {
        dfs(startIndex);
    }

    private void dfs(int v) {
        marked[v] = true;
        nodesVisited.add(graph.getStation(v));
        for (int w : graph.getAdjacentVertices(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                if (w == endIndex) {
                    marked[w] = true;
                    nodesVisited.add(graph.getStation(w));
                    pathTo(endIndex);
                } else
                    dfs(w);
            }
        }
    }
}
