import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        InventoryService inventory = new InventoryService();
        UsageService usage = new UsageService();
        WasteService waste = new WasteService();
        DemandService demand = new DemandService();
        SpoilageService spoilage = new SpoilageService();

        while (true) {

            System.out.println("\n===== Food Spoilage Tracking System =====");
            System.out.println("1. Add Food Item");
            System.out.println("2. Consume Food (FIFO)");
            System.out.println("3. Log Daily Usage");
            System.out.println("4. Check Expiry Alerts");
            System.out.println("5. Log Waste");
            System.out.println("6. Estimate Demand");
            System.out.println("7. Calculate Spoilage Risk");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {

                case 1:
                    System.out.print("Enter food name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter quantity: ");
                    int qty = sc.nextInt();

                    System.out.print("Enter expiry date (YYYY-MM-DD): ");
                    LocalDate expiry = LocalDate.parse(sc.next());

                    inventory.addItem(name, qty, expiry);
                    System.out.println("✅ Food item added successfully");
                    break;

                case 2:
                    System.out.print("Enter food name: ");
                    String consumeName = sc.nextLine();

                    System.out.print("Enter quantity to consume: ");
                    int consumeQty = sc.nextInt();

                    inventory.consumeItem(consumeName, consumeQty);
                    System.out.println("✅ Food consumed using FIFO");
                    break;

                case 3:
                    System.out.print("Enter food name: ");
                    String usageName = sc.nextLine();

                    System.out.print("Enter used quantity: ");
                    int usedQty = sc.nextInt();

                    usage.logUsage(usageName, usedQty);
                    System.out.println("✅ Daily usage logged");
                    break;

                case 4:
                    inventory.expiryAlert();
                    break;

                case 5:
                    System.out.print("Enter food name: ");
                    String wasteName = sc.nextLine();

                    System.out.print("Enter wasted quantity: ");
                    int wasteQty = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter reason: ");
                    String reason = sc.nextLine();

                    waste.logWaste(wasteName, wasteQty, reason);
                    System.out.println("✅ Waste logged successfully");
                    break;

                case 6:
                    System.out.print("Enter food name: ");
                    String demandName = sc.nextLine();

                    double avg = demand.estimateDemand(demandName);
                    System.out.println("📊 Estimated daily demand: " + avg);
                    break;

                case 7:
                    System.out.print("Enter entry date (YYYY-MM-DD): ");
                    LocalDate entryDate = LocalDate.parse(sc.next());

                    System.out.print("Enter expiry date (YYYY-MM-DD): ");
                    LocalDate expDate = LocalDate.parse(sc.next());

                    double risk = spoilage.calculateRisk(entryDate, expDate);
                    System.out.println("⚠ Spoilage risk: " + risk);
                    break;

                case 8:
                    System.out.println("👋 Exiting system...");
                    sc.close();
                    return;

                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }
}
