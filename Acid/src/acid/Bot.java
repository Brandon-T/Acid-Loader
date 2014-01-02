package acid;

import acid.events.AcidInputManager;
import acid.loaders.AcidLoader;
import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

public class Bot {

    private AcidLoader Loader = null;
    private OperationMode Mode = null;
    private BufferedImage GameBuffer = null, BotBuffer = null;
    private IntBuffer GameNativeBuffer = null, BotNativeBuffer = null;
    
    private AcidInputManager InputManager = null;

    public void load(String World, boolean DownloadGamePack, final int Width, final int Height) {
        try {
            GameBuffer = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_RGB);
            BotBuffer = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_RGB);
            
            Loader = new AcidLoader(World, DownloadGamePack, Width, Height);
        } catch (Exception Ex) {
            System.out.println(Ex.getMessage());
        }
    }
    
    public OperationMode getOperationMode() {
        return Mode;
    }
    
    public void setOperationMode(OperationMode Mode) {
        this.Mode = Mode;
    }
    
    public AcidInputManager getInputManager() {
        return InputManager;
    }
    
    public boolean isInputReady() {
        return InputManager != null && InputManager.isInputManagerReady();
    }
    
    public void updateInputManager(Canvas canvas) {
        if (InputManager == null) {
            InputManager = new AcidInputManager();
        }
        
        if (canvas != null) {
            InputManager.updateBotCanvas(this, canvas);
        }
    }
    
    public AcidLoader getLoader() {
        return Loader;
    }

    public Applet getApplet() {
        return Loader != null ? Loader.getApplet() : null;
    }

    public Canvas getCanvas() {
        if (Loader == null || getApplet() == null || getApplet().getComponentCount() == 0 || !(getApplet().getComponent(0) instanceof Canvas)) {
            return null;
        }

        return (Canvas) getApplet().getComponent(0);
    }
    
    public BufferedImage getGameBuffer() {
        return this.GameBuffer;
    }
    
    public BufferedImage getBotBuffer() {
        return this.BotBuffer;
    }
    
    public void drawGraphics(Graphics g) {
        if (InputManager != null && InputManager.isInputManagerReady()) {
            g.setColor(Color.green);
            Point Position = InputManager.getMousePos();
            g.fillOval(Position.x - 2, Position.y - 2, 4, 4);
        }
    }
}
