//import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Player;

import java.awt.event.KeyEvent;

public class PlayerTest {

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource(value={ //Cambio en los test debido a modificación en la lógica
            "5,-1,5",
            "321,1,321", //Nuevo caso de prueba antes imposible. Límite derecho.
            "100,1,101"
    })
    void testAct(int x, int dx, int xEsperada) {
        Player player = new Player();
        player.setDx(dx);
        player.setX(x);
        player.act();

        assertEquals(xEsperada, player.getX(), "La posición X del Alien no es correcta");
    }

    @ParameterizedTest
    @CsvSource({
            "39,2",
            "37,-2",
            "22,0" //no hace nada, elegimos el dxEsperada que queramos
    })
    void testKeyPressed(int keyCode, int dxEsperada) {
        Player player = new Player();

        if (keyCode != 37 && keyCode != 39) {
            dxEsperada = player.getDx();
        }
        KeyEvent e = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
        e.setKeyCode(keyCode);
        player.keyPressed(e);

        assertEquals(dxEsperada, player.getDx(), "El movimiento dx del Player no debería cambiar");
    }

    @ParameterizedTest
    @CsvSource({
            "39,0",
            "37,0",
            "22,0"
    })
    void testKeyReleased(int keyCode, int dxEsperada) {
        Player player = new Player();

        if (keyCode != 37 && keyCode != 39) {
            dxEsperada = player.getDx();
        }
        KeyEvent e = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
        e.setKeyCode(keyCode);
        player.keyReleased(e);

        assertEquals(dxEsperada, player.getDx(), "El movimiento dx del Player no es correcto");
    }

    @ParameterizedTest
    @CsvSource({ //Cambio en los test debido al nuevo centro y bordes de la pantalla
            "167, 280"
    })
    void testInitPlayer(int xEsperada, int yEsperada) {
        Player player = new Player();

        assertEquals(xEsperada, player.getX(), "La posición X del Player no es correcta");
        assertEquals(yEsperada, player.getY(), "La posición Y del Player no es correcta");
    }
}