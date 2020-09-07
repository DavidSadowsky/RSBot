package FishAndCook;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

import java.util.Date;
import java.util.concurrent.Callable;

public class CookShrimp extends Task {
    final int UNCOOKED_SHRIMP_ID = 317;
    final int RANGE_ID = 114;

    public CookShrimp(ClientContext ctx) {
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
        return (ctx.objects.select().id(RANGE_ID).nearest().poll().tile().distanceTo(ctx.players.local()) < 5 && (ctx.inventory.select().id(UNCOOKED_SHRIMP_ID).count() == 28 || (ctx.inventory.select().id(UNCOOKED_SHRIMP_ID).count() > 0 && (ctx.chat.canContinue() || shouldActivate))));
    }

    @Override
    public void execute() {
        final GameObject RANGE = ctx.objects.select().id(RANGE_ID).nearest().poll();
        final Item SHRIMP = ctx.inventory.select().id(UNCOOKED_SHRIMP_ID).poll();
        if(!RANGE.inViewport()) {
            ctx.camera.angle(270);
        }
        ctx.camera.turnTo(RANGE);
        SHRIMP.click();
        RANGE.click();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.widgets.widget(270).component(14).valid();
            }
        }, 250, 26);
        Component cook = ctx.widgets.widget(270).component(14);
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
