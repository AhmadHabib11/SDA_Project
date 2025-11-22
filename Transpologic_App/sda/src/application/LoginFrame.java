package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

//import bl.UserService;

import controller.ShipmentController;
import controller.DriverController;

import dao.ShipmentDAO;
import dao.ShipmentDAOImpl;
import dao.DriverDAO;
import dao.DriverDAOImpl;
import dao.VehicleDAO;
import dao.VehicleDAOImpl;

import model.Shipment;
import model.Driver;
import model.Vehicle;

import service.ShipmentAllocationService;
import service.ShipmentService;
import service.ShipmentServiceImpl;
import service.AssignmentService;
import service.DriverService;
import service.VehicleService;
import service.UserService;

import util.LoggerUtil;
import util.ValidatorUtil;

/* ==============================================================
   MODERN LOGIN SCREEN
   ============================================================== */
class LoginFrame extends JFrame {
    
    // Color palette from your logo
    private static final Color NAVY = new Color(20, 52, 73);
    private static final Color TEAL = new Color(0, 91, 127);
    private static final Color LIME = new Color(166, 206, 57);
    private static final Color PINK = new Color(232, 49, 110);
    private static final Color YELLOW = new Color(255, 198, 41);
    private static final Color LIGHT_BG = new Color(245, 247, 250);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color TEXT_LIGHT = new Color(108, 117, 125);

    private JTextField tfUser;
    private JPasswordField tfPass;

    public LoginFrame() {
        setTitle("TranspoLogic - Login");
        setSize(500, 750);
        setMinimumSize(new Dimension(450, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        // Main container with custom background
        JPanel main = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                     RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, NAVY,
                    0, getHeight(), TEAL
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Decorative circles
                g2d.setColor(new Color(LIME.getRed(), LIME.getGreen(), 
                                       LIME.getBlue(), 30));
                g2d.fillOval(-50, -50, 200, 200);
                
                g2d.setColor(new Color(PINK.getRed(), PINK.getGreen(), 
                                       PINK.getBlue(), 30));
                g2d.fillOval(getWidth() - 150, getHeight() - 150, 200, 200);
            }
        };
        main.setLayout(new GridBagLayout());

        // Login card panel
        JPanel card = createLoginCard();
        
        main.add(card);
        add(main);
    }

    private JPanel createLoginCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                     RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Rounded rectangle background
                g2d.setColor(WHITE);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 
                                                     getHeight(), 20, 20));
            }
        };
        
        card.setLayout(new BorderLayout(0, 20));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(40, 40, 40, 40));
        card.setPreferredSize(new Dimension(420, 620));
        card.setMaximumSize(new Dimension(520, 800));

        // Logo section
        JPanel logoPanel = createLogoPanel();
        card.add(logoPanel, BorderLayout.NORTH);

        // Form section
        JPanel formPanel = createFormPanel();
        card.add(formPanel, BorderLayout.CENTER);

        // Button section
        JPanel buttonPanel = createButtonPanel();
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createLogoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Logo placeholder
        JLabel logoIcon = new JLabel("🚛") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(80, 80);
            }
        };
        logoIcon.setFont(new Font("SansSerif", Font.PLAIN, 60));
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel title = new JLabel("TRANSPOLOGIC");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(NAVY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Fleet Management System");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(logoIcon);
        panel.add(Box.createVerticalStrut(15));
        panel.add(title);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(30));

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Username field
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblUser.setForeground(TEXT_DARK);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        tfUser = createStyledTextField("Enter your username");
        tfUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password field
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPass.setForeground(TEXT_DARK);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        tfPass = createStyledPasswordField("Enter your password");
        tfPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Remember me checkbox
        JCheckBox cbRemember = new JCheckBox("Remember me");
        cbRemember.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cbRemember.setForeground(TEXT_LIGHT);
        cbRemember.setOpaque(false);
        cbRemember.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbRemember.setFocusPainted(false);

        panel.add(lblUser);
        panel.add(Box.createVerticalStrut(8));
        panel.add(tfUser);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblPass);
        panel.add(Box.createVerticalStrut(8));
        panel.add(tfPass);
        panel.add(Box.createVerticalStrut(12));
        panel.add(cbRemember);

        return panel;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                     RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(LIGHT_BG);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 
                                                     getHeight(), 10, 10));
                
                super.paintComponent(g);
            }
        };
        
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(14, 15, 14, 15));
        field.setOpaque(false);
        field.setForeground(TEXT_DARK);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        field.setPreferredSize(new Dimension(300, 50));
        
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                     RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(LIGHT_BG);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 
                                                     getHeight(), 10, 10));
                
                super.paintComponent(g);
            }
        };
        
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(14, 15, 14, 15));
        field.setOpaque(false);
        field.setForeground(TEXT_DARK);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        field.setPreferredSize(new Dimension(300, 50));
        
        return field;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Login button
        JButton btnLogin = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                     RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(NAVY);
                } else if (getModel().isRollover()) {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, TEAL,
                        0, getHeight(), NAVY
                    );
                    g2d.setPaint(gradient);
                } else {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, LIME,
                        getWidth(), 0, new Color(146, 186, 47)
                    );
                    g2d.setPaint(gradient);
                }
                
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 
                                                     getHeight(), 10, 10));
                
                g2d.setColor(WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };
        
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnLogin.setForeground(WHITE);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(300, 50));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> openDashboard());

        // Forgot password link
        JLabel lblForgot = new JLabel("Forgot password?");
        lblForgot.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblForgot.setForeground(TEAL);
        lblForgot.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.add(Box.createVerticalStrut(20));
        panel.add(btnLogin);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblForgot);

        return panel;
    }

    private void openDashboard() {
        String username = tfUser.getText().trim();
        String password = new String(tfPass.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password", 
                "Login Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserService userService = new UserService();
        boolean loggedIn = userService.login(username, password);

        if (loggedIn) {
            new DashboardFrame().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username or password", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}