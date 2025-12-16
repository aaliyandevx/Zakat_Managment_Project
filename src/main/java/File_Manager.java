import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class File_Manager implements Data_storage {

    private static final String FILE_NAME = "users.txt";
    // FIX: Using your existing file instead of creating 'funds.txt'
    private static final String FUNDS_FILE = "users_data.txt";

    // --- 1. USER HANDLING ---
    @Override
    public void saveUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User u : users) {
                String line = u.getId() + "," + u.getUserName() + "," + u.getPinHash() + "," + u.getRole();

                if (u instanceof Donor) {
                    Donor d = (Donor) u;
                    Asset_Details a = d.getAssetDetails();
                    line += "," + a.getCashOnHand() + "," + a.getGoldGrams() + "," + a.getSilverGrams() + "," + a.getLiabilities();
                } else if (u instanceof Receiver) {
                    Receiver r = (Receiver) u;
                    line += "," + r.isEligible() + "," + r.getNeedsAmount();
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("ERROR: COULD NOT SAVE USERS.");
        }
    }

    @Override
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                String id = parts[0];
                String name = parts[1];
                String hash = parts[2];
                String role = parts[3];

                if (role.equals(User.ROLE_ADMIN)) {
                    users.add(new Admin(id, name, hash));
                } else if (role.equals(User.ROLE_DONOR)) {
                    if (parts.length >= 8) {
                        double cash = Double.parseDouble(parts[4]);
                        double gold = Double.parseDouble(parts[5]);
                        double silver = Double.parseDouble(parts[6]);
                        double liab = Double.parseDouble(parts[7]);
                        users.add(new Donor(id, name, hash, new Asset_Details(cash, gold, silver, liab)));
                    }
                } else if (role.equals(User.ROLE_RECEIVER)) {
                    Receiver r = new Receiver(id, name, hash);
                    if (parts.length >= 6) {
                        r.setEligible(Boolean.parseBoolean(parts[4]));
                        r.setNeedsAmount(Double.parseDouble(parts[5]));
                    }
                    users.add(r);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR LOADING USERS.");
        }
        return users;
    }

    // --- 2. FUNDS HANDLING (Using users_data.txt) ---

    @Override
    public void saveFunds(double amount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FUNDS_FILE))) {
            writer.write(String.valueOf(amount));
        } catch (IOException e) {
            System.out.println("ERROR: COULD NOT SAVE FUNDS.");
        }
    }

    @Override
    public double loadFunds() {
        File file = new File(FUNDS_FILE);
        if (!file.exists()) return 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                return Double.parseDouble(line);
            }
        } catch (Exception e) {
            System.out.println("ERROR LOADING FUNDS.");
        }
        return 0.0;
    }
}