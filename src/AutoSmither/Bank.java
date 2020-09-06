package AutoSmither;

import AutoSmelter.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Bank extends Task {

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    final static int IRON_BAR_ID = 2351;
    final static int HAMMER_ID = 2347;

    @Override
    public boolean activate() {
        return (ctx.inventory.select().id(IRON_BAR_ID).count() == 0 || ctx.inventory.select().isEmpty()) && ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 6;
    }

    @Override
    public void execute() {
        System.out.println("Banking");
        if(ctx.bank.opened()) {
            final int inventCount = ctx.inventory.select().count();
            if(ctx.bank.depositAllExcept(HAMMER_ID)) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count() != inventCount;
                    }
                }, 250, 26);
            }
            if(ctx.bank.withdraw(IRON_BAR_ID, 25)){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(IRON_BAR_ID).count() == 25;
                    }
                }, 250, 26);
            }
        }
        else {
            ctx.camera.turnTo(ctx.bank.nearest());
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
