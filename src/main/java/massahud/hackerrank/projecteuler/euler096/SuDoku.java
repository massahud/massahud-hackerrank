package massahud.hackerrank.projecteuler.euler096;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.Set;

public class SuDoku {
	private final Set<Area> areas = new HashSet<>(9 * 9 * 3);
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
		for (int i = 0; i < 9; i += 3) {
			for (int j = 0; j < 9; j += 3) {
				Area square = new Area();
				areas.add(square);
				for (int k = i, kk = i + 3; k < kk; k++) {
					for (int l = j, ll = j + 3; l < ll; l++) {
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
				if (cells[i][j].getValue() == 0) {
					cells[i][j].setValue(line.charAt(j) - '0');
				} else {
					assert cells[i][j].getValue() == (line.charAt(j) - '0');
				}
			}
		}
		boolean notSolved = false;
		do {
//			System.out.println(toString(cells));
			for (Area area : areas) {
				area.setMustBeValues();
			}
			notSolved = false;
			for (int i = 0; i < 9 && !notSolved; i++) {
				for (int j = 0; j < 9 && !notSolved; j++) {
					if (cells[i][j].getValue() == 0) {
						notSolved = true;
						Area smallest = null;
						for (Area area : areas) {
							if (area.getBlank().size() > 0) {
								if (smallest == null || smallest.getBlank().size() > area.getBlank().size()) {
									smallest = area;
								}
							}
						}
						Cell blankCell = smallest.getBlank().iterator().next();
						blankCell.setValue(blankCell.getMustBe().iterator().next());						
						break;
					}
				}
			}
		} while (notSolved);

		String[] solution = new String[9];
		for (int i = 0; i < 9; i++) {
			StringBuilder line = new StringBuilder();
			for (int j = 0; j < 9; j++) {
				line.append((char) (cells[i][j].getValue() + '0'));
			}
			solution[i] = line.toString();
		}

		return solution;
	}

	public static void main(String[] args) {
		SuDoku sudoku = new SuDoku();
		Scanner scan = new Scanner(SuDoku.class.getResourceAsStream("p096_sudoku.txt"));
//		Scanner scan = new Scanner(SuDoku.class.getResourceAsStream("grid02.txt"));
		String[] board = new String[9];
		while (scan.hasNext()) {
			System.out.println(scan.nextLine());
			for (int i = 0; i < 9; i++) {
				board[i] = scan.nextLine();
			}
			String[] solved = sudoku.solve(board);
			for (String str : solved) {
				if (str.indexOf("0") >= 0) {
					System.out.println("FAIL");
					break;
				}
			}
			// for (String line : board) {
			// System.out.println(line);
			// }
			//
			for (String line : solved) {
				System.out.println(line);
			}

		}
	}

	public String toString(Cell[][] cells) {
		StringBuilder val = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				val.append(cells[i][j].getValue()).append(" ");
			}
			val.append("\n");
		}
		return val.toString();
	}
}
