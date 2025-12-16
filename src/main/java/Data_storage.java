
import java.util.List;

public interface Data_storage {
    // EXISTING METHODS
    void saveUsers(List<User> users);
    List<User> loadUsers();

    // --- NEW METHODS FOR MONEY ---
    void saveFunds(double amount);
    double loadFunds();
}