package acid.events;

import acid.Bot;
import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

public final class AcidKeyListener implements KeyListener {

    private Bot bot = null;
    private Canvas canvas = null;
    private boolean InputEnabled = true;
    private ArrayList<KeyListener> Listeners = null;

    public AcidKeyListener(Bot bot, Canvas canvas) {
        this.construct(bot, canvas);
    }
    
    private void construct(Bot bot, Canvas canvas) {
        this.bot = bot;
        this.canvas = canvas;
        Listeners = new ArrayList<>();
        Listeners.addAll(Arrays.asList(canvas.getKeyListeners()));
        this.removeAllListeners();
    }

    private void removeAllListeners() {
        for (KeyListener Listener : canvas.getKeyListeners()) {
            canvas.removeKeyListener(Listener);
        }
        
        canvas.addKeyListener(this);
    }
    
    private void restoreAllListeners() {
        canvas.removeKeyListener(this);
        
        for (KeyListener Litener : Listeners) {
            canvas.addKeyListener(Litener);
        }
    }

    public void updateBotCanvas(Bot bot, Canvas canvas) {
        this.restoreAllListeners();
        this.construct(bot, canvas);
    }
    
    public boolean isInputEnabled() {
        return InputEnabled;
    }
    
    public void setInputEnabled(boolean Enabled) {
        this.InputEnabled = Enabled;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == bot.getApplet()) {
            e.setSource(canvas);

            for (KeyListener Listener : Listeners) {
                Listener.keyTyped(e);
            }
        } else if (InputEnabled) {
            for (KeyListener Listener : Listeners) {
                Listener.keyTyped(e);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == bot.getApplet()) {
            e.setSource(canvas);

            for (KeyListener Listener : Listeners) {
                Listener.keyPressed(e);
            }
        } else if (InputEnabled) {
            for (KeyListener Listener : Listeners) {
                Listener.keyPressed(e);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == bot.getApplet()) {
            e.setSource(canvas);

            for (KeyListener Listener : Listeners) {
                Listener.keyReleased(e);
            }
        } else if (InputEnabled) {
            for (KeyListener Listener : Listeners) {
                Listener.keyReleased(e);
            }
        }
    }
}
