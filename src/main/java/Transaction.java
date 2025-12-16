import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String txnId;
    private double amount;
    private String type;
    private String timestamp;

    public Transaction(String txnId, double amount, String type) {
        this.txnId = txnId;
        this.amount = amount;
        this.type = type;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.timestamp = dtf.format(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + type + ": " + amount + " PKR";
    }
}