package Miner;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Bank extends Task {

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() == 28 && ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 6;
    }

    @Override
    public void execute() {
        if(ctx.bank.opened()) {
            if(ctx.bank.depositInventory()) {
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
            if(!ctx.bank.inViewport()) {
                ctx.camera.turnTo(ctx.bank.nearest());
            }
            if(ctx.bank.open()) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.opened();
                    }
                }, 250, 26);
            }

        }
    }
}
