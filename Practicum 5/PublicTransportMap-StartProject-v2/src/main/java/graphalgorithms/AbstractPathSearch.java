package graphalgorithms;

import model.Connection;
import model.Line;
import model.Station;
import model.TransportGraph;

import java.util.*;

/**
 * Abstract class that contains methods and attributes shared by the DepthFirstPath en BreadthFirstPath classes
 */
public abstract class AbstractPathSearch {

    protected boolean[] marked;
    protected int[] edgeTo;
    protected int transfers = 0;
    protected List<Station> nodesVisited;
    protected List<Station> nodesInPath;
    protected LinkedList<Integer> verticesInPath;
    protected TransportGraph graph;
    protected final int startIndex;
    protected final int endIndex;


    public AbstractPathSearch(TransportGraph graph, String start, String end) {
        startIndex = graph.getIndexOfStationByName(start);
        endIndex = graph.getIndexOfStationByName(end);
        this.graph = graph;
        nodesVisited = new ArrayList<>();
        marked = new boolean[graph.getNumberOfStations()];
        edgeTo = new int[graph.getNumberOfStations()];
    }

    public abstract void search();

    /**
     * @param vertex Determines whether a path exists to the station as an index from the start station
     * @return
     */
    public boolean hasPathTo(int vertex) {
        return marked[vertex];
    }


    /**
     * Method to build the path to the vertex, index of a Station.
     * First the LinkedList verticesInPath, containing the indexes of the stations, should be build, used as a stack
     * Then the list nodesInPath containing the actual stations is build.
     * Also the number of transfers is counted.
     *
     * @param vertex The station (vertex) as an index
     */
    public void pathTo(int vertex) {

        if (!hasPathTo(vertex)) {
            System.out.println("Has no path to vertex " + vertex);
            return;
        }

        verticesInPath = new LinkedList<>();
        for (int i = vertex; i != startIndex; i = edgeTo[i]) {
            verticesInPath.push(i);
        }
        verticesInPath.push(startIndex);

        // Build the nodesInPath list by using the verticesInPath indices
        nodesInPath = new LinkedList<>();
        for (Integer verticesIndex : verticesInPath) {
            nodesInPath.add(graph.getStationList().get(verticesIndex));
        }

        countTransfers();
    }

    /**
     * Method to count the number of transfers in a path of vertices.
     * Uses the line information of the connections between stations.
     * If two consecutive connections are on different lines there was a transfer.
     */
    public void countTransfers() {
        Line prevLine = null;
        Line currentLine;

        for (int i = 0; i < nodesInPath.size(); i++) {
            if ((i + 1) < nodesInPath.size()) {
                Connection connection = this.graph.getConnection(graph.getIndexOfStationByName(nodesInPath.get(i).getStationName()),
                        graph.getIndexOfStationByName(nodesInPath.get(i + 1).getStationName()));

                currentLine = connection.getLine();
                if (currentLine != prevLine) {
                    prevLine = currentLine;
                    this.transfers++;
                }
            }
        }
        // We do -1 because we don't count the line we first began with, because it is not a transfer
        this.transfers -= 1;
    }


    /**
     * Method to print all the nodes that are visited by the search algorithm implemented in one of the subclasses.
     */
    public void printNodesInVisitedOrder() {
        System.out.print("Nodes in visited order: ");
        for (Station vertex : nodesVisited) {
            System.out.print(vertex.getStationName() + " ");
        }
        System.out.println();
    }

    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder(String.format("Path from %s to %s: ", graph.getStation(startIndex), graph.getStation(endIndex)));
        resultString.append(nodesInPath).append(" with " + transfers).append(" transfers");
        return resultString.toString();
    }

    public int getNodesInPathLength() {
        return nodesInPath.size();
    }

    public int getNodesVisitedLength() {
        return nodesVisited.size();
    }
}
