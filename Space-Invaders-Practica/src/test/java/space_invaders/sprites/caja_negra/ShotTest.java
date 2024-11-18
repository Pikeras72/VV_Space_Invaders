package space_invaders.sprites.caja_negra;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Shot;

class ShotTest {

    @ParameterizedTest
    @CsvSource({
            "100, 100, 106, 99",        // Coordenadas válidas
            "359, 100, 365, 99",        // Coordenadas límites
            "358, 100, 364, 99",
            "0, 100, 6, 99",
            "-1, 100, 5, 99",
            "100, 351, 106, 350",
            "100, 350, 106, 349",
            "100, 0, 106, -1",
            "100, -1, 106, -2"
    })
    void testInitShot(int x, int y, int expectedX, int expectedY) {
        Shot shot = new Shot(x, y);
        assertEquals(expectedX, shot.getX());
        assertEquals(expectedY, shot.getY());
    }
}
