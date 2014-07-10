package statChecker;

public class Summoner {

	private String name;
	private int id;
	
	public Summoner(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "Name: " + this.name + ", ID: " + this.id;
	}

}
