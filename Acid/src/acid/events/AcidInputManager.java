package acid.events;

import acid.Bot;
import acid.Utilities;
import java.awt.Canvas;
import java.awt.Point;
import java.awt.event.*;

public class AcidInputManager {

    private Bot bot = null;
    private AcidKeyListener KeyInputListener = null;
    private AcidMouseListener MouseInputListener = null;
    private AcidFocusListener FocusInputListener = null;
    private boolean MouseEntered = false;
    private boolean[] MouseClicked = new boolean[3];
    private Point MousePosition = new Point(-1, -1);

    public boolean hasFocus() {
        return bot != null && bot.getCanvas() != null && bot.getCanvas().isFocusOwner();
    }

    public boolean isInputManagerReady() {
        return KeyInputListener != null && MouseInputListener != null && FocusInputListener != null && bot != null && bot.getCanvas() != null;
    }

    public boolean isKeyInputEnabled() {
        return KeyInputListener != null && KeyInputListener.isInputEnabled();
    }

    public boolean isMouseInputEnabled() {
        return MouseInputListener != null && MouseInputListener.isInputEnabled();
    }

    public boolean setKeyInputEnabled(boolean Enabled) {
        if (KeyInputListener != null) {
            KeyInputListener.setInputEnabled(Enabled);
            return true;
        }
        return false;
    }

    public boolean setMouseInputEnabled(final boolean Enabled) {
        if (MouseInputListener != null) {
            MouseInputListener.setInputEnabled(Enabled);
            if (FocusInputListener != null) {
                FocusInputListener.setInputEnabled(Enabled);
            }
            return true;
        }
        return false;
    }

    public void updateBotCanvas(Bot bot, Canvas canvas) {
        this.bot = bot;
        if (KeyInputListener == null) {
            KeyInputListener = new AcidKeyListener(bot, canvas);
        } else {
            KeyInputListener.updateBotCanvas(bot, canvas);
        }

        if (MouseInputListener == null) {
            MouseInputListener = new AcidMouseListener(bot, canvas);
        } else {
            MouseInputListener.updateBotCanvas(bot, canvas);
        }

        if (FocusInputListener == null) {
            FocusInputListener = new AcidFocusListener(bot, canvas);
        } else {
            FocusInputListener.updateBotCanvas(bot, canvas);
        }
    }

    private boolean isClickable(int Button) {
        if (Button < 1 || Button > 3) {
            return false;
        }
        return !MouseClicked[Button - 1];
    }

    public void getFocus() {
        if (bot.getCanvas() != null && !bot.getCanvas().isFocusOwner()) {
            FocusInputListener.focusGained(new FocusEvent(bot.getApplet(), FocusEvent.FOCUS_GAINED, false));
        }
    }

    public void loseFocus() {
        if (bot.getCanvas() != null && bot.getCanvas().isFocusOwner()) {
            FocusInputListener.focusGained(new FocusEvent(bot.getApplet(), FocusEvent.FOCUS_LOST, true));
        }
    }

    public Point getMousePos() {
        return MouseInputListener.getPosition();
    }

    public void holdMouse(int Button) {
        boolean Clickable = isClickable(Button);

        if (MouseEntered && Clickable) {
            int Mask = (Button == 1 ? MouseEvent.BUTTON1_DOWN_MASK : 0) | (Button == 2 ? MouseEvent.BUTTON2_DOWN_MASK : 0) | (Button == 3 ? MouseEvent.META_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK : 0);
            if (Mask != 0) {
                MouseClicked[Button - 1] = true;

                switch (Button) {
                    case 1:
                        Button = MouseEvent.BUTTON1;
                        break;
                    case 2:
                        Button = MouseEvent.BUTTON2;
                        break;
                    case 3:
                        Button = MouseEvent.BUTTON3;
                        break;
                }
                MouseInputListener.mousePressed(new MouseEvent(bot.getApplet(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), Mask, MousePosition.x, MousePosition.y, 1, false, Button));
            }
        }
    }

    public void releaseMouse(int Button) {
        boolean Clickable = isClickable(Button);

        if (MouseEntered && !Clickable) {
            int Mask = (Button == 1 ? MouseEvent.BUTTON1_DOWN_MASK : 0) | (Button == 2 ? MouseEvent.BUTTON2_DOWN_MASK : 0) | (Button == 3 ? MouseEvent.META_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK : 0);
            if (Mask != 0) {
                MouseClicked[Button - 1] = false;

                switch (Button) {
                    case 1:
                        Button = MouseEvent.BUTTON1;
                        break;
                    case 2:
                        Button = MouseEvent.BUTTON2;
                        break;
                    case 3:
                        Button = MouseEvent.BUTTON3;
                        break;
                }

                long Time = System.currentTimeMillis();
                MouseInputListener.mouseReleased(new MouseEvent(bot.getApplet(), MouseEvent.MOUSE_RELEASED, Time, Mask, MousePosition.x, MousePosition.y, 1, false, Button));
                MouseInputListener.mouseClicked(new MouseEvent(bot.getApplet(), MouseEvent.MOUSE_CLICKED, Time, Mask, MousePosition.x, MousePosition.y, 1, false, Button));
            }
        }
    }

    /*public void moveMouse(int X, int Y) {
     int Mask = (MouseClicked[0] ? MouseEvent.BUTTON1_DOWN_MASK : 0) | (MouseClicked[1] ? MouseEvent.BUTTON2_DOWN_MASK : 0) | (MouseClicked[2] ? MouseEvent.BUTTON3_DOWN_MASK : 0);
        
     if (MouseClicked[0] || MouseClicked[1] || MouseClicked[2]) {
     MouseInputListener.mouseDragged(new MouseEvent(bot.getApplet(), MouseEvent.MOUSE_DRAGGED, System.currentTimeMillis(), Mask, X, Y, 0, false, 0));
     } else if (X > 0 && X < bot.getApplet().getWidth() && Y > 0 && Y < bot.getApplet().getHeight()) {
     if (MouseEntered) {
     MouseInputListener.mouseMoved(new MouseEvent(bot.getApplet(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), Mask, X, Y, 0, false, 0));
     } else {
     MouseEntered = true;
     MouseInputListener.mouseEntered(new MouseEvent(bot.getApplet(), MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), Mask, X, Y, 0, false, 0));
     }
     } else if (MouseEntered) {
     MouseEntered = false;
     MouseInputListener.mouseExited(new MouseEvent(bot.getApplet(), MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), Mask, X, Y, 0, false, 0));
     }
        
     this.MousePosition.setLocation(X, Y);
     }*/
    public void clickMouse(int Button) {
        if (isClickable(Button)) {
            this.holdMouse(Button);
            Utilities.sleep((int) (Math.random() * 56 + 90));
            this.releaseMouse(Button);
        }
    }

    public void sendKeys(String Text) {
        char[] Data = Text.toCharArray();

        for (char C : Data) {
            long Time = System.currentTimeMillis();
            if (bot.getApplet() != null) {
                KeyInputListener.keyPressed(new KeyEvent(bot.getApplet(), KeyEvent.KEY_PRESSED, Time, 0, (int) C, C, KeyEvent.KEY_LOCATION_STANDARD));
                KeyInputListener.keyTyped(new KeyEvent(bot.getApplet(), KeyEvent.KEY_TYPED, Time, 0, 0, C, KeyEvent.KEY_LOCATION_UNKNOWN));
                Utilities.sleep((int) ((Math.random() * 0.1 + 1) * 90));
                KeyInputListener.keyReleased(new KeyEvent(bot.getApplet(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, (int) C, C, KeyEvent.KEY_LOCATION_STANDARD));
            }
        }
    }
}
