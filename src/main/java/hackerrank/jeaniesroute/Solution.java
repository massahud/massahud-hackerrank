package hackerrank.jeaniesroute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
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
			AtomicLong biggest = new AtomicLong(0);
			return findBiggestBranchRecursive(mustVisit.iterator().next(), null, biggest);
		}

		private BiConsumer<long[], Long> accumulateTwoLargestDistances() {
			return (max, d) -> {
				if (d > max[0]) {
					max[1] = max[0];
					max[0] = d;
				} else if (d > max[1]) {
					max[1] = d;
				}
			};
		}

		private BiConsumer<long[], long[]> getTwoLargestDistances() {
			return (max1, max2) -> {
				long max = Math.max(max1[0], max2[0]);
				long secondMax = Math.max(Math.max(Math.min(max1[0], max2[0]), max1[1]), max2[1]);
				max1[0] = max;
				max1[1] = secondMax;
			};
		}

		private long findBiggestBranchRecursive(final City city, final City parent, final AtomicLong biggest) {
			final long distanceToParent = city.getDistanceTo(parent);
			if (parent != null && city.getNeighbors().size() == 1) {
				return city.getDistanceTo(parent);
			} else {
				long maxDistanceThroughParent = 0;
				final long[] twoMaxChilsDistances = city.getNeighbors().stream().parallel()
						.filter(neighbor -> !neighbor.equals(parent))
						.map(neighbor -> findBiggestBranchRecursive(neighbor, city, biggest))
						.collect(() -> new long[2], accumulateTwoLargestDistances(), getTwoLargestDistances());
				maxDistanceThroughParent = twoMaxChilsDistances[0] + distanceToParent;
				synchronized (biggest) {
					biggest.set(Math.max(biggest.get(),
							Math.max(maxDistanceThroughParent, twoMaxChilsDistances[0] + twoMaxChilsDistances[1])));

				}
				if (parent == null) {
					return biggest.get();
				}
				return maxDistanceThroughParent;
			}
		}

		public long getMaxDistanceNeededToTravel(Set<City> mustVisit) {
			prune(mustVisit);
			long totalDistance = findTotalDistance(mustVisit);
			// System.out.println(totalDistance);
			long biggestBranchDistance = findBiggestBranchDistance(mustVisit);
			// System.out.println(biggestBranchDistance);
			return totalDistance * 2 - biggestBranchDistance;
		}
	}

	public static void main(String[] args) {
		// Scanner scan = new Scanner(System.in);
		Scanner scan = new Scanner(Solution.class.getResourceAsStream("input4.txt"));
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
		// printGraphViz(mustVisit.iterator().next(), null, mustVisit);

		System.out.println(solver.getMaxDistanceNeededToTravel(mustVisit));

		// printGraphViz(mustVisit.iterator().next(), null, mustVisit);
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
