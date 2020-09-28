package AutoChopper;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.Random;
import java.util.concurrent.Callable;

public class ChopOak extends Task {

    Area area = new Area(new Tile(3102,3241,0), new Tile(3105,3241,0), new Tile(3105,3246,0), new Tile(3102, 3246,0));

    final static int OAK_ID = 10820;

    GameObject CURR_TREE;


    public ChopOak(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (ctx.players.local().animation() == -1 && ctx.inventory.select().count() < 28) || ((!CURR_TREE.valid() || ctx.chat.canContinue()) && ctx.inventory.select().count() < 28);
    }

    @Override
    public void execute() {
        if(!area.contains(ctx.players.local())) {
            ctx.movement.step(area.getRandomTile());
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.players.local().inMotion();
                }
            },200,20);
        }
        GameObject TREE_TO_CHOP;
        System.out.println("Trying to cut logs...");
            if(!ctx.objects.select().id(OAK_ID).nearest().within(area).poll().inViewport()) {
                Random rand = new Random();
                ctx.camera.turnTo(ctx.objects.select().id(OAK_ID).nearest().within(area).poll());
            }
            int count = 0;
            long CURRENT_TIME = System.currentTimeMillis();
            while(!ctx.objects.select().id(OAK_ID).nearest().within(area).poll().valid()) {
                if(count == 0) {
                    System.out.println("Waiting for tree to respawn...");
                    count++;
                }
                if(System.currentTimeMillis() - CURRENT_TIME > 30000) {
                    return;
                }
            }
            TREE_TO_CHOP = ctx.objects.select().id(OAK_ID).nearest().within(area).poll();
            System.out.println("Cutting tree...");

        CURR_TREE = TREE_TO_CHOP;
        TREE_TO_CHOP.interact("Chop down");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() != -1;
            }
        }, 200, 20);
    }
}
