package FishAndCook;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Skills;

import java.util.concurrent.Callable;

public class FishShrimp extends Task {

    public FishShrimp(ClientContext ctx) {
        super(ctx);
    }

    Skills skills = new Skills(ctx);

    @Override
    public boolean activate() {
        return (skills.level(10) < 20 && ctx.players.local().animation() == -1 && ctx.inventory.select().id(317).count() < 27);
    }

    @Override
    public void execute() {
        final Npc SHRIMPS_TO_FISH = ctx.npcs.select().id(1525).nearest().poll();
        if(!SHRIMPS_TO_FISH.inViewport()) {
            ctx.camera.angle(90);
        }
        ctx.camera.turnTo(SHRIMPS_TO_FISH);
        SHRIMPS_TO_FISH.interact("Small Net");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() != -1;
            }
        },250,25);
    }
}
