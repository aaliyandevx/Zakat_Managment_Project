import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static List<User> users = new ArrayList<>();
    public static double globalZakatPool = 0.0;

    private static final Data_storage storage = new File_Manager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("SYSTEM: LOADING DATA...");

        // 1. LOAD USERS
        users = storage.loadUsers();

        // 2. LOAD MONEY (Critical for persistence)
        globalZakatPool = storage.loadFunds();

        System.out.println("SYSTEM: LOADED " + users.size() + " USERS.");
        // We still log this to the console on startup for debugging, but NOT in the menu
        System.out.println("SYSTEM: LOADED FUNDS: " + globalZakatPool + " PKR");

        // --- ADMIN CHECK ---
        boolean adminExists = false;
        for (User u : users) {
            if (u.getRole().equals(User.ROLE_ADMIN)) {
                adminExists = true;
                break;
            }
        }

        if (!adminExists) {
            System.out.println("SYSTEM: NO ADMIN FOUND. CREATING DEFAULT 'admin'.");
            String defaultHash = generateHash("1234");
            users.add(new Admin("ADM-001", "admin", defaultHash));
            storage.saveUsers(users);
        }

        // MAIN LOOP
        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("   SECURE ZAKAT & DONATION SYSTEM (OOP)   ");
            // Funds are hidden here for security
            System.out.println("========================================");
            System.out.println("1. LOGIN");
            System.out.println("2. REGISTER AS DONOR");
            System.out.println("3. REGISTER AS RECEIVER");
            System.out.println("4. GUEST MODE");
            System.out.println("5. EXIT & SAVE");
            System.out.print("SELECT OPTION: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1": handleLogin(); break;
                case "2": handleRegistration(User.ROLE_DONOR); break;
                case "3": handleRegistration(User.ROLE_RECEIVER); break;
                case "4":
                    Guest guest = new Guest();
                    guest.showDashboard();
                    break;
                case "5":
                    // 3. SAVE EVERYTHING ON EXIT
                    storage.saveUsers(users);
                    storage.saveFunds(globalZakatPool); // SAVES THE MONEY TO FILE

                    System.out.println("DATA SAVED. GOODBYE!");
                    running = false;
                    break;
                default: System.out.println("INVALID OPTION.");
            }
        }
    }

    private static void handleLogin() {
        System.out.print("ENTER USERNAME: ");
        String username = scanner.nextLine();
        System.out.print("ENTER PIN: ");
        String pin = scanner.nextLine();

        User foundUser = null;
        for (User u : users) {
            if (u.getUserName().equalsIgnoreCase(username)) {
                foundUser = u;
                break;
            }
        }

        if (foundUser != null && foundUser.login(pin)) {
            System.out.println("LOGIN SUCCESSFUL! WELCOME, " + foundUser.getUserName());
            foundUser.showDashboard();
            // AUTO-SAVE AFTER LOGOUT
            storage.saveUsers(users);
            storage.saveFunds(globalZakatPool);
        } else {
            System.out.println("ERROR: INVALID CREDENTIALS.");
        }
    }

    private static void handleRegistration(String role) {
        System.out.println("\n--- NEW " + role.toUpperCase() + " REGISTRATION ---");
        System.out.print("CHOOSE USERNAME: ");
        String name = scanner.nextLine();

        for (User u : users) {
            if (u.getUserName().equalsIgnoreCase(name)) {
                System.out.println("ERROR: USERNAME ALREADY TAKEN.");
                return;
            }
        }

        // --- NEW PIN VALIDATION LOOP ---
        String pin = "";
        while (true) {
            System.out.print("CHOOSE 4-DIGIT PIN: ");
            pin = scanner.nextLine();

            // REGEX: Checks if it is exactly 4 digits (0-9)
            if (pin.matches("\\d{4}")) {
                break; // Valid input, exit loop
            }
            System.out.println("ERROR: PIN MUST BE EXACTLY 4 NUMBERS (e.g., 1234).");
        }
        // -------------------------------

        String pinHash = generateHash(pin);
        String id = "U" + System.currentTimeMillis();

        if (role.equals(User.ROLE_DONOR)) {
            System.out.println("--- INITIAL ASSET DECLARATION ---");
            System.out.print("CASH ON HAND: ");
            double cash = Double.parseDouble(scanner.nextLine());
            System.out.print("GOLD (GRAMS): ");
            double gold = Double.parseDouble(scanner.nextLine());
            System.out.print("SILVER (GRAMS): ");
            double silver = Double.parseDouble(scanner.nextLine());
            System.out.print("DEBT: ");
            double debt = Double.parseDouble(scanner.nextLine());

            Asset_Details assets = new Asset_Details(cash, gold, silver, debt);
            users.add(new Donor(id, name, pinHash, assets));
            System.out.println("REGISTRATION SUCCESSFUL!");

        } else if (role.equals(User.ROLE_RECEIVER)) {
            users.add(new Receiver(id, name, pinHash));
            System.out.println("\n--- REGISTRATION SUCCESSFUL ---");
            System.out.println("NOTE: Your account is PENDING APPROVAL.");
        }
        storage.saveUsers(users);
        storage.saveFunds(globalZakatPool); // Save money too
    }

    private static String generateHash(String pin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(pin.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedhash);
        } catch (Exception e) {
            throw new RuntimeException("HASH ERROR");
        }
    }
}