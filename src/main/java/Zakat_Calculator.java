
public interface Zakat_Calculator {
    // CONSTANT: MINIMUM WEALTH REQUIRED (GOLD STANDARD)
    // 87.48 grams of gold * approx rate (e.g. 22000 PKR/gram)
    double NISAB_THRESHOLD = 87.48 * 22000;

    // ANY CLASS IMPLEMENTING THIS MUST DEFINE HOW TO CALCULATE ZAKAT
    double calculatePotentialZakat();
}