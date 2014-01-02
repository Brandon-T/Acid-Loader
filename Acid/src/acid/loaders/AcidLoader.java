package acid.loaders;

import acid.Utilities;
import java.applet.Applet;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public final class AcidLoader extends JPanel {

    private Applet applet = null;
    private boolean Initialized = false;
    private HashMap<String, String> Parameters = new HashMap<>();
    private static final long serialVersionUID = -5836846270535785031L;
    private static final Pattern CodeRegex = Pattern.compile("code=(.*) ");
    private static final Pattern ArchiveRegex = Pattern.compile("archive=(.*) ");
    private static final Pattern ParameterRegex = Pattern.compile("<param name=\"([^\\s]+)\"\\s+value=\"([^>]*)\">");

    public static ImageIcon splashIcon() {
        try {
            ImageIcon Icon = new ImageIcon(new URL("http://www.runescape.com/img/game/splash.gif"));
            Icon.setImageObserver(null);
            return Icon;
        } catch (Exception Ex) {
            Ex.toString();
        }
        return null;
    }

    public AcidLoader(String World, boolean DownloadGamePack, int Width, int Height) {
        try {
            this.setLayout(new BorderLayout(0, 0));
            String PageSource = Utilities.downloadPage(new URL(World));
            Matcher CodeMatcher = CodeRegex.matcher(PageSource);
            Matcher ArchiveMatcher = ArchiveRegex.matcher(PageSource);

            if (CodeMatcher.find() && ArchiveMatcher.find()) {
                String Archive = ArchiveMatcher.group(1);
                String JarLocation = World + "/" + Archive;
                String Code = CodeMatcher.group(1).replaceAll(".class", "");
                Matcher ParameterMatcher = ParameterRegex.matcher(PageSource);
                AcidAppletStub Stub = new AcidAppletStub(new URL(World), new URL(World), this.Parameters);

                while (ParameterMatcher.find()) {
                    this.Parameters.put(ParameterMatcher.group(1), ParameterMatcher.group(2));
                }

                if (!DownloadGamePack) {
                    URLClassLoader ClassLoader = new URLClassLoader(new URL[]{new URL(JarLocation)});
                    applet = (Applet) ClassLoader.loadClass(Code).newInstance();
                } else {
                    Utilities.downloadFile(new URL(JarLocation), "./gamepack.jar");
                    URLClassLoader ClassLoader = new URLClassLoader(new URL[]{new URL("file:./gamepack.jar")});
                    applet = (Applet) ClassLoader.loadClass(Code).newInstance();
                }

                applet.setStub(Stub);
                applet.setPreferredSize(new Dimension(Width, Height));
                this.add(applet, BorderLayout.CENTER);
            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException Ex) {
            System.out.println(Ex.getMessage());
        }
    }

    public Applet getApplet() {
        return applet;
    }

    public void start() {
        if (applet != null && !Initialized) {
            applet.init();
            applet.start();
            Initialized = true;
        }
    }

    public void destruct() {
        if (applet != null && Initialized) {
            Initialized = false;
            this.remove(applet);
            //applet.stop();
            //applet.destroy();
            applet = null;
        }
    }
}
