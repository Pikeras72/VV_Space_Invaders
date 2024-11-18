//import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import space_invaders.sprites.Player;

public class PlayerTest {

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource(value={
            "2,-1,1",
            "100,1,101"
    })
    void testActPlayer(int x, int dx, int xEsperada) {
        Player player = new Player();
        player.setDx(dx);
        player.setX(x);
        player.act();

        assertEquals(xEsperada, player.getX(), "La posición X del Alien no es correcta");
    }

}
