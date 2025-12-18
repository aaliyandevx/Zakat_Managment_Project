
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame() {
        // 1. SETUP THEME PROPERTIES BEFORE COMPONENTS
        UIManager.put("Button.arc", 15);
        UIManager.put("Component.arc", 15);
        UIManager.put("TextComponent.arc", 15);
        UIManager.put("Component.focusWidth", 1);
        UIManager.put("Button.innerFocusWidth", 0);

        // 2. WINDOW CONFIG
        setTitle("Zakat Portal - Secure Access");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(18, 18, 18)); // Pure Dark Background
        setLayout(new GridBagLayout());

        // 3. THE CENTER CARD
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(30, 30, 30)); // Slightly lighter dark for the card
        card.setPreferredSize(new Dimension(380, 480));
        card.setBorder(new EmptyBorder(40, 40, 40, 40));

        // 4. LOGO & LABELS
        JLabel iconLabel = new JLabel("🌙"); // Moon icon for Zakat/Dark theme
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Zakat Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(46, 204, 113)); // Emerald Green
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Enter your credentials to continue");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 5. INPUT FIELDS
        userField = new JTextField();
        userField.setMaximumSize(new Dimension(300, 45));
        userField.putClientProperty("JTextField.placeholderText", "Username");
        userField.putClientProperty("JTextField.leadingIcon", new ImageIcon("user_icon.png")); // If you have icons

        passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(300, 45));
        passField.putClientProperty("JTextField.placeholderText", "4-Digit PIN");
        passField.putClientProperty("JTextField.showRevealButton", true); // Shows the eye icon

        // 6. ACTION BUTTON
        JButton loginBtn = new JButton("SIGN IN");
        loginBtn.setMaximumSize(new Dimension(300, 50));
        loginBtn.setBackground(new Color(46, 204, 113));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 7. ASSEMBLY
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 50)));
        card.add(userField);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(passField);
        card.add(Box.createRigidArea(new Dimension(0, 40)));
        card.add(loginBtn);

        add(card);

        // 8. LOGIC
        loginBtn.addActionListener(e -> {
            // Your login logic here
            System.out.println("Attempting Login...");
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        FlatMacDarkLaf.setup(); // SLEEK MAC DARK THEME
        new LoginFrame();
    }
}