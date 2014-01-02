package acid;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import acid.loaders.AcidLoader;

public class Frame extends JFrame {

    private boolean Loaded = false;
    private JLabel SplashScreen = null;
    private JTabbedPane TabPanel = null;
    private JToolBar ToolBar = null;
    private JButton ScreenshotButton = null;
    private JToggleButton KeyInputButton = null;
    private JToggleButton MouseInputButton = null;
    private java.awt.Image CloseTabIcon = null;
    private java.awt.Image CloseTabGrayIcon = null;
    private static final Object TabPaneLock = new Object();

    public Frame() {
        this.setResizable(false);
        this.setTitle("Acid-Bot v0.1");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        Utilities.setCustomTheme(UIManager.getSystemLookAndFeelClassName());
        this.addSplashScreen(AcidLoader.splashIcon());
        this.setFocusable(false);
        this.setToolBar();
        this.centerWindow();
        this.addListeners();
    }

    private void centerWindow() {
        Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int X = (int) ((ScreenSize.getWidth() - this.getWidth()) / 2);
        int Y = (int) ((ScreenSize.getHeight() - this.getHeight()) / 2);
        this.setLocation(X, Y);
    }

    private void setToolBar() {
        TabPanel = new JTabbedPane();
        this.add(TabPanel);
        this.add(ToolBar = new JToolBar(), BorderLayout.NORTH);
        Utilities Utility = Utilities.getInstance();
        ToolBar.add(Box.createHorizontalGlue());
        ToolBar.add(ScreenshotButton = new JButton(new ImageIcon(Utility.scaleImage(16, 16, Utility.loadResourceImage("/icons/Screenshot.png")))));
        ToolBar.add(KeyInputButton = new JToggleButton(new ImageIcon(Utility.scaleImage(16, 16, Utility.loadResourceImage("/icons/Keyboard.png")))));
        ToolBar.add(MouseInputButton = new JToggleButton(new ImageIcon(Utility.scaleImage(16, 16, Utility.loadResourceImage("/icons/Mouse.png")))));
        CloseTabIcon = Utility.scaleImage(13, 13, Utility.loadResourceImage("/icons/CloseTab.png"));
        CloseTabGrayIcon = Utility.grayScale(CloseTabIcon, 50);

        ScreenshotButton.setSize(30, 30);
        KeyInputButton.setSize(30, 30);
        MouseInputButton.setSize(30, 30);

        ScreenshotButton.setFocusable(false);
        KeyInputButton.setFocusable(false);
        MouseInputButton.setFocusable(false);
        this.setButtonToolTips(null);

        ToolBar.setFloatable(false);
        ToolBar.setFocusable(false);
        ToolBar.setVisible(false);
    }

    public final void addSplashScreen(ImageIcon Img) {
        this.SplashScreen = new JLabel(Img);
        this.getContentPane().add(SplashScreen, BorderLayout.CENTER);
        this.pack();
    }

    public final void removeSplashScreen() {
        if (this.SplashScreen != null) {
            this.getContentPane().remove(this.SplashScreen);
            this.ToolBar.setVisible(true);
            this.SplashScreen = null;
        }
    }

    private void setBotButtonStates(Bot bot) {
        if (bot != null && bot.getInputManager() != null) {
            boolean KeyInput = !bot.getInputManager().isKeyInputEnabled();
            boolean MouseInput = !bot.getInputManager().isMouseInputEnabled();
            KeyInputButton.setSelected(KeyInput);
            MouseInputButton.setSelected(MouseInput);
            KeyInputButton.getModel().setSelected(KeyInput);
            MouseInputButton.getModel().setSelected(MouseInput);
            if (bot.getCanvas() != null) {
                bot.getCanvas().requestFocus();
            }
        } else {
            KeyInputButton.setSelected(true);
            MouseInputButton.setSelected(true);
            KeyInputButton.getModel().setPressed(true);
            MouseInputButton.getModel().setPressed(true);
        }
    }

    private void setButtonToolTips(Bot bot) {
        ScreenshotButton.setToolTipText("ScreenShot");

        if (bot != null && bot.getInputManager() != null) {
            if (bot.getInputManager().isKeyInputEnabled()) {
                KeyInputButton.setToolTipText("Disable Key-Input");
            } else {
                KeyInputButton.setToolTipText("Enable Key-Input");
            }

            if (bot.getInputManager().isMouseInputEnabled()) {
                MouseInputButton.setToolTipText("Disable Mouse-Input");
            } else {
                MouseInputButton.setToolTipText("Enable Mouse-Input");
            }
        } else {
            KeyInputButton.setToolTipText("Disable Key-Input");
            MouseInputButton.setToolTipText("Disable Mouse-Input");
        }
    }

    private void addTab(final JTabbedPane TabPane, final String Title, final Component component) {
        this.addTab(TabPane, Title, component, null, null);
    }

    private void addTab(final JTabbedPane TabPane, final String Title, final Component component, final Icon TabIcon, final Icon CloseIcon) {
        TabPane.addTab(null, component);
        JButton CloseButton = new JButton();
        JLabel TitleLabel = new JLabel(Title);

        JPanel HeaderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        ((FlowLayout) HeaderPanel.getLayout()).setHgap(0);
        ((FlowLayout) HeaderPanel.getLayout()).setVgap(0);


        CloseButton.setBorder(null);
        CloseButton.setOpaque(false);
        CloseButton.setFocusable(false);
        CloseButton.setBorderPainted(false);

        TitleLabel.setBorder(null);
        TitleLabel.setIcon(TabIcon);
        TitleLabel.setFocusable(false);

        CloseButton.setIcon(CloseIcon);
        CloseButton.setRolloverIcon(CloseIcon);
        CloseButton.setRolloverEnabled(true);

        if (CloseIcon == null) {
            CloseButton.setIcon(new ImageIcon(this.CloseTabIcon));
            CloseButton.setRolloverIcon(new ImageIcon(this.CloseTabGrayIcon));
            CloseButton.setPreferredSize(new Dimension(15, 15));
        }

        HeaderPanel.add(TitleLabel);
        HeaderPanel.add(CloseButton);
        HeaderPanel.setOpaque(false);
        HeaderPanel.setFocusable(false);
        TabPane.setTabComponentAt(TabPane.indexOfComponent(component), HeaderPanel);

        CloseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame.this.removeBot((AcidLoader) component);
            }
        });
    }

    public void addBot(final Bot bot) {
        bot.load("http://world43.runescape.com", false, 765, 553);
        Frame.this.removeSplashScreen();
        Frame.this.pack();
        synchronized (Frame.this.TabPanel) {
            Frame.this.addTab(TabPanel, "Client[" + (Frame.this.TabPanel.getTabCount() + 1) + "]", bot.getLoader());
            Frame.this.TabPanel.setSelectedComponent(bot.getLoader());
            Frame.this.setButtonToolTips(bot);
            Frame.this.TabPanel.updateUI();
        }

        Frame.this.pack();
        Frame.this.setSize(getWidth(), getHeight() + 1);

        if (!Loaded) {
            Frame.this.centerWindow();
            Loaded = true;
        }

        bot.getLoader().start();
        while (bot.getCanvas() == null) {
            Utilities.sleep(10);
        }

        while (true) {
            while (bot != null && !bot.isInputReady()) {
                Utilities.sleep(100);
            }

            if (bot != null && bot.getInputManager() != null && bot.getApplet() != null) {
                Utilities.sleep(3000);
                bot.getInputManager().getFocus();
                bot.getInputManager().sendKeys("S");
                bot.getInputManager().setKeyInputEnabled(false);
                bot.getInputManager().setMouseInputEnabled(false);
                KeyInputButton.getModel().setPressed(true);
                MouseInputButton.getModel().setPressed(true);
                KeyInputButton.setSelected(true);
                MouseInputButton.setSelected(true);
                break;
            }
            Utilities.sleep(100);
        }
    }

    private void removeBot(final AcidLoader loader) {
        if (loader == null || loader.getApplet() == null || loader.getApplet().getComponentCount() < 1) {
            return;
        }

        synchronized (Frame.this.TabPanel) {
            Frame.this.TabPanel.remove(loader);
        }

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AcidClientPool.remove(loader.getApplet().getComponent(0).getClass().getClassLoader().hashCode());
                    loader.destruct();
                }
            }).start();
        } catch (Exception Ex) {
            System.out.println(Ex.getMessage());
        }
    }

    private void removeAllBots() {
        synchronized (Frame.this.TabPanel) {
            Frame.this.TabPanel.removeAll();
        }

        List<Bot> Clients = AcidClientPool.getBots();
        if (Clients != null) {
            synchronized (Clients) {
                for (Bot bot : Clients) {
                    if (bot != null && bot.getLoader() != null) {
                        bot.getLoader().destruct();
                    }
                }
                AcidClientPool.removeAll();
            }
            System.gc();
        }
    }

    private Bot getBot(int TabIndex) {
        if (TabPanel != null && TabIndex < TabPanel.getTabCount()) {
            if (TabIndex != -1) {
                Applet applet = (Applet) ((AcidLoader) TabPanel.getSelectedComponent()).getApplet();
                if (applet != null) {
                    if (applet.getComponentCount() > 0) {
                        TabIndex = applet.getComponent(0).getClass().getClassLoader().hashCode();
                        return AcidClientPool.getBot(TabIndex);
                    }
                }
            }
        }
        return null;
    }

    private void addListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Frame.this.setVisible(false);
                Frame.this.removeAllBots();
                AcidClientPool.removeAll();
                System.gc();
                Frame.this.dispose();
                System.exit(0);
            }
        });

        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                Frame.this.requestFocusInWindow();
            }
        });

        TabPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                synchronized (Frame.this.TabPanel) {
                    int Index = TabPanel.getSelectedIndex();
                    if (Index != -1) {
                        Bot bot = Frame.this.getBot(Index);
                        Frame.this.setButtonToolTips(bot);
                        Frame.this.setBotButtonStates(bot);
                    }
                }
            }
        });

        ScreenshotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int Index = TabPanel.getSelectedIndex();
                if (Index != -1) {
                    Bot bot = Frame.this.getBot(Index);
                    if (bot != null) {
                        Utilities.screenShot(bot.getGameBuffer());
                    }
                }
            }
        });

        KeyInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (Frame.this.KeyInputButton) {
                    int Index = TabPanel.getSelectedIndex();
                    if (Index != -1) {
                        Bot bot = Frame.this.getBot(Index);
                        if (bot != null && bot.getInputManager() != null) {
                            bot.getInputManager().setKeyInputEnabled(!((JToggleButton) e.getSource()).isSelected());
                            Frame.this.setButtonToolTips(bot);
                        }
                    }
                }
            }
        });

        MouseInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (Frame.this.MouseInputButton) {
                    int Index = TabPanel.getSelectedIndex();
                    if (Index != -1) {
                        Bot bot = Frame.this.getBot(Index);
                        if (bot != null && bot.getInputManager() != null) {
                            bot.getInputManager().setMouseInputEnabled(!((JToggleButton) e.getSource()).isSelected());
                            Frame.this.setButtonToolTips(bot);
                        }
                    }
                }
            }
        });
    }
}
