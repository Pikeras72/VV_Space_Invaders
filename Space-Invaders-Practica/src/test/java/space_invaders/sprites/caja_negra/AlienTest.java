package space_invaders.sprites.caja_negra;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

import main.Commons;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AlienTest {

    @ParameterizedTest
    @CsvSource({
            "100, 100, 100, 100"    ,                  // Coordenadas válidas
            "-1, 100, 0, 100",                // Coordenada X fuera de límite
            "359, 100, 358, 100" // Coordenada X máxima
    })
    void testInitAlien(int x, int y, int expectedX, int expectedY) throws InterruptedException {
        Alien alien = new Alien(x, y);

        assertEquals(expectedX, alien.getX(), "Coordenada x incorrecta");
        assertEquals(expectedY, alien.getY(), "Coordenada y incorrecta");
    }

    @ParameterizedTest
    @CsvSource({
            "100, 1, 101",  // Dirección derecha
            "101, -1, 100"  // Dirección izquierda
    })
    void testAct(int initialX, int direction, int expectedX) {
        Alien alien = new Alien(initialX, 100);
        alien.act(direction);
        assertEquals(expectedX, alien.getX());
    }
}


