# 🎮 Hide N Pick - Card Matching Game

A feature-rich memory matching game built with Java Swing. Test your memory skills by finding all matching pairs with a countdown preview mode!

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Complete-success?style=for-the-badge)

---

## 📖 Table of Contents
- [About](#about)
- [Features](#features)
- [Installation](#installation)
- [How to Play](#how-to-play)
- [Game Modes](#game-modes)
- [Technologies Used](#technologies-used)
- [Customization](#customization)
- [Future Enhancements](#future-enhancements)

---

## 🎯 About

**FlipNMatch** is a modern take on the classic memory card game. Players must find matching pairs of symbols by remembering their positions. The game features multiple difficulty levels, a preview mode to help you memorize card positions, high score tracking, and a sleek dark-themed interface.

---

## ✨ Features

### 🎮 **Core Gameplay**
- **Preview Mode** - All cards shown for 5 seconds at game start
- **Smart Match Detection** - Instant feedback on matches
- **Color-Coded Cards** - Blue (hidden), White (revealed), Green (matched)
- **Move Counter** - Track your performance
- **Auto-Flip** - Non-matching cards flip back after 1 second

### 🏆 **Advanced Features**
- **High Score System** - Saves your best performance to disk
- **Timer** - Track how fast you complete the game
- **3 Difficulty Levels:**
  - 🟢 Easy (3×4 grid - 12 cards, 6 pairs)
  - 🟠 Medium (4×4 grid - 16 cards, 8 pairs)
  - 🔴 Hard (6×6 grid - 36 cards, 18 pairs)
- **New Record Detection** - Popup when you beat your best score

### 🎨 **UI/UX**
- **Modern Dark Theme** - Professional gradient design
- **Hover Effects** - Interactive card highlighting
- **Custom Graphics** - Hand-drawn checkmarks for matched cards
- **Full-Screen Mode** - Maximized window
- **Smooth Animations** - Card transitions

---

## 💻 Installation

### Prerequisites
- Java Development Kit (JDK) 8 or higher

### Check if Java is Installed
```bash
java -version
javac -version
```

### Steps to Run

1. **Compile the Code**
   ```bash
   javac MemoryGame.java Card.java
   ```

2. **Run the Game**
   ```bash
   java MemoryGame
   ```

---

## 🎮 How to Play

### Game Flow

1. **Preview Phase (5 seconds)**
   - All cards are revealed
   - Memorize positions
   - Countdown: "Starting in X seconds..."

2. **Gameplay Phase**
   - Cards flip back
   - Timer starts
   - Click cards to find matches

3. **Matching**
   - **Match** ✅ - Cards turn green, stay revealed
   - **No Match** ❌ - Cards flip back after 1 second

4. **Victory**
   - Find all pairs to win
   - Beat your high score!

### Controls
- **Left Click** - Flip card
- **Easy/Medium/Hard** - Change difficulty
- **New Game** - Restart

---

## 🎯 Game Modes

| Difficulty | Grid | Cards | Pairs |
|-----------|------|-------|-------|
| 🟢 Easy | 3×4 | 12 | 6 |
| 🟠 Medium | 4×4 | 16 | 8 |
| 🔴 Hard | 6×6 | 36 | 18 |

---

## 📁 Project Structure

```
flipnmatch/
├── MemoryGame.java       # Main game logic
├── Card.java             # Card component
├── highscores.txt        # High scores (auto-generated)
└── README.md             # Documentation
```

---

## 🛠️ Technologies Used

- **Java** - Programming language
- **Swing** - GUI framework
- **AWT** - Graphics and events
- **File I/O** - High score persistence

### Key Concepts
- Object-Oriented Programming
- Event-Driven Programming
- Custom Graphics (Graphics2D)
- State Management
- Timer and Delayed Actions

---

## 🎨 Customization

### Change Preview Duration
```java
// MemoryGame.java
private int previewCountdown = 5; // seconds
```

### Change Symbols
```java
// Card.java
private static final String[] SYMBOLS = {
    "1", "2", "3", "4", 
    "5", "6", "7", "8"
};
```

**Other options:**
- Numbers: `"1", "2", "3", "4", "5", "6", "7", "8"`
- Shapes: `"●", "■", "▲", "◆", "★", "◉", "▼", "◈"`
- Arrows: `"↑", "↓", "←", "→", "↖", "↗", "↘", "↙"`
- Letters: `"A", "B", "C", "D", "E", "F", "G", "H"`

### Change Colors
```java
// Card.java
private static final Color BACK_COLOR = new Color(74, 144, 226);
private static final Color MATCHED_COLOR = new Color(46, 204, 113);
```

---

## 🚀 Future Enhancements

- [ ] Sound effects
- [ ] Card flip animations  
- [ ] Pause/Resume button
- [ ] Multiple themes
- [ ] Multiplayer mode
- [ ] Statistics dashboard

---

## 📝 License

This project is open source and available under the MIT License.

---

## 👨‍💻 Author

**Your Name**
- GitHub: @yourusername
- Email: your.email@example.com

---

## ⭐ Show Your Support

Give a ⭐ if you like this project!

---

**Made with ❤️ and Java**

**Happy Gaming! 🎮**
