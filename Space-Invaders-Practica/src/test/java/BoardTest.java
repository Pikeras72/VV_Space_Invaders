import static org.junit.jupiter.api.Assertions.*;

import main.Board;

import main.Commons;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Shot;

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


    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource(value={
            "100, 100, false, 24, 0",  // C1: Disparo no visible
            "100, 100, true, 24, 0",   // C2: Disparo fuera del tablero
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

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource(value={
            Commons.NUMBER_OF_ALIENS_TO_DESTROY+", Game won!",
            Commons.CHANCE+", Game Over"
    })
    void testUpdate(int deaths, String messageEsperado) {
        Board board = new Board();
        board.setDeaths(deaths);
        board.update();
        assertEquals(messageEsperado, board.getMessage());


}
