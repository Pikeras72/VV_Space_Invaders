package caja_negra;

import static org.junit.jupiter.api.Assertions.*;

import main.Board;
import main.Commons;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Shot;

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
            "100, 100, false, 0, 0",  // C1: Disparo no visible
            "100, -1, true, 0, 0",   // C2: Disparo fuera del tablero
            "100, 100, true, 0, 0",  // C3: Disparo sigue en el tablero
            "100, 100, true, 0, 1"   // C4: Disparo impacta a un alien
    })
    void testUpdateShots(int shotX, int shotY, boolean shotVisible, int remainingAliens, int expectedDeaths) {
        // Configuración inicial del tablero
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


        // Verificar el número de aliens restantes
        assertEquals(remainingAliens, board.getAliens().size(), "Error en el conteo de aliens");

        // Verificar el contador de muertes
        assertEquals(expectedDeaths, board.getDeaths(), "Error en el contador de muertes");

        // Validar que en el caso C1 no se modifica el estado del tablero
        if (!shotVisible) { // Caso C1 específico
            assertEquals(initialAliens, board.getAliens().size(), "C1: Los aliens no deberían cambiar");
            assertEquals(initialDeaths, board.getDeaths(), "C1: Las muertes no deberían cambiar");
        }
    }

    @ParameterizedTest
    @CsvSource({
            "Commons.BOARD_HEIGHT - 1, true", // Aliens alcanzan borde inferior
            "Commons.BOARD_WIDTH - 1, false" // Alien alcanza borde derecho
    })
    void testUpdateAliens(int position, boolean gameOver) {
        Board board = new Board();
        Alien alien = new Alien(position, 100);
        board.getAliens().clear();
        board.getAliens().add(alien);
        board.update_aliens();
        assertEquals(gameOver, board.isInGame());
    }

    @ParameterizedTest
    @CsvSource({
            "Commons.BOARD_HEIGHT - 1, true", // Aliens alcanzan borde inferior
            "Commons.BOARD_WIDTH - 1, false" // Alien alcanza borde derecho
    })
    void testUpdateBomb(){

    }
}
