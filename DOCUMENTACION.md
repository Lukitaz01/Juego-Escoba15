# LA ESCOBA DE 15 - Documentación Completa

## 📋 TABLA DE CONTENIDOS
1. [Reglas del Juego](#reglas-del-juego)
2. [Estructura del Proyecto](#estructura-del-proyecto)
3. [Guía de Clases](#guía-de-clases)


---

## 🎮 REGLAS DEL JUEGO

### Objetivo
Capturar cartas de la mesa que sumen exactamente 15 con las cartas de tu mano.

### Valores de las Cartas
- **Cartas 1-7**: Valen su número (1, 2, 3, 4, 5, 6, 7)
- **Sota**: Vale **8 puntos**
- **Caballo**: Vale **9 puntos**
- **Rey**: Vale **10 puntos**

### Cómo Jugar a la Escoba de 15
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

