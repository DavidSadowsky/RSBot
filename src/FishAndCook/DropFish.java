package FishAndCook;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class DropFish extends Task {
    final int COOKED_TROUT = 333;
    final int UNCOOKED_TROUT = 335;
    final int BURNT_TROUT = 343;
    final int COOKED_SALMON = 329;
    final int UNCOOKED_SALMON = 331;
    final int BURNT_SALMON = 343;

    final int[] FISH_TO_DROP_IDS = { COOKED_TROUT, BURNT_TROUT, COOKED_SALMON, BURNT_SALMON };

    public DropFish(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.select().count() > 2 && (ctx.inventory.select().id(UNCOOKED_TROUT).count() == 0 && ctx.inventory.select().id(UNCOOKED_SALMON).count() == 0));
    }

    @Override
    public void execute() {
        while(ctx.inventory.select().count() > 2) {
            final int INVENT_COUNT = ctx.inventory.select().count();
            Item FISH_TO_DROP = ctx.inventory.select().id(FISH_TO_DROP_IDS).poll();
            ctx.inventory.drop(FISH_TO_DROP, true);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count() != INVENT_COUNT;
                }
            },250,25);
        }
    }
}
