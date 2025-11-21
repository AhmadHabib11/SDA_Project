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

class AssignRouteFrame extends JFrame {

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

 private JComboBox<String> cbVehicles;
 private JTable tblRoutes;
 private DashboardFrame dashboard;

 public AssignRouteFrame(DashboardFrame dashboard) {
     this.dashboard = dashboard;

     setTitle("TranspoLogic - Assign Route");
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

     JLabel title = new JLabel("Assign Route");
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

     JLabel icon = new JLabel("🗺️", SwingConstants.CENTER);
     icon.setFont(new Font("SansSerif", Font.PLAIN, 50));
     icon.setAlignmentX(Component.CENTER_ALIGNMENT);

     JLabel title = new JLabel("Assign Route to Vehicle");
     title.setFont(new Font("SansSerif", Font.BOLD, 24));
     title.setForeground(NAVY);
     title.setAlignmentX(Component.CENTER_ALIGNMENT);

     JLabel subtitle = new JLabel("Select a vehicle and choose a delivery route");
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

     // Vehicle dropdown
     JPanel vehiclePanel = new JPanel();
     vehiclePanel.setOpaque(false);
     vehiclePanel.setLayout(new BoxLayout(vehiclePanel, BoxLayout.Y_AXIS));

     JLabel lblVehicle = new JLabel("Select Vehicle *");
     lblVehicle.setFont(new Font("SansSerif", Font.BOLD, 14));
     lblVehicle.setForeground(TEXT_DARK);
     lblVehicle.setAlignmentX(Component.LEFT_ALIGNMENT);

     cbVehicles = new JComboBox<>(new String[]{
         "TRK-001 | Tata Ace",
         "TRK-002 | Bolero Pickup",
         "TRK-003 | Eicher Pro"
     });
     cbVehicles.setFont(new Font("SansSerif", Font.PLAIN, 14));
     cbVehicles.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

     vehiclePanel.add(lblVehicle);
     vehiclePanel.add(Box.createVerticalStrut(8));
     vehiclePanel.add(cbVehicles);

     // Table of available routes
     String[] cols = {"Route ID", "Origin", "Destination", "Distance (km)"};
     Object[][] data = {
         {"RT-01", "Mumbai", "Pune", 150},
         {"RT-02", "Delhi", "Agra", 230},
         {"RT-03", "Chennai", "Vellore", 140},
         {"RT-04", "Bangalore", "Mysore", 160}
     };

     tblRoutes = new JTable(data, cols);
     tblRoutes.setRowHeight(28);
     tblRoutes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

     JScrollPane scroll = new JScrollPane(tblRoutes);
     scroll.setBorder(BorderFactory.createEmptyBorder());

     panel.add(vehiclePanel, BorderLayout.NORTH);
     panel.add(scroll, BorderLayout.CENTER);

     return panel;
 }

 /* ==============================================================
    BUTTON PANEL
    ============================================================== */
 private JPanel createButtonPanel() {
     JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
     panel.setOpaque(false);

     JButton btnAssign = createPrimaryButton("Assign Route");
     btnAssign.addActionListener(e -> assignRoute());

     JButton btnSave = createPrimaryButton("Save");
     btnSave.addActionListener(e -> saveRoute());

     JButton btnCancel = createSecondaryButton("Cancel");
     btnCancel.addActionListener(e -> goBack());

     panel.add(btnAssign);
     panel.add(btnSave);
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
     btn.setPreferredSize(new Dimension(200, 50));
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
    LOGIC – ASSIGN ROUTE
    ============================================================== */
 private void assignRoute() {
     int selectedRow = tblRoutes.getSelectedRow();

     if (selectedRow == -1) {
         JOptionPane.showMessageDialog(this, "Please select a route.", "Error", JOptionPane.WARNING_MESSAGE);
         return;
     }

     String vehicle = cbVehicles.getSelectedItem().toString();
     String routeId = tblRoutes.getValueAt(selectedRow, 0).toString();

     JOptionPane.showMessageDialog(this,
             "Route assigned successfully!\n\n" +
                     "Vehicle: " + vehicle + "\n" +
                     "Assigned Route: " + routeId,
             "Success",
             JOptionPane.INFORMATION_MESSAGE);
 }

 private void saveRoute() {
     JOptionPane.showMessageDialog(this,
             "Route assignment saved successfully!",
             "Saved",
             JOptionPane.INFORMATION_MESSAGE);
     goBack();
 }

 /* ==============================================================
    NAVIGATION
    ============================================================== */
 private void goBack() {
     dashboard.setVisible(true);
     this.dispose();
 }
}