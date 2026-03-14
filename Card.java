import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Card extends JButton {
    private int value;
    private String symbol;
    private boolean flipped;
    private boolean matched;
    private boolean hovered;
    private boolean previewMode;
    
    private static final Color BACK_COLOR = new Color(74, 144, 226); // Vibrant Blue
    private static final Color BACK_HOVER_COLOR = new Color(92, 162, 244);
    private static final Color FRONT_COLOR = new Color(250, 250, 250); // Clean White
    private static final Color MATCHED_COLOR = new Color(46, 204, 113); // Success Green
    
    // Array of standard unicode symbols for the cards
    private static final String[] SYMBOLS = {
        "1", "2", "3", "4", 
        "5", "6", "7", "8"
    };
    
    public Card(int value) {
        this.value = value;
        this.symbol = SYMBOLS[value - 1]; // value 1-8, array index 0-7
        this.flipped = false;
        this.matched = false;
        this.hovered = false;
        this.previewMode = false;
        
        setPreferredSize(new Dimension(110, 110));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!matched && !flipped) {
                    hovered = true;
                    repaint();
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }
    
    public void flip() {
        flipped = !flipped;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        int arc = Math.max(10, Math.min(w, h) * 20 / 110); // rounded corners
        
        if (matched) {
            // Matched State (Green)
            g2.setColor(MATCHED_COLOR);
            g2.fillRoundRect(2, 2, w-4, h-4, arc, arc);
            
            // Subtle glow
            g2.setColor(new Color(255, 255, 255, 120));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(2, 2, w-4, h-4, arc, arc);
            
            // Draw checkmark manually for better visibility
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(Math.max(3, w / 20), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            int centerX = w / 2;
            int centerY = h / 2;
            int size = Math.min(w, h) / 3;
            
            // Draw checkmark as two lines
            int x1 = centerX - size/2;
            int y1 = centerY;
            int x2 = centerX - size/6;
            int y2 = centerY + size/2;
            int x3 = centerX + size/2;
            int y3 = centerY - size/3;
            
            g2.drawLine(x1, y1, x2, y2);
            g2.drawLine(x2, y2, x3, y3);
        } else if (flipped || previewMode) {
            // Front State (White with Symbol) - show during flip OR preview
            g2.setColor(FRONT_COLOR);
            g2.fillRoundRect(2, 2, w-4, h-4, arc, arc);
            
            // Subtle shadow inner border
            g2.setColor(new Color(0, 0, 0, 30));
            g2.drawRoundRect(2, 2, w-4, h-4, arc, arc);
            
            // Draw Symbol
            g2.setColor(new Color(40, 40, 50));
            g2.setFont(new Font("Segoe UI", Font.BOLD, Math.max(12, Math.min(w, h) * 46 / 110)));
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(symbol);
            int th = fm.getAscent();
            g2.drawString(symbol, (w - tw)/2, (h + th)/2 - fm.getDescent() + 2);
        } else {
            // Back State (Gradient Blue)
            Color bg = hovered ? BACK_HOVER_COLOR : BACK_COLOR;
            GradientPaint gp = new GradientPaint(0, 0, bg.brighter(), 0, h, bg.darker());
            g2.setPaint(gp);
            g2.fillRoundRect(2, 2, w-4, h-4, arc, arc);
            
            if (hovered) {
                // Glow effect or border
                g2.setColor(new Color(255, 255, 255, 120));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(3, 3, w-6, h-6, arc, arc);
            } else {
                // Normal subtle border
                g2.setColor(new Color(255, 255, 255, 40));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(2, 2, w-4, h-4, arc, arc);
            }
            
            // Draw ? symbol
            g2.setColor(new Color(255, 255, 255, 160));
            g2.setFont(new Font("Segoe UI", Font.BOLD, Math.max(12, Math.min(w, h) * 52 / 110)));
            FontMetrics fm = g2.getFontMetrics();
            String text = "?";
            int tw = fm.stringWidth(text);
            int th = fm.getAscent();
            g2.drawString(text, (w - tw)/2, (h + th)/2 - fm.getDescent() + 4);
        }
        
        g2.dispose();
    }
    
    public int getValue() {
        return value;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public boolean isFlipped() {
        return flipped;
    }
    
    public boolean isMatched() {
        return matched;
    }
    
    public void setMatched(boolean matched) {
        this.matched = matched;
        repaint();
    }
    
    public void setPreviewMode(boolean previewMode) {
        this.previewMode = previewMode;
        repaint();
    }
}