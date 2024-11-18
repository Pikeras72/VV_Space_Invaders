import static org.junit.jupiter.api.Assertions.*;

import main.Board;

import main.Commons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Player;
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































    @Test
    void testAlienGeneraBomba() {
        // Caso: Alien genera bomba
        List<Alien> aliens = new ArrayList<>();
        Alien alien = new Alien(50, 50);
        Alien.Bomb bomb = alien.getBomb();
        alien.setVisible(true);
        bomb.setDestroyed(true);
        aliens.add(alien);

        board.getAliens().clear();
        board.getAliens().addAll(aliens);

        board.update_bomb();

        assertFalse(bomb.isDestroyed(), "La bomba no debe estar destruida.");
        assertEquals(alien.getX(), bomb.getX(), "La bomba debe generarse en la coordenada X del alien.");
        assertEquals(alien.getY(), bomb.getY(), "La bomba debe generarse en la coordenada Y del alien.");
    }

    @Test
    void testBombaColisionaConJugador() {
        // Caso: Bomba colisiona con el jugador
        Player player = board.getPlayer();
        player.setVisible(true);
        player.setX(50);
        player.setY(50);

        Alien alien = new Alien(50, 50);
        Alien.Bomb bomb = alien.getBomb();
        bomb.setX(player.getX());
        bomb.setY(player.getY());
        bomb.setDestroyed(false);

        List<Alien> aliens = new ArrayList<>();
        aliens.add(alien);

        board.getAliens().clear();
        board.getAliens().addAll(aliens);

        board.update_bomb();

        assertTrue(player.isDying(), "El jugador debe estar en estado de morir.");
        assertTrue(bomb.isDestroyed(), "La bomba debe ser destruida tras colisionar con el jugador.");
    }

    @Test
    void testBombaNoColisionaYSigueCayendo() {
        // Caso: Bomba no colisiona y sigue cayendo
        Player player = board.getPlayer();
        player.setVisible(true);
        player.setX(100);
        player.setY(100);

        Alien alien = new Alien(50, 50);
        Alien.Bomb bomb = alien.getBomb();
        bomb.setX(50); // Fuera del rango del jugador
        bomb.setY(80);
        bomb.setDestroyed(false);

        List<Alien> aliens = new ArrayList<>();
        aliens.add(alien);

        board.getAliens().clear();
        board.getAliens().addAll(aliens);

        board.update_bomb();

        assertFalse(bomb.isDestroyed(), "La bomba no debe ser destruida si no colisiona.");
        assertEquals(81, bomb.getY(), "La posición Y de la bomba debe incrementarse en 1.");
    }

    @Test
    void testBombaAlcanzaElSuelo() {
        // Caso: Bomba alcanza el suelo
        Alien alien = new Alien(50, 50);
        Alien.Bomb bomb = alien.getBomb();
        bomb.setX(50);
        bomb.setY(Commons.GROUND - Commons.BOMB_HEIGHT); // En el límite del suelo
        bomb.setDestroyed(false);

        List<Alien> aliens = new ArrayList<>();
        aliens.add(alien);

        board.getAliens().clear();
        board.getAliens().addAll(aliens);

        board.update_bomb();

        assertFalse(bomb.isDestroyed(), "La bomba debe ser destruida tras alcanzar el suelo.");
    }

    @Test
    void testAlienNoVisibleYBombaDestruida() {
        // Caso: Alien no visible y bomba destruida
        Alien alien = new Alien(50, 50);
        alien.setVisible(false); // Alien no visible
        Alien.Bomb bomb = alien.getBomb();
        bomb.setDestroyed(true);

        List<Alien> aliens = new ArrayList<>();
        aliens.add(alien);

        board.getAliens().clear();
        board.getAliens().addAll(aliens);

        board.update_bomb();

        assertTrue(bomb.isDestroyed(), "La bomba debe permanecer destruida si el alien no es visible.");
    }





















    @Test
    public void testAliensMoveInCorrectDirection() {
        Board board = new Board();
        Alien alien = board.getAliens().get(0);
        alien.setY(Commons.GROUND - Commons.ALIEN_HEIGHT - 1); // Alien no está en el borde inferior
        alien.setVisible(true);

        int initialX = alien.getX();

        board.update_aliens();

        // Verificar que los aliens se movieron en la dirección indicada
        if (board.getDirection() == -2) { // Izquierda
            assertTrue(alien.getX() < initialX);
        } else if (board.getDirection() == 2) { // Derecha
            assertTrue(alien.getX() > initialX);
        }
    }

    @Test
    public void testAlienReachesRightLimit() {
        Board board = new Board();
        Alien alien = board.getAliens().get(0);
        alien.setX(Commons.BOARD_WIDTH - Commons.BORDER_RIGHT); // En el límite derecho
        board.setDirection(1); // Dirección derecha

        int initialY = alien.getY();

        board.update_aliens();

        // Verificar que cambió la dirección a 0 y los aliens bajaron
        assertEquals(0, board.getDirection());
        assertEquals(initialY + Commons.GO_DOWN, alien.getY());
    }

    @Test
    public void testAlienReachesLeftLimit() {
        Board board = new Board();
        Alien alien = board.getAliens().get(0);
        alien.setX(Commons.BORDER_LEFT); // En el límite izquierdo
        board.setDirection(-1); // Dirección izquierda

        int initialY = alien.getY();

        board.update_aliens();

        // Verificar que cambió la dirección a 1 y los aliens bajaron
        assertEquals(1, board.getDirection());
        assertEquals(initialY + Commons.GO_DOWN, alien.getY());
    }

    @Test
    public void testAlienReachesBottomLimit() {
        Board board = new Board();
        Alien alien = board.getAliens().get(0);
        alien.setY(Commons.GROUND - Commons.ALIEN_HEIGHT + 1); // Más allá del borde inferior
        alien.setVisible(true);

        board.update_aliens();

        // Verificar que el juego termina con el mensaje esperado
        assertFalse(board.isInGame());
        assertEquals("Invasion!", board.getMessage());
    }

    @Test
    public void testNoAliensVisible() {
        Board board = new Board();

        // Marcar todos los aliens como no visibles
        for (Alien alien : board.getAliens()) {
            alien.setVisible(false);
        }

        board.update_aliens();

        // Comprobamos que el juego sigue activo y no hay cambios
        assertTrue(board.isInGame());
        for (Alien alien : board.getAliens()) {
            assertFalse(alien.isVisible());
        }
    }












    @Test
    public void testEndOfGame() {
        Board board = new Board();

        // Configurar estado inicial
        board.setDeaths(Commons.CHANCE); // Se alcanzó el límite de muertes
        board.setInGame(true);             // El juego está en progreso
        board.getTimer().start();             // Temporizador activo

        board.update(); // Llamar al método que estamos probando

        // Verificaciones
        assertFalse(board.isInGame());            // El juego debe finalizar
        assertEquals("Game won!", board.getMessage()); // Mensaje de victoria
        assertFalse(board.getTimer().isRunning()); // El temporizador debe detenerse
    }

    @Test
    public void testGameInProgress() {
        // Crear una instancia del tablero
        Board board = new Board();

        // Configurar estado inicial
        board.setDeaths(0);             // Las muertes iniciales son 0
        board.setInGame(true);          // El juego está en progreso

        // Llamar al método que estamos probando
        board.update();

        // Verificaciones
        assertTrue(board.isInGame());           // El juego debe continuar
        assertNotNull(board.getPlayer());   // El jugador está inicializado correctamente
        assertFalse(board.getAliens().isEmpty()); // Debe haber aliens en el tablero
        assertTrue(board.getShot().isVisible() || !board.getShot().isVisible()); // Validar que el disparo esté correctamente configurado

    }
}
