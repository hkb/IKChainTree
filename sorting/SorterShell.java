package sorting;

import dataStructures.Set;

public class SorterShell extends Sorter {
	public void Sort(Set set, SortTool tool, boolean descending) {
		int inc;
		int comp = (descending)? SortTool.COMP_LESS : SortTool.COMP_GRTR;
		for (inc = 1; inc <= set.getSize()/9; inc = 3*inc+1);
		for (; inc > 0; inc /= 3) {
			for (int i = inc+1; i <= set.getSize(); i += inc) {
				Object t = set.get(i-1);
				int j = i;
				while ((j > inc) && (tool.compare(set.get(j-inc-1), t) == comp)) {
					set.set(j-1, set.get(j-inc-1));
					j -= inc;
				}
				set.set(j-1, t);
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

