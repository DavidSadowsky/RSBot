package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

@Script.Manifest(name="GoblinAutoFighter", description="Fights goblins", properties = "author=David Sadowsky; topic=999; client=4;")

public class GoblinAutoFighter extends PollingScript<ClientContext> {

    final static int GOBLIN_IDS[] = { 3029, 3030, 3031, 3032, 3033, 3034, 3035 };
    final static int FOOD_ID = 315;

    @Override
    public void start() {
        System.out.println("Started");
    }

    @Override
    public void stop() {
        System.out.println("Stopped");
    }

    @Override
    public void poll() {
        // constant loop
        if(needsHeal()) {
            heal();
        }
        else if(shouldAttack()) {
            attack();
        }
    }

    public boolean needsHeal() {
        return ctx.combat.health() < 6;
    }

    public boolean shouldAttack() {
        return !ctx.players.local().inCombat() && ctx.players.local().animation() == -1;
    }

    public void attack() {
        final Npc GOBLIN_TO_ATTACK = ctx.npcs.select().id(GOBLIN_IDS).select(new Filter<Npc>() {


            @Override
            public boolean accept(Npc npc) {
                return !npc.inCombat();
            }
        }).nearest().poll();

        GOBLIN_TO_ATTACK.interact("Attack");

        Condition.wait(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inCombat();
            }
        }, 200, 20);
    }

    public void heal() {
        Item foodToEat = ctx.inventory.id(FOOD_ID).poll();
        foodToEat.interact("Eat", "Shrimps");

        Condition.wait(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final int currHealth = ctx.combat.health();
                return currHealth > 6;
            }
        }, 150, 20);
    }
}
