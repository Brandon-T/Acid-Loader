package acid.events;

import acid.Bot;
import java.awt.Canvas;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;

public class AcidMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {

    private Bot bot = null;
    private Canvas canvas = null;
    private Point Position = null;
    private boolean InputEnabled = true;
    private ArrayList<MouseListener> MouseListeners = null;
    private ArrayList<MouseMotionListener> MouseMotionListeners = null;
    private ArrayList<MouseWheelListener> MouseWheelListeners = null;

    public AcidMouseListener(Bot bot, Canvas canvas) {
        this.construct(bot, canvas);
    }
    
    private void construct(Bot bot, Canvas canvas) {
        this.bot = bot;
        this.canvas = canvas;
        this.MouseListeners = new ArrayList<>();
        this.MouseMotionListeners = new ArrayList<>();
        this.MouseWheelListeners = new ArrayList<>();
        this.MouseListeners.addAll(Arrays.asList(canvas.getMouseListeners()));
        this.MouseMotionListeners.addAll(Arrays.asList(canvas.getMouseMotionListeners()));
        this.MouseWheelListeners.addAll(Arrays.asList(canvas.getMouseWheelListeners()));
        this.removeAllListeners();
    }
    
    private void removeAllListeners() {
        for (MouseListener Listener : canvas.getMouseListeners()) {
            canvas.removeMouseListener(Listener);
        }
        
        for (MouseMotionListener Listener : canvas.getMouseMotionListeners()) {
            canvas.removeMouseMotionListener(Listener);
        }
        
        for (MouseWheelListener Listener : canvas.getMouseWheelListeners()) {
            canvas.removeMouseWheelListener(Listener);
        }
        
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addMouseWheelListener(this);
    }
    
    private void restoreAllListeners() {
        canvas.removeMouseListener(this);
        canvas.removeMouseMotionListener(this);
        canvas.removeMouseWheelListener(this);
        
        for (MouseListener Listener : MouseListeners) {
            canvas.addMouseListener(Listener);
        }
        
        for (MouseMotionListener Listener : MouseMotionListeners) {
            canvas.addMouseMotionListener(Listener);
        }
        
        for (MouseWheelListener Listener : MouseWheelListeners) {
            canvas.addMouseWheelListener(Listener);
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
    
    public Point getPosition() {
        return Position == null ? new Point(-1, -1) : Position;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == bot.getApplet()) {
            e.setSource(canvas);

            for (MouseListener Listener : MouseListeners) {
                Listener.mouseClicked(e);
            }
        } else if (InputEnabled) {
            for (MouseListener Listener : MouseListeners) {
                Listener.mouseClicked(e);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == bot.getApplet()) {
            e.setSource(canvas);

            for (MouseListener Listener : MouseListeners) {
                Listener.mousePressed(e);
            }
        } else if (InputEnabled) {
            for (MouseListener Listener : MouseListeners) {
                Listener.mousePressed(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == bot.getApplet()) {
            e.setSource(canvas);

            for (MouseListener Listener : MouseListeners) {
                Listener.mouseReleased(e);
            }
        } else if (InputEnabled) {
            for (MouseListener Listener : MouseListeners) {
                Listener.mouseReleased(e);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == bot.getApplet()) {
            e.setSource(canvas);

            for (MouseListener Listener : MouseListeners) {
                Listener.mouseEntered(e);
            }
        } else if (InputEnabled) {
            for (MouseListener Listener : MouseListeners) {
                Listener.mouseEntered(e);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == bot.getApplet()) {
            e.setSource(canvas);

            for (MouseListener Listener : MouseListeners) {
                Listener.mouseExited(e);
            }
        } else if (InputEnabled) {
            for (MouseListener Listener : MouseListeners) {
                Listener.mouseExited(e);
            }
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getSource() == bot.getApplet()) {
            e.setSource(canvas);

            for (MouseMotionListener Listener : MouseMotionListeners) {
                Listener.mouseDragged(e);
            }
        } else if (InputEnabled) {
            for (MouseMotionListener Listener : MouseMotionListeners) {
                Listener.mouseDragged(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getSource() == bot.getApplet() || InputEnabled) {
            e.setSource(canvas);

            for (MouseMotionListener Listener : MouseMotionListeners) {
                Listener.mouseMoved(e);
            }
            Position = new Point(e.getX(), e.getY());
        } else if (InputEnabled) {
            for (MouseMotionListener Listener : MouseMotionListeners) {
                Listener.mouseMoved(e);
            }
            Position = new Point(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getSource() == bot.getApplet()) {
            e.setSource(canvas);

            for (MouseWheelListener Listener : MouseWheelListeners) {
                Listener.mouseWheelMoved(e);
            }
        } else if (InputEnabled) {
            for (MouseWheelListener Listener : MouseWheelListeners) {
                Listener.mouseWheelMoved(e);
            }
        }
    }
}