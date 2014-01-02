package acid;

public class Acid {

    public static void main(String[] args) throws Exception {
        Frame F = new Frame();
        F.setVisible(true);
        addBot(F, new Bot());
        //addBot(F, new Bot());
        //addBot(F, new Bot());
    }
    
    public static void addBot(final Frame F, final Bot bot) {
        AcidClientPool.add(bot);
        new Thread(new Runnable() {
            @Override
            public void run() {
                F.addBot(bot);
            }
        }).start();
    }
}