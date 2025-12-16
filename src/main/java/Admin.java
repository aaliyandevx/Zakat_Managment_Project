import java.util.Scanner;

public class Admin extends User {

    public Admin(String id, String userName, String pinHash) {
        super(id, userName, pinHash, User.ROLE_ADMIN);
    }

    @Override
    public void showDashboard() {
        Scanner sc = new Scanner(System.in);
        boolean staying = true;

        while (staying) {
            System.out.println("\n=== ADMIN CONTROL PANEL: " + getUserName().toUpperCase() + " ===");
            System.out.println("SYSTEM FUNDS AVAILABLE: " + Main.globalZakatPool + " PKR");
            System.out.println("--------------------------------");
            System.out.println("1. VIEW ALL USERS (SYSTEM AUDIT)");
            System.out.println("2. APPROVE NEW ACCOUNTS (VERIFY IDENTITY)");
            System.out.println("3. VIEW & FUND REQUESTS (DISTRIBUTE ZAKAT)");
            System.out.println("4. DELETE A USER");
            System.out.println("5. LOGOUT");
            System.out.print("ENTER COMMAND: ");

            String cmd = sc.nextLine();

            switch (cmd) {
                case "1": viewAllUsers(); break;
                case "2": approveReceiver(sc); break;
                case "3": distributeFunds(sc); break; // <--- CALLS THE FIXED METHOD
                case "4": deleteUser(sc); break;
                case "5":
                    logout();
                    staying = false;
                    break;
                default: System.out.println("INVALID COMMAND.");
            }
        }
    }

    @Override
    public void logout() {
        System.out.println("ADMIN LOGGING OUT...");
    }

    // --- AUTHORITY 1: VIEW ALL USERS ---
    private void viewAllUsers() {
        System.out.println("\n--- SYSTEM USER LOG ---");
        if (Main.users.isEmpty()) {
            System.out.println("NO USERS FOUND.");
        } else {
            for (User u : Main.users) {
                System.out.println(u.toString());
            }
        }
    }

    // --- AUTHORITY 2: APPROVE ACCOUNTS ---
    private void approveReceiver(Scanner sc) {
        boolean pendingFound = false;
        System.out.println("\n--- PENDING ACCOUNT APPROVALS ---");
        for (User u : Main.users) {
            if (u instanceof Receiver) {
                Receiver r = (Receiver) u;
                if (!r.isEligible()) {
                    System.out.println("PENDING: " + r.getUserName() + " (ID: " + r.getId() + ")");
                    pendingFound = true;
                }
            }
        }

        if (!pendingFound) {
            System.out.println(">>> NO PENDING ACCOUNT APPROVALS. <<<");
            return;
        }

        System.out.print("ENTER ID TO APPROVE: ");
        String targetId = sc.nextLine();

        for (User u : Main.users) {
            if (u.getId().equals(targetId) && u instanceof Receiver) {
                ((Receiver) u).setEligible(true);
                System.out.println("SUCCESS: ACCOUNT APPROVED.");
                return;
            }
        }
        System.out.println("ERROR: ID NOT FOUND.");
    }

    // --- AUTHORITY 3: DISTRIBUTE FUNDS (FIXED) ---
    private void distributeFunds(Scanner sc) {
        System.out.println("\n--- ACTIVE FUNDING REQUESTS ---");
        boolean requestFound = false;

        // 1. LIST REQUESTS
        for (User u : Main.users) {
            if (u instanceof Receiver) {
                Receiver r = (Receiver) u;
                // Only show if Approved AND Needs Money
                if (r.isEligible() && r.getNeedsAmount() > 0) {
                    System.out.println(" > REQUEST: " + r.getUserName() + " | NEEDS: " + r.getNeedsAmount() + " PKR | ID: " + r.getId());
                    requestFound = true;
                }
            }
        }

        // 2. THE FIX: EXIT IMMEDIATELY IF NO REQUESTS
        if (!requestFound) {
            System.out.println(">>> NO PENDING REQUESTS FOUND. <<<");
            return; // Stops here. Doesn't ask for ID.
        }

        // 3. CHECK VAULT BALANCE
        if (Main.globalZakatPool <= 0) {
            System.out.println("\nCRITICAL ERROR: VAULT IS EMPTY (0 PKR). Cannot distribute funds.");
            return;
        }

        // 4. ASK FOR ID (Only happens if requests exist AND money exists)
        System.out.print("\nENTER RECEIVER ID TO PAY: ");
        String targetId = sc.nextLine();

        for (User u : Main.users) {
            if (u.getId().equals(targetId) && u instanceof Receiver) {
                Receiver r = (Receiver) u;

                // Double check logical safety
                if(r.getNeedsAmount() <= 0) {
                    System.out.println("ERROR: This user does not need funds.");
                    return;
                }

                double amountToPay = 0.0;

                // Smart Calculation: Pay full amount OR whatever is left in vault
                if (Main.globalZakatPool >= r.getNeedsAmount()) {
                    amountToPay = r.getNeedsAmount();
                } else {
                    amountToPay = Main.globalZakatPool;
                }

                // Execute Transaction
                Main.globalZakatPool -= amountToPay;
                r.acceptPayment(amountToPay);

                System.out.println("SUCCESS: TRANSFERRED " + amountToPay + " PKR TO " + r.getUserName());

                if (r.getNeedsAmount() > 0) {
                    System.out.println("NOTE: Receiver still needs " + r.getNeedsAmount() + " PKR.");
                } else {
                    System.out.println("NOTE: Request fully satisfied.");
                }
                return;
            }
        }
        System.out.println("ERROR: USER ID NOT FOUND.");
    }

    // --- AUTHORITY 4: DELETE USER ---
    private void deleteUser(Scanner sc) {
        System.out.print("ENTER USER ID TO REMOVE: ");
        String targetId = sc.nextLine();

        boolean removed = Main.users.removeIf(u -> u.getId().equals(targetId) && !u.getRole().equals(User.ROLE_ADMIN));

        if (removed) {
            System.out.println("SUCCESS: USER REMOVED.");
        } else {
            System.out.println("ERROR: USER NOT FOUND OR CANNOT DELETE ADMIN.");
        }
    }
}