//import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Shot;

import java.awt.event.KeyEvent;

public class ShotTest {
    @ParameterizedTest
    @CsvSource({
            "170, 170, 176, 169",
    })
    void testInitShot(int x, int y, int expectedX, int expectedY) {
        Shot shot = new Shot(x, y);
        assertEquals(expectedX, shot.getX());
        assertEquals(expectedY, shot.getY());
    }
}
