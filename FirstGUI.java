import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.ansi.UnixTerminal;

import java.io.IOException;
import java.util.Random;

public class FirstGUI {
    public static void main (String[] args) {

        Terminal term = null;
        final TextGraphics textGraphics1;
        KeyStroke keyStroke;
        Screen screen;
        try {

            term = new UnixTerminal();
            screen = new TerminalScreen(term);
            screen.startScreen();
            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            final Window window = new BasicWindow("SAD 2019  -   CLIENT CHAT");

            Panel absolutPanel = new Panel(new AbsoluteLayout());
            absolutPanel.setPreferredSize(new TerminalSize(term.getTerminalSize().getColumns()-10,term.getTerminalSize().getRows()-10));

            Label users = new Label("Hector\nJordi\nCobo");
            users.setPosition(new TerminalPosition(term.getTerminalSize().getColumns()-20,0));
            users.setSize(new TerminalSize(8,3));
            absolutPanel.addComponent(users);

            Separator separatorHor = new Separator(Direction.HORIZONTAL);
            separatorHor.setSize(new TerminalSize(term.getTerminalSize().getColumns(),1));
            separatorHor.setPosition(new TerminalPosition(0,16));
            absolutPanel.addComponent(separatorHor);

            Separator separatorVert = new Separator(Direction.VERTICAL);
            separatorVert.setSize(new TerminalSize(1,term.getTerminalSize().getRows()));
            separatorVert.setPosition(new TerminalPosition(term.getTerminalSize().getColumns()-22,0));
            absolutPanel.addComponent(separatorVert);

            Button send = new Button("Send", new Runnable() {
                @Override
                public void run() {
                     // Here might be the socket write calling
                }
            });
            send.setPosition(new TerminalPosition(term.getTerminalSize().getColumns()-20,term.getTerminalSize().getRows()-13));
            send.setSize(new TerminalSize(8,1));
            absolutPanel.addComponent(send);

            TextBox textBox = new TextBox();
            textBox.setPosition(new TerminalPosition(0,17));
            textBox.setSize(new TerminalSize(term.getTerminalSize().getColumns()- 23,term.getTerminalSize().getRows()-16));
            absolutPanel.addComponent(textBox);


            window.setComponent(absolutPanel);
            textGUI.addWindowAndWait(window);

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
