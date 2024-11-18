package space_invaders.sprites.caja_negra;

import static org.junit.jupiter.api.Assertions.*;

import main.Board;
import main.Commons;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Shot;

class BoardTest {

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
            "8, Invasion!"   // Ningún alien destruido
    })
    void testUpdate(int deaths, String expected) {
        Board board = new Board();
        board.setDeaths(deaths);
        board.update();
        assertEquals(expected, board.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "false, true, 100, 0, true",
            "true, true, 100, 0, true",
            "true, false, 100, 0, true",
            "true, true, 100, 0, true",

    })
    void testUpdateShots(boolean shotVisible, boolean alienVisible, int shotX, int shotY, boolean shotsEmpty) {
        Board board = new Board();
        Shot shot = new Shot(shotX, shotY);
        shot.setVisible(shotVisible);
        board.setShot(shot);

        if (!shotsEmpty) {
            Alien alien = new Alien(shotX, shotY);
            alien.setVisible(alienVisible);
            board.getAliens().add(alien);
        }

        board.update_shots();
        assertEquals(shot, board.getShot());
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
