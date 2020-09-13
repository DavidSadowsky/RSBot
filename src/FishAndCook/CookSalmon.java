package FishAndCook;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

import java.util.Date;
import java.util.concurrent.Callable;

public class CookSalmon extends Task {
    final int UNCOOKED_TROUT = 335;
    final int UNCOOKED_SALMON = 331;
    final int FIRE_ID = 26185;

    public CookSalmon(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        long startTime = System.currentTimeMillis();
        boolean shouldActivate = true;
        while(new Date().getTime() - startTime < 3000) {
            if(ctx.players.local().animation() != -1) {
                shouldActivate = false;
            }
        }
        return (ctx.inventory.select().count() == 28 && (ctx.inventory.select().id(UNCOOKED_TROUT).count() == 0 && ctx.inventory.select().id(UNCOOKED_SALMON).count() > 0 && (ctx.chat.canContinue() || shouldActivate)));
    }

    @Override
    public void execute() {
        final GameObject FIRE = ctx.objects.select().id(FIRE_ID).nearest().poll();
        final Item SALMON_TO_COOK = ctx.inventory.select().id(UNCOOKED_SALMON).poll();
        if(!FIRE.inViewport()) {
            ctx.camera.angle(ctx.camera.x() + 10);
        }
        ctx.camera.turnTo(FIRE);
        if(FIRE.tile().distanceTo(ctx.players.local().tile()) > 3) {
            ctx.camera.pitch(100);
            ctx.movement.step(new Tile(3105, 3430, 0));
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.players.local().inMotion() && ctx.movement.destination().equals(Tile.NIL);
                }
            },250,25);
        }
        SALMON_TO_COOK.interact("Use");
        FIRE.interact("Use");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.widgets.widget(270).component(14).valid();
            }
        }, 250, 26);
        final Component cook = ctx.widgets.widget(270).component(14);
        cook.click();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() == -1;
            }
        }, 250,26);
        Condition.sleep(5000);
    }
}
