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
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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

/* ==============================================================
REVENUE REPORT SCREEN
============================================================== */
class RevenueReportFrame extends JFrame {

 // Same color palette from RegisterVehicleFrame
 private static final Color NAVY = new Color(20, 52, 73);
 private static final Color TEAL = new Color(0, 91, 127);
 private static final Color LIME = new Color(166, 206, 57);
 private static final Color LIGHT_BG = new Color(245, 247, 250);
 private static final Color WHITE = Color.WHITE;
 private static final Color TEXT_DARK = new Color(33, 37, 41);
 private static final Color TEXT_LIGHT = new Color(108, 117, 125);

 private DashboardFrame dashboard;
 private JFormattedTextField tfStartDate;
 private JFormattedTextField tfEndDate;

 private JPanel resultsPanel;

 public RevenueReportFrame(DashboardFrame dashboard) {
     this.dashboard = dashboard;

     setTitle("TranspoLogic - Revenue Report");
     setSize(750, 750);
     setLocationRelativeTo(null);
     setDefaultCloseOperation(DISPOSE_ON_CLOSE);

     JPanel main = new JPanel(new BorderLayout());
     main.setBackground(LIGHT_BG);

     main.add(createTopBar(), BorderLayout.NORTH);
     main.add(createContentPanel(), BorderLayout.CENTER);

     add(main);
 }

 /* ==============================================================
    TOP BAR (same design as Register Vehicle)
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

     JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
     leftPanel.setOpaque(false);

     JButton btnBack = createTopBarButton("← Back");
     btnBack.addActionListener(e -> goBack());

     JLabel title = new JLabel("Revenue Report");
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

             if (getModel().isPressed()) {
                 g2d.setColor(new Color(166, 206, 57, 200));
             } else if (getModel().isRollover()) {
                 g2d.setColor(LIME);
             } else {
                 g2d.setColor(new Color(255, 255, 255, 35));
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
     btn.setPreferredSize(new Dimension(120, 40));

     return btn;
 }

 /* ==============================================================
    MAIN CONTENT PANEL
    ============================================================== */
 private JPanel createContentPanel() {
     JPanel content = new JPanel(new GridBagLayout());
     content.setOpaque(false);
     content.setBorder(new EmptyBorder(40, 40, 40, 40));

     JPanel card = createReportCard();
     content.add(card);

     return content;
 }

 /* ==============================================================
    CARD (same rounded-card style)
    ============================================================== */
 private JPanel createReportCard() {
     JPanel card = new JPanel() {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2d = (Graphics2D) g;
             g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                  RenderingHints.VALUE_ANTIALIAS_ON);

             g2d.setColor(WHITE);
             g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(),
                     getHeight(), 20, 20));

             GradientPaint gradient = new GradientPaint(
                 0, 0, LIME,
                 getWidth(), 0, new Color(146, 186, 47)
             );
             g2d.setPaint(gradient);
             g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 5,
                     20, 20));
         }
     };

     card.setLayout(new BorderLayout(0, 30));
     card.setOpaque(false);
     card.setBorder(new EmptyBorder(40, 45, 40, 45));
     card.setPreferredSize(new Dimension(560, 560));

     card.add(createHeader(), BorderLayout.NORTH);
     card.add(createFiltersPanel(), BorderLayout.CENTER);

     resultsPanel = createResultsPanel();
     card.add(resultsPanel, BorderLayout.SOUTH);

     return card;
 }

 /* HEADER */
 private JPanel createHeader() {
     JPanel panel = new JPanel();
     panel.setOpaque(false);
     panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

     JLabel icon = new JLabel("📊", SwingConstants.CENTER);
     icon.setFont(new Font("SansSerif", Font.PLAIN, 55));
     icon.setAlignmentX(Component.CENTER_ALIGNMENT);

     JLabel title = new JLabel("Revenue Report");
     title.setAlignmentX(Component.CENTER_ALIGNMENT);
     title.setFont(new Font("SansSerif", Font.BOLD, 24));
     title.setForeground(NAVY);

     JLabel subtitle = new JLabel("Generate revenue analysis for shipments");
     subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
     subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
     subtitle.setForeground(TEXT_LIGHT);

     panel.add(icon);
     panel.add(Box.createVerticalStrut(15));
     panel.add(title);
     panel.add(Box.createVerticalStrut(5));
     panel.add(subtitle);

     return panel;
 }

 /* FILTER PANEL */
 private JPanel createFiltersPanel() {
     JPanel panel = new JPanel();
     panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
     panel.setOpaque(false);

     JLabel lbl1 = new JLabel("Start Date (dd-mm-yyyy)");
     lbl1.setFont(new Font("SansSerif", Font.BOLD, 13));
     lbl1.setForeground(TEXT_DARK);

     JLabel lbl2 = new JLabel("End Date (dd-mm-yyyy)");
     lbl2.setFont(new Font("SansSerif", Font.BOLD, 13));
     lbl2.setForeground(TEXT_DARK);

     tfStartDate = createDateField();
     tfEndDate = createDateField();

     JButton btnGenerate = createPrimaryButton("Generate Report");
     btnGenerate.addActionListener(e -> showReport());

     panel.add(lbl1);
     panel.add(tfStartDate);
     panel.add(Box.createVerticalStrut(15));

     panel.add(lbl2);
     panel.add(tfEndDate);
     panel.add(Box.createVerticalStrut(20));

     panel.add(btnGenerate);

     return panel;
 }

 private JFormattedTextField createDateField() {
     JFormattedTextField tf = new JFormattedTextField();
     tf.setFont(new Font("SansSerif", Font.PLAIN, 14));
     tf.setBorder(new EmptyBorder(14, 15, 14, 15));
     tf.setOpaque(true);
     tf.setBackground(LIGHT_BG);
     tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
     return tf;
 }

 private JButton createPrimaryButton(String text) {
     JButton btn = new JButton(text) {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2d = (Graphics2D) g;

             if (getModel().isPressed())
                 g2d.setColor(new Color(146, 186, 47));
             else if (getModel().isRollover())
                 g2d.setPaint(new GradientPaint(0, 0, new Color(186, 226, 77),
                         getWidth(), 0, LIME));
             else
                 g2d.setPaint(new GradientPaint(0, 0, LIME,
                         getWidth(), 0, new Color(146, 186, 47)));

             g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(),
                     getHeight(), 10, 10));

             g2d.setColor(WHITE);
             g2d.setFont(getFont());
             FontMetrics fm = g2d.getFontMetrics();
             g2d.drawString(text,
                     (getWidth() - fm.stringWidth(text)) / 2,
                     ((getHeight() - fm.getHeight()) / 2) + fm.getAscent());
         }
     };

     btn.setFont(new Font("SansSerif", Font.BOLD, 15));
     btn.setForeground(WHITE);
     btn.setContentAreaFilled(false);
     btn.setBorderPainted(false);
     btn.setPreferredSize(new Dimension(220, 48));
     btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

     return btn;
 }

 /* RESULTS PANEL */
 private JPanel createResultsPanel() {
     JPanel panel = new JPanel();
     panel.setOpaque(false);
     panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
     return panel;
 }

 private void showReport() {
     resultsPanel.removeAll();

     JLabel total = new JLabel("Total Revenue: Rs 450,000");
     total.setFont(new Font("SansSerif", Font.BOLD, 16));
     total.setForeground(NAVY);

     JTable table = new JTable(
         new Object[][] {
             {"Shipment A", "Rs 120,000"},
             {"Shipment B", "Rs 95,000"},
             {"Shipment C", "Rs 150,000"},
             {"Shipment D", "Rs 85,000"},
         },
         new String[]{"Shipment", "Revenue"}
     );

     JScrollPane scroll = new JScrollPane(table);
     scroll.setPreferredSize(new Dimension(450, 150));

     resultsPanel.add(Box.createVerticalStrut(20));
     resultsPanel.add(total);
     resultsPanel.add(Box.createVerticalStrut(15));
     resultsPanel.add(scroll);

     resultsPanel.revalidate();
     resultsPanel.repaint();
 }

 private void goBack() {
     dashboard.setVisible(true);
     this.dispose();
 }
}