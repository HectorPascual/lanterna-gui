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
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import com.googlecode.lanterna.terminal.ansi.UnixTerminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private String name;
    private int termColumns;
    private int termRows;

    public FirstGUI(MySocket socket){
        this.socket = socket;

    }

    public void run () {

        try {

            term = new UnixTerminal();

            termColumns = term.getTerminalSize().getColumns();
            termRows = term.getTerminalSize().getRows();
            screen = new TerminalScreen(term);
            screen.startScreen();

            textGUI = new MultiWindowTextGUI(screen);
            window = new BasicWindow("SAD 2019  -   CLIENT CHAT");
            window.setHints(Arrays.asList(Window.Hint.FULL_SCREEN));

            initComponents();
            sizeComponents();
            initPanel();


            term.addResizeListener(new TerminalResizeListener() {
                @Override
                public void onResized(Terminal terminal, TerminalSize terminalSize) {
                    termColumns = terminalSize.getColumns();
                    termRows = terminalSize.getRows();
                    sizeComponents();
                }

            });

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

    private void sizeComponents(){
        users.setPosition(new TerminalPosition(termColumns*82/100, 0));
        users.setSize(new TerminalSize(8, termRows));

        separatorHor.setSize(new TerminalSize(termColumns, 1));
        separatorHor.setPosition(new TerminalPosition(0, termRows*60/100));

        separatorVert.setSize(new TerminalSize(1, termRows));
        separatorVert.setPosition(new TerminalPosition(termColumns*80/100, 0));

        textBox.setPosition(new TerminalPosition(0, termRows*64/100));
        textBox.setSize(new TerminalSize(termColumns*79/100, termRows*50/100));

        send.setPosition(new TerminalPosition(termColumns*83/100, termRows*70/100));
        send.setSize(new TerminalSize(8, 1));

    }
    private void initComponents(){

        try {

            users = new Label("");


            chatContent = new Label("");
            chatContent.setPosition(new TerminalPosition(1, 0));
            chatContent.setSize(term.getTerminalSize());


            separatorHor = new Separator(Direction.HORIZONTAL);


            separatorVert = new Separator(Direction.VERTICAL);


            textBox = new TextBox();


            send = new Button("Send", new Runnable() {
                @Override
                public void run() {
                    sendAction();
                }
            });
        } catch (IOException e){

        }
    }

    private void initPanel(){
        try {
            absolutPanel = new Panel(new AbsoluteLayout());
            absolutPanel.setPreferredSize(new TerminalSize(term.getTerminalSize().getColumns(), term.getTerminalSize().getRows()));

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
        name = TextInputDialog.showDialog(textGUI, "","Type your name (max 8 letters)","");
        socket.write(name);
    }

    public void updateUserList(List<String> usersList){
        String tmp = "";
        for(String s : usersList){
            tmp = tmp + s + '\n';
        }
        users.setText(tmp);
    }

    private void sendAction(){
        socket.write(textBox.getText());
        String input = textBox.getText();
        textBox.setText("");
        write(input);
    }

}
