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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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

class UpdateShipmentStatusFrame extends JFrame {

 // Color palette (same as Dashboard / AllocateShipmentFrame)
 private static final Color NAVY = new Color(20, 52, 73);
 private static final Color TEAL = new Color(0, 91, 127);
 private static final Color LIME = new Color(166, 206, 57);
 private static final Color PINK = new Color(232, 49, 110);
 private static final Color YELLOW = new Color(255, 198, 41);
 private static final Color LIGHT_BG = new Color(245, 247, 250);
 private static final Color WHITE = Color.WHITE;
 private static final Color TEXT_DARK = new Color(33, 37, 41);
 private static final Color TEXT_LIGHT = new Color(108, 117, 125);

 private JTable tblShipments;
 private JComboBox<String> cbStatus;
 private DashboardFrame dashboard; // parent for navigation

 public UpdateShipmentStatusFrame(DashboardFrame dashboard) {
     this.dashboard = dashboard;

     setTitle("TranspoLogic - Update Shipment Status");
     setSize(900, 700);
     setLocationRelativeTo(null);
     setDefaultCloseOperation(DISPOSE_ON_CLOSE);

     JPanel main = new JPanel(new BorderLayout());
     main.setBackground(LIGHT_BG);

     main.add(createTopBar(), BorderLayout.NORTH);
     main.add(createContentPanel(), BorderLayout.CENTER);

     add(main);
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
             GradientPaint gradient = new GradientPaint(0, 0, NAVY, getWidth(), 0, TEAL);
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

     JLabel title = new JLabel("Update Shipment Status");
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
             g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

             if (getModel().isPressed()) g2d.setColor(new Color(LIME.getRed(), LIME.getGreen(), LIME.getBlue(), 200));
             else if (getModel().isRollover()) g2d.setColor(LIME);
             else g2d.setColor(new Color(255, 255, 255, 30));

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
     btn.setContentAreaFilled(false);
     btn.setBorderPainted(false);
     btn.setFocusPainted(false);
     btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
     btn.setPreferredSize(new Dimension(120, 40));
     return btn;
 }

 /* ==============================================================
    CONTENT
    ============================================================== */
 private JPanel createContentPanel() {
     JPanel container = new JPanel(new GridBagLayout());
     container.setBackground(LIGHT_BG);

     JPanel card = createFormCard();
     container.add(card);
     return container;
 }

 private JPanel createFormCard() {
     JPanel card = new JPanel() {
         @Override
         protected void paintComponent(Graphics g) {
             super.paintComponent(g);
             Graphics2D g2d = (Graphics2D) g;
             g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

             g2d.setColor(WHITE);
             g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

             GradientPaint gradient = new GradientPaint(0, 0, TEAL, getWidth(), 0, new Color(0, 120, 140));
             g2d.setPaint(gradient);
             g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 5, 20, 20));
         }
     };

     card.setOpaque(false);
     card.setLayout(new BorderLayout(0, 20));
     card.setBorder(new EmptyBorder(30, 30, 30, 30));
     card.setPreferredSize(new Dimension(760, 540));

     card.add(createHeaderPanel(), BorderLayout.NORTH);
     card.add(createFormPanel(), BorderLayout.CENTER);
     card.add(createButtonPanel(), BorderLayout.SOUTH);

     return card;
 }

 private JPanel createHeaderPanel() {
     JPanel header = new JPanel();
     header.setOpaque(false);
     header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

     JLabel icon = new JLabel("🚚", SwingConstants.CENTER);
     icon.setFont(new Font("SansSerif", Font.PLAIN, 44));
     icon.setAlignmentX(Component.CENTER_ALIGNMENT);

     JLabel title = new JLabel("Driver — Update Shipment Status");
     title.setFont(new Font("SansSerif", Font.BOLD, 22));
     title.setForeground(NAVY);
     title.setAlignmentX(Component.CENTER_ALIGNMENT);

     JLabel subtitle = new JLabel("Select an assigned shipment and update its status");
     subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
     subtitle.setForeground(TEXT_LIGHT);
     subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

     header.add(icon);
     header.add(Box.createVerticalStrut(10));
     header.add(title);
     header.add(Box.createVerticalStrut(6));
     header.add(subtitle);

     return header;
 }

 private JPanel createFormPanel() {
     JPanel panel = new JPanel(new BorderLayout(0, 18));
     panel.setOpaque(false);

     // Table (assigned shipments)
     String[] cols = {"Shipment ID", "Customer", "Pickup", "Drop-off", "Status"};
     Object[][] rows = {
             {"SHIP-101", "Acme Corp", "Karachi Warehouse", "Lahore Depot", "In-Transit"},
             {"SHIP-102", "Beta Traders", "Multan Hub", "Faisalabad", "In-Transit"},
             {"SHIP-103", "Gamma LLC", "Hyderabad", "Sukkur", "Pending"},
     };

     DefaultTableModel model = new DefaultTableModel(rows, cols) {
         // Make columns non-editable via direct typing
         @Override
         public boolean isCellEditable(int row, int column) {
             return false;
         }
     };

     tblShipments = new JTable(model);
     tblShipments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     tblShipments.setRowHeight(30);

     // Make header visually slightly bolder
     tblShipments.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
     tblShipments.setFont(new Font("SansSerif", Font.PLAIN, 13));

     JScrollPane scroll = new JScrollPane(tblShipments);
     scroll.setBorder(BorderFactory.createEmptyBorder());
     panel.add(scroll, BorderLayout.CENTER);

     // Bottom area: dropdown always visible (as requested)
     JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
     bottom.setOpaque(false);

     JLabel lblStatus = new JLabel("Update Status:");
     lblStatus.setFont(new Font("SansSerif", Font.BOLD, 14));
     lblStatus.setForeground(TEXT_DARK);

     cbStatus = new JComboBox<>(new String[]{"In-Transit", "Delivered"});
     cbStatus.setFont(new Font("SansSerif", Font.PLAIN, 14));
     cbStatus.setPreferredSize(new Dimension(180, 36));

     bottom.add(lblStatus);
     bottom.add(cbStatus);

     panel.add(bottom, BorderLayout.SOUTH);

     return panel;
 }

 /* ==============================================================
    BUTTONS
    ============================================================== */
 private JPanel createButtonPanel() {
     JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
     panel.setOpaque(false);

     JButton btnSubmit = createPrimaryButton("Submit Update");
     btnSubmit.addActionListener(e -> submitUpdate());

     JButton btnCancel = createSecondaryButton("Cancel");
     btnCancel.addActionListener(e -> goBack());

     panel.add(btnSubmit);
     panel.add(btnCancel);

     return panel;
 }

 private JButton createPrimaryButton(String text) {
     JButton btn = new JButton(text) {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2d = (Graphics2D) g;
             g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

             if (getModel().isPressed()) g2d.setColor(new Color(0, 80, 100));
             else if (getModel().isRollover()) {
                 GradientPaint gp = new GradientPaint(0, 0, TEAL, getWidth(), 0, new Color(0, 110, 130));
                 g2d.setPaint(gp);
             } else {
                 GradientPaint gp = new GradientPaint(0, 0, new Color(0, 110, 130), getWidth(), 0, TEAL);
                 g2d.setPaint(gp);
             }

             g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

             g2d.setColor(WHITE);
             g2d.setFont(getFont());
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
     btn.setPreferredSize(new Dimension(220, 48));
     return btn;
 }

 private JButton createSecondaryButton(String text) {
     JButton btn = new JButton(text) {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2d = (Graphics2D) g;
             g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

             g2d.setColor(LIGHT_BG);
             g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

             g2d.setColor(TEXT_DARK);
             g2d.setStroke(new BasicStroke(2));
             g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 10, 10));

             g2d.setFont(getFont());
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
     btn.setPreferredSize(new Dimension(150, 48));
     return btn;
 }

 /* ==============================================================
    LOGIC — Submit update for single selected shipment
    ============================================================== */
 private void submitUpdate() {
     int selectedRow = tblShipments.getSelectedRow();
     if (selectedRow == -1) {
         JOptionPane.showMessageDialog(this,
                 "Please select a shipment to update.",
                 "No Selection",
                 JOptionPane.WARNING_MESSAGE);
         return;
     }

     String currentStatus = tblShipments.getValueAt(selectedRow, 4).toString();
     String newStatus = cbStatus.getSelectedItem().toString();

     if (currentStatus.equalsIgnoreCase(newStatus)) {
         int c = JOptionPane.showConfirmDialog(this,
                 "The shipment already has status \"" + currentStatus + "\". Do you still want to submit?",
                 "Confirm",
                 JOptionPane.YES_NO_OPTION);
         if (c != JOptionPane.YES_OPTION) return;
     }

     // Update model
     DefaultTableModel model = (DefaultTableModel) tblShipments.getModel();
     model.setValueAt(newStatus, selectedRow, 4);

     JOptionPane.showMessageDialog(this,
             "Status updated successfully.\n\nShipment: " + model.getValueAt(selectedRow, 0)
                     + "\nNew Status: " + newStatus,
             "Success",
             JOptionPane.INFORMATION_MESSAGE);

     // Optionally — scroll to the updated row or reselect
     tblShipments.setRowSelectionInterval(selectedRow, selectedRow);
 }

 /* ==============================================================
    NAVIGATION
    ============================================================== */
 private void goBack() {
     if (dashboard != null) {
         dashboard.setVisible(true);
     }
     this.dispose();
 }
}