package application;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

//import bl.VehicleService;

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

import util.LoggerUtil;
import util.ValidatorUtil;

/* ============================================================== 
   REGISTER VEHICLE SCREEN (Fixed for cross-platform)
   ============================================================== */
class RegisterVehicleFrame extends JFrame {

    // Color palette
    private static final Color NAVY = new Color(20, 52, 73);
    private static final Color TEAL = new Color(0, 91, 127);
    private static final Color LIME = new Color(166, 206, 57);
    private static final Color LIGHT_BG = new Color(245, 247, 250);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color TEXT_LIGHT = new Color(108, 117, 125);

    private JTextField tfRegNo;
    private JTextField tfModel;
    private JTextField tfCapacity;
    private JTextField tfStatus;
    private DashboardFrame dashboard;

    public RegisterVehicleFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("TranspoLogic - Register Vehicle");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Main container
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(LIGHT_BG);

        // Top bar
        JPanel topBar = createTopBar();
        main.add(topBar, BorderLayout.NORTH);

        // Content area
        JPanel content = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        main.add(scrollPane, BorderLayout.CENTER);

        add(main);

        pack(); // Adjust frame size to fit content
        setMinimumSize(new Dimension(600, 600));
        setLocationRelativeTo(null);
        setVisible(true);
    }

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

        JLabel title = new JLabel("Register Vehicle");
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
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(LIME.getRed(), LIME.getGreen(), LIME.getBlue(), 200));
                } else if (getModel().isRollover()) {
                    g2d.setColor(LIME);
                } else {
                    g2d.setColor(new Color(255, 255, 255, 30));
                }

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
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(WHITE);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

                GradientPaint gradient = new GradientPaint(0, 0, LIME, getWidth(), 0, new Color(146, 186, 47));
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 5, 20, 20));
            }
        };

        card.setLayout(new BorderLayout(0, 30));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel headerPanel = createHeaderPanel();
        card.add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = createFormFields();
        card.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel icon = new JLabel("📝", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 50));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Vehicle Registration");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(NAVY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Enter vehicle details to add to fleet");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(icon);
        panel.add(Box.createVerticalStrut(15));
        panel.add(title);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitle);

        return panel;
    }

    private JPanel createFormFields() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel lblRegNo = new JLabel("Registration Number *");
        lblRegNo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblRegNo.setForeground(TEXT_DARK);
        lblRegNo.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfRegNo = createStyledTextField();
        tfRegNo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblModel = new JLabel("Model *");
        lblModel.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblModel.setForeground(TEXT_DARK);
        lblModel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfModel = createStyledTextField();
        tfModel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblCapacity = new JLabel("Capacity (kg) *");
        lblCapacity.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblCapacity.setForeground(TEXT_DARK);
        lblCapacity.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfCapacity = createStyledTextField();
        tfCapacity.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblStatus = new JLabel("Status");
        lblStatus.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblStatus.setForeground(TEXT_DARK);
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfStatus = createStyledTextField();
        tfStatus.setText("Available");
        tfStatus.setEditable(false);
        tfStatus.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblRegNo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(tfRegNo);
        panel.add(Box.createVerticalStrut(20));

        panel.add(lblModel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(tfModel);
        panel.add(Box.createVerticalStrut(20));

        panel.add(lblCapacity);
        panel.add(Box.createVerticalStrut(8));
        panel.add(tfCapacity);
        panel.add(Box.createVerticalStrut(20));

        panel.add(lblStatus);
        panel.add(Box.createVerticalStrut(8));
        panel.add(tfStatus);

        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(LIGHT_BG);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

                super.paintComponent(g);
            }
        };

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(14, 15, 14, 15));
        field.setOpaque(false);
        field.setForeground(TEXT_DARK);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // width auto-fits
        field.setPreferredSize(new Dimension(0, 50)); // width flexible
        return field;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setOpaque(false);

        JButton btnRegister = new JButton("Register Vehicle") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(146, 186, 47));
                } else if (getModel().isRollover()) {
                    g2d.setPaint(new GradientPaint(0, 0,
                        new Color(186, 226, 77), getWidth(), 0, LIME));
                } else {
                    g2d.setPaint(new GradientPaint(0, 0,
                        LIME, getWidth(), 0, new Color(146, 186, 47)));
                }

                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(),
                    getHeight(), 10, 10));

                g2d.setColor(WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };
        
        btnRegister.setPreferredSize(new Dimension(200, 50));
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnRegister.setForeground(WHITE);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> registerVehicle());

        JButton btnCancel = new JButton("Cancel") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(200, 200, 200));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(220, 220, 220));
                } else {
                    g2d.setColor(LIGHT_BG);
                }

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
        
        btnCancel.setPreferredSize(new Dimension(150, 50));
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnCancel.setForeground(TEXT_DARK);
        btnCancel.setContentAreaFilled(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> goBack());

        panel.add(btnRegister);
        panel.add(btnCancel);

        return panel;
    }

    private void registerVehicle() {
        String regNo = tfRegNo.getText().trim();
        String model = tfModel.getText().trim();
        String capacityStr = tfCapacity.getText().trim();

        if (regNo.isEmpty() || model.isEmpty() || capacityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Capacity must be a valid number", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        VehicleService vehicleService = new VehicleService();
        boolean success = vehicleService.addVehicle(regNo, model, capacity, "Available");

        if (success) {
            JOptionPane.showMessageDialog(this, "Vehicle registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            goBack();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to register vehicle. It may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goBack() {
        dashboard.setVisible(true);
        this.dispose();
    }
}
