package sorting;

import java.util.Random;
import dataStructures.Set;
import dataStructures.Heap;

public class SorterHeap extends Sorter {
	public void Sort(Set set, SortTool tool, boolean descending) {
//		int comp = (descending)? SortTool.COMP_GRTR : SortTool.COMP_LESS;
		Heap heap = new Heap(set, tool);
		int size = set.getSize();
		for (int i = 0; i < size; i++) set.set(size-i-1,heap.extract());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random randGen = new Random();
		Integer [] iArray = new Integer[100];
		for (int i = 0; i < 100; ++i) iArray[i] = new Integer(randGen.nextInt(999));
		for (int i = 0; i < 100; ++i) System.out.print(iArray[i] + " ");
		System.out.println();
		Set set = new Set(iArray);
		Sorter sort = new SorterHeap();
		sort.Sort(set,new SortToolInteger());
		for (int i = 0; i < 100; ++i) System.out.print(set.get(i) + " ");
		System.out.println();


	}

}

