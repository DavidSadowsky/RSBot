package AutoMiner;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

import java.util.Random;
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
                final Item IRON_ORE = ctx.inventory.select().id(440).poll();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.depositBox.contains(IRON_ORE);
                    }
                }, 250, 26);
            }
        }
        else {

            Random rand = new Random();
            ctx.camera.angle(180);
            final GameObject depositBox = ctx.objects.select().id(26254).nearest().poll();
            depositBox.bounds(-18, 20, -92, -41, -15, 7);
            if(!depositBox.inViewport()) {
                ctx.camera.angle(ctx.camera.x() + rand.nextInt(10));
            }
            if(depositBox.interact("Deposit")) {
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
