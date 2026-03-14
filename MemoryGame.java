import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;

public class MemoryGame extends JFrame {
    private int gridSize = 4; // Default: Medium difficulty
    
    private Card[] cards;
    private Card firstCard = null;
    private Card secondCard = null;
    private int matchesFound = 0;
    private javax.swing.Timer flipBackTimer;
    private javax.swing.Timer previewTimer;
    private JLabel statusLabel;
    private JLabel movesLabel;
    private JLabel timerLabel;
    private JLabel bestScoreLabel;
    private int moveCount = 0;
    private int seconds = 0;
    private javax.swing.Timer gameTimer;
    private JPanel gameBoardPanel;
    private boolean isPreviewMode = false;
    private int previewCountdown = 3; // Preview for 5 seconds
    
    // High scores
    private int bestMoves = Integer.MAX_VALUE;
    private int bestTime = Integer.MAX_VALUE;
    
    // Difficulty settings
    private String difficulty = "Medium";
    
    public MemoryGame() {
        setTitle("Memory Card Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Load high scores
        loadHighScores();
        
        // Set modern dark background
        Color bgDark = new Color(24, 26, 33);
        Color panelDark = new Color(34, 37, 46);
        Color accentColor = new Color(74, 144, 226);
        getContentPane().setBackground(bgDark);
        
        // ===== TOP: Beautiful Heading =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(panelDark);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 4, 0, accentColor),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel headingLabel = new JLabel("HIDE 'N' PICK", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Courier New", Font.BOLD, 50));
        headingLabel.setForeground(Color.WHITE);
        headerPanel.add(headingLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // ===== STATUS BAR =====
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(bgDark);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        statusPanel.setLayout(new GridLayout(2, 1, 0, 10));
        
        // Top row: Status message
        JPanel topStatusRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topStatusRow.setBackground(bgDark);
        statusLabel = new JLabel("Find all matching pairs!");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        statusLabel.setForeground(new Color(220, 220, 230));
        topStatusRow.add(statusLabel);
        
        // Bottom row: Stats
        JPanel bottomStatusRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        bottomStatusRow.setBackground(bgDark);
        
        movesLabel = new JLabel("Moves: 0");
        movesLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        movesLabel.setForeground(new Color(180, 190, 210));
        
        timerLabel = new JLabel("Time: 0s");
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        timerLabel.setForeground(new Color(180, 190, 210));
        
        bestScoreLabel = new JLabel("Best: " + (bestMoves == Integer.MAX_VALUE ? "---" : bestMoves + " moves"));
        bestScoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bestScoreLabel.setForeground(new Color(255, 185, 0)); // Gold
        
        bottomStatusRow.add(movesLabel);
        bottomStatusRow.add(timerLabel);
        bottomStatusRow.add(bestScoreLabel);
        
        statusPanel.add(topStatusRow);
        statusPanel.add(bottomStatusRow);
        
        // ===== CENTER: Game Board =====
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(bgDark);
        
        gameBoardPanel = new JPanel();
        updateGameBoard();
        
        centerWrapper.add(gameBoardPanel);
        
        JPanel mainCenterPanel = new JPanel(new BorderLayout());
        mainCenterPanel.setBackground(bgDark);
        mainCenterPanel.add(statusPanel, BorderLayout.NORTH);
        mainCenterPanel.add(centerWrapper, BorderLayout.CENTER);
        
        add(mainCenterPanel, BorderLayout.CENTER);
        
        // ===== BOTTOM: Buttons =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(bgDark);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        // Difficulty buttons (modern flat colors)
        JButton easyButton = createDifficultyButton("Easy (3×4)", new Color(46, 204, 113), 3);
        JButton mediumButton = createDifficultyButton("Medium (4×4)", new Color(243, 156, 18), 4);
        JButton hardButton = createDifficultyButton("Hard (6×6)", new Color(231, 76, 60), 6);
        
        // New game button
        JButton resetButton = new JButton("New Game");
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resetButton.setPreferredSize(new Dimension(180, 45));
        resetButton.setBackground(new Color(52, 152, 219));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setBorderPainted(false);
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.addActionListener(e -> resetGame());
        addHoverEffect(resetButton, new Color(52, 152, 219), new Color(41, 128, 185));
        
        bottomPanel.add(easyButton);
        bottomPanel.add(mediumButton);
        bottomPanel.add(hardButton);
        bottomPanel.add(Box.createHorizontalStrut(30));
        bottomPanel.add(resetButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Start the game
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 700));
        
        initializeCards();
        startTimer();
    }
    
    private JButton createDifficultyButton(String text, Color color, int size) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setPreferredSize(new Dimension(140, 45));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> changeDifficulty(size));
        addHoverEffect(button, color, color.brighter());
        return button;
    }
    
    private void addHoverEffect(JButton button, Color normal, Color hover) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hover);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(normal);
            }
        });
    }
    
    private void changeDifficulty(int newSize) {
        gridSize = newSize;
        difficulty = (newSize == 3) ? "Easy" : (newSize == 4) ? "Medium" : "Hard";
        resetGame();
    }
    
    private void updateGameBoard() {
        gameBoardPanel.removeAll();
        int r = gridSize == 3 ? 3 : gridSize;
        int c = gridSize == 3 ? 4 : gridSize;
        gameBoardPanel.setLayout(new GridLayout(r, c, 10, 10));
        gameBoardPanel.setBackground(new Color(34, 37, 46));
        gameBoardPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private void initializeCards() {
        updateGameBoard();
        
        int totalCards = gridSize == 3 ? 12 : gridSize * gridSize;
        int pairs = totalCards / 2;
        
        List<Integer> values = new ArrayList<>();
        for (int i = 1; i <= pairs; i++) {
            values.add(i % 8 == 0 ? 8 : i % 8); // Wrap around to 8 symbols
            values.add(i % 8 == 0 ? 8 : i % 8);
        }
        
        Collections.shuffle(values);
        
        int cardSize = 100;
        if (gridSize == 6) cardSize = 65;
        else if (gridSize == 3) cardSize = 115;
        
        Dimension d = new Dimension(cardSize, cardSize);
        cards = new Card[totalCards];
        for (int i = 0; i < totalCards; i++) {
            cards[i] = new Card(values.get(i));
            cards[i].setPreferredSize(d);
            cards[i].setMinimumSize(d);
            cards[i].setMaximumSize(d);
            cards[i].addActionListener(new CardClickListener());
            gameBoardPanel.add(cards[i]);
        }
        
        gameBoardPanel.revalidate();
        gameBoardPanel.repaint();
        
        // Start preview mode
        startPreviewMode();
    }
    
    private void startTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        seconds = 0;
        gameTimer = new javax.swing.Timer(1000, e -> {
            seconds++;
            timerLabel.setText("Time: " + seconds + "s");
        });
        gameTimer.start();
    }
    
    private void startPreviewMode() {
        isPreviewMode = true;
        previewCountdown = 3;
        
        // Show all cards
        for (Card card : cards) {
            card.setPreviewMode(true);
        }
        
        // Update status to show countdown
        statusLabel.setText("Memorize the cards! Starting in " + previewCountdown + " seconds...");
        
        // Start countdown timer
        if (previewTimer != null) {
            previewTimer.stop();
        }
        
        previewTimer = new javax.swing.Timer(1000, e -> {
            previewCountdown--;
            if (previewCountdown > 0) {
                statusLabel.setText("Memorize the cards! Starting in " + previewCountdown + " seconds...");
            } else {
                endPreviewMode();
                previewTimer.stop();
            }
        });
        previewTimer.start();
    }
    
    private void endPreviewMode() {
        isPreviewMode = false;
        
        // Hide all cards
        for (Card card : cards) {
            card.setPreviewMode(false);
        }
        
        statusLabel.setText("Find all matching pairs!");
        
        // Start the game timer
        startTimer();
    }
    
    private class CardClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Ignore clicks during preview mode
            if (isPreviewMode) {
                return;
            }
            
            Card clickedCard = (Card) e.getSource();
            
            if (clickedCard.isMatched() || clickedCard.isFlipped()) {
                return;
            }
            
            if (firstCard != null && secondCard != null) {
                return;
            }
            
            clickedCard.flip();
            
            if (firstCard == null) {
                firstCard = clickedCard;
            } else {
                secondCard = clickedCard;
                moveCount++;
                movesLabel.setText("Moves: " + moveCount);
                
                checkForMatch();
            }
        }
    }
    
    private void checkForMatch() {
        int totalCards = gridSize == 3 ? 12 : gridSize * gridSize;
        int totalPairs = totalCards / 2;
        
        if (firstCard.getValue() == secondCard.getValue()) {
            firstCard.setMatched(true);
            secondCard.setMatched(true);
            matchesFound++;
            statusLabel.setText("Match found! (" + matchesFound + "/" + totalPairs + ")");
            
            firstCard = null;
            secondCard = null;
            
            if (matchesFound == totalPairs) {
                gameTimer.stop();
                statusLabel.setText("YOU WON in " + moveCount + " moves and " + seconds + " seconds!");
                
                // Check if new high score
                if (moveCount < bestMoves) {
                    bestMoves = moveCount;
                    bestTime = seconds;
                    saveHighScores();
                    bestScoreLabel.setText("Best: " + bestMoves + " moves (NEW RECORD!)");
                    JOptionPane.showMessageDialog(this, 
                        "NEW HIGH SCORE!\n\n" +
                        "You beat your previous record!\n" +
                        "Moves: " + moveCount + "\n" +
                        "Time: " + seconds + "s",
                        "New Record!",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            statusLabel.setText("Not a match... try again!");
            
            if (flipBackTimer != null) {
                flipBackTimer.stop();
            }
            
            flipBackTimer = new javax.swing.Timer(1000, e -> {
                firstCard.flip();
                secondCard.flip();
                firstCard = null;
                secondCard = null;
                statusLabel.setText("Find all matching pairs!");
            });
            flipBackTimer.setRepeats(false);
            flipBackTimer.start();
        }
    }
    
    private void resetGame() {
        firstCard = null;
        secondCard = null;
        matchesFound = 0;
        moveCount = 0;
        seconds = 0;
        
        if (flipBackTimer != null) {
            flipBackTimer.stop();
        }
        
        if (previewTimer != null) {
            previewTimer.stop();
        }
        
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        initializeCards();
        
        movesLabel.setText("Moves: 0");
        timerLabel.setText("Time: 0s");
        
        // Don't start timer here - it will start after preview
    }
    
    // ===== HIGH SCORE SYSTEM =====
    private void loadHighScores() {
        try {
            File file = new File("highscores.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                if (line != null) {
                    String[] parts = line.split(",");
                    bestMoves = Integer.parseInt(parts[0]);
                    bestTime = Integer.parseInt(parts[1]);
                }
                reader.close();
            }
        } catch (Exception e) {
            // If file doesn't exist or error reading, use defaults
            bestMoves = Integer.MAX_VALUE;
            bestTime = Integer.MAX_VALUE;
        }
    }
    
    private void saveHighScores() {
        try {
            PrintWriter writer = new PrintWriter("highscores.txt");
            writer.println(bestMoves + "," + bestTime);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MemoryGame game = new MemoryGame();
            game.setVisible(true);
        });
    }
}