package space_invaders.sprites.caja_blanca.PruebasAlien;

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
}
