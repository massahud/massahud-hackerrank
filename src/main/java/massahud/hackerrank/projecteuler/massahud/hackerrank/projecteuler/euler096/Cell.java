package massahud.hackerrank.projecteuler.massahud.hackerrank.projecteuler.euler096;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

public class Cell extends Observable {
	private static final String VALUE_SET = "VALUE_SET";
	private final Set<Integer> possible = new HashSet<>(9);
	private int value;
	
	public Cell() {
		reset();
	}

	public void setValue(int value) {
		reset();
		if (value != 0) {
			for (int v = 1; v <= 9; v++) {
				if (v != value) {
					removePossible(v);
				}
			}
		}
	}

	public int getValue() {
		return value;
	}

	public void removePossible(int val) {
		if (possible.remove(val)) {
			if (possible.size() == 1) {
				this.value = possible.iterator().next();
				notifyObservers(VALUE_SET);
			}
		}
		
	}

	public void reset() {
		this.value = 0;
		for (int i = 1; i <= 9; i++) {
			possible.add(i);
		}
	}
}
