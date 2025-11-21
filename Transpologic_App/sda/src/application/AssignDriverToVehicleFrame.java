package application;

import java.awt.BasicStroke;
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
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import bl.AssignmentService;

/* ==============================================================
ASSIGN DRIVER TO VEHICLE SCREEN
============================================================== */
class AssignDriverToVehicleFrame extends JFrame {

    // Color palette (same theme)
    private static final Color NAVY = new Color(20, 52, 73);
    private static final Color TEAL = new Color(0, 91, 127);
    private static final Color LIME = new Color(166, 206, 57);
    private static final Color PINK = new Color(232, 49, 110);
    private static final Color YELLOW = new Color(255, 198, 41);
    private static final Color LIGHT_BG = new Color(245, 247, 250);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color TEXT_LIGHT = new Color(108, 117, 125);

    private JComboBox<String> cbDrivers;
    private JComboBox<String> cbVehicles;

    private DashboardFrame dashboard;

    public AssignDriverToVehicleFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("TranspoLogic - Assign Driver to Vehicle");
        setSize(700, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(LIGHT_BG);

        JPanel topBar = createTopBar();
        main.add(topBar, BorderLayout.NORTH);

        JPanel content = createContentPanel();
        main.add(content, BorderLayout.CENTER);

        add(main);

        // ✅ Load drivers and vehicles from DB
        loadComboBoxes();
    }

    // --------------------- TOP BAR ---------------------
    private JPanel createTopBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, NAVY, getWidth(), 0, TEAL);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        bar.setPreferredSize(new Dimension(0, 70));
        bar.setBorder(new EmptyBorder(15, 30, 15, 30));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);

        JButton btnBack = createTopBarButton("← Back");
        btnBack.addActionListener(e -> goBack());

        JLabel title = new JLabel("Assign Driver to Vehicle");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(WHITE);

        leftPanel.add(btnBack);
        leftPanel.add(title);
        bar.add(leftPanel, BorderLayout.WEST);

        return bar;
    }

    private JButton createTopBarButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed())
                    g2d.setColor(new Color(LIME.getRed(), LIME.getGreen(), LIME.getBlue(), 200));
                else if (getModel().isRollover())
                    g2d.setColor(LIME);
                else
                    g2d.setColor(new Color(255, 255, 255, 30));

                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));

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
        btn.setPreferredSize(new Dimension(120, 40));
        return btn;
    }

    // --------------------- CONTENT PANEL ---------------------
    private JPanel createContentPanel() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(LIGHT_BG);
        content.setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel card = createFormCard();
        content.add(card);

        return content;
    }

    private JPanel createFormCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(WHITE);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(),
                        getHeight(), 20, 20));

                GradientPaint gradient = new GradientPaint(0, 0, LIME,
                        getWidth(), 0, new Color(146, 186, 47));
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 5, 20, 20));
            }
        };

        card.setLayout(new BorderLayout(0, 30));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(50, 50, 50, 50));
        card.setPreferredSize(new Dimension(550, 450));

        card.add(createHeaderPanel(), BorderLayout.NORTH);
        card.add(createFormFields(), BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("🚚", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 50));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Assign Driver");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(NAVY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Choose driver and vehicle below");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(icon);
        panel.add(Box.createVerticalStrut(10));
        panel.add(title);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitle);

        return panel;
    }

    // --------------------- FORM FIELDS ---------------------
    private JPanel createFormFields() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Drivers
        JLabel lblDriver = new JLabel("Select Driver *");
        lblDriver.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblDriver.setForeground(TEXT_DARK);

        cbDrivers = new JComboBox<>();
        cbDrivers.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Vehicles
        JLabel lblVehicle = new JLabel("Select Vehicle *");
        lblVehicle.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblVehicle.setForeground(TEXT_DARK);

        cbVehicles = new JComboBox<>();
        cbVehicles.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        panel.add(lblDriver);
        panel.add(Box.createVerticalStrut(8));
        panel.add(cbDrivers);
        panel.add(Box.createVerticalStrut(25));

        panel.add(lblVehicle);
        panel.add(Box.createVerticalStrut(8));
        panel.add(cbVehicles);

        return panel;
    }

    // --------------------- BUTTONS ---------------------
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setOpaque(false);

        JButton btnAssign = createPrimaryButton("Assign");
        btnAssign.addActionListener(e -> assignDriver());

        JButton btnCancel = createSecondaryButton("Cancel");
        btnCancel.addActionListener(e -> goBack());

        panel.add(btnAssign);
        panel.add(btnCancel);

        return panel;
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed())
                    g2d.setColor(new Color(146, 186, 47));
                else if (getModel().isRollover())
                    g2d.setPaint(new GradientPaint(0, 0,
                            new Color(186, 226, 77), getWidth(), 0, LIME));
                else
                    g2d.setPaint(new GradientPaint(0, 0,
                            LIME, getWidth(), 0, new Color(146, 186, 47)));

                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(),
                        getHeight(), 10, 10));

                g2d.setColor(WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setForeground(WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 50));
        return btn;
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed())
                    g2d.setColor(new Color(210, 210, 210));
                else if (getModel().isRollover())
                    g2d.setColor(new Color(230, 230, 230));
                else
                    g2d.setColor(LIGHT_BG);

                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(),
                        getHeight(), 10, 10));

                g2d.setColor(TEXT_DARK);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2,
                        getHeight() - 2, 10, 10));

                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setForeground(TEXT_DARK);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 50));

        return btn;
    }

    // --------------------- ASSIGN DRIVER ---------------------
    private void assignDriver() {
        String selectedDriver = (String) cbDrivers.getSelectedItem();
        String selectedVehicle = (String) cbVehicles.getSelectedItem();

        if (selectedDriver == null || selectedVehicle == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select both driver and vehicle!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedVehicle.contains("Under Maintenance")) {
            JOptionPane.showMessageDialog(this,
                    "Vehicle is under maintenance!\nAssignment failed.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Extract registration number
     // Extract registration number and model
        String[] vehicleParts = selectedVehicle.split(" - ");
        String vehicleModel = vehicleParts[0].trim();
        String regNo = vehicleParts[1].split(" ")[0].trim();

        AssignmentService service = new AssignmentService();
        boolean success = service.assignDriverToVehicle(selectedDriver, regNo, vehicleModel);


        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Driver Assigned Successfully!\n\n" +
                            "Driver: " + selectedDriver + "\n" +
                            "Vehicle: " + selectedVehicle,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            goBack();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to assign driver.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // --------------------- LOAD FROM DB ---------------------
    private void loadComboBoxes() {
        AssignmentService service = new AssignmentService();

        cbDrivers.removeAllItems();
        for (String driver : service.getAvailableDrivers()) {
            cbDrivers.addItem(driver);
        }

        cbVehicles.removeAllItems();
        for (String vehicle : service.getAvailableVehicles()) {
            cbVehicles.addItem(vehicle);
        }
    }

    // --------------------- BACK ---------------------
    private void goBack() {
        dashboard.setVisible(true);
        this.dispose();
    }
}
