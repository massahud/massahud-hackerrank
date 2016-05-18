package hackerrank.jeaniesroute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

public class Solution {
	
	private static class Road {
		int length;
		final City city;
		public Road(City city, int length) {
			this.length = length;
			this.city = city;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((city == null) ? 0 : city.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Road other = (Road) obj;
			if (city == null) {
				if (other.city != null)
					return false;
			} else if (!city.equals(other.city))
				return false;
			return true;
		}
	}
	
	private static class City {
		final int id;
		final HashSet<Road> roads = new HashSet<>();
	    boolean mustPass;
		public City(int id, boolean mustPass) {
			this.id = id;
			this.mustPass = mustPass; 
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			City other = (City) obj;
			if (id != other.id)
				return false;
			return true;
		}
		
	}
	public static void main(String[] args) {
		final Scanner scan = new Scanner(System.in);
		final int numberOfCities = scan.nextInt();
		final int numberOfLetters = scan.nextInt();
		final Set<Integer> letters = new HashSet<>(numberOfLetters);
		for (int i = 0; i < numberOfLetters; i++) {
			letters.add(scan.nextInt());
		}
		final HashMap<Integer, City> cities = new HashMap<>(numberOfCities);
		IntStream.range(0, numberOfCities).sequential().forEach((i) -> cities.put(i, new City(i, letters.contains(i))));
		for (int i = 0; i < numberOfCities-1; i++) {
			City city1 = cities.get(scan.nextInt());
			City city2 = cities.get(scan.nextInt());
			int length = scan.nextInt();
			city1.roads.add(new Road(city2,length));
			city2.roads.add(new Road(city1,length));
		}
		
		City leaf = findLeaf(cities);
		prune(leaf, null, cities);
	}
	
	private static City findLeaf(HashMap<Integer, City> cities) {
		return cities.values().stream().parallel().filter((city) -> city.roads.size() == 1).findAny().get();		
	}

	/**
	 * Prune the tree removing not necessary leafs and intermediary nodes.
	 * 
	 * The first city MUST BE a leaf with parameter from null.
	 * 
	 * @param me
	 * @param from
	 * @param cities
	 * @return
	 */
	private static Road prune(City me, City from, HashMap<Integer, City> cities) {
		Road[] roads = me.roads.toArray(new Road[me.roads.size()]);
		Road previousRoad = null;
		for (int i = 0; i < roads.length; i++) {
			Road road = roads[i];
			if (road.city.equals(from)) {
				previousRoad = road;
			} else {
				Road newRoad = prune(road.city, me, cities);
				if (newRoad.length == 0) {
					me.roads.remove(road);		
					cities.remove(road.city.id);
				} else {
					me.mustPass = true;
					me.roads.remove(road);
					me.roads.add(newRoad);
				}
			}
		}
		if (me.roads.size() == 1) { 
			// leaf
			if (!me.mustPass) {
				return new Road(me, 0);
			} else {
				return me.roads.iterator().next();
			}
		} else if (me.roads.size() == 2) {
			// middle of single path (remove)
			
			Iterator<Road> it = me.roads.iterator();
			Road nextRoad = it.next();			
			if (nextRoad.equals(previousRoad)) {				
				nextRoad = it.next();
			}
			cities.remove(nextRoad.city.id);
			me.roads.remove(nextRoad);
			return new Road(me, previousRoad.length + nextRoad.length);
		} else {
			// more than 2 roads, can't prune
			return previousRoad;
		}		
	}
}
