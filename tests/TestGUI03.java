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

public class TestGUI03 {
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


            Panel contentPanel = new Panel(new GridLayout(2)); // can hold multiple sub-components that will be added to a window
            //contentPanel.setPreferredSize(term.getTerminalSize());
            GridLayout gridLayout = (GridLayout)contentPanel.getLayoutManager();
            gridLayout.setHorizontalSpacing(50);

            Label title = new Label("This is a label that spans two columns");
            title.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING,GridLayout.Alignment.BEGINNING,true,false,2,1));
            contentPanel.addComponent(title);

            contentPanel.addComponent(new Label("Text Box (aligned)"));
            contentPanel.addComponent(
                    new TextBox()
                            .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER)));

            contentPanel.addComponent(new Label("Password Box (right aligned)"));
            contentPanel.addComponent(
                    new TextBox()
                            .setMask('*')
                            .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER)));


            contentPanel.addComponent(new Label("Button (centered)"));
            contentPanel.addComponent(new Button("Button", new Runnable() {
                @Override
                public void run() {
                    MessageDialog.showMessageDialog(textGUI, "MessageBox", "This is a message box", MessageDialogButton.OK);
                }
            }).setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));

            contentPanel.addComponent(
                    new EmptySpace()
                            .setLayoutData(
                                    GridLayout.createHorizontallyFilledLayoutData(2)));
            contentPanel.addComponent(
                    new Separator(Direction.HORIZONTAL)
                            .setLayoutData(
                                    GridLayout.createHorizontallyFilledLayoutData(2)));
            contentPanel.addComponent(
                    new Button("Close", new Runnable() {
                        @Override
                        public void run() {
                            window.close();
                        }
                    }).setLayoutData(
                            GridLayout.createHorizontallyEndAlignedLayoutData(2)));

            while(screen.pollInput() != KeyType.Escape){
                window.close()
            }
            window.setComponent(contentPanel);
            textGUI.addWindowAndWait(window);
           // textGUI.addWindowAndWait(window2);
            //exit
            term.exitPrivateMode();


        } catch (IOException e) {

        } finally {
            try {
                screen.stopScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
