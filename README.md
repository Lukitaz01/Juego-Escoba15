# La Escoba de 15

Juego de cartas tradicional argentino para 2 jugadores.

## 🎮 Descripción

La Escoba de 15 es un juego de cartas español donde el objetivo es capturar cartas de la mesa que sumen exactamente 15 puntos con las cartas de tu mano.

## 📋 Reglas Rápidas

- **Objetivo**: Capturar cartas que sumen 15
- **Valores**:
  - Cartas 1-7: su número
  - Sota: 8 puntos
  - Caballo: 9 puntos
  - Rey: 10 puntos
- **Escoba**: ¡Vaciar la mesa completamente!

## 🚀 Cómo Ejecutar

### Compilar:
```bash
javac -d out/production/GameFramework src/escoba/**/*.java src/escoba/*.java
```

### Ejecutar:
```bash
java -cp out/production/GameFramework escoba.Main
```

## 🎯 Comandos del Juego

- `jugar 1` - Poner carta #1 en la mesa
- `jugar 1 llevar 2 3` - Jugar carta #1 y capturar cartas #2 y #3 (deben sumar 15)
- `ayuda` - Ver ayuda completa
- `salir` - Salir del juego

## 📖 Ejemplo de Jugada

```
CARTAS EN LA MESA:
  [1] 7 de Oro (valor: 7)
  [2] 5 de Espada (valor: 5)
  [3] 3 de Copa (valor: 3)

TU MANO:
  [1] As de Basto (valor: 1)
  [2] Sota de Oro (valor: 8)
  [3] Rey de Copa (valor: 10)

>>> jugar 2 llevar 1
Resultado: Sota(8) + 7 de Oro(7) = 15 ✓ ¡Capturado!
```

## 🏆 Sistema de Puntos

Al final del juego se otorgan puntos por:

- **1 punto** por cada escoba (vaciar la mesa)
- **1 punto** por más cartas capturadas
- **1 punto** por más cartas de Oro
- **1 punto** por tener el 7 de Oro
- **1 punto** por más 7s capturados

## 📂 Estructura del Proyecto

```
src/escoba/
├── model/              # Modelos de datos (Card, Deck, Player)
├── game/               # Lógica del juego (GameState, ScoreCalculator)
├── view/               # Interfaz de usuario (PlayerView)
├── controller/         # Control del juego (GameController)
└── Main.java          # Punto de entrada
```

## 📚 Documentación Completa

Para modificar el juego (valores de cartas, colores, reglas, etc.), consulta:
- **DOCUMENTACION.md** - Guía completa con ejemplos de modificación

## 🛠️ Modificaciones Comunes

### Cambiar valores de cartas:
Edita `src/escoba/model/Card.java` → método `getGameValue()`

### Cambiar colores:
Edita `src/escoba/view/PlayerView.java` → método `createWindow()`

### Cambiar comandos:
Edita `src/escoba/controller/GameController.java` → método `processPlayerInput()`

## 📝 Características

✅ Interfaz en español
✅ Código completamente comentado
✅ Reglas correctas del juego argentino
✅ Valores correctos (Sota=8, Caballo=9, Rey=10)
✅ Sistema de puntos completo
✅ Dos ventanas independientes
✅ Fácil de modificar y extender

## 🤝 Contribuir

Este es un proyecto educativo. Siéntete libre de modificarlo y aprender de él.

## 📄 Licencia

Proyecto educativo de código abierto.

---

¡Diviértete jugando a La Escoba de 15! 🎴
