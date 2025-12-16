import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;
import java.util.Objects;

public abstract class User {
    // STANDARDIZED ROLE CONSTANTS
    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_DONOR = "Donor";
    public static final String ROLE_RECEIVER = "Receiver";
    public static final String ROLE_GUEST = "Guest";

    private final String id;
    private String userName;
    private String pinHash;
    private final String role;

    protected User(String id, String userName, String pinHash, String role) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID CANNOT BE EMPTY");
        }

        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("USERNAME CANNOT BE EMPTY");
        }

        this.id = id;
        this.userName = userName;
        this.pinHash = pinHash;
        this.role = role;
    }

    // LOGIN USING BUILT-IN JAVA SHA-256
    public boolean login(String pin) {
        String inputHash = hashPin(pin);
        return this.pinHash.equals(inputHash);
    }

    public void updateProfile(Scanner sc) {
        System.out.print("Enter new username (leave blank to keep current): ");
        String newName = sc.nextLine();
        if (!newName.isBlank()) {
            setUserName(newName);
            System.out.println("USERNAME UPDATED SUCCESSFULLY.");
        }

        System.out.print("Enter new PIN (leave blank to keep current): ");
        String newPin = sc.nextLine();
        if (!newPin.isBlank()) {
            updatePin(newPin);
            System.out.println("PIN UPDATED SUCCESSFULLY.");
        }
    }

    protected void setUserName(String newName) {
        this.userName = newName;
    }

    protected void updatePin(String newPin) {
        this.pinHash = hashPin(newPin);
    }

    // NEW HELPER: SHA-256 HASHING (BUILT-IN)
    private String hashPin(String pin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(pin.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedhash);
        } catch (Exception e) {
            throw new RuntimeException("ERROR: HASHING ALGORITHM NOT FOUND");
        }
    }

    public abstract void showDashboard();
    public abstract void logout();

    public String getId() { return id; }
    public String getUserName() { return userName; }
    public String getRole() { return role; }
    public String getPinHash() { return pinHash; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public String toString() {
        return id + "," + userName + "," + pinHash + "," + role;
    }
}