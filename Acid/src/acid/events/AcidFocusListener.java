package acid.events;

import acid.Bot;
import java.awt.Canvas;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Arrays;

public class AcidFocusListener implements FocusListener {
    
    private Bot bot = null;
    private Canvas canvas = null;
    private boolean InputEnabled = true;
    private ArrayList<FocusListener> Listeners = null;
    
    public AcidFocusListener(Bot bot, Canvas canvas) {
        this.construct(bot, canvas);
    }
    
    private void construct(Bot bot, Canvas canvas) {
        this.bot = bot;
        this.canvas = canvas;
        Listeners = new ArrayList<>();
        Listeners.addAll(Arrays.asList(canvas.getFocusListeners()));
        this.removeAllListeners();
    }

    private void removeAllListeners() {
        for (FocusListener Listener : canvas.getFocusListeners()) {
            canvas.removeFocusListener(Listener);
        }
        
        canvas.addFocusListener(this);
    }
    
    private void restoreAllListeners() {
        canvas.removeFocusListener(this);
        
        for (FocusListener Listener : Listeners) {
            canvas.addFocusListener(Listener);
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
    public void focusGained(FocusEvent e) {
        if (e.getSource() == bot.getApplet()) {
            e.setSource(canvas);

            for (FocusListener Listener : Listeners) {
                Listener.focusGained(e);
            }
        } else if (InputEnabled) {
            for (FocusListener Listener : Listeners) {
                Listener.focusGained(e);
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == bot.getApplet()) {
            e.setSource(canvas);

            for (FocusListener Listener : Listeners) {
                Listener.focusLost(e);
            }
        } else if (InputEnabled) {
            for (FocusListener Listener : Listeners) {
                Listener.focusLost(e);
            }
        }
    }
}
