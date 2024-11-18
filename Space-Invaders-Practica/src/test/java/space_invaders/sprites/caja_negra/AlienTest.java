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
            "100, 100, 100, 100",       // Coordenadas válidas
            "-1, 100, 0, 100",          // Coordenada x fuera de límite inferior
            "359, 100, 358, 100",       // Coordenada x fuera del limite superior
            "100, -1, 100, 0",          // Coordenada y fuera del limite inferior
            "100, 351, 100, 350",       // Coordenada y fuera del limite superior
            "-1, -1, 0, 0",             // Coordenada x e y fuera del limite inferior
            "-1, 351, 0, 350",          // Coordenada x fuera del limite inferior e y fuera del limite superior
            "359, -1, 358, 0",          // Coordenada x fuera del limite superior e y fuera del límite inferior
            "359, 351, 358, 350"        // Coordenada x e y fuera del limite superior
    })
    void testInitAlien(int x, int y, int expectedX, int expectedY) {
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

    @ParameterizedTest
    @CsvSource({
            "100, 100, 100, 100",       // Coordenadas válidas
            "358, 100, 358, 100",
            "359, 100, 358, 100",
            "0, 100, 0, 100",
            "-1, 100, 0, 100",
            "100, 350, 100, 350",
            "100, 351, 100, 350",
            "100, 0, 100, 0",
            "100, -1, 100, 0"
    })
    void testInitBomb(int x, int y, int expectedX, int expectedY){
        Alien.Bomb bomb = new Alien.Bomb(x, y);

        assertEquals(expectedX, bomb.getX(), "Coordenada x incorrecta");
        assertEquals(expectedY, bomb.getY(), "Coordenada y incorrecta");
    }
}


