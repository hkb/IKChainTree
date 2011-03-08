package sorting;
import java.util.Random;

import dataStructures.Set;
public class SorterInsertion extends Sorter {
	public void Sort(Set set, SortTool tool, boolean descending) {
		int comp = (descending)? SortTool.COMP_LESS : SortTool.COMP_GRTR;
		for (int i = 1; i < set.getSize(); ++i) {
			Object t = set.get(i);
			int j = i;
			while ((j > 0) && (tool.compare(set.get(j-1), t) == comp)) {
				set.set(j,set.get(j-1));
				--j;
			}
			set.set(j, t);
		}
	}

	public static void main(String[] args) {
		Random randGen = new Random();
		Integer [] iArray = new Integer[10];
		for (int i = 0; i < 10; ++i) iArray[i] = new Integer(randGen.nextInt(99));
		for (int i = 0; i < 10; ++i) System.out.print(iArray[i] + " ");
		System.out.println();
		Set set = new Set(iArray);
		Sorter sort = new SorterInsertion();
		sort.Sort(set,new SortToolInteger(), true);
		for (int i = 0; i < 10; ++i) System.out.print((Integer)set.get(i) + " ");
		System.out.println();
	}
}
