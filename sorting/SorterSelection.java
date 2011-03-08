package sorting;
import dataStructures.Set;
import java.util.Random;

public class SorterSelection extends Sorter {
	public void Sort(Set set, SortTool tool, boolean descending) {
		int comp = (descending)? SortTool.COMP_GRTR : SortTool.COMP_LESS;
		int i, j, min;
		Object t;
		int size = set.getSize();
		for (i = 0; i < size; ++i) {
			min = i; 
			t = set.get(min);
			for (j = i+1; j < size; ++j)  
				if (tool.compare(set.get(j), t) == comp) {
					min = j;
					t = set.get(min);
				}
			set.set(min, set.get(i));
			set.set(i, t);
		}
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Random randGen = new Random();
		Integer [] iArray = new Integer[10];
		for (int i = 0; i < 10; ++i) iArray[i] = new Integer(randGen.nextInt(99));
		for (int i = 0; i < 10; ++i) System.out.print(iArray[i] + " ");
		System.out.println();
		Set set = new Set(iArray);
		Sorter sort = new SorterSelection();
		sort.Sort(set,new SortToolInteger(), true);
		for (int i = 0; i < 10; ++i) System.out.print((Integer)set.get(i) + " ");
		System.out.println();
	}

}
