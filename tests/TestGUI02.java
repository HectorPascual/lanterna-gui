import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import com.googlecode.lanterna.terminal.ansi.UnixTerminal;

import java.io.IOException;
import java.util.Random;

public class TestGUI02 {
    public static void main (String[] args) {

        Terminal term = null;
        final TextGraphics textGraphics1;
        KeyStroke keyStroke;
        Screen screen;
        try {
            term = new UnixTerminal();
          //  term.enterPrivateMode();

            screen = new TerminalScreen(term);
            screen.startScreen();

            Random rand = new Random();
            TerminalSize terminalSize = screen.getTerminalSize();
            for(int c = 0; c < terminalSize.getColumns(); c++){
                for(int r = 0; r < terminalSize.getRows(); r++) {
                    screen.setCharacter(c,r, new TextCharacter(' ', TextColor.ANSI.DEFAULT,
                            TextColor.ANSI.values()[rand.nextInt(TextColor.ANSI.values().length)]));
                }
            }

            screen.refresh();

            long startTime = System.currentTimeMillis();
            while(System.currentTimeMillis() - startTime < 4000) {
                // The call to pollInput() is not blocking, unlike readInput()
                if (screen.pollInput() != null) {
                    break;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignore) {
                    break;
                }
            }


                //exit
            term.exitPrivateMode();
        } catch (IOException e) {

        } finally {
            try {
                term.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
