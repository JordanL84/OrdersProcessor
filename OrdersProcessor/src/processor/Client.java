package processor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Client implements Runnable {
	
	private int id;
	private Map<Item, Integer> items;
	private TotalOrders totalOrders;
	private Map<String, Double> itemPrices;
	private String fileName;
	
	public Client(String fileName, Map<String, Double> itemPrices, TotalOrders totalOrders)  {
		this.totalOrders = totalOrders;
		items = new TreeMap<Item, Integer>();
		this.itemPrices = itemPrices;
		this.fileName = fileName;
	}
	
	public void run() {
		FileReader fileReader;
		try {
			fileReader = new FileReader(fileName);
			//BufferedReader bufferedReader = new BufferedReader(fileReader);
			Scanner fileScanner = new Scanner(fileReader);
			fileScanner.next();
			id = fileScanner.nextInt();
			
			while (fileScanner.hasNextLine()) {
				String itemName = fileScanner.next();
				fileScanner.next();
				boolean contains = false;
				for (Item item : items.keySet()) {
					if (item.getName().equals(itemName)) {
						items.put(item, items.get(item)+1);
						contains = true;
						break;
					}
				}
				if (!(contains)) {
					double cost = itemPrices.get(itemName);
					items.put(new Item(itemName, cost), 1);
				}
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
		}
		
		System.out.println("Reading order for clientwith id: " + id);
		totalOrders.addStrs(id, items);
	}	
}

