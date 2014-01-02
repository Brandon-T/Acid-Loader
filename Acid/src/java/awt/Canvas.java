package java.awt;

import acid.AcidClientPool;
import acid.Bot;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.peer.CanvasPeer;
import javax.accessibility.*;

public class Canvas extends Component implements Accessible {

    private Bot bot = null;
    private boolean Focused = false;
    private static int nameCounter = 0;
    private static final String base = "canvas";
    private static final long serialVersionUID = -2284879212465893870L;

    public Canvas() {
        super();
    }

    public Canvas(GraphicsConfiguration config) {
        this();
        setGraphicsConfiguration(config);
    }

    private void updateInputManager() {
        bot = AcidClientPool.getBot(this.getClass().getClassLoader().hashCode());

        if (bot != null) {
            bot.updateInputManager(this);
        }
    }

    @Override
    public Graphics getGraphics() {
        this.updateInputManager();

        if (super.getGraphics() != null && bot != null && bot.getBotBuffer() != null && bot.getGameBuffer() != null) {
            Graphics g = bot.getBotBuffer().getGraphics();
            g.drawImage(bot.getGameBuffer(), 0, 0, null);      
            bot.drawGraphics(g);
            g.dispose();

            Graphics2D G = (Graphics2D) super.getGraphics();
            G.drawImage(bot.getBotBuffer(), 0, 0, null);

            return bot.getGameBuffer().getGraphics();
        }

        return super.getGraphics();
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, width, height);
    }

    @Override
    public void update(Graphics g) {
        g.clearRect(0, 0, width, height);
        paint(g);
    }

    public BufferedImage getGameBuffer() {
        return bot.getGameBuffer();
    }

    public BufferedImage getBotBuffer() {
        return bot.getBotBuffer();
    }

    @Override
    public final boolean hasFocus() {
        return Focused;
    }

    @Override
    public final boolean isValid() {
        return visible;
    }

    @Override
    public final boolean isVisible() {
        return visible;
    }

    @Override
    public final boolean isDisplayable() {
        return true;
    }

    @Override
    public final void setVisible(boolean visible) {
        super.setVisible(visible);
        this.visible = visible;
        
        if (bot == null) {
            bot = AcidClientPool.getBot(this.getClass().getClassLoader().hashCode());
        }
        
        if (bot != null && bot.getOperationMode() != null) {
            switch (bot.getOperationMode()) {
                case OPENGL: bot.updateInputManager(this); break;
                case DIRECTX: bot.updateInputManager(this); break;
                default: break;
            }
        }
    }

    @Override
    void setGraphicsConfiguration(GraphicsConfiguration gc) {
        synchronized (getTreeLock()) {
            CanvasPeer peer = (CanvasPeer) getPeer();
            if (peer != null) {
                gc = peer.getAppropriateGraphicsConfiguration(gc);
            }
            super.setGraphicsConfiguration(gc);
        }
    }

    @Override
    String constructComponentName() {
        synchronized (Canvas.class) {
            return base + nameCounter++;
        }
    }

    @Override
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (peer == null) {
                peer = getToolkit().createCanvas(this);
            }
            super.addNotify();
        }
    }

    @Override
    boolean postsOldMouseEvents() {
        return true;
    }

    @Override
    public void createBufferStrategy(int numBuffers) {
        super.createBufferStrategy(numBuffers);
    }

    @Override
    public void createBufferStrategy(int numBuffers,
            BufferCapabilities caps) throws AWTException {
        super.createBufferStrategy(numBuffers, caps);
    }

    @Override
    public BufferStrategy getBufferStrategy() {
        return super.getBufferStrategy();
    }

    @Override
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleAWTCanvas();
        }
        return accessibleContext;
    }

    protected class AccessibleAWTCanvas extends AccessibleAWTComponent {

        private static final long serialVersionUID = -6325592262103146699L;

        @Override
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.CANVAS;
        }
    }
}