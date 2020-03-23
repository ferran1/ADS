package controller;

import graphalgorithms.AStarShortestPath;
import graphalgorithms.BreadthFirstPath;
import graphalgorithms.DepthFirstPath;
import graphalgorithms.DijkstraShortesPath;
import model.Station;
import model.TransportGraph;

import java.util.ArrayList;
import java.util.List;

public class TransportGraphLauncher {

    public static void main(String[] args) {

        // Assignment A
        String[] redLine = {"red", "metro", "A", "B", "C", "D"};
        String[] blueLine = {"blue", "metro", "E", "B", "F", "G"};
        String[] greenLine = {"green", "metro", "H", "I", "C", "G", "J"};
        String[] yellowLine = {"yellow", "bus", "A", "E", "H", "D", "G", "A"};

        TransportGraph transportGraph = new TransportGraph.Builder()
                .addLine(redLine)
                .addLine(blueLine)
                .addLine(greenLine)
                .addLine(yellowLine)
                .buildStationSet()
                .addLinesToStations()
                .buildConnections()
                .build();

        // Builder test
        System.out.println(transportGraph);

        // DepthFirstPath algorithm
        DepthFirstPath dfpTest = new DepthFirstPath(transportGraph, "E", "J");
        dfpTest.search();
        System.out.println("Result of DepthFirstSearch:");
        System.out.println(dfpTest);
        dfpTest.printNodesInVisitedOrder();
        System.out.println();

        // BreadthFirstPath algorithm
        BreadthFirstPath bfsTest = new BreadthFirstPath(transportGraph, "E", "J");
        bfsTest.search();
        System.out.println("Result of BreadthFirstSearch:");
        System.out.println(bfsTest);
        bfsTest.printNodesInVisitedOrder();

        for (Station station : transportGraph.getStationList()) {
            for (Station nextStation : transportGraph.getStationList()) {
                if (!station.equals(nextStation)) {
                    DepthFirstPath dfpTest2 = new DepthFirstPath(transportGraph, station.getStationName(), nextStation.getStationName());
                    BreadthFirstPath bfsTest2 = new BreadthFirstPath(transportGraph, station.getStationName(), nextStation.getStationName());
                    dfpTest2.search();
                    bfsTest2.search();
                    if (dfpTest2.getNodesInPathLength() < bfsTest2.getNodesInPathLength()) {
                        System.out.println("DFS: " + dfpTest2);
                    } else {
                        System.out.printf("BFS: %s, %d visited nodes, and path length: %d \n", bfsTest2, bfsTest2.getNodesVisitedLength(), bfsTest2.getNodesInPathLength());
                    }
                }
            }
        }

        // Dijkstra and A-star Algorithms vertices
        String[] redLine2 = {"red", "metro", "Haven", "Marken", "Steigerplein", "Centrum", "Meridiaan", "Dukdalf", "Oostvaarders"};
        String[] blueLine2 = {"blue", "metro", "Trojelaan", "Coltrane Cirkel", "Meridiaan", "Robijnpark", "Violetplantsoen"};
        String[] purpleLine2 = {"purple", "metro", "Grote Sluis", "Grootzeil", "Coltrane Cirkel", "Centrum", "Swingstraat"};
        String[] greenLine2 = {"green", "metro", "Ymeerdijk", "Trojelaan", "Steigerplein", "Swingstraat", "Bachgracht", "Nobelplein"};
        String[] yellowLine2 = {"yellow", "bus", "Grote Sluis", "Ymeerdijk", "Haven", "Nobelplein", "Violetplantsoen", "Oostvaarders", "Grote Sluis"};

        // Dijkstra Algorithm (Assignment B)
        List<Double> weights = new ArrayList<>();
        // redLine weights
        weights.add(4.5);
        weights.add(4.7);
        weights.add(6.1);
        weights.add(3.5);
        weights.add(5.4);
        weights.add(5.6);
        // blueLine weights
        weights.add(6.0);
        weights.add(5.3);
        weights.add(5.1);
        weights.add(3.3);
        // purpleLine weights
        weights.add(6.2);
        weights.add(5.2);
        weights.add(3.8);
        weights.add(3.6);
        // greenLine weights
        weights.add(5.0);
        weights.add(3.7);
        weights.add(6.9);
        weights.add(3.9);
        weights.add(3.4);
        // yellowLine weights
        weights.add(26.0);
        weights.add(19.0);
        weights.add(37.0);
        weights.add(25.0);
        weights.add(22.0);
        weights.add(28.0);
        weights.add(26.0);

        TransportGraph transportGraph2 = new TransportGraph.Builder()
                .addLine(redLine2)
                .addLine(blueLine2)
                .addLine(purpleLine2)
                .addLine(greenLine2)
                .addLine(yellowLine2)
                .buildStationSet()
                .addLinesToStations()
                .buildConnections()
                .setWeightsToConnections(weights)
                .build();

        // DijkstraShortesPath algorithm
        DijkstraShortesPath dijkstraTest = new DijkstraShortesPath(transportGraph2, "Oostvaarders", "Violetplantsoen");
        dijkstraTest.search();
        System.out.println("\nResult of Dijkstra:");
        System.out.println(dijkstraTest);
        dijkstraTest.printNodesInVisitedOrder();
        System.out.println("Total Weight of path = " + dijkstraTest.getTotalWeight());

        // A-star Algorithm (Assignment C)
        List<String> locations = new ArrayList<>();
        // redLine locations
        locations.add("14.1");
        locations.add("12.3");
        locations.add("10.5");
        locations.add("8.8");
        locations.add("6.9");
        locations.add("3.10");
        locations.add("0.11");
        // blueLine locations
        locations.add("9.3");
        locations.add("7.6");
        locations.add("6.9");
        locations.add("6.12");
        locations.add("5.14");
        // purpleLine locations
        locations.add("2.3");
        locations.add("4.6");
        locations.add("7.6");
        locations.add("8.8");
        locations.add("10.9");
        // greenLine locations
        locations.add("9.0");
        locations.add("9.3");
        locations.add("10.5");
        locations.add("10.9");
        locations.add("11.11");
        locations.add("12.13");
        // yellowLine locations
        locations.add("2.3");
        locations.add("9.0");
        locations.add("14.1");
        locations.add("12.13");
        locations.add("5.14");
        locations.add("0.11");
        locations.add("2.3");

        TransportGraph transportGraph3 = new TransportGraph.Builder()
                .addLine(redLine2)
                .addLine(blueLine2)
                .addLine(purpleLine2)
                .addLine(greenLine2)
                .addLine(yellowLine2)
                .buildStationSet()
                .addLinesToStations()
                .buildConnections()
                .setWeightsToConnections(weights)
                .setLocationsToStations(locations)
                .build();

        // Test of locations
        for (Station station : transportGraph3.getStationList()) {
            System.out.println(station.getStationName() + "\tX: " + station.getLocation().getX() + "\tY: " + station.getLocation().getY());
        }

        // Test of estimatedTime
        // If on diagonal line
        Station firstStation = transportGraph3.getStation(transportGraph3.getIndexOfStationByName("Grote Sluis"));
        Station secondStation = transportGraph3.getStation(transportGraph3.getIndexOfStationByName("Nobelplein"));
        System.out.println("Estimated traveling time WITH diagonal access: " + firstStation.getLocation().travelTime(secondStation.getLocation()));

        // If not in a diagonal line
        firstStation = transportGraph3.getStation(transportGraph3.getIndexOfStationByName("Grote Sluis"));
        secondStation = transportGraph3.getStation(transportGraph3.getIndexOfStationByName("Grootzeil"));
        System.out.println("Estimated traveling time WITHOUT diagonal access: " + firstStation.getLocation().travelTime(secondStation.getLocation()));

        // Test of A-star algorithm
        AStarShortestPath aStarShortestPath = new AStarShortestPath(transportGraph3, "Oostvaarders", "Violetplantsoen");
//        AStarShortestPath aStarShortestPath = new AStarShortestPath(transportGraph, "Oostvaarders", "Haven");
//        AStarShortestPath aStarShortestPath = new AStarShortestPath(transportGraph, "Trojelaan", "Meridiaan");
        aStarShortestPath.search();
        System.out.println("\nResult of A-star:");
        System.out.println(aStarShortestPath);
        aStarShortestPath.printNodesInVisitedOrder();

        for (Station station : transportGraph3.getStationList()) {
            for (Station nextStation : transportGraph3.getStationList()) {
                if (!station.equals(nextStation)) {
                    System.out.printf("\n%-15s %-30s %-15s %15s", "Algorithm", "From-To station", "Nodes visited", "PATH");

                    DijkstraShortesPath dijkstraShortesPath = new DijkstraShortesPath(transportGraph3, station.getStationName(), nextStation.getStationName());
                    AStarShortestPath aStarShortesPath = new AStarShortestPath(transportGraph3, station.getStationName(), nextStation.getStationName());
                    dijkstraShortesPath.search();
                    aStarShortesPath.search();

                    System.out.printf("\n%-15s %-30s %-10s %-15s %s", "Dijkstra", "" + station.getStationName() + "-" + nextStation.getStationName()
                            , dijkstraShortesPath.getNodesVisitedLength(), "", dijkstraShortesPath.toString());
                    System.out.printf("\n%-15s %-30s %-10s %-15s %s\n", "A-Star", "" + station.getStationName() + "-" + nextStation.getStationName()
                            , aStarShortesPath.getNodesVisitedLength(), "", aStarShortesPath.toString());
                }
            }
        }

    }
}
