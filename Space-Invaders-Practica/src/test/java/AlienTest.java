//import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import main.Commons;

import space_invaders.sprites.Alien;

public class AlienTest {

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource(value={
            "170,170,170,170",
            "170,-5,170,0",
            "170,360,170,"+Commons.BOARD_HEIGHT,
            "-5,170,0,170",
            "360,170,"+Commons.BOARD_WIDTH+",170"
    })
    void testInitAlien(int x, int y, int xEsperada, int yEsperada) {
        Alien alien = new Alien(x, y);

        assertEquals(xEsperada, alien.getX(), "La posición X del Alien no es correcta");
        assertEquals(yEsperada, alien.getY(), "La posición Y del Alien no es correcta");
    }

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource(value={
            "360,-5,358,0",
            "170,170,170,170",
    })
    void testInitBomb(int x, int y, int xEsperada, int yEsperada) {
        Alien.Bomb bomb = new Alien.Bomb(x, y);

        assertEquals(xEsperada, bomb.getX(), "La posición X de la Bomba no es correcta");
        assertEquals(yEsperada, bomb.getY(), "La posición Y de la Bomba no es correcta");
    }

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource(value={
            "1,171"
    })
    void testAct(int x, int xEsperada) {
        Alien alien = new Alien(170,170);
        alien.act(1);

        assertEquals(xEsperada, alien.getX());
    }

}
