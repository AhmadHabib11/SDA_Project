package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/* ==============================================================
   MODERN DASHBOARD SCREEN
   ============================================================== */
class DashboardFrame extends JFrame {
    
    // Color palette
    private static final Color NAVY = new Color(20, 52, 73);
    private static final Color TEAL = new Color(0, 91, 127);
    private static final Color LIME = new Color(166, 206, 57);
    private static final Color PINK = new Color(232, 49, 110);
    private static final Color YELLOW = new Color(255, 198, 41);
    private static final Color LIGHT_BG = new Color(245, 247, 250);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color CARD_BG = new Color(255, 255, 255);

    public DashboardFrame() {
        setTitle("TranspoLogic - Dashboard");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Main container
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(LIGHT_BG);

        // Top bar
        JPanel topBar = createTopBar();
        main.add(topBar, BorderLayout.NORTH);

        // Content area
        JPanel content = createContentPanel();
        main.add(content, BorderLayout.CENTER);

        add(main);
    }

    private JPanel createTopBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, NAVY,
                    getWidth(), 0, TEAL
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bar.setPreferredSize(new Dimension(0, 70));
        bar.setBorder(new EmptyBorder(15, 30, 15, 30));

        // Left side - Logo and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        
        JLabel logo = new JLabel("🚛");
        logo.setFont(new Font("SansSerif", Font.PLAIN, 32));
        
        JLabel title = new JLabel("TRANSPOLOGIC");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(WHITE);
        
        leftPanel.add(logo);
        leftPanel.add(title);

        // Right side - User info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("👤 Admin User");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userLabel.setForeground(WHITE);
        
        JButton btnLogout = createTopBarButton("Logout");
        btnLogout.addActionListener(e -> logout());
        
        rightPanel.add(userLabel);
        rightPanel.add(btnLogout);

        bar.add(leftPanel, BorderLayout.WEST);
        bar.add(rightPanel, BorderLayout.EAST);

        return bar;
    }

    private JButton createTopBarButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                     RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(PINK.getRed(), PINK.getGreen(), 
                                           PINK.getBlue(), 200));
                } else if (getModel().isRollover()) {
                    g2d.setColor(PINK);
                } else {
                    g2d.setColor(new Color(255, 255, 255, 30));
                }
                
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 
                                                     getHeight(), 8, 8));
                
                g2d.setColor(WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };
        
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 40));
        
        return btn;
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBackground(LIGHT_BG);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Dashboard title
        JLabel dashTitle = new JLabel("Dashboard");
        dashTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        dashTitle.setForeground(NAVY);

        JLabel subtitle = new JLabel("Manage your fleet operations");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(108, 117, 125));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(LIGHT_BG);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dashTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(dashTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        // Grid of feature cards
        JPanel grid = new JPanel(new GridLayout(4, 3, 20, 20));
        grid.setBackground(LIGHT_BG);

        String[][] features = {
            {"📝", "Register Vehicle", "Add new vehicles to fleet", "#LIME"},
            {"👨‍✈️", "Create Driver Profile", "Register driver information", "#TEAL"},
            {"🔗", "Assign Driver to Vehicle", "Link drivers with vehicles", "#PINK"},
            {"📦", "Create Shipment", "Create new shipment orders", "#YELLOW"},
            {"🚚", "Allocate Shipment", "Assign shipments to vehicles", "#LIME"},
            {"📊", "Update Status", "Track shipment progress", "#TEAL"},
            {"🗺️", "Assign Route", "Plan delivery routes", "#PINK"},
            {"⏰", "Monitor Schedule", "View fleet schedules", "#YELLOW"},
            {"🔧", "Record Maintenance", "Log maintenance activities", "#LIME"},
            {"💰", "Revenue Report", "View revenue analytics", "#TEAL"},
            {"🏆", "Driver Performance", "Analyze driver metrics", "#PINK"},
            {"🚪", "Logout", "Exit the system", "#NAVY"}
        };

        for (String[] feature : features) {
            JPanel card = createFeatureCard(feature[0], feature[1], feature[2], feature[3]);
            grid.add(card);
        }

        content.add(titlePanel, BorderLayout.NORTH);
        content.add(grid, BorderLayout.CENTER);

        return content;
    }

    private JPanel createFeatureCard(String icon, String title, 
                                      String desc, String colorCode) {
        JPanel card = new JPanel() {
            private boolean hovered = false;
            
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hovered = true;
                        repaint();
                    }
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                     RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow effect when hovered
                if (hovered) {
                    g2d.setColor(new Color(0, 0, 0, 20));
                    g2d.fill(new RoundRectangle2D.Float(4, 4, getWidth() - 4, 
                                                         getHeight() - 4, 15, 15));
                }
                
                // Card background
                g2d.setColor(WHITE);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 
                                                     getHeight(), 15, 15));
                
                // Colored top border
                Color accentColor = getColorFromCode(colorCode);
                g2d.setColor(accentColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 5, 15, 15));
            }
        };
        
        card.setLayout(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(25, 20, 25, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icon
        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("SansSerif", Font.PLAIN, 38));
        
        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblDesc.setForeground(new Color(108, 117, 125));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        textPanel.add(lblTitle);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(lblDesc);
        
        card.add(lblIcon, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        
        // Click handler
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                openScreen(title);
            }
        });
        
        return card;
    }

    private Color getColorFromCode(String code) {
        return switch (code) {
            case "#LIME" -> LIME;
            case "#TEAL" -> TEAL;
            case "#PINK" -> PINK;
            case "#YELLOW" -> YELLOW;
            case "#NAVY" -> NAVY;
            default -> TEAL;
        };
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }

    private void openScreen(String name) {
        switch (name) {
            case "Register Vehicle" -> {
                new RegisterVehicleFrame(this).setVisible(true);
                this.setVisible(false);
            }
            
            case "Create Driver Profile" -> {
                new CreateDriverProfileFrame(this).setVisible(true);
                this.setVisible(false);
            }
            
            case "Assign Driver to Vehicle" -> {
                new AssignDriverToVehicleFrame(this).setVisible(true);
                this.setVisible(false);
            }
            
            case "Create Shipment" -> {
                new CreateShipmentFrame(this).setVisible(true);
                this.setVisible(false);
            }
            
            case "Allocate Shipment" -> {
                new AllocateShipmentFrame(this).setVisible(true);
                this.setVisible(false);
            }
            
            case "Update Status" -> {
                new UpdateShipmentStatusFrame(this).setVisible(true);
                this.setVisible(false);
            }
            
            case "Assign Route" -> {
                new AssignRouteFrame(this).setVisible(true);
                this.setVisible(false);
            }
            
            case "Monitor Schedule" -> {
             new MonitorScheduleFrame(this).setVisible(true);
             this.setVisible(false);
            }
            
            case "Record Maintenance" -> {
             new RecordMaintenanceActivityFrame(this).setVisible(true);
             this.setVisible(false);
            }
            
            case "Revenue Report" -> {
                new RevenueReportFrame(this).setVisible(true);
                this.setVisible(false);
            }
            
            case "Driver Performance" -> {
                new DriverPerformanceReportFrame(this).setVisible(true);
                this.setVisible(false);
            }

            case "Logout" -> logout();
            default ->
                JOptionPane.showMessageDialog(this, "Feature not implemented: " + name);
        }
    }
}