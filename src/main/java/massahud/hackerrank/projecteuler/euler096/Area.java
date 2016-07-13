
package massahud.hackerrank.projecteuler.euler096;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class Area implements Observer {
	
	private final Set<Cell> cells = new HashSet<>(9);
	private final Set<Cell> blank = new HashSet<>(9);
	private final Set<Cell> filled = new HashSet<>(9);
	
	public void addCell(Cell cell) {
		if (cell.getValue() != 0) {
			throw new IllegalArgumentException("Added cell should be blank.");
		}
		if (cells.size() == 9) {
			throw new IllegalStateException("Can not add more cells, area has 9 cells already.");
		}
		cells.add(cell);
		blank.add(cell);
		cell.addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg.equals(Cell.VALUE_SET)) {
			Cell cell = (Cell)o;		
			if (cell.getValue() != 0) {
				blank.remove(cell);
				filled.add(cell);
				for (Cell c : new ArrayList<>(blank)) {
					c.removePossible(cell.getValue());			
				}
			}		
		} else if (arg instanceof Area && !arg.equals(this)) {
			Area area = (Area)arg;
			Set<Cell> intersection = new HashSet<>(blank);
			intersection.retainAll(area.cells);
			if (!intersection.isEmpty() && intersection.size() < blank.size()) {
				Cell c = intersection.iterator().next();
				if (c.getMustBe().size() == intersection.size()) {
					for (Cell b : new ArrayList<>(blank)) {
						if (!intersection.contains(b)) {
							b.removePossible(c.getMustBe().toArray(new Integer[0]));
						}
					}
				}
			}
		}		
	}
	
	public void reset() {
		for (Cell c: cells) {
			c.reset();
			blank.add(c);			
		}
		filled.clear();
	}
	
	public void setMustBeValues() {
		Set<Integer> shouldBe = new HashSet<>(9);
		List<Cell> blanks = new ArrayList<>(blank);
		for (int i = 1; i <= 9; i++) {
			shouldBe.add(i);
		}
		for (Cell f : filled) {
			shouldBe.remove(f.getValue());
		}
		for (Cell c : blanks) {
			c.setMustBe(shouldBe);
		}
		for (Cell c : blanks) {
			c.notifyObservers(this);
		}
	}
	
}