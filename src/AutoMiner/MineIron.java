package AutoMiner;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.Random;
import java.util.concurrent.Callable;

public class MineIron extends Task {


    final static int IRON_IDS[] = { 11365, 11364 };
    final static int IRON_ID = 440;

    GameObject CURR_ROCK;


    public MineIron(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (ctx.players.local().animation() == -1 && ctx.inventory.select().count() < 28) || ((!CURR_ROCK.valid() || ctx.chat.canContinue()) && ctx.inventory.select().count() < 28);
    }

    @Override
    public void execute() {
        GameObject rockToMine;
        System.out.println("Trying to mine iron...");
            if(!ctx.objects.select().id(IRON_IDS).nearest().poll().inViewport()) {
                Random rand = new Random();
                ctx.camera.turnTo(ctx.objects.select().id(IRON_IDS).nearest().poll());
            }
            rockToMine = ctx.objects.select().id(IRON_IDS).nearest().poll();
            System.out.println("Mining iron");

        CURR_ROCK = rockToMine;
        rockToMine.interact("Mine");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() != -1;
            }
        }, 200, 20);
    }
}
