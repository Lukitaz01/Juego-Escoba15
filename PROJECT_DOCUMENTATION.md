# La Escoba de 15 - Project Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture](#architecture)
3. [Design Patterns](#design-patterns)
4. [Project Structure](#project-structure)
5. [Key Components](#key-components)
6. [How the Code Works](#how-the-code-works)
7. [Project Strengths](#project-strengths)
8. [Running the Application](#running-the-application)

---

## Project Overview

**La Escoba de 15** is a traditional Spanish card game implementation in Java using Swing for the graphical user interface. The game supports two players with separate windows, implementing a clean MVC architecture with the Observer pattern for real-time updates.

### Game Rules
- Players take turns playing cards from their hand
- Goal: Capture cards from the table that sum to exactly 15
- **Escoba**: Clearing all cards from the table earns bonus points
- Final scoring based on: card count, "Oro" cards, 7 of Oro, number of 7s, and escobas

---

## Architecture

This project follows the **Model-View-Controller (MVC)** architectural pattern with strict separation of concerns:

### MVC Components

#### **Model** (`escoba.model`, `escoba.game`)
- **Responsibilities**: Game logic, business rules, data management
- **Classes**:
  - `GameState`: Core game state and logic
  - `Player`: Player data and operations
  - `Card`: Card representation
  - `Deck`: Deck management
  - `ScoreCalculator`: Scoring logic

#### **View** (`escoba.view`)
- **Responsibilities**: User interface, display, user input capture
- **Classes**:
  - `PlayerView`: Swing-based GUI for each player
  - Implements `IObserver` to receive model updates

#### **Controller** (`escoba.controller`)
- **Responsibilities**: Input parsing, coordinating Model and View
- **Classes**:
  - `GameController`: Interprets user commands and delegates to Model

---

## Design Patterns

### 1. **Observer Pattern**
**Purpose**: Automatic synchronization between Model and Views

```
GameState (Observable)
    ↓ notifies
PlayerView (Observer) ← updates UI automatically
```

**Benefits**:
- Views stay synchronized with game state
- Loose coupling between Model and View
- Easy to add new observers (e.g., logging, AI players)

**Implementation**:
- `IObservable`: Interface for subjects (GameState)
- `IObserver`: Interface for observers (PlayerView)
- `Observable`: Base class providing observer management
- `GameEvent`: Enum defining event types (GAME_STARTED, CARDS_DEALT, etc.)

### 2. **MVC Pattern**
**Purpose**: Separation of concerns

**Flow**:
```
User Input → Controller → Model (business logic) → Observer notification → View update
```

**Benefits**:
- Clear responsibilities
- Testable components
- Maintainable codebase
- Reusable framework components

---

## Project Structure

```
GameFramework/
├── src/
│   ├── framework/
│   │   └── observer/          # Reusable Observer pattern framework
│   │       ├── IObservable.java
│   │       ├── IObserver.java
│   │       └── Observable.java
│   │
│   └── escoba/                # Escoba de 15 game
│       ├── Main.java          # Entry point
│       ├── controller/
│       │   └── GameController.java   # MVC Controller
│       ├── model/
│       │   ├── Card.java
│       │   ├── Deck.java
│       │   └── Player.java
│       ├── game/              # Game logic (Model)
│       │   ├── GameState.java
│       │   └── ScoreCalculator.java
│       ├── view/
│       │   └── PlayerView.java       # MVC View
│       └── events/
│           └── GameEvent.java        # Event types
│
├── out/                       # Compiled classes
└── PROJECT_DOCUMENTATION.md
```

---

## Key Components

### GameState (Model Core)
**Location**: `escoba.game.GameState`

**Responsibilities**:
- Maintains game state (deck, table, players, turn)
- Implements game logic (card playing, capturing, validation)
- Extends `Observable` to notify views of changes

**Key Methods**:
- `startNewGame()`: Initializes new game
- `playCardOnTable(int cardIndex)`: Places card on table
- `attemptCapture(int cardIndex, List<Integer> tableIndices)`: Validates and executes capture
- `nextTurn()`: Advances game to next turn
- `finishGame()`: Handles game end logic

### PlayerView (View)
**Location**: `escoba.view.PlayerView`

**Responsibilities**:
- Displays game state to player
- Captures user input
- Implements `IObserver` to receive updates from GameState

**Features**:
- Color-coded UI (Player 1: soft blue, Player 2: soft green)
- Real-time game state display
- Help system with game instructions

**Key Methods**:
- `displayGameState()`: Shows table, hand, scores
- `update()`: Observer pattern callback
- `displayHelp()`: Shows game instructions

### GameController (Controller)
**Location**: `escoba.controller.GameController`

**Responsibilities**:
- Parses user input
- Delegates to Model for business logic
- Coordinates view updates

**Key Methods**:
- `processPlayerInput()`: Main input handler
- `handlePlayCommand()`: Parses play commands
- `handleCapture()`: Coordinates capture validation and feedback
- `updateBothViews()`: Refreshes all views

---

## How the Code Works

### 1. Application Startup
```java
// Main.java
GameState gameState = new GameState();
PlayerView view1 = new PlayerView("Player 1", 1, 100, 100);
PlayerView view2 = new PlayerView("Player 2", 2, 750, 100);
GameController controller = new GameController(gameState, view1, view2);
```

**What happens**:
1. GameState (Model) is created
2. Two PlayerViews (Views) are created with different color schemes
3. GameController (Controller) connects Model and Views
4. Views are registered as observers of GameState

### 2. Game Flow

**User Input Flow**:
```
Player types command → View captures input → Controller.processPlayerInput()
    → Controller parses command → Delegates to Model (GameState)
    → Model executes logic → Model notifies observers (Views)
    → Views update automatically
```

**Example: Playing a card**
```
User: "jugar 1 llevar 2 3"
    ↓
Controller: Parses "play card 1, capture table cards 2 and 3"
    ↓
Model: GameState.attemptCapture(0, [1, 2])
    ↓
Model: Validates sum = 15, executes capture
    ↓
Model: notifyObservers(GameEvent.CARDS_CAPTURED)
    ↓
Views: update() method called, display success message
```

### 3. Observer Pattern in Action

**When GameState changes**:
```java
// In GameState
public boolean attemptCapture(int cardIndex, List<Integer> tableIndices) {
    // ... validation and logic ...
    notifyObservers(GameEvent.CARDS_CAPTURED);

    if (isTableEmpty()) {
        notifyObservers(GameEvent.ESCOBA_SCORED);
    }
    return true;
}
```

**Views receive notification**:
```java
// In PlayerView
public void update(IObservable observable, Object event) {
    if (event == GameEvent.CARDS_CAPTURED) {
        // Handle captured cards event
    }
}
```

---

## Project Strengths

### 1. **Clean Architecture**
- ✅ **Strict MVC separation**: Each component has clear responsibilities
- ✅ **Model independence**: Game logic is completely independent of UI
- ✅ **Testable**: Business logic can be tested without GUI

### 2. **Design Patterns**
- ✅ **Observer Pattern**: Automatic view synchronization
- ✅ **Reusable Framework**: Observer components can be used in other projects
- ✅ **Event-driven**: Decoupled communication via events

### 3. **Code Quality**
- ✅ **Well-documented**: Comprehensive JavaDoc comments
- ✅ **Readable**: Clear naming conventions
- ✅ **Maintainable**: Modular design, single responsibility principle
- ✅ **Extensible**: Easy to add features (AI players, network play, new game modes)

### 4. **User Experience**
- ✅ **Color-coded views**: Easy to distinguish players
- ✅ **Real-time updates**: Both players see changes immediately
- ✅ **Comprehensive help**: In-game instructions in Spanish
- ✅ **Error feedback**: Clear validation messages

### 5. **Technical Excellence**
- ✅ **No code duplication**: Reusable components
- ✅ **Proper encapsulation**: Private fields, public interfaces
- ✅ **Separation of concerns**: UI, logic, and data are separate
- ✅ **Scalability**: Can easily support multiple game modes

### 6. **OOP Principles**
- ✅ **Inheritance**: Observable base class
- ✅ **Polymorphism**: IObserver, IObservable interfaces
- ✅ **Encapsulation**: Private fields with controlled access
- ✅ **Abstraction**: Clear interfaces for framework components

---

## Running the Application

### Prerequisites
- Java JDK 8 or higher
- Command line or IDE (Eclipse, IntelliJ IDEA, etc.)

### Compilation
```bash
cd GameFramework
javac -d out src/framework/observer/*.java src/escoba/**/*.java
```

### Execution
```bash
cd GameFramework
java -cp out escoba.Main
```

### Game Controls
- `jugar <card#>` - Place card on table
- `jugar <card#> llevar <table#> <table#> ...` - Capture cards
- `ayuda` - Show help
- `salir` - Exit game

### Example Commands
```
jugar 1                    # Play card 1 on table
jugar 2 llevar 1 3         # Play card 2, capture table cards 1 and 3
ayuda                      # Show help
```

---

## Presentation Points

### For Academic/Professional Presentation

**1. Architecture Highlights**
- "This project demonstrates proper MVC architecture with complete separation of concerns"
- "The Observer pattern enables real-time synchronization between model and multiple views"
- "The framework components are reusable across different game implementations"

**2. Design Decisions**
- "Game logic is centralized in the Model, making it testable without UI dependencies"
- "Controller only coordinates; it doesn't contain business logic"
- "Views are passive observers that react to model changes"

**3. Code Quality**
- "Follows SOLID principles: Single Responsibility, Open/Closed, Dependency Inversion"
- "Comprehensive documentation for maintainability"
- "Clean code with meaningful names and proper structure"

**4. Extensibility**
- "Easy to add: AI players, network multiplayer, game statistics, different card games"
- "Framework Observer pattern can be reused for any observable system"
- "Color scheme is configurable per player"

**5. Technical Skills Demonstrated**
- Java OOP (inheritance, polymorphism, interfaces)
- Design Patterns (Observer, MVC)
- GUI Development (Swing)
- Software Architecture
- Clean Code principles

---

## Future Enhancements

Potential improvements to discuss:
1. **AI Player**: Implement computer opponent
2. **Network Play**: Remote multiplayer support
3. **Persistence**: Save/load game state
4. **Statistics**: Track win rates, average scores
5. **Animations**: Card movement effects
6. **Sound Effects**: Audio feedback
7. **Internationalization**: Multi-language support
8. **Different Game Modes**: Variations of Escoba

---

## Summary

This project is a **professional-grade implementation** of La Escoba de 15 that demonstrates:
- Strong understanding of software architecture (MVC)
- Mastery of design patterns (Observer)
- Clean, maintainable code
- User-focused design
- Extensible, scalable architecture

The codebase is **presentation-ready** and showcases solid software engineering principles applicable to real-world development.
