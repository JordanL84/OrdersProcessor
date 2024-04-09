package processor;

public class Item implements Comparable<Item> {
	private String name;
	private double cost;
	
	public Item(String name, double cost) {
		this.name = name;
		this.cost = cost;
	}
	
	public String getName() {
		return name;
	}
	
	public double getCost() {
		return cost;
	}
	
	public int compareTo(Item other) {
		return name.compareTo(other.getName());
	}
}