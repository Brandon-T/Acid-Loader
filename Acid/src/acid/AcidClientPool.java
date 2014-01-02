package acid;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AcidClientPool {
    //Collections.unmodifiableList
    private static AcidClientPool instance = new AcidClientPool();
    private static final LinkedList<Bot> Clients = new LinkedList<>();
    
    private AcidClientPool() {     
    }
    
    public static AcidClientPool getInstance() {
        return instance;
    }
    
    public static Bot add(final Bot bot) {
        synchronized(Clients) {
            Clients.add(bot);
        }
        return bot;
    }
    
    public static Bot remove(Bot bot) {
        if (bot != null) {
            synchronized(Clients) {
                Clients.remove(bot);
            }
        }
        return bot;
    }
    
    public static Bot remove(int hashCode) {
        synchronized(Clients) {
            for (Iterator<Bot> it = Clients.iterator(); it.hasNext();) {
                Bot bot = it.next();
                if (bot.getCanvas().getClass().getClassLoader().hashCode() == hashCode) {
                    it.remove();
                    return bot;
                }
            }
        }
        return null;
    }
    
    public static void removeAll() {
        synchronized(Clients) {
            Clients.clear();
        }
    }
    
    public static List<Bot> getBots() {
        return Clients;
    }
    
    public static List<Bot> getAllBots() {
        return Collections.unmodifiableList(Clients);
    }
    
    public static Bot getBot(int hashCode) {
        synchronized(Clients) {
            for (Bot bot : Clients) {
                if (bot.getCanvas() != null && (bot.getCanvas().getClass().getClassLoader().hashCode() == hashCode)) {
                    return bot;
                }
            }
        }
        return null;
    }
}
