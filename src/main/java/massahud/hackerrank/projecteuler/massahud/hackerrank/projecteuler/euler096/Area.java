
package massahud.hackerrank.projecteuler.massahud.hackerrank.projecteuler.euler096;

import java.util.HashSet;
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
		Cell cell = (Cell)o;
		if (cell.getValue() != 0) {
			blank.remove(cell);
			filled.add(cell);
			for (Cell c : blank) {
				c.removePossible(cell.getValue());			
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
	
}