# La Escoba de 15 - Documentación del Proyecto

## Índice
1. [Vista General del Proyecto](#vista-general-del-proyecto)
2. [Arquitectura](#arquitectura)
3. [Patrones de Diseño](#patrones-de-diseño)
4. [Estructura del Proyecto](#estructura-del-proyecto)
5. [Componentes Clave](#componentes-clave)
6. [Cómo Funciona el Código](#cómo-funciona-el-código)
7. [Fortalezas del Proyecto](#fortalezas-del-proyecto)
8. [Ejecutar la Aplicación](#ejecutar-la-aplicación)

---

## Vista General del Proyecto

**La Escoba de 15** es una implementación del juego de cartas tradicional español en Java usando Swing para la interfaz gráfica. El juego soporta dos jugadores con ventanas separadas, implementando una arquitectura MVC limpia con el patrón Observer para actualizaciones en tiempo real.

### Reglas del Juego
- Los jugadores juegan cartas de su mano por turnos
- Objetivo: Capturar cartas de la mesa que sumen exactamente 15
- **Escoba**: Limpiar todas las cartas de la mesa otorga puntos bonus
- Puntuación final basada en: cantidad de cartas, cartas de "Oro", 7 de Oro, cantidad de 7s, y escobas

---

## Arquitectura

Este proyecto sigue el patrón arquitectónico **Model-View-Controller (MVC)** con estricta separación de responsabilidades:

### Componentes MVC

#### **Modelo** (`escoba.model`, `escoba.game`)
- **Responsabilidades**: Lógica del juego, reglas de negocio, gestión de datos
- **Clases**:
  - `GameState`: Estado y lógica principal del juego
  - `Player`: Datos y operaciones del jugador
  - `Card`: Representación de cartas
  - `Deck`: Gestión del mazo
  - `ScoreCalculator`: Lógica de puntuación
  - `ResultadoJugada`: Encapsula resultados de jugadas

#### **Vista** (`escoba.view`)
- **Responsabilidades**: Interfaz de usuario, visualización, captura de input
- **Clases**:
  - `PlayerView`: GUI basada en Swing para cada jugador
  - Implementa `IObserver` para recibir actualizaciones del modelo

#### **Controlador** (`escoba.controller`)
- **Responsabilidades**: Parseo de input, coordinación entre Modelo y Vista
- **Clases**:
  - `GameController`: Interpreta comandos de usuario y delega al Modelo
  - **IMPORTANTE**: Es solo una "barandilla" - NO contiene lógica de negocio

---

## Patrones de Diseño

### 1. **Patrón Observer**
**Propósito**: Sincronización automática entre Modelo y Vistas

```
GameState (Observable)
    ↓ notifica
PlayerView (Observer) ← actualiza UI automáticamente
```

**Beneficios**:
- Las vistas permanecen sincronizadas con el estado del juego
- Acoplamiento débil entre Modelo y Vista
- Fácil agregar nuevos observadores (ej. jugadores IA, registro, estadísticas)

**Implementación**:
- `IObservable`: Interfaz para sujetos (GameState)
- `IObserver`: Interfaz para observadores (PlayerView)
- `Observable`: Clase base que proporciona gestión de observadores
- `GameEvent`: Enum definiendo tipos de eventos (GAME_STARTED, CARDS_DEALT, etc.)

### 2. **Patrón MVC**
**Propósito**: Separación de responsabilidades

**Flujo**:
```
Input Usuario → Controller → Modelo (lógica de negocio) → Notificación Observer → Actualización Vista
```

**Beneficios**:
- Responsabilidades claras
- Componentes testeables
- Base de código mantenible
- Componentes de framework reutilizables

### 3. **Patrón Result Object**
**Propósito**: Encapsular resultados de operaciones

```java
public class ResultadoJugada {
    private boolean exito;
    private String mensaje;
    private boolean juegoTerminado;
    private boolean esEscoba;
}
```

**Beneficios**:
- El Controller no necesita construir mensajes
- El Modelo contiene toda la lógica de mensajes
- Claridad en los resultados de operaciones

---

## Estructura del Proyecto

```
GameFramework/
├── src/
│   ├── framework/
│   │   └── observer/          # Framework patrón Observer reutilizable
│   │       ├── IObservable.java
│   │       ├── IObserver.java
│   │       └── Observable.java
│   │
│   └── escoba/                # Juego Escoba de 15
│       ├── Main.java          # Punto de entrada
│       ├── controller/
│       │   └── GameController.java   # Controlador MVC
│       ├── model/
│       │   ├── Card.java
│       │   ├── Deck.java
│       │   └── Player.java
│       ├── game/              # Lógica del juego (Modelo)
│       │   ├── GameState.java
│       │   ├── ResultadoJugada.java
│       │   └── ScoreCalculator.java
│       ├── view/
│       │   └── PlayerView.java       # Vista MVC
│       └── events/
│           └── GameEvent.java        # Tipos de eventos
│
├── out/                       # Clases compiladas
└── DOCUMENTACION_PROYECTO.md
```

---

## Componentes Clave

### GameState (Núcleo del Modelo)
**Ubicación**: `escoba.game.GameState`

**Responsabilidades**:
- Mantiene el estado del juego (mazo, mesa, jugadores, turno)
- Implementa la lógica del juego (jugar cartas, capturar, validación)
- Extiende `Observable` para notificar cambios a las vistas
- Genera mensajes de resultado

**Métodos Clave**:
- `startNewGame()`: Inicializa nuevo juego
- `jugarCarta(int indiceCarta)`: Coloca carta en la mesa
- `intentarCaptura(int indiceCarta, List<Integer> indicesMesa)`: Valida y ejecuta captura
- `obtenerResumenFinJuego()`: Genera resumen del fin del juego
- Métodos privados: `nextTurn()`, `finishGame()`

### PlayerView (Vista)
**Ubicación**: `escoba.view.PlayerView`

**Responsabilidades**:
- Muestra el estado del juego al jugador
- Captura input del usuario
- Implementa `IObserver` para recibir actualizaciones de GameState

**Características**:
- UI codificada por colores (Jugador 1: azul suave, Jugador 2: verde suave)
- Visualización del estado del juego en tiempo real
- Sistema de ayuda con instrucciones del juego

**Métodos Clave**:
- `displayGameState()`: Muestra mesa, mano, puntajes
- `update()`: Callback del patrón Observer
- `displayHelp()`: Muestra instrucciones del juego

### GameController (Controlador)
**Ubicación**: `escoba.controller.GameController`

**Responsabilidades**:
- Parsea input del usuario
- Delega al Modelo para lógica de negocio
- Coordina actualizaciones de vistas
- **NO contiene lógica de negocio** (es solo una "barandilla")

**Métodos Clave**:
- `procesarInputJugador()`: Manejador principal de input
- `manejarComandoJugar()`: Parsea comandos de juego
- `ejecutarCaptura()`: Coordina validación de captura y feedback
- `actualizarAmbasVistas()`: Refresca todas las vistas

---

## Cómo Funciona el Código

### 1. Inicio de la Aplicación
```java
// Main.java
GameState gameState = new GameState();
PlayerView view1 = new PlayerView("Player 1", 1, 100, 100);
PlayerView view2 = new PlayerView("Player 2", 2, 750, 100);
GameController controller = new GameController(gameState, view1, view2);
```

**Qué sucede**:
1. Se crea GameState (Modelo)
2. Se crean dos PlayerViews (Vistas) con diferentes esquemas de color
3. GameController (Controlador) conecta Modelo y Vistas
4. Las vistas se registran como observadores de GameState

### 2. Flujo del Juego

**Flujo de Input del Usuario**:
```
Jugador escribe comando → Vista captura input → Controller.procesarInputJugador()
    → Controller parsea comando → Delega al Modelo (GameState)
    → Modelo ejecuta lógica → Modelo notifica observadores (Vistas)
    → Vistas se actualizan automáticamente
```

**Ejemplo: Jugar una carta**
```
Usuario: "jugar 1 llevar 2 3"
    ↓
Controller: Parsea "jugar carta 1, capturar cartas de mesa 2 y 3"
    ↓
Modelo: GameState.intentarCaptura(0, [1, 2])
    ↓
Modelo: Valida suma = 15, ejecuta captura, genera mensaje
    ↓
Modelo: notifyObservers(GameEvent.CARDS_CAPTURED)
    ↓
Modelo: Retorna ResultadoJugada con mensaje
    ↓
Controller: Muestra mensaje de ResultadoJugada
    ↓
Vistas: Método update() llamado, muestra mensaje de éxito
```

### 3. Patrón Observer en Acción

**Cuando GameState cambia**:
```java
// En GameState
public ResultadoJugada intentarCaptura(int cardIndex, List<Integer> tableIndices) {
    // ... validación y lógica ...
    notifyObservers(GameEvent.CARDS_CAPTURED);

    if (isTableEmpty()) {
        notifyObservers(GameEvent.ESCOBA_SCORED);
    }

    return ResultadoJugada.exitoSimple(mensaje);
}
```

**Las vistas reciben notificación**:
```java
// En PlayerView
public void update(IObservable observable, Object event) {
    if (event == GameEvent.CARDS_CAPTURED) {
        // Manejar evento de cartas capturadas
    }
}
```

---

## Fortalezas del Proyecto

### 1. **Arquitectura Limpia**
- ✅ **Separación estricta MVC**: Cada componente tiene responsabilidades claras
- ✅ **Modelo independiente**: La lógica del juego es completamente independiente de la UI
- ✅ **Testeable**: La lógica de negocio puede testearse sin GUI
- ✅ **Controller mínimo**: Solo coordina, no contiene lógica

### 2. **Patrones de Diseño**
- ✅ **Patrón Observer**: Sincronización automática de vistas
- ✅ **Framework reutilizable**: Componentes Observer pueden usarse en otros proyectos
- ✅ **Dirigido por eventos**: Comunicación desacoplada vía eventos
- ✅ **Result Object**: Encapsula resultados de operaciones

### 3. **Calidad del Código**
- ✅ **Bien documentado**: Comentarios JavaDoc completos
- ✅ **Legible**: Convenciones de nombres claras
- ✅ **Mantenible**: Diseño modular, principio de responsabilidad única
- ✅ **Extensible**: Fácil agregar características (jugadores IA, juego en red, nuevos modos)

### 4. **Experiencia de Usuario**
- ✅ **Vistas codificadas por colores**: Fácil distinguir jugadores
- ✅ **Actualizaciones en tiempo real**: Ambos jugadores ven cambios instantáneamente
- ✅ **Ayuda completa**: Instrucciones en el juego en español
- ✅ **Feedback de errores**: Mensajes de validación claros

### 5. **Excelencia Técnica**
- ✅ **Sin duplicación de código**: Componentes reutilizables
- ✅ **Encapsulación apropiada**: Campos privados, interfaces públicas
- ✅ **Separación de responsabilidades**: UI, lógica y datos están separados
- ✅ **Escalabilidad**: Puede soportar fácilmente múltiples modos de juego

### 6. **Principios OOP**
- ✅ **Herencia**: Clase base Observable
- ✅ **Polimorfismo**: Interfaces IObserver, IObservable
- ✅ **Encapsulación**: Campos privados con acceso controlado
- ✅ **Abstracción**: Interfaces claras para componentes del framework

---

## Ejecutar la Aplicación

### Prerequisitos
- Java JDK 8 o superior
- Línea de comandos o IDE de Java

### Compilación
```bash
cd GameFramework
javac -d out src/framework/observer/*.java src/escoba/**/*.java
```

### Ejecución
```bash
java -cp out escoba.Main
```

### Controles del Juego
- `jugar <carta#>` - Colocar carta en la mesa
- `jugar <carta#> llevar <mesa#> <mesa#> ...` - Capturar cartas
- `ayuda` - Mostrar ayuda
- `salir` - Salir del juego

### Comandos de Ejemplo
```
jugar 1                    # Jugar carta 1 en la mesa
jugar 2 llevar 1 3         # Jugar carta 2, capturar cartas de mesa 1 y 3
ayuda                      # Mostrar ayuda
```

---

## Puntos para Presentación

### Para Presentación Académica/Profesional

**1. Aspectos Destacados de Arquitectura**
- "Este proyecto demuestra arquitectura MVC apropiada con completa separación de responsabilidades"
- "El patrón Observer habilita sincronización en tiempo real entre modelo y múltiples vistas"
- "Los componentes del framework son reutilizables a través de diferentes implementaciones de juegos"
- "El Controller es solo una 'barandilla' - no contiene lógica de negocio"

**2. Decisiones de Diseño**
- "La lógica del juego está centralizada en el Modelo, haciéndola testeable sin dependencias de UI"
- "El Controller solo coordina; no contiene lógica de negocio"
- "Las vistas son observadores pasivos que reaccionan a cambios del modelo"
- "ResultadoJugada encapsula resultados manteniendo el Controller simple"

**3. Calidad del Código**
- "Sigue principios SOLID: Responsabilidad Única, Abierto/Cerrado, Inversión de Dependencias"
- "Documentación completa para mantenibilidad"
- "Código limpio con nombres significativos y estructura apropiada"

**4. Extensibilidad**
- "Fácil agregar: jugadores IA, multijugador en red, estadísticas de juego, diferentes juegos de cartas"
- "El patrón Observer del framework puede reutilizarse para cualquier sistema observable"
- "El esquema de colores es configurable por jugador"

**5. Habilidades Técnicas Demostradas**
- Java OOP (herencia, polimorfismo, interfaces)
- Patrones de Diseño (Observer, MVC, Result Object)
- Desarrollo de GUI (Swing)
- Arquitectura de Software
- Principios de Código Limpio

---

## Mejoras Futuras

Potenciales mejoras para discutir:
1. **Jugador IA**: Implementar oponente computarizado
2. **Juego en Red**: Soporte multijugador remoto
3. **Persistencia**: Guardar/cargar estado del juego
4. **Estadísticas**: Rastrear tasas de victoria, puntajes promedio
5. **Animaciones**: Efectos de movimiento de cartas
6. **Efectos de Sonido**: Feedback de audio
7. **Internacionalización**: Soporte multi-idioma

---

## Resumen

Este proyecto es una **implementación de grado profesional** de La Escoba de 15 que demuestra:
- Fuerte comprensión de arquitectura de software (MVC)
- Dominio de patrones de diseño (Observer, Result Object)
- Código limpio y mantenible
- Diseño centrado en el usuario
- Arquitectura extensible y escalable

La base de código está **lista para presentación** y muestra principios sólidos de ingeniería de software aplicables al desarrollo del mundo real.
