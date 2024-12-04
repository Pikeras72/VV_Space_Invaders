package space_invaders.sprites;

import main.Commons;

import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;

public class Player extends Sprite {

    private int width;

    public Player() {

        initPlayer();
    }
    /**
     * Crea un nuevo jugador, le asigna su imagen en la interfaz y lo ubica en el centro de la pantalla
     * */
    private void initPlayer() {

        var playerImg = "src/main/resources/images/player.png";
        var ii = new ImageIcon(playerImg);

        width = ii.getImage().getWidth(null);
        setImage(ii.getImage());

        int START_X = Math.round((float) (Commons.BOARD_WIDTH - Commons.BORDER_RIGHT
                - Commons.BORDER_LEFT) / 2) + Commons.BORDER_LEFT; //Corrección posición x del jugador al inicio
        setX(START_X);

        int START_Y = Commons.GROUND + Commons.PLAYER_HEIGHT/2; //Corrección posición y del jugador al inicio
        setY(START_Y);
    }

    /**
     * Mueve la posición del jugador a la izquierda o a la derecha.
     * Si el jugador ha alcanzado el borde de la pantalla y se intenta mover fuera de la pantalla,
     * lo mantendrá quieto en el borde.  * */
    public void act() {

        x += dx;

        if (x < Commons.BORDER_LEFT + Math.round((float) Commons.PLAYER_WIDTH /2)) { //Antes x >= 2. Limite izquierdo.

            x = Commons.BORDER_LEFT + Math.round((float) Commons.PLAYER_WIDTH /2); //Volver al limite izquierdo.
        }

        if (x > (Commons.BOARD_WIDTH - Commons.BORDER_RIGHT - Commons.PLAYER_WIDTH/2))  { //Antes Commons.BOARD_WIDTH - 2 * width

            x = (Commons.BOARD_WIDTH - Commons.BORDER_RIGHT - Commons.PLAYER_WIDTH/2); //Antes Commons.BOARD_WIDTH - 2 * width
        }
    }
    /**
     * Comprueba qué tecla está pulsada y:
     * - Si se pulsa la tecla flecha a la izquierda, guarda el movimiento hacia la izquierda
     * - Si se pulsa la tecla flecha a la derecha, guarda el movimiento hacia la derecha
     * Cualquier otra tecla de movimiento no produce acción
     * @param e tecla presionada
     * */
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = -2; //Corrección signo incorrecto. Antes dx=2.
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 2;
        }
    }
    /**
     * Comprueba si la tecla e ya no está pulsada:
     * - Si se suelta la tecla flecha a la izquierda, reinicia el desplazamiento de la izquierda a 0
     * - Si se pulsa la tecla flecha a la derecha, reinicia el desplazamiento de la derecha a 0
     * Cualquier otra tecla de movimiento no produce acción
     * @param e tecla presionada
     * */
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
    }
}