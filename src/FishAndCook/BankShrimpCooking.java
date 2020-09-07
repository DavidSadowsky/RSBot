package FishAndCook;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import z.Ca;

import java.util.concurrent.Callable;

public class BankShrimpCooking extends Task {
    public BankShrimpCooking(ClientContext ctx) {
        super(ctx);
    }

    final int FISHING_NET_ID = 303;
    final int UNCOOKED_SHRIMP_ID = 317;

    @Override
    public boolean activate() {
        return (ctx.inventory.select().id(FISHING_NET_ID).count() > 0 || (ctx.inventory.select().id(UNCOOKED_SHRIMP_ID).count() == 0 && ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 6));
    }

    @Override
    public void execute() {
        ctx.camera.turnTo(ctx.bank.nearest());
        if(ctx.bank.opened()) {
            final int inventCount = ctx.inventory.select().count();
            if(ctx.bank.depositInventory()) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count() != inventCount;
                    }
                }, 250, 26);
            }
            if(ctx.bank.withdraw(UNCOOKED_SHRIMP_ID, 28)) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(UNCOOKED_SHRIMP_ID).count() == 28;
                    }
                },250,29);
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
