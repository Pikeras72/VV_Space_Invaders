package caja_negra;

import static org.junit.jupiter.api.Assertions.*;

import main.Board;
import main.Commons;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Player;
import space_invaders.sprites.Shot;

import java.util.Objects;

public class BoardTest {

    @ParameterizedTest
    @CsvSource({
            "0, " + Commons.ALIEN_INIT_X + ", " + Commons.ALIEN_INIT_Y,     // Primer alien
            "8, 186, 23",                                                   // Alien en el medio
            "23, 240, 59"                                                   // Último alien  //LINEA CAMBIADA
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
            "8, Game Over"
    })
    void testUpdate(int deaths, String expected) {
        Board board = new Board();
        board.setDeaths(deaths);
        board.update();
        assertEquals(expected, board.getMessage());
    }



    @ParameterizedTest
    @CsvSource({
            "100, 100, false, 0",  // C1: Disparo no visible
            "100, -1, true, 0",   // C2: Disparo fuera del tablero
            "100, 100, true, 0",  // C3: Disparo sigue en el tablero
            "100, 100, true, 1"   // C4: Disparo impacta a un alien
    })
    void testUpdateShots(int shotX, int shotY, boolean shotVisible, int expectedDeaths) {
        Board board = new Board();

        Shot shot = new Shot(shotX, shotY);
        shot.setVisible(shotVisible);
        board.setShot(shot);

        Alien alien = new Alien(shot.getX(), shot.getY());
        board.getAliens().add(alien);

        // Estado inicial antes de actualizar
        int initialAliens = board.getAliens().size();
        int initialDeaths = board.getDeaths();

        board.update_shots();

        if (expectedDeaths > 0) {
            // Verificar el contador de muertes
            assertEquals(true, alien.isDying());
            assertEquals(expectedDeaths, board.getDeaths(), "Error en el contador de muertes");
        }


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
                assertEquals(alienY + Commons.GO_DOWN, alien.getY(), "El alien debería haber bajado");
            }

            // Verificar el movimiento horizontal
            if (direction == 1 && alienX <= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT) {
                assertNotEquals(alienX , alien.getX(), "El alien no se movió correctamente hacia la derecha");
            } else if (direction == -1 && alienX > Commons.BORDER_LEFT) {
                assertNotEquals(alienX, alien.getX(), "El alien no se movió correctamente hacia la izquierda");
            }
        }
    }

    @ParameterizedTest
    @CsvSource({
            "100, 200, 100, 330, true, 100, 201, false, false",  // C1: Bomba no creada o destruida, debe crearse en la posición del alien
            "100, 200, 100, 330, false, 100, 201, false, false", // C2: Bomba no destruida, se mueve hacia abajo
            "100, 200, 100, 330, false, 100, 200, true, true",   // C3: Bomba alcanza al jugador, cambia estado a "dying"
            "100, 285, 100, 330, false, 100, " + (Commons.GROUND - Commons.BOMB_HEIGHT) + ", true, false" // C4: Bomba llega al suelo, se destruye
    })
    void testUpdateBomb(int alienX, int alienY, int playerX, int playerY, boolean bombDestroyed,
                        int expectedBombX, int expectedBombY, boolean expectedBombDestroyed, boolean expectedPlayerDying) {
        // Configurar el tablero
        Board board = new Board();

        // Configurar el alien
        Alien alien = new Alien(alienX, alienY);
        board.getAliens().add(alien);

        // Configurar la bomba
        Alien.Bomb bomb = alien.getBomb();
        bomb.setDestroyed(bombDestroyed);
        bomb.setX(alienX);
        bomb.setY(alienY);

        // Configurar el jugador
        Player player = new Player();
        player.setX(playerX);
        player.setY(playerY);
        board.setPlayer(player);

        // Llamar al método de actualización
        board.update_bomb();

        // Verificar que la bomba se haya creado correctamente si estaba destruida
        if (bombDestroyed ) {
            assertTrue(bomb.isDestroyed(), "La bomba debería haberse creado, estado destroyed a true");
            assertEquals(expectedBombX, bomb.getX(), "La bomba debería estar en la posición X del alien");
            assertEquals(expectedBombY, bomb.getY() + 1, "La bomba debería estar en la posición Y del alien");
        }

        // Verificar el movimiento de la bomba (C2)
        if (!bomb.isDestroyed() && bomb.getY() != expectedBombY && !expectedBombDestroyed) {
            assertEquals(expectedBombY, bomb.getY(), "La bomba debería moverse hacia abajo");
        }

        // Verificar si la bomba ha alcanzado al jugador (C3)
        if (expectedPlayerDying){
            bomb.setX(playerX);
            bomb.setY(playerY);
            board.update_bomb();

            assertTrue(player.isDying(), "El jugador debería haber cambiado su estado a 'dying'");
            assertEquals(expectedPlayerDying, player.isDying(), "El estado de muerte del jugador no es el esperado");
            assertTrue(bomb.isDestroyed(), "La bomba debería haberse destruido después de golpear al jugador");

        }

        // Verificar si la bomba ha llegado al suelo (C4)
        if (bomb.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) {
            assertTrue(bomb.isDestroyed(), "La bomba debería haberse destruido cuando llegó al suelo");
            assertEquals(expectedBombDestroyed, bomb.isDestroyed(), "El estado de la bomba no es el esperado");
        }
    }

}
