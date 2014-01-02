package acid.loaders;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.URL;
import java.util.HashMap;

public class AcidAppletStub implements AppletStub {
    
    private URL DocumentBase = null, CodeBase = null;
    private HashMap<String, String> Parameters = null;
    
    public AcidAppletStub(URL DocumentBase, URL CodeBase, HashMap<String, String> Parameters) {
        this.DocumentBase = DocumentBase;
        this.CodeBase = CodeBase;
        this.Parameters = Parameters;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public URL getDocumentBase() {
        return DocumentBase;
    }

    @Override
    public URL getCodeBase() {
        return CodeBase;
    }

    @Override
    public String getParameter(String name) {
        return Parameters.get(name);
    }

    @Override
    public AppletContext getAppletContext() {
        return null;
    }

    @Override
    public void appletResize(int width, int height) {
    }
}
