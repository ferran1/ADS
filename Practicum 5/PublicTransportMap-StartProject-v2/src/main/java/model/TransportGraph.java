package model;

import java.util.*;

public class TransportGraph {

    private int numberOfStations;
    private int numberOfConnections;
    private List<Station> stationList;
    private Map<String, Integer> stationIndices;
    private List<Integer>[] adjacencyLists;
    private Connection[][] connections;

    public TransportGraph(int size) {
        this.numberOfStations = size;
        stationList = new ArrayList<>(size);
        stationIndices = new HashMap<>();
        connections = new Connection[size][size];
        adjacencyLists = (List<Integer>[]) new List[size];
        for (int vertex = 0; vertex < size; vertex++) {
            adjacencyLists[vertex] = new LinkedList<>();
        }
    }

    /**
     * @param vertex Station to be added to the stationList
     *               The method also adds the station with it's index to the map stationIndices
     */
    public void addVertex(Station vertex) {
        this.stationList.add(vertex);
        this.stationIndices.put(vertex.getStationName(), stationList.indexOf(vertex));
    }


    /**
     * Method to add an edge to a adjancencyList. The indexes of the vertices are used to define the edge.
     * Method also increments the number of edges, that is number of Connections.
     * The grap is bidirected, so edge(to, from) should also be added.
     *
     * @param from
     * @param to
     */
    private void addEdge(int from, int to) {
        this.adjacencyLists[from].add(to);
        this.adjacencyLists[to].add(from);
        this.numberOfConnections++;
    }


    /**
     * Method to add an edge in the form of a connection between stations.
     * The method also adds the edge as an edge of indices by calling addEdge(int from, int to).
     * The method adds the connecion to the connections 2D-array.
     * The method also builds the reverse connection, Connection(To, From) and adds this to the connections 2D-array.
     *
     * @param connection The edge as a connection between stations
     */
    public void addEdge(Connection connection) {
        String stationNameFrom = connection.getFrom().getStationName();
        String stationNameTo = connection.getTo().getStationName();

        Integer stationIndexFrom = stationIndices.get(stationNameFrom);
        Integer stationIndexTo = stationIndices.get(stationNameTo);

        // Use of private helper method 'addEdge'
        this.addEdge(stationIndexFrom, stationIndexTo);

        // Add the connection to the 2D array
        connections[stationIndexFrom][stationIndexTo] = connection;

        // Make reverse connection
        Connection reverseConnection = new Connection(connection.getTo(), connection.getFrom(), connection.getWeight(), connection.getLine());
        connections[stationIndexTo][stationIndexFrom] = reverseConnection;
    }

    public List<Integer> getAdjacentVertices(int index) {
        return adjacencyLists[index];
    }

    public Connection getConnection(int from, int to) {
        return connections[from][to];
    }

    public int getIndexOfStationByName(String stationName) {
        return stationIndices.get(stationName);
    }

    public Station getStation(int index) {
        return stationList.get(index);
    }

    public int getNumberOfStations() {
        return numberOfStations;
    }

    public List<Station> getStationList() {
        return stationList;
    }

    public int getNumberEdges() {
        return numberOfConnections;
    }

    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder();
        resultString.append(String.format("Graph with %d vertices and %d edges: \n", numberOfStations, numberOfConnections));
        for (int indexVertex = 0; indexVertex < numberOfStations; indexVertex++) {
            resultString.append(stationList.get(indexVertex) + ": ");
            int loopsize = adjacencyLists[indexVertex].size() - 1;
            for (int indexAdjacent = 0; indexAdjacent < loopsize; indexAdjacent++) {
                resultString.append(stationList.get(adjacencyLists[indexVertex].get(indexAdjacent)).getStationName() + "-");
            }
            resultString.append(stationList.get(adjacencyLists[indexVertex].get(loopsize)).getStationName() + "\n");
        }
        return resultString.toString();
    }


    /**
     * A Builder helper class to build a TransportGraph by adding lines and building sets of stations and connections from these lines.
     * Then build the graph from these sets.
     */
    public static class Builder {

        private Set<Station> stationSet;
        private List<Line> lineList;
        private Set<Connection> connectionSet;

        public Builder() {
            lineList = new ArrayList<>();
            stationSet = new HashSet<>();
            connectionSet = new HashSet<>();
        }

        /**
         * Method to add a line to the list of lines and add stations to the line.
         *
         * @param lineDefinition String array that defines the line. The array should start with the name of the line,
         *                       followed by the type of the line and the stations on the line in order.
         *                       E.g.: String[] redLine = {"red", "metro", "A", "B", "C", "D"};
         * @return
         */
        public Builder addLine(String[] lineDefinition) {
            Line line = new Line(lineDefinition[1], lineDefinition[0]);
            for (int i = 2; i < lineDefinition.length; i++) {
                line.addStation(new Station(lineDefinition[i]));
            }
            this.lineList.add(line);
            return this;
        }


        /**
         * Method that reads all the lines and their stations to build a set of stations.
         * Stations that are on more than one line will only appear once in the set.
         *
         * @return
         */
        public Builder buildStationSet() {
            for (Line line : this.lineList) {
                List<Station> stationsOnLine = line.getStationsOnLine();
                this.stationSet.addAll(stationsOnLine);
            }

            return this;
        }

        /**
         * For every station on the set of station add the lines of that station to the lineList in the station
         *
         * @return
         */
        public Builder addLinesToStations() {
            for (Line line : this.lineList) {
                for (Station station : this.stationSet) {
                    if (line.getStationsOnLine().contains(station)) {
                        station.addLine(line);
                    }
                }
            }

            return this;
        }

        /**
         * Method that uses the list of Lines to build connections from the consecutive stations in the stationList of a line.
         *
         * @return
         */
        public Builder buildConnections() {
            for (Line line : this.lineList) {

                for (int i = 0; i < line.getStationsOnLine().size(); i++) {
                    Station currentStation = line.getStationsOnLine().get(i);

                    // If the line has no more stations than 2
                    // and there is no next station for the current Station,
                    // we cannot add a connection
                    if (line.getStationsOnLine().size() >= 2 && i + 1 != line.getStationsOnLine().size()) {
                        Station nextStation = line.getStationsOnLine().get(i + 1);
                        Connection connection1 = new Connection(currentStation, nextStation, 0, line);
                        this.connectionSet.add(connection1);
                    }
                }

            }

            return this;
        }

        public Builder setWeightsToConnections(List<Double> weights) {
            Iterator<Double> weightIterator = weights.iterator();
            for (Line commonLine : lineList) {
                for (Station station : commonLine.getStationsOnLine()) {
                    for (Connection connection : connectionSet) {
                        if (connection.getLine().equals(commonLine) && connection.getFrom().equals(station)) {
                            connection.setWeight(weightIterator.next());
                        }
                    }
                }
            }
            return this;
        }

        public Builder setLocationsToStations(List<String> locations) {
            Iterator<String> locationsIterator = locations.iterator();
            for (Line commonLine : lineList) {
                for (Station station : commonLine.getStationsOnLine()) {
                    String[] arr = String.valueOf(locationsIterator.next()).split("\\.");
                    int[] intArr = new int[2];
                    intArr[0] = Integer.parseInt(arr[0]); // X
                    intArr[1] = Integer.parseInt(arr[1]); // Y
                    station.setLocation(new Location(intArr[0], intArr[1]));
                }
            }
            return this;
        }


        /**
         * Method that builds the graph.
         * All stations of the stationSet are added as vertices to the graph.
         * All connections of the connectionSet are addes as edges to the graph.
         *
         * @return
         */
        public TransportGraph build() {
            TransportGraph graph = new TransportGraph(stationSet.size());

            // Add all stations as vertices to the graph
            for (Station station : stationSet) {
                graph.addVertex(station);
            }

            // Add all connections as edges to the graph
            for (Connection connection : connectionSet) {
                graph.addEdge(connection);
            }

            return graph;
        }

    }
}
