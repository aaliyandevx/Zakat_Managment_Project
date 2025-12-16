import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Donor extends User implements Zakat_Calculator {

    private Asset_Details assetDetails;
    private List<Transaction> transactionHistory = new ArrayList<>();

    // MARKET RATES FOR AUTO-LIQUIDATION (Must match your Asset_Details logic)
    private static final double PRICE_PER_GRAM_GOLD = 22000.0;
    private static final double PRICE_PER_GRAM_SILVER = 300.0;

    public Donor(String id, String userName, String pinHash, Asset_Details assetDetails) {
        super(id, userName, pinHash, User.ROLE_DONOR);
        this.assetDetails = assetDetails;
    }

    @Override
    public void showDashboard() {
        Scanner sc = new Scanner(System.in);
        boolean staying = true;

        while (staying) {
            System.out.println("\n=== DONOR DASHBOARD: " + getUserName().toUpperCase() + " ===");
            System.out.println("NET WORTH: " + assetDetails.getNetWorth() + " PKR");
            System.out.println("-------------------------");
            System.out.println("1. VIEW ASSETS");
            System.out.println("2. CHECK ELIGIBILITY & PAY ZAKAT");
            System.out.println("3. MAKE A VOLUNTARY DONATION");
            System.out.println("4. VIEW DONATION HISTORY");
            System.out.println("5. LOGOUT");
            System.out.print("SELECT OPTION: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("\n--- ASSET DETAILS ---");
                    System.out.println("CASH: " + assetDetails.getCashOnHand());
                    System.out.println("GOLD: " + assetDetails.getGoldGrams() + "g");
                    System.out.println("SILVER: " + assetDetails.getSilverGrams() + "g");
                    System.out.println("LIABILITIES: -" + assetDetails.getLiabilities());
                    break;
                case "2":
                    performZakatCheck(sc);
                    break;
                case "3":
                    System.out.print("\nENTER DONATION AMOUNT: ");
                    try {
                        double amount = Double.parseDouble(sc.nextLine());
                        makeDonation(amount);
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: INVALID NUMBER.");
                    }
                    break;
                case "4":
                    viewHistory();
                    break;
                case "5":
                    staying = false;
                    logout();
                    break;
                default:
                    System.out.println("INVALID OPTION.");
            }
        }
    }

    private void performZakatCheck(Scanner sc) {
        double netWorth = assetDetails.getNetWorth();
        double zakatDue = calculatePotentialZakat();

        System.out.println("\n=========================================");
        System.out.println("        ZAKAT ELIGIBILITY REPORT         ");
        System.out.println("=========================================");
        System.out.println("CURRENT NET WORTH: " + netWorth + " PKR");
        System.out.println("NISAB THRESHOLD:   " + NISAB_THRESHOLD + " PKR");

        if (zakatDue > 0) {
            System.out.println("STATUS: [ ELIGIBLE ] (Sahib-e-Nisab)");
            System.out.println("MANDATORY ZAKAT DUE: " + zakatDue + " PKR");

            System.out.println("\nWOULD YOU LIKE TO PAY THIS NOW? (Y/N)");
            System.out.print("> ");
            if (sc.nextLine().equalsIgnoreCase("Y")) {
                makeDonation(zakatDue);
            }
        } else {
            System.out.println("STATUS: [ NOT ELIGIBLE ]");
        }
    }

    @Override
    public double calculatePotentialZakat() {
        if (assetDetails.getNetWorth() >= NISAB_THRESHOLD) {
            return assetDetails.getNetWorth() * 0.025;
        }
        return 0.0;
    }

    // --- THE MAGIC "NO HASSLE" METHOD ---
    private void makeDonation(double amount) {
        if (amount <= 0) {
            System.out.println("ERROR: INVALID AMOUNT.");
            return;
        }

        // CHECK 1: DO WE HAVE CASH?
        if (amount > assetDetails.getCashOnHand()) {
            System.out.println("⚠️ INSUFFICIENT CASH. ATTEMPTING AUTO-LIQUIDATION...");

            double deficit = amount - assetDetails.getCashOnHand();

            // CHECK 2: ARE WE RICH ENOUGH IN TOTAL?
            if (amount > assetDetails.getNetWorth()) {
                System.out.println("CRITICAL ERROR: YOU ARE BANKRUPT. EVEN SELLING ASSETS WON'T COVER THIS.");
                return;
            }

            // --- AUTO LIQUIDATE GOLD ---
            double goldValue = assetDetails.getGoldGrams() * PRICE_PER_GRAM_GOLD;
            if (deficit > 0 && goldValue > 0) {
                double gramsToSell = Math.min(assetDetails.getGoldGrams(), deficit / PRICE_PER_GRAM_GOLD);
                if (gramsToSell < 1 && deficit < PRICE_PER_GRAM_GOLD) gramsToSell = 0.1; // Min sell

                double cashGenerated = gramsToSell * PRICE_PER_GRAM_GOLD;

                // UPDATE ASSETS
                assetDetails.setGoldGrams(assetDetails.getGoldGrams() - gramsToSell);
                assetDetails.setCashOnHand(assetDetails.getCashOnHand() + cashGenerated);

                System.out.printf(" >> SOLD %.2f GRAMS OF GOLD -> +%.2f CASH GENERATED.\n", gramsToSell, cashGenerated);
                deficit -= cashGenerated;
            }

            // (You could add Silver logic here too if Gold wasn't enough)

            // FINAL CHECK AFTER SELLING
            if (assetDetails.getCashOnHand() < amount) {
                System.out.println("ERROR: LIQUIDATION FAILED TO COVER AMOUNT.");
                return;
            }
        }

        // PAY THE DONATION
        assetDetails.setCashOnHand(assetDetails.getCashOnHand() - amount);
        Main.globalZakatPool += amount;

        Transaction txn = new Transaction("TXN" + System.currentTimeMillis(), amount, "ZAKAT PAID");
        addTransaction(txn);

        System.out.println("SUCCESS: PAYMENT OF " + amount + " PKR PROCESSED.");
        System.out.println("REMAINING CASH: " + assetDetails.getCashOnHand());
    }

    public void addTransaction(Transaction t) {
        transactionHistory.add(t);
    }

    public void viewHistory() {
        if (transactionHistory.isEmpty()) System.out.println("NO HISTORY.");
        for (Transaction t : transactionHistory) System.out.println(t.toString());
    }

    @Override
    public void logout() { System.out.println("LOGGING OUT..."); }
    public Asset_Details getAssetDetails() { return assetDetails; }
}