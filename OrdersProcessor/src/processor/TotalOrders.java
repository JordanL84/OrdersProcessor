package processor;

import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.*;

public class TotalOrders {
	private Map<Item, Integer> allOrders;
	private PrintWriter printWriter;
	private Map<Integer, String> orderStrs;
	
	public TotalOrders(PrintWriter printWriter) {
		allOrders = new TreeMap<Item, Integer>();
		this.printWriter = printWriter;
		this.orderStrs = new TreeMap<>();
	}
	public void addStrs(Integer id, Map<Item, Integer> items) {
		/*printWriter.println("----- Order details for client with Id: "+id+" -----");
		double totalCost = 0;
		for (Item item : items.keySet()) {
			double cost = item.getCost() * items.get(item);
			printWriter.print("Item's name: " + item.getName());
			printWriter.printf(", Cost per item: $%.2f", item.getCost());
			printWriter.print(", Quantity: " + items.get(item));
			printWriter.printf(", Cost: $%.2f", cost);
			printWriter.println();
			totalCost += cost;
			
			boolean contains = false;
			for (Item xItem : allOrders.keySet()) {
				if (xItem.getName().equals(item.getName())) {
					allOrders.put(xItem, allOrders.get(xItem)+items.get(item));
					contains = true;
					break;
				}
			}
			if (!(contains))
				allOrders.put(item, items.get(item));;
		}
		printWriter.printf("Order Total: $%.2f", totalCost);
		printWriter.println();*/
		
		String str = "----- Order details for client with Id: "+id+" -----\n";
		double totalCost = 0;
		for (Item item : items.keySet()) {
			double cost = item.getCost() * items.get(item);
			str += ("Item's name: " + item.getName());
			str += ", Cost per item: " + NumberFormat.getCurrencyInstance().format(item.getCost());
			str+= ", Quantity: " + items.get(item);
			str += ", Cost: " + NumberFormat.getCurrencyInstance().format(cost) + "\n";
			totalCost += cost;
			
			synchronized(this) {
				boolean contains = false;
				for (Item xItem : allOrders.keySet()) {
					if (xItem.getName().equals(item.getName())) {
						allOrders.put(xItem, allOrders.get(xItem)+items.get(item));
						contains = true;
						break;
					}
				}
				if (!(contains))
					allOrders.put(item, items.get(item));
			}
		}
		str += "Order Total: " + NumberFormat.getCurrencyInstance().format(totalCost) + "\n";
		synchronized(this) {
			orderStrs.put(id, str);
		}
	}
	
	public void addToFile() {
		for (String str : orderStrs.values())
			printWriter.print(str);
	}
	
	public void addSummary() {
		printWriter.println("***** Summary of all orders *****");
		double totalCost = 0;
		for (Item item : allOrders.keySet()) {
			double cost = item.getCost() * allOrders.get(item);
			totalCost += cost;
			printWriter.print("Summary - Item's name: " + item.getName());
			printWriter.print(", Cost per item: " +
							  NumberFormat.getCurrencyInstance().format(item.getCost()));
			printWriter.print(", Number sold: " + allOrders.get(item));
			printWriter.println(", Item's Total: " + NumberFormat.getCurrencyInstance().format(cost));
		}
		String totalCostStr = NumberFormat.getCurrencyInstance().format(totalCost);
		printWriter.println("Summary Grand Total: " + totalCostStr);
	}
}
