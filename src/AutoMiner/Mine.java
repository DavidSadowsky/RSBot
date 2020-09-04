package AutoMiner;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import java.util.Random;
import java.util.concurrent.Callable;

public class Mine extends Task {


    final static int COPPER_IDS[] = { 10943, 11161 };
    final static int TIN_IDS[] = { 11361, 11360 };
    final static int TIN_ID = 438;

    GameObject CURR_ROCK;


    public Mine(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (ctx.players.local().animation() == -1 && ctx.inventory.select().count() < 28) || ((!CURR_ROCK.valid() || ctx.chat.canContinue()) && ctx.inventory.select().count() < 28);
    }

    @Override
    public void execute() {
        GameObject rockToMine;
        if(ctx.inventory.select().id(TIN_ID).count() == 14) {
            if(!ctx.objects.select().id(COPPER_IDS).nearest().poll().inViewport()) {
                Random rand = new Random();
                ctx.camera.turnTo(ctx.objects.select().id(COPPER_IDS).nearest().poll());
            }
            rockToMine = ctx.objects.select().id(COPPER_IDS).nearest().poll();
            System.out.println("Mining copper");
        }
        else {
            if(!ctx.objects.select().id(TIN_IDS).nearest().poll().inViewport()) {
                Random rand = new Random();
                ctx.camera.turnTo(ctx.objects.select().id(TIN_IDS).nearest().poll());
            }
            rockToMine = ctx.objects.select().id(TIN_IDS).nearest().poll();
            System.out.println("Mining tin");
        }

        rockToMine.interact("Mine");
        CURR_ROCK = rockToMine;

        Condition.wait(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() != -1;
            }
        }, 200, 20);
    }
}
