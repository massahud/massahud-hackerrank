package massahud.hackerrank.projecteuler.massahud.hackerrank.projecteuler.euler096;

import java.util.HashSet;
import java.util.Set;

public class SuDoku {
	private final Set<Area> areas = new HashSet<>(9*9*3);
	private final Cell[][] cells = new Cell[9][9];
	
	public SuDoku() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				cells[i][j] = new Cell();
			}
		}
		for (int i = 0; i < 9; i++) {
			Area line = new Area();
			Area col = new Area();
			areas.add(line);
			areas.add(col);
			for (int j = 0; j < 9; j++) {
				line.addCell(cells[i][j]);
				col.addCell(cells[j][i]);
			}
		}
		for (int i = 0; i < 9; i+=3) {
			for (int j = 0; j < 9; j+=3) {
				Area square = new Area();
				areas.add(square);
				for (int k = i, kk = i+3; k < kk; k++) {
					for (int l = j, ll = j+3; l < ll; l++) {
						square.addCell(cells[k][l]);
					}
				}
			}
		}
	}
	
	private void reset() {
		for (Area area : areas) {
			area.reset();
		}
	}
	
	public String[] solve(String[] state) {
		reset();
		
		for (int i = 0; i < 9; i++) {
			String line = state[i];
			for (int j = 0; j < 9; j++) {
				cells[i][j].setValue(line.charAt(j) - '0');
			}
		}		
		String[] solution = new String[9];
		for (int i = 0; i < 9; i++) {
			StringBuilder line = new StringBuilder();
			for (int j = 0; j < 9; j++) {
				line.append((char)(cells[i][j].getValue() + '0'));
			}
			solution[i] = line.toString();
		}
		
		return solution;
	}
	
	public static void main(String[] args) {
		SuDoku sudoku = new SuDoku();
		String[] board = new String[] {
				"003020600",
				"900305001",
				"001806400",
				"008102900",
				"700000008",
				"006708200",
				"002609500",
				"800203009",
				"005010300"
		};
		System.out.println("BOARD");
		for (String line : board) {
			System.out.println(line);
		}
		System.out.println("SOLUTION");
		for (String line : sudoku.solve(board)) {
			System.out.println(line);
		}
	}
}
