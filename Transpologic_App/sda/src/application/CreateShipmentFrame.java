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

import bl.ShipmentService;

/* ==============================================================
CREATE SHIPMENT SCREEN
============================================================== */
class CreateShipmentFrame extends JFrame {
    //Same theme palette
    private static final Color NAVY = new Color(20, 52, 73);
    private static final Color TEAL = new Color(0, 91, 127);
    private static final Color LIME = new Color(166, 206, 57);
    private static final Color PINK = new Color(232, 49, 110);
    private static final Color YELLOW = new Color(255, 198, 41);
    private static final Color LIGHT_BG = new Color(245, 247, 250);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color TEXT_LIGHT = new Color(108, 117, 125);

    private JTextField tfCargoType;
    private JTextField tfWeight;
    private JTextField tfDestination;
    private JTextField tfPickup;

    private DashboardFrame dashboard;

    public CreateShipmentFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("TranspoLogic - Create Shipment");
        setSize(700, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(LIGHT_BG);

        main.add(createTopBar(), BorderLayout.NORTH);
        main.add(createContentPanel(), BorderLayout.CENTER);

        add(main);
    }

    /* --------------------------------------------------------------
    TOP BAR
    -------------------------------------------------------------- */
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

        JLabel title = new JLabel("Create Shipment");
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

    /* --------------------------------------------------------------
    CONTENT PANEL
    -------------------------------------------------------------- */
    private JPanel createContentPanel() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(LIGHT_BG);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel card = createFormCard();
        content.add(card, BorderLayout.CENTER);

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
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

                g2d.setPaint(new GradientPaint(0, 0, LIME, getWidth(), 0, new Color(146, 186, 47)));
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 5, 20, 20));
            }
        };

        card.setOpaque(false);
        card.setLayout(new BorderLayout(0, 20));
        card.setBorder(new EmptyBorder(30, 50, 30, 50));
        card.setPreferredSize(new Dimension(550, 600));

        // Add header (always visible)
        card.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Add scrollable form fields in the center
        JScrollPane scrollPane = createScrollableFormFields();
        card.add(scrollPane, BorderLayout.CENTER);
        
        // Add buttons (always visible)
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JScrollPane createScrollableFormFields() {
        JPanel formFieldsPanel = createFormFields();
        
        JScrollPane scrollPane = new JScrollPane(formFieldsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(WHITE);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        
        return scrollPane;
    }

    /* --------------------------------------------------------------
    HEADER
    -------------------------------------------------------------- */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel icon = new JLabel("📦", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 50));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Create Shipment");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(NAVY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Enter shipment details below");
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

    /* --------------------------------------------------------------
    FORM FIELDS
    -------------------------------------------------------------- */
    private JPanel createFormFields() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel lblCargo = new JLabel("Cargo Type *");
        lblCargo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblCargo.setForeground(TEXT_DARK);
        tfCargoType = createStyledTextField();

        JLabel lblWeight = new JLabel("Weight (kg) *");
        lblWeight.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblWeight.setForeground(TEXT_DARK);
        tfWeight = createStyledTextField();

        JLabel lblDest = new JLabel("Destination *");
        lblDest.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblDest.setForeground(TEXT_DARK);
        tfDestination = createStyledTextField();

        JLabel lblPickup = new JLabel("Pickup Point *");
        lblPickup.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPickup.setForeground(TEXT_DARK);
        tfPickup = createStyledTextField();

        // Add all fields with proper spacing
        panel.add(lblCargo); 
        panel.add(Box.createVerticalStrut(6));
        panel.add(tfCargoType); 
        panel.add(Box.createVerticalStrut(15));

        panel.add(lblWeight); 
        panel.add(Box.createVerticalStrut(6));
        panel.add(tfWeight); 
        panel.add(Box.createVerticalStrut(15));

        panel.add(lblDest); 
        panel.add(Box.createVerticalStrut(6));
        panel.add(tfDestination); 
        panel.add(Box.createVerticalStrut(15));

        panel.add(lblPickup); 
        panel.add(Box.createVerticalStrut(6));
        panel.add(tfPickup);
        
        // Add some extra space at the bottom to ensure all fields are accessible
        panel.add(Box.createVerticalStrut(10));

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
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

                super.paintComponent(g);
            }
        };

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(12, 15, 12, 15));
        field.setOpaque(false);
        field.setForeground(TEXT_DARK);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setPreferredSize(new Dimension(450, 45));
        return field;
    }

    /* --------------------------------------------------------------
    BUTTON PANEL
    -------------------------------------------------------------- */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnCreate = new JButton("Create Shipment") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                     RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(146, 186, 47));
                } else if (getModel().isRollover()) {
                    g2d.setPaint(new GradientPaint(0, 0, new Color(186, 226, 77),
                                                   getWidth(), 0, LIME));
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

        btnCreate.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnCreate.setForeground(WHITE);
        btnCreate.setContentAreaFilled(false);
        btnCreate.setBorderPainted(false);
        btnCreate.setFocusPainted(false);
        btnCreate.setPreferredSize(new Dimension(220, 45));
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreate.addActionListener(e -> createShipment());

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
        btnCancel.setPreferredSize(new Dimension(150, 45));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> goBack());

        panel.add(btnCreate);
        panel.add(btnCancel);

        return panel;
    }

    /* --------------------------------------------------------------
    BACK + SAVE
    -------------------------------------------------------------- */
    private void createShipment() {
        String cargo = tfCargoType.getText().trim();
        String weight = tfWeight.getText().trim();
        String dest = tfDestination.getText().trim();
        String pickup = tfPickup.getText().trim();

        if (cargo.isEmpty() || weight.isEmpty() ||
            dest.isEmpty() || pickup.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                "Please fill all required fields.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        ShipmentService service = new ShipmentService();
        boolean success = service.createShipment(cargo, weight, dest, pickup);

        if (success) {
            JOptionPane.showMessageDialog(this,
                "Shipment created!\n\n" +
                "Cargo: " + cargo + "\n" +
                "Weight: " + weight + " kg\n" +
                "Destination: " + dest + "\n" +
                "Pickup: " + pickup,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
            goBack();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to create shipment.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void goBack() {
        dashboard.setVisible(true);
        dispose();
    }
}