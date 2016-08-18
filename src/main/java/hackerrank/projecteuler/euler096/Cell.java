package hackerrank.projecteuler.euler096;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

public class Cell extends Observable {

	public static final String VALUE_SET = "VALUE_SET";
	private final Set<Integer> possible = new HashSet<>(9);
	private final Set<Integer> mustBe = new HashSet<>(9);
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

	public void removePossible(Integer ... values) {
		for (int val : values) {
			if (possible.remove(val)) {
				mustBe.remove(val);
				setChanged();
				if (possible.size() == 1) {
					this.value = possible.iterator().next();
					notifyObservers(VALUE_SET);
				}
			}
		}
	}
	
	public void setMustBe(Collection<Integer> values) {
		if (possible.retainAll(values)) {
			setChanged();
		}
		mustBe.clear();
		mustBe.addAll(values);
		if (possible.size() == 1) {
			setValue(possible.iterator().next());		
		}
	}

	public void reset() {
		value = 0;
		mustBe.clear();
		for (int i = 1; i <= 9; i++) {
			possible.add(i);
		}
	}

	public Set<Integer> getMustBe() {
		return mustBe;
	}

	public Set<Integer> getPossible() {
		return possible;
	}

	@Override
	public String toString() {
		return "Cell [possible=" + possible + ", mustBe=" + mustBe + ", value=" + value + "]\n";
	}
	
	
}
