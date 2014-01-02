package acid.loaders;

import java.io.File;
import java.util.ArrayList;

public class AcidPluginLoader {
    private static ArrayList<String> Plugins = new ArrayList<>();
    private static AcidPluginLoader instance = new AcidPluginLoader();
    
    private AcidPluginLoader() {
    }
    
    public static AcidPluginLoader getInstance() {
        return instance;
    }
    
    public static void load(String Path) {
        File plugin = new File(Path.trim());
        String pluginName = plugin.getName().toLowerCase();
        
        if (plugin.isFile() && !Plugins.contains(pluginName)) {
            System.load(plugin.getAbsolutePath());
            Plugins.add(pluginName);
            System.out.println("Loaded: Acid_" + plugin.getName());
            
            if (pluginName.contains("opengl32.dll")) {
                AcidGLDXLoader.OpenGLLoaded = true;
            } else if (pluginName.contains("d3d9d.dll") || pluginName.contains("d3d11d.dll")) {
                AcidGLDXLoader.DirectXLoaded = true;
            }
        }
    }
    
    public static void loadAll(String[] Paths) {
        for (String Path : Paths) {
            load(Path);
        }
    }
}
