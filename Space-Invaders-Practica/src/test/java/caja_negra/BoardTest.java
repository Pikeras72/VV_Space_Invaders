package caja_negra;

import static org.junit.jupiter.api.Assertions.*;

import main.Board;
import main.Commons;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Shot;

import java.util.Objects;

public class BoardTest {

    @ParameterizedTest
    @CsvSource({
            "0, " + Commons.ALIEN_INIT_X + ", " + Commons.ALIEN_INIT_Y,     // Primer alien
            "8, 186, 23",                                                   // Alien en el medio
            "23, 258, 77"                                                   // Último alien
    })
    void testGameInit(int alienIndex, int xExpected, int yExpected) {
        Board board = new Board();
        Alien alien = board.getAliens().get(alienIndex);

        assertEquals(xExpected, alien.getX(), "Coordenada x erronea");
        assertEquals(yExpected, alien.getY(), "Coordenada y erronea");
    }

    @ParameterizedTest
    @CsvSource({
            "24, Game won!",  // Todos los aliens destruidos
            "8, Invasion!"
    })
    void testUpdate(int deaths, String expected) {
        Board board = new Board();
        board.setDeaths(deaths);
        board.update();
        assertEquals(expected, board.getMessage());
    }



    @ParameterizedTest
    @CsvSource({
            "100, 100, false, 24, 0",  // C1: Disparo no visible
            "100, -1, true, 24, 0",   // C2: Disparo fuera del tablero
            "100, 100, true, 24, 0",  // C3: Disparo sigue en el tablero
            "100, 100, true, 24, 1"   // C4: Disparo impacta a un alien
    })
    void testUpdateShots(int shotX, int shotY, boolean shotVisible, int remainingAliens, int expectedDeaths) {
        Board board = new Board();

        Shot shot = new Shot(shotX, shotY);
        shot.setVisible(shotVisible);
        board.setShot(shot);

        if (expectedDeaths > 0) { // Añadimos un alien solo si se espera una muerte
            Alien alien = new Alien(shotX, shotY);
            board.getAliens().add(alien);
        }

        // Estado inicial antes de actualizar
        int initialAliens = board.getAliens().size();
        int initialDeaths = board.getDeaths();

        board.update_shots();

        // Verificar el contador de muertes
        assertEquals(expectedDeaths, board.getDeaths(), "Error en el contador de muertes");

        // Validar que en el caso C1 no se modifica el estado del tablero
        if (!shotVisible) { // Caso C1 específico
            assertEquals(shotX + 6, shot.getX());
            assertEquals(shotY - 1, shot.getY());

            assertEquals(initialAliens, board.getAliens().size(), "C1: Los aliens no deberían cambiar");
            assertEquals(initialDeaths, board.getDeaths(), "C1: Las muertes no deberían cambiar");
        }
    }

    @ParameterizedTest
    @CsvSource({
            // Alien X, Alien Y, Direction, Expected Direction, Expected Message
            "10, 278, 1, 1, Invasion!",         // C1: Aliens alcanzan el borde inferior
            "328, 100, 1, -1, null",                // C2: Aliens alcanzan el borde derecho
            "5, 100, -1, 1, null",                  // C3: Aliens alcanzan el borde izquierdo
            "100, 100, 1, 1, null",                 // C4: Aliens se mueven normalmente a la derecha
            "100, 100, -1, -1, null"                // C4: Aliens se mueven normalmente a la izquierda
    })
    void testUpdateAliens(int alienX, int alienY, int direction, int expectedDirection, String expectedMessage) {
        // Configurar el tablero
        Board board = new Board();
        board.setDirection(direction);

        // Configurar el alien
        Alien alien = new Alien(alienX, alienY);
        board.getAliens().clear(); // Asegurarse de que la lista está vacía
        board.getAliens().add(alien);

        // Llamar al método de actualización
        board.update_aliens();

        // Verificar el mensaje del juego
        if (!Objects.equals(expectedMessage, "null")){       //C1
            assertEquals(expectedMessage, board.getMessage(), "El mensaje del juego no es el esperado");
            assertFalse(board.isInGame(), "El juego debería haber terminado cuando los aliens alcanzan el borde inferior");

        }else {
            // Verificar la dirección del movimiento
            assertEquals(expectedDirection, board.getDirection(), "La dirección no es la esperada");

            // Verificar que el alien ha bajado si está en el borde
            if (direction != expectedDirection) {
                assertEquals(Commons.GO_DOWN, alien.getY() - alienY, "El alien debería haber bajado");
            }

            // Verificar el movimiento horizontal
            if (direction == 1 && alienX <= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT) {
                assertEquals(alienX + direction, alien.getX(), "El alien no se movió correctamente hacia la derecha");
            } else if (direction == -1 && alienX > Commons.BORDER_LEFT) {
                assertEquals(alienX + direction, alien.getX(), "El alien no se movió correctamente hacia la izquierda");
            }
        }
        


    }
    @ParameterizedTest
    @CsvSource({
            "Commons.BOARD_HEIGHT - 1, true", // Aliens alcanzan borde inferior
            "Commons.BOARD_WIDTH - 1, false" // Alien alcanza borde derecho
    })
    void testUpdateBomb(){

    }
}
