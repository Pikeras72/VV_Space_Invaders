import static org.junit.jupiter.api.Assertions.*;

import main.Board;

import main.Commons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Shot;

import java.util.ArrayList;
import java.util.List;

public class BoardTest {

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource(value={
            "8, 186, 23",
    })
    void testGameInit(int alienIndex, int xExpected, int yExpected) {
        Board board = new Board();
        Alien alien = board.getAliens().get(alienIndex);

        assertEquals(xExpected, alien.getX(), "Coordenada x erronea");
        assertEquals(yExpected, alien.getY(), "Coordenada y erronea");
    }

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testDisparoImpactaEnAlien() {
        // Caso: Disparo impacta en alienígena
        Shot shot = board.getShot();
        List<Alien> aliens = new ArrayList<>();
        Alien alien = new Alien(50, 50);
        aliens.add(alien);

        board.getAliens().clear();
        board.getAliens().addAll(aliens);

        shot.setX(alien.getX() + Commons.ALIEN_WIDTH / 2);
        shot.setY(alien.getY() + Commons.ALIEN_HEIGHT / 2);
        shot.setVisible(true);

        alien.setVisible(true);

        board.update_shots();

        assertTrue(alien.isDying(), "El alien debe estar marcado como muriendo.");
        assertFalse(shot.isVisible(), "El disparo debe desaparecer tras impactar.");
        assertEquals(1, board.getDeaths(), "Las muertes deberían ser: 1.");
    }

    @Test
    void testDisparoNoImpactaEnAlien() {
        // Caso: Disparo no impacta en alienígena existente
        Shot shot = board.getShot();
        List<Alien> aliens = new ArrayList<>();
        Alien alien = new Alien(50, 50);
        aliens.add(alien);

        board.getAliens().clear();
        board.getAliens().addAll(aliens);

        shot.setX(200); // Fuera del rango del alien
        shot.setY(200);
        shot.setVisible(true);

        alien.setVisible(true);

        board.update_shots();

        assertTrue(alien.isVisible(), "El alien debe seguir visible.");
        assertTrue(shot.isVisible(), "El disparo debe seguir visible.");
        assertEquals(196, shot.getY(), "El disparo debe seguir moviéndose hacia arriba.");
    }

    @Test
    void testDisparoSaleDelTablero() {
        // Caso: Disparo sale del tablero
        Shot shot = board.getShot();
        shot.setX(50);
        shot.setY(-10); // Fuera del tablero
        shot.setVisible(true);

        board.update_shots();

        assertFalse(shot.isVisible(), "El disparo debe desaparecer al salir del tablero.");
    }

    @Test
    void testDisparoArrayAliensVacio() {
        // Caso: Disparo existente, array de aliens vacío
        Shot shot = board.getShot();
        board.getAliens().clear(); // No hay aliens en el tablero
        shot.setX(50);
        shot.setY(50);
        shot.setVisible(true);

        board.update_shots();

        assertTrue(shot.isVisible(), "El disparo debe seguir visible.");
        assertEquals(46, shot.getY(), "El disparo debe seguir moviéndose hacia arriba.");
    }

    @Test
    void testNoExisteDisparo() {
        // Caso: No existe disparo
        Shot shot = board.getShot();
        shot.setVisible(false); // El disparo no es visible

        board.update_shots();

        assertFalse(shot.isVisible(), "El disparo no debe estar visible.");
        // No ocurre ninguna acción, así que no hay más aserciones necesarias
    }

    @Test
    void testAlienDestruidoDisparoVisible() {
        // Caso: Alien destruido y disparo visible
        Shot shot = board.getShot();
        List<Alien> aliens = new ArrayList<>();
        Alien alien = new Alien(50, 50);
        aliens.add(alien);

        board.getAliens().clear();
        board.getAliens().addAll(aliens);

        shot.setX(50);
        shot.setY(50);
        shot.setVisible(true);

        alien.setVisible(false); // El alien ya está destruido

        board.update_shots();

        assertTrue(shot.isVisible(), "El disparo debe seguir visible.");
        assertEquals(46, shot.getY(), "El disparo debe seguir moviéndose hacia arriba.");
    }












}
