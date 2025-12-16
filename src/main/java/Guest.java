import java.util.Scanner;

public class Guest {

    // GUEST IS NOT A CHILD OF 'USER' BECAUSE IT DOESN'T HAVE AN ID OR PIN.
    // IT IS JUST A HELPER CLASS FOR THE MENU.

    public void showDashboard() {
        Scanner sc = new Scanner(System.in);
        boolean staying = true;

        while (staying) {
            System.out.println("\n=== GUEST PORTAL: WELCOME ===");
            System.out.println("1. VIEW LIVE SYSTEM FUNDS (TRANSPARENCY)");
            System.out.println("2. QUICK ZAKAT CALCULATOR (DEMO)");
            System.out.println("3. ABOUT THIS SYSTEM");
            System.out.println("4. BACK TO MAIN MENU");
            System.out.print("SELECT OPTION: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    viewSystemFunds();
                    break;
                case "2":
                    runDemoCalculator(sc);
                    break;
                case "3":
                    System.out.println("\n--- ABOUT US ---");
                    System.out.println("This is a Secure OOP-based Zakat Management System.");
                    System.out.println("We connect Donors directly with verified Receivers.");
                    System.out.println("100% of donations go to the Global Pool.");
                    break;
                case "4":
                    staying = false; // Exit the loop
                    break;
                default:
                    System.out.println("INVALID OPTION.");
            }
        }
    }

    // FEATURE 1: SHOW THE GLOBAL POOL FROM MAIN
    private void viewSystemFunds() {
        System.out.println("\n--- TRANSPARENCY REPORT ---");
        System.out.println("CURRENT AVAILABLE FUNDS: " + Main.globalZakatPool + " PKR");
        System.out.println("These funds are waiting to be distributed to approved Receivers.");
    }

    // FEATURE 2: A TEMPORARY CALCULATOR (DOES NOT SAVE DATA)
    private void runDemoCalculator(Scanner sc) {
        System.out.println("\n--- QUICK ZAKAT CALCULATOR (DEMO) ---");
        System.out.println("Note: This data will NOT be saved. Register to save history.");

        try {
            System.out.print("Enter Cash on Hand: ");
            double cash = Double.parseDouble(sc.nextLine());

            System.out.print("Enter Value of Gold/Silver: ");
            double goldSilver = Double.parseDouble(sc.nextLine());

            System.out.print("Enter Total Liabilities (Debts): ");
            double debt = Double.parseDouble(sc.nextLine());

            double netWorth = (cash + goldSilver) - debt;
            double threshold = 135000.0; // Hardcoded Nisab for Demo

            System.out.println("---------------------------------");
            System.out.println("YOUR NET WORTH: " + netWorth);

            if (netWorth >= threshold) {
                double zakat = netWorth * 0.025;
                System.out.println("STATUS: You are Sahib-e-Nisab (Eligible to Pay).");
                System.out.println("ESTIMATED ZAKAT: " + zakat + " PKR");
                System.out.println(">> PLEASE REGISTER AS A DONOR TO PAY NOW. <<");
            } else {
                System.out.println("STATUS: You are NOT required to pay Zakat.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Please enter valid numbers.");
        }
    }
}