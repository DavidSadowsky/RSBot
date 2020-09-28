package AutoChopper;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.Random;
import java.util.concurrent.Callable;

public class ChopWillow extends Task {

    Area area = new Area(new Tile(3090,3238,0), new Tile(3090,3230,0), new Tile(3086,3230,0), new Tile(3086,3238,0));

    final static int[] TREE_IDS = { 10819, 10829, 10831 };

    GameObject CURR_TREE;


    public ChopWillow(ClientContext ctx) {
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
        System.out.println("Trying to cut willow tree...");
            if(!ctx.objects.select().id(TREE_IDS).nearest().within(area).poll().inViewport()) {
                Random rand = new Random();
                ctx.camera.turnTo(ctx.objects.select().id(TREE_IDS).nearest().within(area).poll());
            }
            TREE_TO_CHOP = ctx.objects.select().id(TREE_IDS).nearest().within(area).poll();
            ctx.camera.turnTo(TREE_TO_CHOP);
            System.out.println("Cutting willow tree...");

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
