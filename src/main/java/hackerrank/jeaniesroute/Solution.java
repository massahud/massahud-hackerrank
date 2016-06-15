package hackerrank.jeaniesroute;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Solution {

    private static class City {

        private final Integer id;
        private final Map<City, Long> distances;

        public City(Integer id) {
            this.id = id;
            this.distances = new HashMap<>();
        }

        public final void addNeighbor(City neighbor, Long distance) {
            this.distances.put(neighbor, distance);
        }

        public final Integer getId() {
            return id;
        }

        public final Set<City> getNeighbors() {
            return distances.keySet();
        }

        public final Long getDistanceTo(City neighbor) {
            Long distance = distances.get(neighbor);
            return distance == null ? 0 : distance;
        }

        @Override
        public final int hashCode() {
            return 13 * Objects.hash(id);
        }

        @Override
        public final boolean equals(Object other) {
            if (Objects.isNull(other) || !(other instanceof City)) {
                return false;
            }
            City otherCity = (City) other;
            return Objects.equals(id, otherCity.id);
        }

        @Override
        public String toString() {
            return Objects.toString(id);
        }

    }

    public static class RouteSolver {

        private void prune(Set<City> mustVisit) {
            pruneRecursive(mustVisit.iterator().next(), null, mustVisit);
        }

        private boolean pruneRecursive(City city, City parent, Set<City> mustVisit) {

            if (parent != null && city.getNeighbors().size() == 1) {
                return mustVisit.contains(city);
            }

            boolean mustPass = mustVisit.contains(city);
            List<City> remove = new LinkedList<>();
            for (City neighbor : city.getNeighbors()) {
                if (!neighbor.equals(parent)) {
                    boolean cannotPrune = pruneRecursive(neighbor, city, mustVisit);
                    if (cannotPrune) {
                        mustPass = true;
                    } else {
                        remove.add(neighbor);
                    }
                }
            }
            city.getNeighbors().removeAll(remove);
            return mustPass;
        }

        private long findTotalDistance(Set<City> mustVisit) {
            return findTotalDistanceRecursive(mustVisit.iterator().next(), null);
        }

        private long findTotalDistanceRecursive(City city, City parent) {
            if (parent != null && city.getNeighbors().size() == 1) {
                return city.getDistanceTo(parent);
            }
            long totalDistance = parent != null ? city.getDistanceTo(parent) : 0;

            for (City neighbor : city.getNeighbors()) {
                if (!neighbor.equals(parent)) {
                    totalDistance += findTotalDistanceRecursive(neighbor, city);
                }
            }
            return totalDistance;
        }

        private long findBiggestBranchDistance(Set<City> mustVisit) {
            long[] biggest = new long[]{0};
            return findBiggestBranchRecursive(mustVisit.iterator().next(), null, biggest);
        }

        private long findBiggestBranchRecursive(City city, City parent, long[] biggest) {
            if (parent != null && city.getNeighbors().size() == 1) {
                return city.getDistanceTo(parent);
            } else {
                boolean updated = false;
                long biggest1 = 0;
                long biggest2 = 0;
                for (City neighbor : city.getNeighbors()) {
                    if (!neighbor.equals(parent)) {
                        long branchDistance = findBiggestBranchRecursive(neighbor, city, biggest);
                        if (branchDistance > biggest1) {
                            biggest2 = biggest1;
                            biggest1 = branchDistance;
                        } else if (branchDistance > biggest2) {
                            biggest2 = branchDistance;
                        }
                        updateBiggestBranch(city.getDistanceTo(parent)+biggest1, biggest);
                        updateBiggestBranch(biggest1+biggest2, biggest);
                    }
                }                                            
                if (parent == null) {
                    return biggest[0];
                }
                return biggest1 + city.getDistanceTo(parent);                
            }
        }

        private boolean updateBiggestBranch(long distance, long[] biggest) {
            if (distance >= biggest[0]) {
                biggest[0] = distance;
                return true;
            }
            return false;
        }

        public long getMaxDistanceNeededToTravel(Set<City> mustVisit) {
            prune(mustVisit);
            long totalDistance = findTotalDistance(mustVisit);
//            System.out.println(totalDistance);
            long biggestBranchDistance = findBiggestBranchDistance(mustVisit);
//            System.out.println(biggestBranchDistance);
            return totalDistance * 2 - biggestBranchDistance;
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int numberOfCities = scan.nextInt();
        City[] cities = IntStream.range(1, numberOfCities + 1).mapToObj(di -> new City(di)).toArray(City[]::new);
        int numberOfLetters = scan.nextInt();
        Set<City> mustVisit = new HashSet<>(numberOfLetters);
        for (int i = 0; i < numberOfLetters; i++) {
            mustVisit.add(cities[scan.nextInt() - 1]);
        }
        for (int i = 0; i < numberOfCities - 1; i++) {
            City city1 = cities[scan.nextInt() - 1];
            City city2 = cities[scan.nextInt() - 1];
            long distance = scan.nextLong();
            city1.addNeighbor(city2, distance);
            city2.addNeighbor(city1, distance);
        }

        final RouteSolver solver = new RouteSolver();
        //printGraphViz(mustVisit.iterator().next(), null, mustVisit);

        System.out.println(solver.getMaxDistanceNeededToTravel(mustVisit));

        //printGraphViz(mustVisit.iterator().next(), null, mustVisit);
    }

    private static void printGraphViz(City city, City parent, Set<City> mustVisit) {
        if (parent == null) {
            System.out.println("graph{splines=false;");
        }
        if (mustVisit.contains(city)) {
            System.out.format("%d [style=filled,color=red];\n", city.getId());
        }
        if (parent != null) {
            System.out.format("%d -- %d [label=%d];\n", parent.getId(), city.getId(), city.getDistanceTo(parent));
        }
        for (City neighbor : city.getNeighbors()) {
            if (!neighbor.equals(parent)) {
                printGraphViz(neighbor, city, mustVisit);
            }
        }

        if (parent == null) {
            System.out.println("}");
        }

    }

}

