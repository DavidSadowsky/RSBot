package AutoSplasher;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

public class Splash extends Task {

    public Splash(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !ctx.players.local().inCombat() && ctx.players.local().animation() == -1;
    }

    @Override
    public void execute() {
        final Npc NPC_TO_ATTACK = ctx.npcs.select().id(1338).nearest().poll();
        NPC_TO_ATTACK.interact("Attack");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inCombat();
            }
        },250,25);
    }
}
