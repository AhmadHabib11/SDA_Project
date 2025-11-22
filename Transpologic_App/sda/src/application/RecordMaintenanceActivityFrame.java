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
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

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

class RecordMaintenanceActivityFrame extends JFrame {

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
 private JTextField txtDate, txtCost, txtNextServiceDate;
 private JTextArea txtDescription;

 private DashboardFrame dashboard;

 public RecordMaintenanceActivityFrame(DashboardFrame dashboard) {
     this.dashboard = dashboard;

     setTitle("TranspoLogic - Record Maintenance Activity");
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

     JLabel title = new JLabel("Record Maintenance Activity");
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
             if (getModel().isPressed()) g2d.setColor(LIME);
             else if (getModel().isRollover()) g2d.setColor(new Color(180, 240, 100));
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

             GradientPaint gradient = new GradientPaint(0, 0, YELLOW, getWidth(), 0, new Color(225, 178, 20));
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

     JLabel icon = new JLabel("🛠️", SwingConstants.CENTER);
     icon.setFont(new Font("SansSerif", Font.PLAIN, 50));
     icon.setAlignmentX(Component.CENTER_ALIGNMENT);

     JLabel title = new JLabel("Record Maintenance Activity");
     title.setFont(new Font("SansSerif", Font.BOLD, 24));
     title.setForeground(NAVY);
     title.setAlignmentX(Component.CENTER_ALIGNMENT);

     JLabel subtitle = new JLabel("Enter vehicle maintenance details");
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
    FORM PANEL — Two Column Layout
    ============================================================== */
 private JPanel createFormPanel() {
     JPanel form = new JPanel(new GridLayout(1, 2, 30, 0));
     form.setOpaque(false);

     // LEFT COLUMN
     JPanel left = new JPanel();
     left.setOpaque(false);
     left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

     JLabel lblVehicle = new JLabel("Select Vehicle *");
     lblVehicle.setFont(new Font("SansSerif", Font.BOLD, 14));
     lblVehicle.setForeground(TEXT_DARK);

     cbVehicles = new JComboBox<>(new String[]{
         "TRK-001 | Tata Ace",
         "TRK-002 | Mahindra Bolero",
         "TRK-003 | Eicher Pro"
     });
     cbVehicles.setFont(new Font("SansSerif", Font.PLAIN, 14));
     cbVehicles.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

     JLabel lblDate = new JLabel("Maintenance Date *");
     lblDate.setFont(new Font("SansSerif", Font.BOLD, 14));
     lblDate.setForeground(TEXT_DARK);

     txtDate = new JTextField();
     txtDate.setPreferredSize(new Dimension(200, 40));

     JLabel lblCost = new JLabel("Cost (₹) *");
     lblCost.setFont(new Font("SansSerif", Font.BOLD, 14));
     lblCost.setForeground(TEXT_DARK);

     txtCost = new JTextField();
     txtCost.setPreferredSize(new Dimension(200, 40));

     left.add(lblVehicle);
     left.add(Box.createVerticalStrut(8));
     left.add(cbVehicles);
     left.add(Box.createVerticalStrut(20));

     left.add(lblDate);
     left.add(Box.createVerticalStrut(8));
     left.add(txtDate);
     left.add(Box.createVerticalStrut(20));

     left.add(lblCost);
     left.add(Box.createVerticalStrut(8));
     left.add(txtCost);

     // RIGHT COLUMN
     JPanel right = new JPanel();
     right.setOpaque(false);
     right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

     JLabel lblDesc = new JLabel("Maintenance Description *");
     lblDesc.setFont(new Font("SansSerif", Font.BOLD, 14));
     lblDesc.setForeground(TEXT_DARK);

     txtDescription = new JTextArea(6, 20);
     txtDescription.setLineWrap(true);
     txtDescription.setWrapStyleWord(true);

     JScrollPane scrollDesc = new JScrollPane(txtDescription);
     scrollDesc.setPreferredSize(new Dimension(200, 150));

     JLabel lblNextService = new JLabel("Next Service Date");
     lblNextService.setFont(new Font("SansSerif", Font.BOLD, 14));
     lblNextService.setForeground(TEXT_DARK);

     txtNextServiceDate = new JTextField();
     txtNextServiceDate.setPreferredSize(new Dimension(200, 40));

     right.add(lblDesc);
     right.add(Box.createVerticalStrut(8));
     right.add(scrollDesc);
     right.add(Box.createVerticalStrut(20));

     right.add(lblNextService);
     right.add(Box.createVerticalStrut(8));
     right.add(txtNextServiceDate);

     form.add(left);
     form.add(right);

     return form;
 }

 /* ==============================================================
    BUTTON PANEL
    ============================================================== */
 private JPanel createButtonPanel() {
     JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
     panel.setOpaque(false);

     JButton btnSave = createPrimaryButton("Save");
     btnSave.addActionListener(e -> saveRecord());

     JButton btnCancel = createSecondaryButton("Cancel");
     btnCancel.addActionListener(e -> goBack());

     panel.add(btnSave);
     panel.add(btnCancel);

     return panel;
 }

 private JButton createPrimaryButton(String text) {
     JButton btn = new JButton(text);
     btn.setFont(new Font("SansSerif", Font.BOLD, 15));
     btn.setContentAreaFilled(false);
     btn.setBorderPainted(true);
     btn.setFocusPainted(false);
     btn.setOpaque(true);
     btn.setBackground(YELLOW);
     btn.setForeground(Color.BLACK);
     btn.setPreferredSize(new Dimension(180, 50));
     return btn;
 }

 private JButton createSecondaryButton(String text) {
     JButton btn = new JButton(text);
     btn.setFont(new Font("SansSerif", Font.BOLD, 15));
     btn.setOpaque(true);
     btn.setBackground(Color.BLACK);
     btn.setForeground(Color.WHITE);
     btn.setPreferredSize(new Dimension(180, 50));
     return btn;
 }

 /* ==============================================================
    LOGIC – SAVE MAINTENANCE RECORD
    ============================================================== */
 private void saveRecord() {
     JOptionPane.showMessageDialog(this,
             "Maintenance record saved successfully!",
             "Success",
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