
public class Asset_Details {

    // ASSETS
    private double cashOnHand;
    private double goldGrams;
    private double silverGrams;

    // LIABILITIES (DEBT)
    private double liabilities;

    // CONSTRUCTOR
    public Asset_Details(double cashOnHand, double goldGrams, double silverGrams, double liabilities) {
        this.cashOnHand = cashOnHand;
        this.goldGrams = goldGrams;
        this.silverGrams = silverGrams;
        this.liabilities = liabilities;
    }

    // CALCULATES TOTAL NET WORTH (USED BY DONOR)
    public double getNetWorth() {
        // STATIC RATES FOR DEMO (You could make these dynamic later)
        double goldRatePerGram = 22000.0;
        double silverRatePerGram = 2500.0;

        double totalAssets = cashOnHand + (goldGrams * goldRatePerGram) + (silverGrams * silverRatePerGram);

        return totalAssets - liabilities; // Wealth minus Debt
    }

    // GETTERS & SETTERS (Standard Boilerplate)
    public double getCashOnHand() { return cashOnHand; }
    public void setCashOnHand(double cashOnHand) { this.cashOnHand = cashOnHand; }

    public double getGoldGrams() { return goldGrams; }
    public void setGoldGrams(double goldGrams) { this.goldGrams = goldGrams; }

    public double getSilverGrams() { return silverGrams; }
    public void setSilverGrams(double silverGrams) { this.silverGrams = silverGrams; }

    public double getLiabilities() { return liabilities; }
    public void setLiabilities(double liabilities) { this.liabilities = liabilities; }

    // SAVES DATA AS STRING: "5000:10:5:0" (Cash:Gold:Silver:Debt)
    @Override
    public String toString() {
        return cashOnHand + ":" + goldGrams + ":" + silverGrams + ":" + liabilities;
    }
}