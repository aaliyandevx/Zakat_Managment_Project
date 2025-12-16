# 🌙 Secure Zakat & Donation System (OOP Project)

A robust, object-oriented Java application for managing Zakat collection, distribution, and asset management. This system features **File Persistence**, **Role-Based Security**, and **Smart Asset Liquidation**.

---

## 🚀 Getting Started

### 1. Prerequisites
* **Java JDK 17+** (Recommended JDK 21 or 24).
* **IntelliJ IDEA** (Preferred).
* **Maven** (Dependencies are handled in `pom.xml`).

### 2. Installation
1.  **Clone/Download** this repository.
2.  Open the project folder in IntelliJ IDEA.
3.  **Load Maven Changes:**
    * Click the small "M" icon in the top-right of `pom.xml` to download dependencies.
4.  **Run the App:**
    * Navigate to `src/main/java/com/zakat/management/Main.java`.
    * Click the Green Play Button (▶).

---

## 🧪 How to Test (Step-by-Step Guide)

Follow this script to see all OOP features in action.

### Scenario 1: The "Fresh Install" & Admin Login
*The system detects no users and automatically creates a default Admin.*

1.  **Run the App.**
2.  Select **1. LOGIN**.
3.  **Username:** `admin`
4.  **PIN:** `1234`
5.  *Result:* You will see the **Admin Dashboard** and current Vault Balance (Starts at 0.0 PKR).
6.  **Logout.**

### Scenario 2: The "Smart Donor" (Auto-Liquidation)
*Test the system's ability to automatically sell Gold if the user has no Cash.*

1.  Select **2. REGISTER AS DONOR**.
2.  **Username:** `Adil`
3.  **PIN:** `7777` (Must be 4 digits!).
4.  **Assets Declaration:**
    * **Cash:** `100` (Very low cash).
    * **Gold:** `800` (Rich in assets).
    * **Silver/Debt:** `0`.
5.  **Login as Adil** (`Adil` / `7777`).
6.  Select **2. CHECK ELIGIBILITY & PAY**.
    * *System notices you are "Sahib-e-Nisab" (Rich).*
    * *System calculates Zakat (approx 440,000 PKR).*
7.  **Confirm Payment (Type `Y`).**
    * *Result:* The system will **Auto-Liquidate** ~20g of your Gold to get the Cash, then pay the Zakat.
    * *Check Console:* "SOLD 20.25 GRAMS OF GOLD -> CASH GENERATED."

### Scenario 3: The Receiver & Approval Process
*Receivers cannot get money until an Admin approves them.*

1.  **Logout** and select **3. REGISTER AS RECEIVER**.
2.  **Username:** `Bilal` | **PIN:** `9999`.
3.  **Login as Bilal.**
    * *Result:* Dashboard shows **[ STATUS: PENDING ]**. You cannot request funds yet.
4.  **Login as Admin** (`admin` / `1234`).
5.  Select **2. APPROVE NEW ACCOUNTS**.
    * Enter Bilal's ID (copied from the list).
    * *Result:* "ACCOUNT APPROVED."

### Scenario 4: Distributing Funds
*Now that the Vault has money (from Adil) and Bilal is approved.*

1.  **Login as Bilal.**
2.  Select **2. REQUEST FUNDS** -> Enter `50000`.
3.  **Login as Admin.**
4.  Select **3. VIEW & FUND REQUESTS**.
    * You will see Bilal's request.
5.  Enter Bilal's ID to pay him.
    * *Result:* "TRANSFERRED 50000 PKR TO BILAL."

---

## 📂 Project Structure

* **`Main.java`**: The central controller and menu system.
* **`User.java`**: Parent class (Inheritance).
* **`Donor.java`**: Handles Asset logic and Zakat Calculation (Polymorphism).
* **`Receiver.java`**: Handles eligibility status.
* **`Admin.java`**: System monitoring and approvals.
* **`File_Manager.java`**: Saves data to `users.txt` so data isn't lost on exit.
* **`users.txt`**: Database of all users (Auto-generated).
* **`users_data.txt`**: Stores the global Zakat Pool balance.

---

## 🛡️ Security Features
* **PIN Hashing:** User PINs are not stored as plain text (SHA-256).
* **Validation:** Input loops prevent invalid data entry (e.g., non-numeric PINs).
* **Role Separation:** Donors cannot see Admin panels; Receivers cannot see Donors.
