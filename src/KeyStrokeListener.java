import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.util.concurrent.atomic.AtomicBoolean;

public class KeyStrokeListener implements WindowListener {

    private FirstGUI gui;

    public KeyStrokeListener(FirstGUI gui) {
        this.gui = gui;
    }

    public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
        if (keyStroke.getKeyType() == KeyType.Escape) {
            gui.close();
        }
    }

    public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
        // TODO Auto-generated method stub
    }

    public void onResized(Window window, TerminalSize oldSize, TerminalSize newSize) {
        // TODO Auto-generated method stub
    }

    public void onMoved(Window window, TerminalPosition oldPosition, TerminalPosition newPosition) {
        // TODO Auto-generated method stub
    }
}