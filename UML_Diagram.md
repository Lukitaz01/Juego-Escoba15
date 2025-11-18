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

## Diagrama en Formato PlantUML

El archivo completo del diagrama está disponible en `UML_Diagram.puml`

Para generar la imagen PNG desde PlantUML:

```bash
# Si tienes PlantUML instalado
java -jar plantuml.jar UML_Diagram.puml

# O usando el servidor web de PlantUML
# Copia el contenido de UML_Diagram.puml en: http://www.plantuml.com/plantuml/
```

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

## Patrones de Diseño Implementados

### 1. Observer Pattern
**Propósito**: Sincronizar automáticamente las vistas cuando cambia el estado del juego

**Implementación**:
- `Observable` - Clase base para objetos observables
- `IObserver` - Interfaz para observadores
- `GameState extends Observable` - El modelo notifica cambios
- `PlayerView implements IObserver` - Las vistas se actualizan automáticamente

### 2. MVC (Model-View-Controller)
**Propósito**: Separar responsabilidades

**Implementación**:
- **Model**: `GameState`, `Card`, `Deck`, `Player`, `ScoreCalculator`
- **View**: `PlayerView`
- **Controller**: `GameController` (actúa como "barandilla", sin lógica de negocio)

### 3. Result Object Pattern
**Propósito**: Encapsular resultados de operaciones

**Implementación**:
- `ResultadoJugada` - Encapsula éxito, mensaje, y flags especiales

## Notas de Arquitectura

1. **GameController como Barandilla**
   - No contiene lógica de negocio
   - Solo parsea input y coordina
   - Toda la lógica está en GameState

2. **GameState es el Cerebro**
   - Valida todas las jugadas
   - Genera todos los mensajes
   - Maneja las reglas del juego
   - Notifica eventos a las vistas

3. **Observer Pattern para Sincronización**
   - Las vistas se actualizan automáticamente
   - Sin acoplamiento directo entre modelo y vista
   - Fácil agregar nuevos observadores (IA, estadísticas, etc.)

4. **Separación Limpia**
   - Modelo no conoce la vista
   - Vista no conoce el modelo directamente
   - Controller actúa como intermediario

## Extensibilidad

El diseño permite fácilmente agregar:
- **Jugador IA**: Crear `AIPlayerView implements IObserver`
- **Estadísticas**: Crear `StatsObserver implements IObserver`
- **Red**: Crear `NetworkPlayerView implements IObserver`
- **Replay**: Grabar eventos y reproducir
- **Sonido**: Crear `SoundObserver implements IObserver`
