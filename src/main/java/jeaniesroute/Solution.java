/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jeaniesroute;

import java.io.*;
import java.util.*;

/**
 *
 * @author Geraldo Massahud
 * 
 * Informações SVN
 * @version $Revision: $:
 * @author  Última modificação por $Author: $:
 * @date    $Date: $:
 */
public class Solution {
    
    private static class Road {
        final City city;
        final int length;
        
        private Road(City city, int length) {
            this.city = city;
            this.length = length;
        }
    }
    
    private static class City {
        private final Integer id;
        private final ArrayList<Road> roads = new ArrayList<>();
        
        private City(Integer id) {
            this.id = id;
        }
    }
        
    private static City findLeaf(City[] cities) {        
        for (City city : cities) {
            if (city.roads.size() == 1) return city;
        }
        return null;
    }
    private static void prune(City[] cities, Set<Integer> letters) {        
        City city = findLeaf(cities);
        
    }
    
    private static int pruneRoad(City city, City parent, Road parentRoad) {
        if (city.roads.size() == 1) {            
            return  parentRoad.length;
        }
        return 0;        
    }
        
    public static void main(String[] args) {
        final Scanner scan = new Scanner(System.in);
        final int N = scan.nextInt();
        int K = scan.nextInt();
        final City[] cities = new City[N];
        Arrays.parallelSetAll(cities, (i) -> new City(i));
        final HashSet<Integer> letters = new HashSet<>(K);
        for (int i = 0; i < K; i++) {
            letters.add(scan.nextInt());            
        }
        for (int i = 0; i < N-1; i++) {
            City city1 = cities[scan.nextInt()];
            City city2 = cities[scan.nextInt()];
            int length = scan.nextInt();
            city1.roads.add(new Road(city2, length));
            city2.roads.add(new Road(city1, length));
        }
        
        prune(cities, letters);
                
    }
    
}
