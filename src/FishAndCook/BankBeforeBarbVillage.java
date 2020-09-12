package FishAndCook;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Skills;
import z.Ca;

import java.util.concurrent.Callable;

public class BankBeforeBarbVillage extends Task {
    public BankBeforeBarbVillage(ClientContext ctx) {
        super(ctx);
    }

    Skills skills = new Skills(ctx);
    final int COOKED_SHRIMP_ID = 315;
    final int FLY_FISHING_ROD = 309;
    final int FEATHER = 314;

    @Override
    public boolean activate() {
        return (ctx.inventory.select().id(COOKED_SHRIMP_ID).count() == 28 && ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 6 && skills.level(7) >= 20);
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
            if(ctx.bank.withdraw(FLY_FISHING_ROD, 1)) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(FLY_FISHING_ROD).count() == 1;
                    }
                },250,29);
            }
            if(ctx.bank.withdraw(FEATHER, 4000)) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(FEATHER).count() > 0;
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
