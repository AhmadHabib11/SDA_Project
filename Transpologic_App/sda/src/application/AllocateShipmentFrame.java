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
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bl.ShipmentAllocationService;
import database.DBConnection;

class AllocateShipmentFrame extends JFrame {

    // Color palette (same as Dashboard)
    private static final Color NAVY = new Color(20, 52, 73);
    private static final Color TEAL = new Color(0, 91, 127);
    private static final Color LIME = new Color(166, 206, 57);
    private static final Color PINK = new Color(232, 49, 110);
    private static final Color YELLOW = new Color(255, 198, 41);
    private static final Color LIGHT_BG = new Color(245, 247, 250);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color TEXT_LIGHT = new Color(108, 117, 125);

    private JComboBox<String> cbShipments;
    private JTable tblVehicles;
    private DefaultTableModel tableModel;
    private DashboardFrame dashboard;

    public AllocateShipmentFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("TranspoLogic - Allocate Shipment");
        setSize(900, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(LIGHT_BG);

        JPanel topBar = createTopBar();
        JPanel content = createContentPanel();

        main.add(topBar, BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);

        add(main);
        
        // Load data from database
        loadUnallocatedShipments();
        loadAvailableVehicles();
    }

    /* ==============================================================
       TOP BAR
       ============================================================== */
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

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        left.setOpaque(false);

        JButton btnBack = createTopBarButton("← Back");
        btnBack.addActionListener(e -> goBack());

        JLabel title = new JLabel("Allocate Shipment");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(WHITE);

        left.add(btnBack);
        left.add(title);
        bar.add(left, BorderLayout.WEST);

        return bar;
    }

    private JButton createTopBarButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                if (getModel().isPressed()) g2d.setColor(new Color(LIME.getRed(), LIME.getGreen(), LIME.getBlue(), 200));
                else if (getModel().isRollover()) g2d.setColor(LIME);
                else g2d.setColor(new Color(255, 255, 255, 30));

                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));

                g2d.setColor(WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 40));
        return btn;
    }

    /* ==============================================================
       CONTENT PANEL
       ============================================================== */
    private JPanel createContentPanel() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(LIGHT_BG);

        JPanel card = createFormCard();

        content.add(card);
        return content;
    }

    private JPanel createFormCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                g2d.setColor(WHITE);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

                GradientPaint gradient = new GradientPaint(
                        0, 0, YELLOW,
                        getWidth(), 0, new Color(225, 178, 20)
                );
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 5, 20, 20));
            }
        };

        card.setOpaque(false);
        card.setLayout(new BorderLayout(0, 25));
        card.setBorder(new EmptyBorder(40, 40, 40, 40));
        card.setPreferredSize(new Dimension(700, 580));

        card.add(createHeaderPanel(), BorderLayout.NORTH);
        card.add(createFormPanel(), BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    /* ==============================================================
       CARD HEADER
       ============================================================== */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("📦", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 50));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Allocate Shipment to Vehicle");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(NAVY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Select a shipment and assign it to an available vehicle");
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

    /* ==============================================================
       FORM PANEL
       ============================================================== */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);

        // Shipment dropdown
        JPanel shipmentPanel = new JPanel();
        shipmentPanel.setOpaque(false);
        shipmentPanel.setLayout(new BoxLayout(shipmentPanel, BoxLayout.Y_AXIS));

        JLabel lblShip = new JLabel("Select Shipment *");
        lblShip.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblShip.setForeground(TEXT_DARK);
        lblShip.setAlignmentX(Component.LEFT_ALIGNMENT);

        cbShipments = new JComboBox<>();
        cbShipments.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cbShipments.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        shipmentPanel.add(lblShip);
        shipmentPanel.add(Box.createVerticalStrut(8));
        shipmentPanel.add(cbShipments);

        // Table of available vehicles
        String[] cols = {"Registration No.", "Model", "Capacity (kg)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblVehicles = new JTable(tableModel);
        tblVehicles.setRowHeight(28);
        tblVehicles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tblVehicles);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        panel.add(shipmentPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    /* ==============================================================
       DATABASE LOADING METHODS
       ============================================================== */
    private void loadUnallocatedShipments() {
        String sql = "SELECT id, cargo_type, weight, destination FROM shipments WHERE vehicle_no IS NULL";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            Vector<String> shipments = new Vector<>();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String cargoType = rs.getString("cargo_type");
                int weight = rs.getInt("weight");
                String destination = rs.getString("destination");
                shipments.add("ID-" + id + " | " + cargoType + " | " + weight + "kg | To: " + destination);
            }
            
            if (shipments.isEmpty()) {
                shipments.add("No unallocated shipments available");
            }
            
            cbShipments.setModel(new DefaultComboBoxModel<>(shipments));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading shipments: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAvailableVehicles() {
    	String sql = "SELECT registration_number, model, capacity, status, shipment_status FROM vehicles WHERE shipment_status = 'Available'";

    	
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            tableModel.setRowCount(0);
            
            while (rs.next()) {
                String registrationNumber = rs.getString("registration_number");
                String model = rs.getString("model");
                int capacity = rs.getInt("capacity");
                String status = rs.getString("status");
                
                tableModel.addRow(new Object[]{registrationNumber, model, capacity, rs.getString("shipment_status")});

            }
            
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No available vehicles found in the database.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading vehicles: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /* ==============================================================
       BUTTON PANEL
       ============================================================== */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setOpaque(false);

        JButton btnAssign = createPrimaryButton("Confirm Assignment");
        btnAssign.addActionListener(e -> assignShipment());

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

                if (getModel().isPressed()) g2d.setColor(new Color(200, 170, 20));
                else if (getModel().isRollover()) {
                    GradientPaint gp = new GradientPaint(0, 0, YELLOW, getWidth(), 0, new Color(225, 178, 20));
                    g2d.setPaint(gp);
                } else {
                    GradientPaint gp = new GradientPaint(0, 0, new Color(225, 178, 20), getWidth(), 0, YELLOW);
                    g2d.setPaint(gp);
                }

                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

                g2d.setColor(WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(240, 50));
        return btn;
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                g2d.setColor(LIGHT_BG);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

                g2d.setColor(TEXT_DARK);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 10, 10));

                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 50));
        return btn;
    }

    /* ==============================================================
       LOGIC – ASSIGN SHIPMENT TO VEHICLE
       ============================================================== */
    private void assignShipment() {
        int selectedRow = tblVehicles.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String shipmentStr = cbShipments.getSelectedItem().toString();
        
        if (shipmentStr.equals("No unallocated shipments available")) {
            JOptionPane.showMessageDialog(this, 
                    "No shipments available for allocation.", 
                    "Error", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Extract shipment ID from the string "ID-1 | clothes | 10kg | To: lahore"
        String shipmentId = shipmentStr.split(" \\| ")[0].replace("ID-", "");
        String weightStr = shipmentStr.split(" \\| ")[2].replace("kg", "");
        int weight = Integer.parseInt(weightStr);

        int capacity = (int) tblVehicles.getValueAt(selectedRow, 2);
        String vehicle = tblVehicles.getValueAt(selectedRow, 0).toString();

        if (weight > capacity) {
            JOptionPane.showMessageDialog(this,
                    "The shipment exceeds the vehicle capacity!",
                    "Capacity Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        ShipmentAllocationService service = new ShipmentAllocationService();
        boolean success = service.allocateShipment(shipmentId, vehicle);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Shipment allocated successfully!\n\n" +
                            "Shipment: " + shipmentStr + "\n" +
                            "Assigned to Vehicle: " + vehicle,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            goBack();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to allocate shipment!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /* ==============================================================
       NAVIGATION
       ============================================================== */
    private void goBack() {
        dashboard.setVisible(true);
        this.dispose();
    }
}