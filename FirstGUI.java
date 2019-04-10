import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialog;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.ansi.UnixTerminal;

import java.io.IOException;
import java.util.Random;

public class FirstGUI {

    private MySocket socket;
    private Label users;
    private Terminal term;
    private Screen screen;
    private Panel absolutPanel;
    private Separator separatorHor;
    private Separator separatorVert;
    private TextBox textBox;
    private Button send;
    private Label chatContent;
    private WindowBasedTextGUI textGUI;
    private Window window;
    private boolean isName;

    public FirstGUI(MySocket socket){
        this.socket = socket;
        isName = true;
    }

    public void run () {

        try {

            term = new UnixTerminal();
            screen = new TerminalScreen(term);
            screen.startScreen();

            textGUI = new MultiWindowTextGUI(screen);
            window = new BasicWindow("SAD 2019  -   CLIENT CHAT");

            initComponents();
            initPanel();

            requestName();

            window.setComponent(absolutPanel);
            textGUI.addWindowAndWait(window);

        } catch (IOException e) {

        } finally {
            try {
                term.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String txt){
        String tmp = chatContent.getText() + '\n' + txt;
        chatContent.setText(tmp);
    }

    private void initComponents(){

        try {

            users = new Label("Hector\nJordi\nCobo");
            users.setPosition(new TerminalPosition(term.getTerminalSize().getColumns() - 20, 0));
            users.setSize(new TerminalSize(8, 3));

            chatContent = new Label("");
            chatContent.setPosition(new TerminalPosition(1, 0));
            chatContent.setSize(term.getTerminalSize());


            separatorHor = new Separator(Direction.HORIZONTAL);
            separatorHor.setSize(new TerminalSize(term.getTerminalSize().getColumns(), 1));
            separatorHor.setPosition(new TerminalPosition(0, 16));

            separatorVert = new Separator(Direction.VERTICAL);
            separatorVert.setSize(new TerminalSize(1, term.getTerminalSize().getRows()));
            separatorVert.setPosition(new TerminalPosition(term.getTerminalSize().getColumns() - 22, 0));

            textBox = new TextBox();
            textBox.setPosition(new TerminalPosition(0, 17));
            textBox.setSize(new TerminalSize(term.getTerminalSize().getColumns() - 23, term.getTerminalSize().getRows() - 16));


            send = new Button("Send", new Runnable() {
                @Override
                public void run() {
                    sendAction();
                }
            });

            send.setPosition(new TerminalPosition(term.getTerminalSize().getColumns() - 20, term.getTerminalSize().getRows() - 13));
            send.setSize(new TerminalSize(8, 1));
        } catch (IOException e){

        }
    }

    private void initPanel(){
        try {
            absolutPanel = new Panel(new AbsoluteLayout());
            absolutPanel.setPreferredSize(new TerminalSize(term.getTerminalSize().getColumns() - 10, term.getTerminalSize().getRows() - 10));

            absolutPanel.addComponent(users);
            absolutPanel.addComponent(chatContent);
            absolutPanel.addComponent(separatorHor);
            absolutPanel.addComponent(separatorVert);
            absolutPanel.addComponent(textBox);
            absolutPanel.addComponent(send);
        } catch (IOException e){

        }
    }


    private void requestName() {
        String name = TextInputDialog.showDialog(textGUI, "","Type your name","");
        socket.write(name);
    }

    private void sendAction(){
        socket.write(textBox.getText());
        String input = textBox.getText();
        textBox.setText("");
        write(input);
    }

}
