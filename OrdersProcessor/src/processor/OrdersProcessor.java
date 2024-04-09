package processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class OrdersProcessor {
	public static void main(String[] args) throws IOException {
		
		Map<String, Double> itemPrices = new HashMap<String, Double>();
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter items data file name: ");
		String itemsFileName = scan.nextLine();
		FileReader itemsFileReader = new FileReader(itemsFileName);
		BufferedReader itemsBufferedReader = new BufferedReader(itemsFileReader);
		Scanner itemsFileScanner = new Scanner(itemsBufferedReader);
		
		while (itemsFileScanner.hasNextLine()) {
			String itemName = itemsFileScanner.next();
			double cost = itemsFileScanner.nextDouble();
			itemPrices.put(itemName, cost);
		}
		System.out.println("Enter 'y' for multiple threads, any other character otherwise: ");
		String threadOption = scan.nextLine();
		System.out.println("Enter number of orders to process: ");
		int ordersNum = scan.nextInt();
		Client[] clients = new Client[ordersNum];
		scan.nextLine();
		System.out.println("Enter order's base filename: ");
		String baseFileName = scan.nextLine();
		System.out.println("Enter result's filename: ");
		String resultsFileName = scan.nextLine();
		
		long startTime = System.currentTimeMillis();
		FileWriter fileWriter = new FileWriter(resultsFileName, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		
		TotalOrders totalOrders = new TotalOrders(printWriter);
		for (int i = 1; i <= ordersNum; i++) {
			clients[i-1] = new Client(baseFileName + i + ".txt", itemPrices, totalOrders);
		}
		if (threadOption.equals("y")) {
			Thread[] threads = new Thread[ordersNum];
			for (int i = 1; i <= ordersNum; i++) {
				threads[i-1] = new Thread(clients[i-1]);
				threads[i-1].start();
			}
			
			for (int i = 1; i <= ordersNum; i++) {
				try {
					threads[i-1].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			for (int i = 1; i <= ordersNum; i++) {
				clients[i-1].run();
			}
		}
		totalOrders.addToFile();
		totalOrders.addSummary();
		scan.close();
		itemsFileScanner.close();
		printWriter.close();
		long endTime = System.currentTimeMillis();
		System.out.println("Processing time (msec): " + (endTime-startTime));
		System.out.println("Results can be found in the file: " + resultsFileName);
	}
}
