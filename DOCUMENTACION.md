# LA ESCOBA DE 15 - Documentación Completa

## 📋 TABLA DE CONTENIDOS
1. [Reglas del Juego](#reglas-del-juego)
2. [Estructura del Proyecto](#estructura-del-proyecto)
3. [Cómo Modificar el Juego](#cómo-modificar-el-juego)
4. [Guía de Clases](#guía-de-clases)
5. [Ejemplos de Modificación](#ejemplos-de-modificación)

---

## 🎮 REGLAS DEL JUEGO

### Objetivo
Capturar cartas de la mesa que sumen exactamente 15 con las cartas de tu mano.

### Valores de las Cartas
- **Cartas 1-7**: Valen su número (1, 2, 3, 4, 5, 6, 7)
- **Sota**: Vale **8 puntos**
- **Caballo**: Vale **9 puntos**
- **Rey**: Vale **10 puntos**

### Cómo Jugar
1. Cada jugador recibe 3 cartas
2. Hay 4 cartas en la mesa
3. En tu turno, puedes:
   - **Capturar**: Jugar una carta + tomar cartas de la mesa que sumen 15
   - **Poner**: Colocar una carta en la mesa si no puedes sumar 15

### Escoba
Si vacías completamente la mesa al capturar, ¡haces una ESCOBA! (+1 punto)

### Puntos Finales
- **1 punto** por cada escoba
- **1 punto** por más cartas capturadas
- **1 punto** por más Oros capturados
- **1 punto** por tener el 7 de Oro
- **1 punto** por más 7s capturados

---

## 📁 ESTRUCTURA DEL PROYECTO

```
src/escoba/
├── model/                  # MODELOS DE DATOS
│   ├── Card.java          # Una carta (número + palo)
│   ├── Deck.java          # El mazo de 40 cartas
│   └── Player.java        # Un jugador (mano + cartas capturadas)
│
├── game/                   # LÓGICA DEL JUEGO
│   ├── GameState.java     # Estado del juego (mesa, turnos, mazo)
│   └── ScoreCalculator.java # Cálculo de puntajes
│
├── view/                   # INTERFAZ DE USUARIO
│   └── PlayerView.java    # Ventana de un jugador
│
├── controller/             # CONTROL DEL JUEGO
│   └── GameController.java # Maneja las jugadas y actualiza vistas
│
└── Main.java              # PUNTO DE ENTRADA (inicia el juego)
```

### Responsabilidades de Cada Carpeta

#### 📦 model/ - MODELOS (Solo datos)
- Define QUÉ es cada cosa
- NO tiene lógica de juego
- **Card**: Representa una carta
- **Deck**: Colección de cartas
- **Player**: Información del jugador

#### 🎲 game/ - LÓGICA (Reglas del juego)
- Define CÓMO funciona el juego
- Maneja turnos, repartir cartas, calcular puntajes
- **GameState**: Administra el estado completo del juego
- **ScoreCalculator**: Calcula quién gana

#### 🖥️ view/ - VISTA (Lo que ve el usuario)
- Define CÓMO se muestra todo
- Solo muestra información, no toma decisiones
- **PlayerView**: La ventana de cada jugador

#### 🎛️ controller/ - CONTROLADOR (Coordina todo)
- Conecta la lógica con la vista
- Procesa comandos del jugador
- Actualiza las vistas cuando cambia algo
- **GameController**: El cerebro que coordina todo

---

## 🔧 CÓMO MODIFICAR EL JUEGO

### Cambiar los Valores de las Cartas

**Archivo**: `src/escoba/model/Card.java`
**Método**: `getGameValue()`

```java
public int getGameValue() {
    switch (cardNumber) {
        case 10: return 8;   // ← Cambiar aquí para Sota
        case 11: return 9;   // ← Cambiar aquí para Caballo
        case 12: return 10;  // ← Cambiar aquí para Rey
        default: return cardNumber;
    }
}
```

**Ejemplo**: Si quieres que Sota valga 10:
```java
case 10: return 10;  // Sota ahora vale 10
```

---

### Cambiar los Palos de la Baraja

**Archivo**: `src/escoba/model/Deck.java`
**Método**: `initialize()`

```java
private void initialize() {
    String[] suits = {"Oro", "Copa", "Espada", "Basto"};  // ← Cambiar aquí
    // ...
}
```

**Ejemplo**: Para agregar un quinto palo:
```java
String[] suits = {"Oro", "Copa", "Espada", "Basto", "Diamante"};
```

---

### Cambiar la Cantidad de Cartas por Ronda

**Archivo**: `src/escoba/game/GameState.java`
**Método**: `dealCardsToPlayers()`

```java
public void dealCardsToPlayers() {
    for (int i = 0; i < 3; i++) {  // ← Cambiar el 3 por otro número
        // ...
    }
}
```

**Ejemplo**: Para repartir 5 cartas:
```java
for (int i = 0; i < 5; i++) {  // Ahora reparte 5 cartas
```

---

### Cambiar Cartas Iniciales en la Mesa

**Archivo**: `src/escoba/game/GameState.java`
**Método**: `startNewGame()`

```java
// Deal 4 cards to table
for (int i = 0; i < 4; i++) {  // ← Cambiar el 4
    Card card = deck.draw();
    if (card != null) {
        table.add(card);
    }
}
```

---

### Cambiar el Objetivo (15 a otro número)

**Archivo**: `src/escoba/controller/GameController.java`
**Método**: `attemptCapture()`

```java
// Check if sum equals 15
if (sum != 15) {  // ← Cambiar ambos 15
    currentView.displayError("¡Las cartas no suman 15! Tu suma = " + sum);
    return;
}
```

**Ejemplo**: Para cambiar a "Escoba de 20":
```java
if (sum != 20) {
    currentView.displayError("¡Las cartas no suman 20! Tu suma = " + sum);
    return;
}
```

---

### Cambiar Colores de la Interfaz

**Archivo**: `src/escoba/view/PlayerView.java`
**Método**: `createWindow()`

```java
textArea.setBackground(Color.BLACK);   // ← Fondo
textArea.setForeground(Color.GREEN);   // ← Texto
```

**Colores disponibles**:
- `Color.BLACK`, `Color.WHITE`, `Color.RED`, `Color.GREEN`
- `Color.BLUE`, `Color.YELLOW`, `Color.CYAN`, `Color.MAGENTA`
- `new Color(R, G, B)` para colores personalizados (0-255)

**Ejemplo**: Fondo azul oscuro con texto blanco:
```java
textArea.setBackground(new Color(0, 0, 50));  // Azul oscuro
textArea.setForeground(Color.WHITE);          // Texto blanco
```

---

### Cambiar Tamaño de las Ventanas

**Archivo**: `src/escoba/view/PlayerView.java`
**Método**: `createWindow()`

```java
frame.setSize(600, 500);  // ← (ancho, alto)
```

**Ejemplo**: Ventanas más grandes:
```java
frame.setSize(800, 600);  // Más ancho y más alto
```

---

### Cambiar Posición Inicial de las Ventanas

**Archivo**: `src/escoba/Main.java`

```java
PlayerView view1 = new PlayerView("Jugador 1", 100, 100);  // ← (x, y)
PlayerView view2 = new PlayerView("Jugador 2", 750, 100);
```

**Ejemplo**: Ventanas una arriba de la otra:
```java
PlayerView view1 = new PlayerView("Jugador 1", 300, 50);
PlayerView view2 = new PlayerView("Jugador 2", 300, 600);
```

---

### Cambiar los Nombres de los Jugadores

**Archivo**: `src/escoba/Main.java`

```java
PlayerView view1 = new PlayerView("Jugador 1", 100, 100);  // ← Cambiar nombre
PlayerView view2 = new PlayerView("Jugador 2", 750, 100);
```

**Ejemplo**: Nombres personalizados:
```java
PlayerView view1 = new PlayerView("Juan", 100, 100);
PlayerView view2 = new PlayerView("María", 750, 100);
```

---

### Modificar el Sistema de Puntos

**Archivo**: `src/escoba/game/ScoreCalculator.java`
**Método**: `calculateScore()`

```java
public static int calculateScore(Player player, Player opponent) {
    int score = 0;

    // Points from escobas
    score += player.getEscobasCount();  // ← 1 punto por escoba

    // Most cards (1 point)
    if (player.getCapturedCount() > opponent.getCapturedCount()) {
        score++;  // ← 1 punto por más cartas
    }

    // ... más puntos
    return score;
}
```

**Ejemplo**: 2 puntos por escoba en vez de 1:
```java
score += player.getEscobasCount() * 2;  // 2 puntos por escoba
```

---

## 📚 GUÍA DETALLADA DE CLASES

### Card.java - La Carta

**¿Qué hace?**
Representa una carta individual.

**Atributos importantes:**
- `cardNumber`: El número de la carta (1-7, 10-12)
- `suit`: El palo ("Oro", "Copa", "Espada", "Basto")

**Métodos clave:**
- `getGameValue()`: Valor para sumar (Sota=8, Caballo=9, Rey=10)
- `getValueName()`: Nombre de la carta ("As", "Sota", etc.)
- `toString()`: Texto completo ("7 de Oro")

---

### Deck.java - El Mazo

**¿Qué hace?**
Crea y maneja el mazo de 40 cartas.

**Métodos clave:**
- `initialize()`: Crea las 40 cartas
- `shuffle()`: Mezcla el mazo
- `draw()`: Saca una carta del mazo
- `isEmpty()`: Verifica si quedan cartas

---

### Player.java - El Jugador

**¿Qué hace?**
Almacena la mano y cartas capturadas de un jugador.

**Métodos clave:**
- `addCardToHand()`: Agrega carta a la mano
- `removeCardFromHand()`: Quita carta de la mano
- `addCapturedCard()`: Agrega carta capturada
- `incrementEscobas()`: Suma una escoba

---

### GameState.java - Estado del Juego

**¿Qué hace?**
Mantiene el estado completo del juego (mazo, mesa, jugadores, turno).

**Métodos clave:**
- `startNewGame()`: Inicia partida nueva
- `dealCardsToPlayers()`: Reparte cartas
- `switchTurn()`: Cambia de turno
- `getCurrentPlayer()`: Obtiene el jugador actual

---

### GameController.java - Controlador

**¿Qué hace?**
Procesa las jugadas y coordina todo.

**Métodos clave:**
- `processPlayerInput()`: Procesa comandos del jugador
- `attemptCapture()`: Intenta capturar cartas
- `placeCardOnTable()`: Pone carta en la mesa
- `nextTurn()`: Avanza al siguiente turno

---

### PlayerView.java - Vista del Jugador

**¿Qué hace?**
Muestra la ventana del jugador con su información.

**Métodos clave:**
- `displayGameState()`: Muestra mesa, mano, puntajes
- `displayMessage()`: Muestra un mensaje
- `displayError()`: Muestra un error
- `displayHelp()`: Muestra ayuda

---

## 🎯 EJEMPLOS DE MODIFICACIÓN COMPLETOS

### Ejemplo 1: Crear "Escoba de 20"

1. **Cambiar objetivo a 20**:
```java
// En GameController.java, método attemptCapture()
if (sum != 20) {  // Cambiar de 15 a 20
    currentView.displayError("¡No suma 20! Suma = " + sum);
    return;
}
```

2. **Cambiar valores de cartas** (opcional):
```java
// En Card.java, método getGameValue()
case 10: return 11;  // Sota vale 11
case 11: return 12;  // Caballo vale 12
case 12: return 13;  // Rey vale 13
```

---

### Ejemplo 2: Juego para 3 Jugadores

1. **En GameState.java**, agregar tercer jugador:
```java
private Player player3;

public GameState() {
    // ...
    this.player3 = new Player("Jugador 3");
    // ...
}
```

2. **En Main.java**, crear tercera vista:
```java
PlayerView view3 = new PlayerView("Jugador 3", 1400, 100);
```

3. **Modificar dealCardsToPlayers()** para repartir al tercer jugador.

---

### Ejemplo 3: Modo "Turbo" (5 cartas por ronda)

```java
// En GameState.java, método dealCardsToPlayers()
public void dealCardsToPlayers() {
    for (int i = 0; i < 5; i++) {  // Cambiar de 3 a 5
        Card card1 = deck.draw();
        if (card1 != null) player1.addCardToHand(card1);

        Card card2 = deck.draw();
        if (card2 != null) player2.addCardToHand(card2);
    }
}
```

---

## ⚡ COMANDOS PARA EJECUTAR

### Compilar el juego:
```bash
javac -d out/production/GameFramework src/escoba/**/*.java src/escoba/*.java
```

### Ejecutar el juego:
```bash
java -cp out/production/GameFramework escoba.Main
```

---

## 🎮 CÓMO JUGAR

### Comandos en el Juego:
- `jugar 1` - Jugar carta #1 en la mesa
- `jugar 1 llevar 2 3` - Jugar carta #1 y capturar cartas #2 y #3 de la mesa
- `ayuda` - Ver ayuda
- `salir` - Salir del juego

### Ejemplo de Jugada:
```
CARTAS EN LA MESA:
  [1] 7 de Oro (valor: 7)
  [2] 5 de Espada (valor: 5)
  [3] 3 de Copa (valor: 3)

TU MANO:
  [1] As de Basto (valor: 1)
  [2] Sota de Oro (valor: 8)
  [3] Rey de Copa (valor: 10)

Comando: jugar 2 llevar 1
Resultado: Sota(8) + 7 de Oro(7) = 15 ✓ ¡Capturado!
```

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### El juego no compila
- Verifica que todos los archivos .java estén en las carpetas correctas
- Asegúrate de compilar desde la carpeta raíz del proyecto

### Las ventanas no se ven
- Verifica las coordenadas (x, y) en Main.java
- Asegúrate de que no estén fuera de la pantalla

### Los valores de las cartas son incorrectos
- Revisa el método `getGameValue()` en Card.java
- Recuerda: Sota=8, Caballo=9, Rey=10

---

## 📝 NOTAS FINALES

- **Siempre compila después de hacer cambios**
- **Haz una copia de seguridad antes de modificar**
- **Prueba cada cambio antes de hacer otro**
- **Lee los comentarios en el código para más detalles**

¡Diviértete modificando y jugando a la Escoba de 15!
