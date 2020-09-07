package FishAndCook;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class BankShrimpFishing extends Task {

    public BankShrimpFishing(ClientContext ctx) {
        super(ctx);
    }

    final int SHRIMP_ID = 317;

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() == 28 && ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 6;
    }

    @Override
    public void execute() {
        ctx.camera.turnTo(ctx.bank.nearest());
        if(ctx.bank.opened()) {
            final int inventCount = ctx.inventory.select().count();
            if(ctx.bank.depositAllExcept(303)) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count() != inventCount;
                    }
                }, 250, 26);
            }
        }
        else {
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
