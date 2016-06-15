package hackerrank.jeaniesroute;


import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

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
            if (parent == null && city.getNeighbors().size() <= 2) {
                long distance = 0;
                for (City neighbor : city.getNeighbors()) {
                    distance += findBiggestBranchRecursive(neighbor, city, biggest);
                }
                updateBiggestBranch(distance, biggest);
                return biggest[0];
            } else if (parent != null && city.getNeighbors().size() == 1) {
                return city.getDistanceTo(parent);
            } else if (city.getNeighbors().size() == 2) {
                for (City neighbor : city.getNeighbors()) {
                    if (!neighbor.equals(parent)) {
                        long branchDistance = findBiggestBranchRecursive(neighbor, city, biggest);
                        if (branchDistance == 0) {
                            return 0;
                        } else {
                            return branchDistance + city.getDistanceTo(parent);
                        }
                    }
                }
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
                        updateBiggestBranch(biggest1+biggest2, biggest);
                    }
                }
                
                if (updated) {
                    updateBiggestBranch(biggest1 + city.getDistanceTo(parent), biggest);
                    return biggest1 + city.getDistanceTo(parent);
                }
            }
            return 0;
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
//        Scanner scan = new Scanner(new StringReader("300 10 44 137 195 20 42 75 109 106 160 125 114 250 2 267 148 6 19 223 10 193 300 4 12 131 8 63 230 7 226 184 6 229 271 1 273 27 1 130 119 3 175 212 9 262 183 1 254 227 6 83 200 9 37 77 8 12 58 6 197 134 4 120 160 10 69 107 9 217 173 4 126 251 1 65 261 1 264 101 1 111 145 1 96 33 1 77 32 1 89 51 2 278 296 10 181 188 4 133 238 10 113 159 3 66 123 5 121 208 10 298 243 3 53 75 8 134 195 7 196 35 5 20 164 10 281 211 9 277 9 7 245 48 6 236 52 9 20 41 1 172 48 9 283 264 9 110 24 9 46 117 6 80 258 9 92 97 8 262 275 6 218 10 10 128 268 8 158 180 10 288 205 10 141 21 9 237 192 8 99 201 7 174 14 3 189 183 5 102 83 7 107 105 10 221 146 7 234 162 10 249 104 3 244 297 9 272 276 6 94 219 1 266 213 3 270 135 2 62 204 9 53 280 4 54 40 7 259 190 5 156 147 3 42 19 2 291 71 7 241 200 5 5 257 1 250 191 10 39 97 5 245 295 2 60 241 10 169 297 5 192 29 3 69 293 8 115 23 7 100 36 2 81 41 2 59 7 4 31 75 8 93 299 4 155 235 3 254 75 8 95 199 8 228 157 9 63 38 9 16 74 4 73 265 3 25 223 1 61 266 5 161 23 1 150 288 10 294 137 7 138 82 9 225 82 9 87 128 10 8 190 1 13 240 5 269 55 8 246 278 7 252 118 6 178 136 7 18 246 2 51 240 6 242 33 9 88 2 2 129 95 2 71 6 10 215 133 4 3 109 2 136 276 7 205 195 7 125 166 6 194 64 6 54 130 4 137 218 10 157 170 8 43 143 7 214 204 8 66 30 3 42 274 5 58 165 6 292 259 6 124 103 6 24 171 6 174 189 4 282 202 2 139 173 10 47 50 2 29 131 2 49 265 1 206 247 6 92 115 1 46 248 7 140 45 8 231 244 6 127 215 4 242 216 10 185 251 10 212 80 1 220 72 5 91 112 1 295 73 5 187 176 5 274 104 9 55 138 2 37 86 10 224 212 8 188 140 1 222 142 10 132 123 7 246 89 9 228 269 9 28 36 3 299 15 6 207 124 7 279 153 8 13 202 8 52 263 8 101 187 9 158 96 9 32 148 8 287 300 7 210 235 10 94 154 2 230 109 6 273 193 7 106 285 2 151 153 6 191 197 7 208 60 1 236 232 6 249 3 9 199 45 4 76 5 7 206 44 7 298 143 2 70 168 5 294 220 10 85 162 9 170 102 7 43 99 3 239 179 1 261 239 1 163 211 3 28 255 1 116 226 4 293 203 5 201 59 1 112 79 2 253 150 9 90 113 5 152 106 3 180 232 3 57 292 8 167 129 7 85 289 7 229 225 9 257 198 8 272 287 9 177 27 10 198 209 5 114 44 7 108 280 8 177 290 6 135 216 9 79 161 7 98 268 8 118 146 6 68 181 5 209 238 3 145 1 1 252 84 1 284 248 2 175 11 2 214 105 10 286 159 10 296 68 5 47 258 4 253 6 8 267 108 3 165 16 4 194 25 3 152 164 3 81 121 3 285 281 5 50 100 10 186 182 10 286 275 5 185 176 10 9 72 2 270 227 8 70 149 4 155 119 3 222 14 8 256 56 7 4 182 10 207 65 3 217 120 5 84 56 1 179 156 4 163 233 6 87 39 3 166 203 4 243 122 7 132 10 7 168 247 5 154 142 3 26 31 10 277 186 8 233 18 8 284 169 3 234 38 8 290 125 8 168 90 2 88 21 1 155 231 7 35 289 1 221 110 1 263 117 9 61 172 1 103 210 7 139 98 1 184 49 7 67 160 7 231 99 3 151 8 2 282 78 3 213 15 2 22 122 4 93 144 1 256 147 1 255 7 3 141 34 8 34 279 8 76 149 2 291 144 1 283 74 7 4 22 1 17 67 5 126 64 10 17 1 6 91 30 4 178 127 1 237 167 10 260 171 9 40 57 1 78 111 9 26 219 3"));
//        Scanner scan = new Scanner(new StringReader("5 3\n" +
//"1 3 4\n" +
//"1 2 1\n" +
//"2 3 2\n" +
//"2 4 2\n" +
//"3 5 3"));
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
        printGraphViz(mustVisit.iterator().next(), null, mustVisit);

        System.out.println(solver.getMaxDistanceNeededToTravel(mustVisit));

        printGraphViz(mustVisit.iterator().next(), null, mustVisit);
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
