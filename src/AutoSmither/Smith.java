package AutoSmither;

import AutoSmelter.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;

import java.util.Date;
import java.util.concurrent.Callable;

public class Smith extends Task {

    final static int IRON_BAR_ID = 2351;
    final static int ANVIL_ID = 2097;


    public Smith(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        long startTime = System.currentTimeMillis();
        boolean shouldActivate = true;

        // Checks if there is no animation for 3 seconds
        while(new Date().getTime() - startTime < 3000) {
            if(ctx.players.local().animation() != -1) {
                shouldActivate = false;
            }
        }
        return (ctx.objects.select().id(ANVIL_ID).nearest().poll().tile().distanceTo(ctx.players.local()) < 3 && (ctx.inventory.select().id(IRON_BAR_ID).count() == 25 || (ctx.inventory.select().id(IRON_BAR_ID).count() < 25 && (ctx.chat.canContinue() || shouldActivate))));
    }

    @Override
    public void execute() {
        GameObject anvil = ctx.objects.select().id(ANVIL_ID).nearest().poll();
        ctx.camera.turnTo(anvil);
        if(anvil.inViewport()) {
            anvil.interact("Smith");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(312).component(22).valid();
                }
            }, 250, 26);
            // Change this to correct widget
            Component smith = ctx.widgets.widget(312).component(22);
            smith.click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().animation() == -1;
                }
            }, 250,26);
            Condition.sleep(5000);
        }
        else {
            ctx.camera.turnTo(anvil);
        }
    }
}
