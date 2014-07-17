package statChecker;

import java.util.Comparator;

public class Champion implements Comparator{
	private int id;
	private String name;
	
	public Champion(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "Name: " + this.name + ", ID: " + this.id;
	}

	public int compare(Champion c1, Champion c2) {
		int result = String.CASE_INSENSITIVE_ORDER.compare(c1.getName(), c2.getName());
		return result;
	}

	@Override
	public int compare(Object o1, Object o2) {
		Champion c1, c2;
		c1 = (Champion)o1;
		c2 = (Champion)o2;
		int result = String.CASE_INSENSITIVE_ORDER.compare(c1.getName(), c2.getName());
		return result;
	}
	
}
