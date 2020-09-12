package FishAndCook;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Skills;

import java.util.concurrent.Callable;

public class FlyFish extends Task {

    public FlyFish(ClientContext ctx) {
        super(ctx);
    }

    final int COOKED_TROUT = 333;
    final int BURNT_TROUT = 334;
    final int UNCOOKED_TROUT = 335;
    final int COOKED_SALMON = 329;
    final int UNCOOKED_SALMON = 331;
    final int BURNT_SALMON = 343;
    Skills skills = new Skills(ctx);

    @Override
    public boolean activate() {
        return ((skills.level(10) < 50 || skills.level(7) < 50) && ctx.players.local().animation() == -1 && ctx.inventory.select().id(COOKED_TROUT).count() == 0 && ctx.inventory.select().id(BURNT_TROUT).count() == 0 && ctx.inventory.select().id(COOKED_SALMON).count() == 0 && ctx.inventory.select().id(BURNT_SALMON).count() == 0 &&  ctx.inventory.select().id(UNCOOKED_TROUT).count() + ctx.inventory.select().id(UNCOOKED_SALMON).count() < 26);
    }

    @Override
    public void execute() {
        final Npc FISHING_SPOT = ctx.npcs.select().id(1526).nearest().poll();
        if(!FISHING_SPOT.inViewport()) {
            ctx.camera.angle(270);
        }
        ctx.camera.turnTo(FISHING_SPOT);
        FISHING_SPOT.interact("Lure");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() != -1;
            }
        },250,25);
    }
}
