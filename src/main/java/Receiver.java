import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Receiver extends User {

    private boolean isEligible = false;
    private double needsAmount = 0.0;
    private List<String> receivedHistory = new ArrayList<>();

    public Receiver(String id, String userName, String pinHash) {
        super(id, userName, pinHash, User.ROLE_RECEIVER);
    }

    @Override
    public void showDashboard() {
        Scanner sc = new Scanner(System.in);
        boolean staying = true;

        while (staying) {
            String status = isEligible ? "APPROVED" : "PENDING APPROVAL";
            System.out.println("\n=== RECEIVER DASHBOARD: " + getUserName().toUpperCase() + " ===");
            System.out.println("STATUS: " + status);
            System.out.println("CURRENT REQUEST: $" + needsAmount);
            System.out.println("-------------------------");
            System.out.println("1. REQUEST AID");
            System.out.println("2. CHECK STATUS");
            System.out.println("3. VIEW RECEIVED FUNDS");
            System.out.println("4. UPDATE PROFILE");
            System.out.println("5. LOGOUT");
            System.out.print("SELECT OPTION: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1": requestAid(sc); break;
                case "2": System.out.println("STATUS: " + status); break;
                case "3": viewReceivedFunds(); break;
                case "4": updateProfile(sc); break;
                case "5": staying = false; logout(); break;
                default: System.out.println("INVALID OPTION.");
            }
        }
    }

    private void viewReceivedFunds() {
        System.out.println("\n--- FUNDS RECEIVED HISTORY ---");
        if (receivedHistory.isEmpty()) {
            System.out.println("NO FUNDS RECEIVED YET.");
        } else {
            for (String record : receivedHistory) {
                System.out.println(record);
            }
        }
    }

    public void acceptPayment(double amount) {
        if (amount > 0) {
            this.needsAmount -= amount;
            if (this.needsAmount < 0) this.needsAmount = 0;
            receivedHistory.add("RECEIVED: $" + amount + " | Date: " + java.time.LocalDate.now());
        }
    }

    private void requestAid(Scanner sc) {
        if (!isEligible) {
            System.out.println("\nERROR: You must be APPROVED by an Admin first.");
            return;
        }
        System.out.print("ENTER AMOUNT NEEDED: ");
        try {
            double amount = Double.parseDouble(sc.nextLine());
            this.needsAmount = amount;
            System.out.println("SUCCESS: AID REQUEST FOR $" + amount + " SUBMITTED.");
        } catch (Exception e) {
            System.out.println("ERROR: INVALID NUMBER.");
        }
    }

    // --- FIX: CHANGED FROM PRIVATE TO PUBLIC ---
    @Override
    public void updateProfile(Scanner sc) {
        System.out.print("Enter new username: ");
        String newName = sc.nextLine();
        if (!newName.isEmpty()) setUserName(newName);
        System.out.println("PROFILE UPDATED.");
    }

    @Override
    public void logout() {
        System.out.println("LOGGING OUT RECEIVER...");
    }

    // Getters/Setters
    public boolean isEligible() { return isEligible; }
    public void setEligible(boolean eligible) { isEligible = eligible; }
    public double getNeedsAmount() { return needsAmount; }
    public void setNeedsAmount(double needsAmount) { this.needsAmount = needsAmount; }
}