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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

//import bl.DriverService;
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
CREATE DRIVER PROFILE SCREEN
============================================================== */
class CreateDriverProfileFrame extends JFrame {
 
 // Color palette (same as your theme)
 private static final Color NAVY = new Color(20, 52, 73);
 private static final Color TEAL = new Color(0, 91, 127);
 private static final Color LIME = new Color(166, 206, 57);
 private static final Color PINK = new Color(232, 49, 110);
 private static final Color YELLOW = new Color(255, 198, 41);
 private static final Color LIGHT_BG = new Color(245, 247, 250);
 private static final Color WHITE = Color.WHITE;
 private static final Color TEXT_DARK = new Color(33, 37, 41);
 private static final Color TEXT_LIGHT = new Color(108, 117, 125);

 private JTextField tfName;
 private JTextField tfCnic;
 private JTextField tfLicense;
 private JTextField tfExperience;
 private JTextField tfHours;

 private DashboardFrame dashboard;

 public CreateDriverProfileFrame(DashboardFrame dashboard) {
     this.dashboard = dashboard;

     setTitle("TranspoLogic - Create Driver Profile");
     setSize(800, 900);
     setMinimumSize(new Dimension(700, 850));
     setLocationRelativeTo(null);
     setDefaultCloseOperation(DISPOSE_ON_CLOSE);
     setResizable(true);

     JPanel main = new JPanel(new BorderLayout());
     main.setBackground(LIGHT_BG);

     JPanel topBar = createTopBar();
     main.add(topBar, BorderLayout.NORTH);

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

     JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
     leftPanel.setOpaque(false);

     JButton btnBack = createTopBarButton("← Back");
     btnBack.addActionListener(e -> goBack());

     JLabel title = new JLabel("Create Driver Profile");
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
     JPanel content = new JPanel(new BorderLayout()); // Changed to BorderLayout for better scroll pane handling
     content.setBackground(LIGHT_BG);
     content.setBorder(new EmptyBorder(20, 20, 20, 20)); // Reduced padding to give more space

     JPanel card = createFormCard();
     
     // Create scroll pane and configure it properly
     JScrollPane scrollPane = new JScrollPane(card);
     scrollPane.setBorder(BorderFactory.createEmptyBorder());
     scrollPane.getViewport().setBackground(LIGHT_BG);
     scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
     scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
     scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling
     
     // Remove the border from the scroll pane
     scrollPane.setBorder(null);
     
     content.add(scrollPane, BorderLayout.CENTER);

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

             GradientPaint gradient = new GradientPaint(
                 0, 0, LIME,
                 getWidth(), 0, new Color(146, 186, 47)
             );
             g2d.setPaint(gradient);
             g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 5, 20, 20));
         }
     };

     card.setLayout(new BorderLayout(0, 30));
     card.setOpaque(false);
     card.setBorder(new EmptyBorder(40, 50, 40, 50)); // Reduced vertical padding
     card.setPreferredSize(new Dimension(600, 800)); // Increased height to accommodate all fields
     card.setMaximumSize(new Dimension(650, 950)); // Allow it to grow taller if needed

     card.add(createHeaderPanel(), BorderLayout.NORTH);
     card.add(createFormFields(), BorderLayout.CENTER);
     card.add(createButtonPanel(), BorderLayout.SOUTH);

     return card;
 }

 private JPanel createHeaderPanel() {
     JPanel panel = new JPanel();
     panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
     panel.setOpaque(false);

     JLabel icon = new JLabel("👤", SwingConstants.CENTER);
     icon.setFont(new Font("SansSerif", Font.PLAIN, 50));
     icon.setAlignmentX(Component.CENTER_ALIGNMENT);

     JLabel title = new JLabel("Driver Profile");
     title.setFont(new Font("SansSerif", Font.BOLD, 24));
     title.setForeground(NAVY);
     title.setAlignmentX(Component.CENTER_ALIGNMENT);

     JLabel subtitle = new JLabel("Enter driver details below");
     subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
     subtitle.setForeground(TEXT_LIGHT);
     subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

     panel.add(icon);
     panel.add(Box.createVerticalStrut(10)); // Reduced spacing
     panel.add(title);
     panel.add(Box.createVerticalStrut(5));
     panel.add(subtitle);

     return panel;
 }

 private JPanel createFormFields() {
     JPanel panel = new JPanel();
     panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
     panel.setOpaque(false);

     // Driver Name
     JLabel lblName = new JLabel("Driver Name *");
     lblName.setFont(new Font("SansSerif", Font.BOLD, 13));
     lblName.setForeground(TEXT_DARK);

     tfName = createStyledTextField();

     // CNIC
     JLabel lblCnic = new JLabel("CNIC *");
     lblCnic.setFont(new Font("SansSerif", Font.BOLD, 13));
     lblCnic.setForeground(TEXT_DARK);

     tfCnic = createStyledTextField();

     // License No
     JLabel lblLicense = new JLabel("License No *");
     lblLicense.setFont(new Font("SansSerif", Font.BOLD, 13));
     lblLicense.setForeground(TEXT_DARK);

     tfLicense = createStyledTextField();

     // Experience
     JLabel lblExp = new JLabel("Experience (years) *");
     lblExp.setFont(new Font("SansSerif", Font.BOLD, 13));
     lblExp.setForeground(TEXT_DARK);

     tfExperience = createStyledTextField();

     // Working Hours
     JLabel lblHours = new JLabel("Working Hours *");
     lblHours.setFont(new Font("SansSerif", Font.BOLD, 13));
     lblHours.setForeground(TEXT_DARK);

     tfHours = createStyledTextField();

     // Add fields with reduced spacing
     panel.add(lblName);
     panel.add(Box.createVerticalStrut(6)); // Reduced from 8
     panel.add(tfName);
     panel.add(Box.createVerticalStrut(15)); // Reduced from 20

     panel.add(lblCnic);
     panel.add(Box.createVerticalStrut(6));
     panel.add(tfCnic);
     panel.add(Box.createVerticalStrut(15));

     panel.add(lblLicense);
     panel.add(Box.createVerticalStrut(6));
     panel.add(tfLicense);
     panel.add(Box.createVerticalStrut(15));

     panel.add(lblExp);
     panel.add(Box.createVerticalStrut(6));
     panel.add(tfExperience);
     panel.add(Box.createVerticalStrut(15));

     panel.add(lblHours);
     panel.add(Box.createVerticalStrut(6));
     panel.add(tfHours);

     return panel;
 }

 private JTextField createStyledTextField() {
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
     field.setBorder(new EmptyBorder(12, 15, 12, 15)); // Reduced vertical padding
     field.setOpaque(false);
     field.setForeground(TEXT_DARK);
     field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Reduced height
     field.setPreferredSize(new Dimension(450, 45)); // Reduced height

     return field;
 }

 private JPanel createButtonPanel() {
     JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
     panel.setOpaque(false);
     panel.setBorder(new EmptyBorder(10, 0, 0, 0)); // Added top margin

     JButton btnCreate = new JButton("Create Profile") {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2d = (Graphics2D) g;
             g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                  RenderingHints.VALUE_ANTIALIAS_ON);

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
             g2d.setFont(getFont());
             FontMetrics fm = g2d.getFontMetrics();
             int x = (getWidth() - fm.stringWidth(getText())) / 2;
             int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
             g2d.drawString(getText(), x, y);
         }
     };

     btnCreate.setFont(new Font("SansSerif", Font.BOLD, 15));
     btnCreate.setForeground(WHITE);
     btnCreate.setContentAreaFilled(false);
     btnCreate.setBorderPainted(false);
     btnCreate.setFocusPainted(false);
     btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
     btnCreate.setPreferredSize(new Dimension(200, 45)); // Reduced height
     btnCreate.addActionListener(e -> saveDriver());

     JButton btnCancel = new JButton("Cancel") {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2d = (Graphics2D) g;
             g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                  RenderingHints.VALUE_ANTIALIAS_ON);

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

             g2d.setFont(getFont());
             FontMetrics fm = g2d.getFontMetrics();
             int x = (getWidth() - fm.stringWidth(getText())) / 2;
             int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
             g2d.drawString(getText(), x, y);
         }
     };

     btnCancel.setFont(new Font("SansSerif", Font.BOLD, 15));
     btnCancel.setForeground(TEXT_DARK);
     btnCancel.setContentAreaFilled(false);
     btnCancel.setBorderPainted(false);
     btnCancel.setFocusPainted(false);
     btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
     btnCancel.setPreferredSize(new Dimension(150, 45)); // Reduced height
     btnCancel.addActionListener(e -> goBack());

     panel.add(btnCreate);
     panel.add(btnCancel);

     return panel;
 }

 private void saveDriver() {
	    String name = tfName.getText().trim();
	    String cnic = tfCnic.getText().trim();
	    String license = tfLicense.getText().trim();
	    String expStr = tfExperience.getText().trim();
	    String hoursStr = tfHours.getText().trim();

	    if (name.isEmpty() || cnic.isEmpty() || license.isEmpty() ||
	        expStr.isEmpty() || hoursStr.isEmpty()) {

	        JOptionPane.showMessageDialog(this,
	            "Please fill in all required fields",
	            "Validation Error",
	            JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    int experience, hours;
	    try {
	        experience = Integer.parseInt(expStr);
	        hours = Integer.parseInt(hoursStr);
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this,
	            "Experience and Working Hours must be numbers",
	            "Validation Error",
	            JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    DriverService driverService = new DriverService();
	    boolean success = driverService.addDriver(name, cnic, license, experience, hours);

	    if (success) {
	        JOptionPane.showMessageDialog(this,
	            "Driver profile created successfully!",
	            "Success",
	            JOptionPane.INFORMATION_MESSAGE);
	        goBack();
	    } else {
	        JOptionPane.showMessageDialog(this,
	            "Failed to create driver profile. CNIC or License may already exist.",
	            "Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}

 private void goBack() {
     dashboard.setVisible(true);
     this.dispose();
 }
}