import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialog;
import com.googlecode.lanterna.input.*;
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
    private KeyStroke keyStroke;
    private Button exit;

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

            KeyStrokeListener listener = new KeyStrokeListener(this);
            window.addWindowListener(listener);
            window.setComponent(absolutPanel);
            textGUI.addWindowAndWait(window);

        } catch (IOException e) {

        } finally {
            try {
                screen.stopScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkKeys(){

        Thread readKeys = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {

                        keyStroke = screen.pollInput();
                        if (keyStroke != null && keyStroke.getKeyType() == KeyType.Escape) {
                            window.close();
                            System.exit(0); //exits the process
                        }
                    }
                }catch(IOException e){

                }
            }
        });
        readKeys.start();

    }

    public void write(String txt){
        String tmp = chatContent.getText() + '\n' + txt;
        chatContent.setText(tmp);
    }

    public void close(){
        window.close();
        System.exit(0); //exits the process
    }

    public void updateUserList(List<String> usersList){
        String tmp = "";
        for(String s : usersList){
            tmp = tmp + s + '\n';
        }
        users.setText(tmp);
    }

    private void requestName() {
        name = TextInputDialog.showDialog(textGUI, "","Type your name (max 8 letters)","");
        socket.write(name);
    }

    private void sizeComponents() {
        users.setPosition(new TerminalPosition(termColumns*82/100, 0));
        users.setSize(new TerminalSize(8, termRows));

        separatorHor.setSize(new TerminalSize(termColumns, 1));
        separatorHor.setPosition(new TerminalPosition(0, termRows*76/100));

        chatContent.setPosition(new TerminalPosition(1, 0));
        chatContent.setSize(new TerminalSize(termColumns,termRows));

        separatorVert.setSize(new TerminalSize(1, termRows));
        separatorVert.setPosition(new TerminalPosition(termColumns*80/100, 0));

        textBox.setPosition(new TerminalPosition(0, termRows*80/100));
        textBox.setSize(new TerminalSize(termColumns*79/100, termRows*50/100));

        send.setPosition(new TerminalPosition(termColumns*85/100, termRows*80/100));
        send.setSize(new TerminalSize(8, 1));

        exit.setPosition(new TerminalPosition(termColumns*85/100, termRows*85/100));
        exit.setSize(new TerminalSize(8, 1));

    }
    private void initComponents(){
        users = new Label("");

        chatContent = new Label("");

        separatorHor = new Separator(Direction.HORIZONTAL);

        separatorVert = new Separator(Direction.VERTICAL);

        textBox = new TextBox();

        send = new Button("Send", new Runnable() {
            @Override
            public void run() {
                if(countLines() == termRows*76/100) chatContent.setText("");
                sendAction();
            }
        });

        exit = new Button("Exit", new Runnable() {
            @Override
            public void run() {
                window.close();
                System.exit(0);
            }
        });
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
            absolutPanel.addComponent(exit);
        } catch (IOException e){

        }
    }

    //Utility to clear screen
    private int countLines(){
        String txt = chatContent.getText();
        String[] array = txt.split("\n");
        return array.length+1;
    }


    private void sendAction(){
        socket.write(textBox.getText());
        String input = textBox.getText();
        textBox.setText("");
        write(input);
    }
}
