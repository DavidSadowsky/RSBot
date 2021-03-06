package AutoSmelter;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;

import java.util.Date;
import java.util.concurrent.Callable;

public class SmeltBronze extends Task {

    final static int FURNACE_ID = 24012;


    public SmeltBronze(ClientContext ctx) {
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
        return (ctx.objects.select().id(FURNACE_ID).nearest().poll().tile().distanceTo(ctx.players.local()) < 6 && (ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() > 14 && (ctx.chat.canContinue() || shouldActivate))));
    }

    @Override
    public void execute() {
        final GameObject furnace = ctx.objects.select().id(FURNACE_ID).nearest().poll();
        if(furnace.inViewport()) {
            furnace.interact("Smelt");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(270).component(14).valid();
                }
            }, 250, 26);
            Component smelt = ctx.widgets.widget(270).component(14);
            smelt.click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().animation() != -1;
                }
            }, 250,26);
            Condition.sleep(5000);
        }
        else {
            ctx.camera.turnTo(furnace);
        }
    }
}
