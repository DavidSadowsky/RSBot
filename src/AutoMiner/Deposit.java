package AutoMiner;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Deposit extends Task {

    public Deposit(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() == 28 && ctx.objects.select().id(26254).nearest().poll().tile().distanceTo(ctx.players.local()) < 6;
    }

    @Override
    public void execute() {
        if(ctx.depositBox.opened()) {
            if(ctx.depositBox.depositInventory()) {
                final int inventCount = ctx.inventory.select().count();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count() == inventCount;
                    }
                }, 250, 26);
            }
        }
        else {
            if(!ctx.objects.select().id(26254).nearest().poll().inViewport()) {
                ctx.camera.angle(180);
            }
            if(ctx.objects.select().id(26254).nearest().poll().click()) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.depositBox.opened();
                    }
                }, 250, 26);
            }

        }
    }
}
