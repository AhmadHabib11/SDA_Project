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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/* ==============================================================
DRIVER PERFORMANCE REPORT SCREEN
============================================================== */
class DriverPerformanceReportFrame extends JFrame {

 // Same color palette as other frames
 private static final Color NAVY = new Color(20, 52, 73);
 private static final Color TEAL = new Color(0, 91, 127);
 private static final Color LIME = new Color(166, 206, 57);
 private static final Color LIGHT_BG = new Color(245, 247, 250);
 private static final Color WHITE = Color.WHITE;
 private static final Color TEXT_DARK = new Color(33, 37, 41);
 private static final Color TEXT_LIGHT = new Color(108, 117, 125);

 private DashboardFrame dashboard;
 private JComboBox<String> cbDrivers;
 private JPanel resultsPanel;

 public DriverPerformanceReportFrame(DashboardFrame dashboard) {
     this.dashboard = dashboard;

     setTitle("TranspoLogic - Driver Performance");
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

     JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
     leftPanel.setOpaque(false);

     JButton btnBack = createTopBarButton("← Back");
     btnBack.addActionListener(e -> goBack());

     JLabel title = new JLabel("Driver Performance");
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
    CONTENT
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
    CARD (rounded, accent bar)
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

     JLabel icon = new JLabel("🚚", SwingConstants.CENTER);
     icon.setFont(new Font("SansSerif", Font.PLAIN, 55));
     icon.setAlignmentX(Component.CENTER_ALIGNMENT);

     JLabel title = new JLabel("Driver Performance Report");
     title.setAlignmentX(Component.CENTER_ALIGNMENT);
     title.setFont(new Font("SansSerif", Font.BOLD, 24));
     title.setForeground(NAVY);

     JLabel subtitle = new JLabel("Select driver to view trips, delays, rating and workload");
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

 /* FILTERS - driver dropdown + generate button */
 private JPanel createFiltersPanel() {
     JPanel panel = new JPanel();
     panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
     panel.setOpaque(false);

     JLabel lblDriver = new JLabel("Select Driver");
     lblDriver.setFont(new Font("SansSerif", Font.BOLD, 13));
     lblDriver.setForeground(TEXT_DARK);

     cbDrivers = createDriverComboBox();

     JButton btnGenerate = createPrimaryButton("Generate Report");
     btnGenerate.addActionListener(e -> showReport());

     panel.add(lblDriver);
     panel.add(cbDrivers);
     panel.add(Box.createVerticalStrut(20));
     panel.add(btnGenerate);

     return panel;
 }

 private JComboBox<String> createDriverComboBox() {
     // sample drivers - replace with real data source when available
     String[] drivers = { "Select driver...", "Ali Khan", "Sara Qureshi", "M. Ahmed", "Zain Noor" };
     JComboBox<String> cb = new JComboBox<>(drivers);
     cb.setFont(new Font("SansSerif", Font.PLAIN, 14));
     cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
     cb.setBorder(new EmptyBorder(10, 12, 10, 12));
     cb.setBackground(LIGHT_BG);
     return cb;
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

 /* RESULTS PANEL (empty initially) */
 private JPanel createResultsPanel() {
     JPanel panel = new JPanel();
     panel.setOpaque(false);
     panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
     return panel;
 }

 /* showReport - populates the results area for selected driver */
 private void showReport() {
     resultsPanel.removeAll();

     String driver = (String) cbDrivers.getSelectedItem();
     if (driver == null || driver.equals("Select driver...")) {
         JOptionPane.showMessageDialog(this,
             "Please select a driver to generate report",
             "Validation", JOptionPane.WARNING_MESSAGE);
         return;
     }

     // Header: driver name
     JLabel hdr = new JLabel("Performance Summary — " + driver);
     hdr.setFont(new Font("SansSerif", Font.BOLD, 16));
     hdr.setForeground(NAVY);

     // Summary cards (trips, delays, rating, workload) using simple panels
     JPanel summaryRow = new JPanel(new GridLayout(1, 4, 12, 12));
     summaryRow.setOpaque(false);
     summaryRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

     summaryRow.add(makeMetricCard("Trips Completed", "48"));
     summaryRow.add(makeMetricCard("Delays", "3"));
     summaryRow.add(makeMetricCard("Driver Rating", "4.6 / 5"));
     summaryRow.add(makeMetricCard("Workload", "Avg 6 trips/week"));

     // Detailed table (per-trip sample)
     JTable table = new JTable(
         new Object[][] {
             {"SHP001", "2025-10-01", "On time"},
             {"SHP012", "2025-10-03", "Delayed (15m)"},
             {"SHP021", "2025-10-07", "On time"},
             {"SHP034", "2025-10-11", "On time"},
         },
         new String[] {"Shipment ID", "Date", "Status"}
     );

     JScrollPane scroll = new JScrollPane(table);
     scroll.setPreferredSize(new Dimension(500, 150));

     // Chart placeholder
     JPanel chartPlaceholder = new JPanel();
     chartPlaceholder.setPreferredSize(new Dimension(520, 140));
     chartPlaceholder.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
     chartPlaceholder.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
     chartPlaceholder.setBackground(new Color(250, 250, 250));
     chartPlaceholder.add(new JLabel("Performance chart will appear here"));

     // Assemble results
     resultsPanel.add(Box.createVerticalStrut(18));
     resultsPanel.add(hdr);
     resultsPanel.add(Box.createVerticalStrut(12));
     resultsPanel.add(summaryRow);
     resultsPanel.add(Box.createVerticalStrut(16));
     resultsPanel.add(new JLabel("Recent Trips"));
     resultsPanel.add(Box.createVerticalStrut(8));
     resultsPanel.add(scroll);
     resultsPanel.add(Box.createVerticalStrut(16));
     resultsPanel.add(new JLabel("Trend"));
     resultsPanel.add(Box.createVerticalStrut(8));
     resultsPanel.add(chartPlaceholder);

     resultsPanel.revalidate();
     resultsPanel.repaint();
 }

 private JPanel makeMetricCard(String title, String value) {
     JPanel card = new JPanel() {
         private boolean hovered = false;
         {
             setOpaque(false);
             addMouseListener(new java.awt.event.MouseAdapter() {
                 @Override
                 public void mouseEntered(java.awt.event.MouseEvent e) { hovered = true; repaint(); }
                 @Override
                 public void mouseExited(java.awt.event.MouseEvent e) { hovered = false; repaint(); }
             });
         }
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2d = (Graphics2D) g;
             g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                  RenderingHints.VALUE_ANTIALIAS_ON);

             if (hovered) {
                 g2d.setColor(new Color(0,0,0,12));
                 g2d.fill(new RoundRectangle2D.Float(4, 4, getWidth()-8, getHeight()-8, 12, 12));
             }

             g2d.setColor(WHITE);
             g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

             g2d.setColor(new Color(230, 230, 230));
             g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 6, 12, 12));
         }
     };

     card.setLayout(new BorderLayout());
     card.setBorder(new EmptyBorder(12,12,12,12));
     card.setPreferredSize(new Dimension(120, 90));

     JLabel lblTitle = new JLabel(title);
     lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
     lblTitle.setForeground(TEXT_DARK);

     JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
     lblValue.setFont(new Font("SansSerif", Font.BOLD, 18));
     lblValue.setForeground(NAVY);

     card.add(lblTitle, BorderLayout.NORTH);
     card.add(lblValue, BorderLayout.CENTER);

     return card;
 }

 private void goBack() {
     dashboard.setVisible(true);
     this.dispose();
 }
}