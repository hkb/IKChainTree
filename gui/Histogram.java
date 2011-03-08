package gui;

import misc.*;

public class Histogram {

	double min;
	double max;
	double delta;
	int size;
	int index;
	int [] frequency;
	
	public Histogram(double min, double max, double delta) {
		this.min = min;
		this.max = max;
		this.delta = delta;
		size =  new Double((max - min)/delta).intValue();
		frequency = new int[size+2];
	}
	
	public void inc(double value) {
		if (value < min) frequency[0] = frequency[0] + 1;
		else {
			if (value >= max) frequency[size+1] = frequency[size+1] + 1;
			else {
				index = new Double((value - min)/delta).intValue();
				frequency[index+1] = frequency[index+1] + 1;
			}
		}
	}
	public void print() {
		System.out.println("]-oo," + min + "[: " + frequency[0]);
		for (int i = 0; i < size; i++) System.out.println("["+ Functions.toString(min+i*delta) + "," + Functions.toString(min+(i+1)*delta) + "]: " + frequency[i]);
		System.out.println("[" + max + ",+oo[: "  + frequency[size+1]);
	}
}
