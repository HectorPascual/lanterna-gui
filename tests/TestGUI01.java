import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.*;
import com.googlecode.lanterna.terminal.ansi.UnixTerminal;

import javax.swing.*;
import java.io.IOException;

public class TestGUI01 {


    public static void main (String[] args) {

        Terminal term = null;
        final TextGraphics textGraphics1;
        KeyStroke keyStroke;
        try {
            term = new UnixTerminal();
            term.enterPrivateMode();
            textGraphics1 = term.newTextGraphics();
            textGraphics1.putString(2,1,"TESTING THE GUI");
            TerminalSize size = term.getTerminalSize();
            textGraphics1.putString(3,1, " Dimensions " + size.getRows() + " x " + size.getColumns());
            term.flush();

            term.addResizeListener(new TerminalResizeListener() {
                @Override
                public void onResized(Terminal terminal, TerminalSize terminalSize) {

                    textGraphics1.putString(3,1, " Dimensions " + terminalSize.getRows() + " x " + terminalSize.getColumns());

                    try {
                        terminal.flush();
                    } catch (IOException e){
                        throw new RuntimeException(e);
                    }
                }

            });

            textGraphics1.putString(0,0,"hola");
            term.setCursorPosition(0,0);
            term.flush();
            keyStroke = term.pollInput();
            while(keyStroke.getKeyType() != KeyType.Escape){
                keyStroke = term.pollInput();

            }

            //exit
            term.exitPrivateMode();
        } catch (IOException e) {

        }finally {
            try {
                term.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

}
