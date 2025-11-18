# UML Class Diagram - La Escoba de 15

## Complete Class Diagram (PlantUML Format)

```plantuml
@startuml Escoba_de_15_Class_Diagram

' ===========================
' FRAMEWORK PACKAGE (Observer Pattern)
' ===========================

package "framework.observer" {
    interface IObservable {
        +addObserver(observer: IObserver): void
        +removeObserver(observer: IObserver): void
        +notifyObservers(event: Object): void
    }

    interface IObserver {
        +update(observable: IObservable, event: Object): void
    }

    abstract class Observable {
        -observers: List<IObserver>
        +addObserver(observer: IObserver): void
        +removeObserver(observer: IObserver): void
        +notifyObservers(event: Object): void
        #getObserverCount(): int
    }

    IObservable <|.. Observable
}

' ===========================
' ESCOBA PACKAGE - EVENTS
' ===========================

package "escoba.events" {
    enum GameEvent {
        GAME_STARTED
        GAME_OVER
        TURN_SWITCHED
        CARDS_DEALT
        CARD_PLACED_ON_TABLE
        CARDS_CAPTURED
        ESCOBA_SCORED
        TABLE_UPDATED
        PLAYER_HAND_UPDATED
        SCORE_UPDATED
    }
}

' ===========================
' ESCOBA PACKAGE - MODEL
' ===========================

package "escoba.model" {
    class Card {
        -suit: String
        -rank: String
        -gameValue: int
        +Card(suit: String, rank: String, gameValue: int)
        +getSuit(): String
        +getRank(): String
        +getGameValue(): int
        +toString(): String
    }

    class Deck {
        -cards: List<Card>
        +Deck()
        -initializeDeck(): void
        +shuffle(): void
        +draw(): Card
        +remainingCards(): int
        +isEmpty(): boolean
    }

    class Player {
        -name: String
        -hand: List<Card>
        -capturedCards: List<Card>
        -escobasCount: int
        +Player(name: String)
        +getName(): String
        +getHand(): List<Card>
        +getCapturedCards(): List<Card>
        +getEscobasCount(): int
        +addCardToHand(card: Card): void
        +removeCardFromHand(index: int): Card
        +addCapturedCard(card: Card): void
        +addCapturedCards(cards: List<Card>): void
        +incrementEscobas(): void
        +hasCardsInHand(): boolean
        +getHandSize(): int
        +getCapturedCount(): int
    }

    Deck "1" *-- "40" Card : contains
    Player "1" o-- "*" Card : has in hand
    Player "1" o-- "*" Card : has captured
}

' ===========================
' ESCOBA PACKAGE - GAME (Model Logic)
' ===========================

package "escoba.game" {
    class GameState {
        -deck: Deck
        -table: List<Card>
        -player1: Player
        -player2: Player
        -currentPlayerNumber: int
        -gameOver: boolean

        +GameState()
        +startNewGame(): void
        +dealCardsToPlayers(): void
        +getCurrentPlayer(): Player
        +getOtherPlayer(): Player
        +switchTurn(): void
        +getTable(): List<Card>
        +getPlayer1(): Player
        +getPlayer2(): Player
        +getCurrentPlayerNumber(): int
        +isGameOver(): boolean
        +setGameOver(gameOver: boolean): void
        +getDeckSize(): int
        +isDeckEmpty(): boolean
        +addCardToTable(card: Card): void
        +removeCardsFromTable(cards: List<Card>): void
        +isTableEmpty(): boolean
        +playCardOnTable(cardIndex: int): boolean
        +attemptCapture(cardIndex: int, tableIndices: List<Integer>): boolean
        +nextTurn(): boolean
        +finishGame(): void
        +calculateCaptureSum(cardIndex: int, tableIndices: List<Integer>): int
    }

    class ScoreCalculator {
        {static} +calculateScore(player: Player, opponent: Player): int
        {static} +getScoreBreakdown(player: Player, opponent: Player): String
        {static} -countOroCards(cards: List<Card>): int
        {static} -countSevenCards(cards: List<Card>): int
        {static} -hasSieteDeOro(cards: List<Card>): boolean
    }

    GameState "1" *-- "1" Deck : manages
    GameState "1" o-- "2" Player : has
    GameState "1" o-- "*" Card : table cards
    GameState ..> ScoreCalculator : uses
}

Observable <|-- GameState

' ===========================
' ESCOBA PACKAGE - VIEW
' ===========================

package "escoba.view" {
    class PlayerView {
        -frame: JFrame
        -textArea: JTextArea
        -inputField: JTextField
        -playerName: String
        -playerNumber: int

        +PlayerView(playerName: String, playerNumber: int, x: int, y: int)
        -createWindow(x: int, y: int): void
        +show(): void
        +setInputListener(listener: ActionListener): void
        +getInput(): String
        +clearInput(): void
        +appendText(text: String): void
        +clearText(): void
        +displayGameState(table: List<Card>, player: Player, opponent: Player, deckSize: int, isCurrentPlayer: boolean): void
        +displayMessage(message: String): void
        +displayError(error: String): void
        +displayHelp(): void
        +update(observable: IObservable, event: Object): void
    }
}

IObserver <|.. PlayerView
PlayerView ..> Card : displays
PlayerView ..> Player : displays

' ===========================
' ESCOBA PACKAGE - CONTROLLER
' ===========================

package "escoba.controller" {
    class GameController {
        -gameState: GameState
        -view1: PlayerView
        -view2: PlayerView

        +GameController(gameState: GameState, view1: PlayerView, view2: PlayerView)
        +startGame(): void
        +processPlayerInput(playerNumber: int, input: String): void
        -handlePlayCommand(playerNumber: int, input: String): void
        -handlePlaceCard(playerNumber: int, cardIndex: int): void
        -handleCapture(playerNumber: int, cardIndex: int, tableIndices: List<Integer>): void
        -handleGameOver(): void
        -displayGameOver(view: PlayerView, p1: Player, p2: Player, score1: int, score2: int): void
        -updateBothViews(): void
        -updatePlayerView(playerNumber: int): void
        -getView(playerNumber: int): PlayerView
    }
}

GameController "1" --> "1" GameState : controls
GameController "1" --> "2" PlayerView : updates

' ===========================
' MAIN ENTRY POINT
' ===========================

package "escoba" {
    class Main {
        {static} +main(args: String[]): void
    }
}

Main ..> GameState : creates
Main ..> PlayerView : creates
Main ..> GameController : creates

' ===========================
' KEY RELATIONSHIPS
' ===========================

GameState ..> GameEvent : notifies with

note right of Observable
    Framework component providing
    Observer pattern implementation.
    Reusable across projects.
end note

note right of GameState
    Model: Contains all game logic
    - Card placement
    - Capture validation
    - Turn management
    - Game state
end note

note right of PlayerView
    View: Displays game state
    and captures user input.
    Observes GameState for updates.
end note

note right of GameController
    Controller: Coordinates between
    Model and Views. Parses input,
    delegates to Model, updates Views.
end note

@enduml
```

---

## Simplified MVC Architecture Diagram

```plantuml
@startuml Escoba_MVC_Pattern

skinparam componentStyle rectangle

package "MODEL" {
    [GameState]
    [Player]
    [Card]
    [Deck]
    [ScoreCalculator]
}

package "VIEW" {
    [PlayerView 1]
    [PlayerView 2]
}

package "CONTROLLER" {
    [GameController]
}

[User Input] --> [PlayerView 1]
[User Input] --> [PlayerView 2]

[PlayerView 1] --> [GameController] : commands
[PlayerView 2] --> [GameController] : commands

[GameController] --> [GameState] : delegates business logic
[GameState] --> [Player]
[GameState] --> [Card]
[GameState] --> [Deck]
[GameController] ..> [ScoreCalculator] : uses

[GameState] ..> [PlayerView 1] : notifies (Observer)
[GameState] ..> [PlayerView 2] : notifies (Observer)

[GameController] --> [PlayerView 1] : updates display
[GameController] --> [PlayerView 2] : updates display

note bottom of [GameState]
    Observable
    (Subject)
end note

note bottom of [PlayerView 1]
    Observer
end note

note bottom of [PlayerView 2]
    Observer
end note

@enduml
```

---

## Observer Pattern Sequence Diagram

```plantuml
@startuml Observer_Pattern_Sequence

actor "Player 1" as P1
participant "PlayerView\n(Observer)" as V1
participant "GameController\n(Controller)" as C
participant "GameState\n(Observable)" as M
participant "PlayerView 2\n(Observer)" as V2
actor "Player 2" as P2

P1 -> V1: Type "jugar 1 llevar 2 3"
activate V1
V1 -> C: processPlayerInput(1, "jugar 1 llevar 2 3")
activate C

C -> C: Parse command
C -> M: attemptCapture(0, [1, 2])
activate M

M -> M: Validate sum = 15
M -> M: Execute capture
M -> M: Check for escoba

M -> M: notifyObservers(CARDS_CAPTURED)
M --> V1: update(GameState, CARDS_CAPTURED)
activate V1
V1 -> V1: Handle event
deactivate V1

M --> V2: update(GameState, CARDS_CAPTURED)
activate V2
V2 -> V2: Handle event
deactivate V2

M -> M: notifyObservers(ESCOBA_SCORED)
M --> V1: update(GameState, ESCOBA_SCORED)
activate V1
V1 -> V1: Display "¡ESCOBA!"
deactivate V1

M --> V2: update(GameState, ESCOBA_SCORED)
activate V2
V2 -> V2: Display "¡ESCOBA!"
deactivate V2

M --> C: return true
deactivate M

C -> M: nextTurn()
activate M
M -> M: switchTurn()
M -> M: notifyObservers(TURN_SWITCHED)
M --> V1: update(GameState, TURN_SWITCHED)
M --> V2: update(GameState, TURN_SWITCHED)
deactivate M

C -> C: updateBothViews()
C -> V1: displayGameState(...)
C -> V2: displayGameState(...)

deactivate C
deactivate V1

V2 -> P2: Display updated state
note right: Player 2's turn now

@enduml
```

---

## Component Diagram

```plantuml
@startuml Component_Diagram

package "Game Framework" {
    [Observer Pattern\nFramework] as Framework
}

package "Escoba de 15 Application" {

    component "Model Layer" {
        [Game State\nManagement] as Model
        database "Game Data" as Data
    }

    component "View Layer" {
        [Player View 1\n(Blue Theme)] as View1
        [Player View 2\n(Green Theme)] as View2
    }

    component "Controller Layer" {
        [Game Controller] as Controller
    }

    component "Events" {
        [Game Events] as Events
    }
}

Framework --> Model : extends Observable
Framework --> View1 : implements IObserver
Framework --> View2 : implements IObserver

Model --> Data : manages
Model --> Events : produces

Controller --> Model : delegates logic
Controller --> View1 : updates
Controller --> View2 : updates

Model ..> View1 : notifies
Model ..> View2 : notifies

View1 --> Controller : sends commands
View2 --> Controller : sends commands

note right of Framework
    Reusable design pattern
    implementation
end note

note bottom of Model
    Contains all business logic:
    - Card management
    - Turn control
    - Validation rules
    - Game flow
end note

@enduml
```

---

## Package Dependency Diagram

```plantuml
@startuml Package_Dependencies

package "framework.observer" as FO {
}

package "escoba.events" as EE {
}

package "escoba.model" as EM {
}

package "escoba.game" as EG {
}

package "escoba.view" as EV {
}

package "escoba.controller" as EC {
}

package "escoba" as E {
}

EG ..> FO : extends Observable
EG ..> EM : uses
EG ..> EE : uses

EV ..> FO : implements IObserver
EV ..> EM : displays
EV ..> EE : handles

EC ..> EG : controls
EC ..> EV : updates
EC ..> EM : uses

E ..> EG : creates
E ..> EV : creates
E ..> EC : creates

note right of FO
    Framework layer
    (reusable)
end note

note bottom of EG
    Model layer
    (business logic)
end note

note bottom of EV
    View layer
    (presentation)
end note

note bottom of EC
    Controller layer
    (coordination)
end note

@enduml
```

---

## How to Generate Visual Diagrams

### Option 1: Online PlantUML Editor
1. Visit: https://www.plantuml.com/plantuml/uml/
2. Copy the PlantUML code from above
3. Paste and view the generated diagram
4. Export as PNG/SVG

### Option 2: VS Code with PlantUML Extension
1. Install "PlantUML" extension in VS Code
2. Create a `.puml` file with the code above
3. Use `Alt+D` to preview
4. Export as needed

### Option 3: Command Line (requires Graphviz)
```bash
# Install PlantUML and Graphviz
# Then run:
java -jar plantuml.jar UML_DIAGRAM.puml
```

---

## Diagram Explanations

### Class Diagram
- Shows all classes, their attributes, methods, and relationships
- Demonstrates inheritance (Observable → GameState)
- Shows interface implementation (IObserver ← PlayerView)
- Illustrates composition and aggregation relationships

### MVC Architecture Diagram
- Simplified view of the MVC pattern implementation
- Shows data flow between components
- Highlights Observer pattern integration

### Sequence Diagram
- Demonstrates runtime behavior
- Shows how Observer pattern works during gameplay
- Illustrates event propagation from Model to Views

### Component Diagram
- High-level architectural view
- Shows major system components
- Demonstrates layer separation

### Package Dependency Diagram
- Shows package structure
- Illustrates dependency directions
- Helps identify architectural layers

---

## Key Architectural Insights

1. **Clean Separation**: Model, View, and Controller are clearly separated
2. **Framework Reusability**: Observer pattern components are in separate package
3. **One-way Dependencies**: Views depend on Model, not vice versa
4. **Event-Driven**: Communication via events (GameEvent enum)
5. **Extensible**: Easy to add new views or game modes

