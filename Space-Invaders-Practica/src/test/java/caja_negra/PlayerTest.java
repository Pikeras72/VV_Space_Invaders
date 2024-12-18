package caja_negra;


import static org.junit.jupiter.api.Assertions.*;
import main.Commons;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;

public class PlayerTest {

    @ParameterizedTest
    @CsvSource({
            "167,280" //Cambio en el caso de prueba debido al nuevo centro con los nuevos bordes
    })
    void testInitPlayer(int xEsperada, int yEsperada) {
        Player alien = new Player();

        assertEquals(xEsperada, alien.getX(), "La posición X del Player no es correcta");
        assertEquals(yEsperada, alien.getY(), "La posición Y del Player no es correcta");
    }

    @ParameterizedTest
    @CsvSource({ //Cambio test debido a los nuevos límites
            "100,102,2",
            "100,98,-2",
            "321,321,2",
            "321,319,-2",
            "319,321,2",
            "319,317,-2",
            "5,7,2",
            "5,5,-2",
            "7,9,2",
            "7,5,-2"
    })
    void testAct(int x, int xEsperada, int dx) {
        Player player = new Player();
        player.setX(x);
        player.setDx(dx);
        player.act();

        assertEquals(xEsperada, player.getX(), "La posición X del Player no es correcta");
    }

    @ParameterizedTest
    @CsvSource({
            "2,39",
            "-2,37"
    })
    void testKeyPressed(int dxEsperada, int keyCode) {
        Player player = new Player();
        KeyEvent e = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
        e.setKeyCode(keyCode);
        player.keyPressed(e);

        assertEquals(dxEsperada, player.getDx(), "El movimiento dx del Player no es correcto");
    }

    @ParameterizedTest
    @CsvSource({
            "0,39",
            "0,37",
    })
    void testKeyReleased(int dxEsperada, int keyCode) {
        Player player = new Player();
        KeyEvent e = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
        e.setKeyCode(keyCode);
        player.keyReleased(e);

        assertEquals(dxEsperada, player.getDx(), "El movimiento dx del Player no es correcto");
    }
}