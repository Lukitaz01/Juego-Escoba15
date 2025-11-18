# Diagrama UML - La Escoba de 15

## Diagrama de Clases Completo

![UML Class Diagram](UML_Diagram.png)

## Descripción del Diagrama

### Paquetes y Responsabilidades

#### 1. **framework.observer** (Patrón Observer)
- `IObserver` - Interfaz para objetos que observan cambios
- `IObservable` - Interfaz para objetos observables
- `Observable` - Clase abstracta base que implementa el patrón Observer

#### 2. **escoba.model** (Modelo de Datos)
- `Card` - Representa una carta española (1-7, Sota, Caballo, Rey)
- `Deck` - Mazo de 40 cartas
- `Player` - Jugador con mano, cartas capturadas y escobas

#### 3. **escoba.events** (Eventos del Juego)
- `GameEvent` - Enum con todos los eventos del juego

#### 4. **escoba.game** (Lógica del Juego)
- `GameState` - Estado del juego, extiende Observable
- `ScoreCalculator` - Calcula puntuaciones al final
- `ResultadoJugada` - Encapsula el resultado de una jugada

#### 5. **escoba.view** (Vista)
- `PlayerView` - Ventana GUI del jugador, implementa IObserver

#### 6. **escoba.controller** (Controlador)
- `GameController` - Coordina entre modelo y vistas

#### 7. **escoba** (Principal)
- `Main` - Punto de entrada de la aplicación

## Relaciones Principales

### Patrón Observer
```
GameState (Observable) --notifica--> PlayerView (Observer)
```

### Arquitectura MVC
```
PlayerView (View) <--> GameController (Controller) <--> GameState (Model)
```

### Composición
- `GameState` contiene 1 `Deck`, 2 `Player`, y múltiples `Card` en mesa
- `Deck` contiene 40 `Card`
- `Player` contiene múltiples `Card` (mano y capturadas)
- `GameController` tiene referencias a `GameState` y 2 `PlayerView`

### Dependencias
- `GameState` retorna `ResultadoJugada`
- `GameState` notifica `GameEvent`
- `ScoreCalculator` calcula puntos de `Player`
- `PlayerView` observa `GameEvent`
- `GameController` usa `ResultadoJugada`


## Diagrama Simplificado (Mermaid)

```mermaid
classDiagram
    %% Framework Observer
    class IObserver {
        <<interface>>
        +update(IObservable, Object)
    }

    class IObservable {
        <<interface>>
        +addObserver(IObserver)
        +removeObserver(IObserver)
        +notifyObservers(Object)
    }

    class Observable {
        <<abstract>>
        -observers: List~IObserver~
        +addObserver(IObserver)
        +removeObserver(IObserver)
        +notifyObservers(Object)
    }

    IObservable <|.. Observable

    %% Model
    class Card {
        -cardNumber: int
        -suit: String
        +getGameValue() int
        +getSuit() String
        +toString() String
    }

    class Deck {
        -cards: List~Card~
        +shuffle()
        +draw() Card
        +isEmpty() boolean
    }

    class Player {
        -name: String
        -hand: List~Card~
        -capturedCards: List~Card~
        -escobasCount: int
        +addCardToHand(Card)
        +getCapturedCards() List~Card~
    }

    %% Events
    class GameEvent {
        <<enumeration>>
        GAME_STARTED
        GAME_OVER
        TURN_SWITCHED
        CARDS_DEALT
        CARDS_CAPTURED
        ESCOBA_SCORED
    }

    %% Game Logic
    class GameState {
        -deck: Deck
        -table: List~Card~
        -player1: Player
        -player2: Player
        -currentPlayerNumber: int
        -gameOver: boolean
        +startNewGame()
        +jugarCarta(int) ResultadoJugada
        +intentarCaptura(int, List~Integer~) ResultadoJugada
    }

    class ScoreCalculator {
        <<utility>>
        +calculateScore(Player, Player) int
        +getScoreBreakdown(Player, Player) String
    }

    class ResultadoJugada {
        -exito: boolean
        -mensaje: String
        -juegoTerminado: boolean
        -esEscoba: boolean
        +isExito() boolean
        +getMensaje() String
    }

    %% View
    class PlayerView {
        -frame: JFrame
        -textArea: JTextArea
        -playerName: String
        -playerNumber: int
        +displayGameState(List~Card~, Player, Player, int, boolean)
        +update(IObservable, Object)
    }

    %% Controller
    class GameController {
        -gameState: GameState
        -view1: PlayerView
        -view2: PlayerView
        +iniciarJuego()
        +procesarInputJugador(int, String)
    }

    %% Main
    class Main {
        +main(String[]) void
    }

    %% Relationships
    Observable <|-- GameState
    IObserver <|.. PlayerView

    GameState *-- Deck
    GameState *-- Player
    GameState o-- Card
    Deck *-- Card
    Player o-- Card

    GameState ..> ResultadoJugada
    GameState ..> GameEvent
    ScoreCalculator ..> Player

    GameController o-- GameState
    GameController o-- PlayerView
    PlayerView ..> GameEvent

    Main ..> GameController
    Main ..> GameState
    Main ..> PlayerView
```
