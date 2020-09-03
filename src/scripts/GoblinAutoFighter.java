package scripts;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;

import java.util.Random;
import java.util.concurrent.Callable;

@Script.Manifest(name="GoblinAutoFighter", description="Fights goblins", properties = "author=David Sadowsky; topic=999; client=4;")

public class GoblinAutoFighter extends PollingScript<ClientContext> {

    final static int GOBLIN_IDS[] = {3029, 3030, 3031, 3032, 3033, 3034, 3035};
    final static int FOOD_ID = 315;
    Tile v1 = new Tile(3260,3245,0);
    Tile v2 =  new Tile(3241,3245,0);
    Tile v3 = new Tile(3241,3226,0);
    Tile v4 = new Tile(3260,3226,0);
    Area GOBLIN_VILLAGE = new Area(v1, v2, v3, v4);
    Combat combat = new Combat(ctx);
    Skills skills = new Skills(ctx);
    String trainingSkill;
    boolean loggedIn = false;
    Game game = new Game(ctx);

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
        if(!loggedIn && game.loggedIn()) {
            loggedIn = true;
            if(combat.style() == Combat.Style.AGGRESSIVE) {
                trainingSkill = "STRENGTH";
                System.out.println("Training [Strength]");
            }
            else if(combat.style() == Combat.Style.ACCURATE) {
                trainingSkill = "ATTACK";
                System.out.println("Training [Attack]");
            }
            else {
                trainingSkill = "DEFENSE";
                System.out.println("Training [Defense]");
            }
        }
        if(shouldReturn()) {
            returnToArea();
        }
        if(shouldSwitchStyles()) {
            switchStyles();
        }
        else if (shouldAttack()) {
            attack();
        }
    }

    public boolean shouldAttack() {
        return !ctx.players.local().inCombat() && ctx.players.local().animation() == -1 && ctx.players.local().interacting().name() != "Goblin" && !ctx.players.local().inMotion();
    }

    public boolean shouldReturn() {
        return ctx.players.local().animation() == -1 && !ctx.players.local().inCombat() && !GOBLIN_VILLAGE.contains(ctx.players.local().tile()) && ctx.players.local().interacting().name() != "Goblin" && !ctx.players.local().inMotion();
    }

    public boolean shouldSwitchStyles() {
        if(skills.level(2) == 40 && trainingSkill == "STRENGTH") {
            return true;
        }
        if(skills.level(1) == 40 && trainingSkill == "DEFENSE") {
            return true;
        }
        if(skills.level(0) == 40 && trainingSkill == "ATTACK") {
            return true;
        }
        return false;
    }

    public void attack() {
        System.out.println("Attacking goblin...");
        final Npc GOBLIN_TO_ATTACK = ctx.npcs.select().id(GOBLIN_IDS).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return !npc.interacting().valid() && npc.tile().matrix(ctx).reachable();
            }
        }).nearest().poll();
        ctx.camera.turnTo(GOBLIN_TO_ATTACK);
        GOBLIN_TO_ATTACK.interact("Attack");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().interacting().name() == "Goblin";
            }
        }, 200);
    }

    public void returnToArea() {
        System.out.println("Returning to center of goblin village...");
        if(ctx.movement.energyLevel() > 50) {
            ctx.movement.running(true);
        }
        ctx.movement.step(GOBLIN_VILLAGE.getRandomTile());
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return GOBLIN_VILLAGE.contains(ctx.players.local().tile());
            }
        }, 500);
    }

    public void switchStyles() {
        if(trainingSkill == "STRENGTH" && skills.level(0) < 40) {
            combat.style(Combat.Style.ACCURATE);
            trainingSkill = "ATTACK";
            System.out.println("Switching to attack training...");
        }
        else if(trainingSkill == "STRENGTH" && skills.level(1) < 40) {
            combat.style(Combat.Style.DEFENSIVE);
            trainingSkill = "DEFENSE";
            System.out.println("Switching to defense training...");
        }
        else if(trainingSkill == "ATTACK" && skills.level(1) < 40) {
            combat.style(Combat.Style.DEFENSIVE);
            trainingSkill = "DEFENSE";
            System.out.println("Switching to defense training...");
        }
        else if(trainingSkill == "ATTACK" && skills.level(2) < 40) {
            combat.style(Combat.Style.AGGRESSIVE);
            trainingSkill = "STRENGTH";
            System.out.println("Switching to strength training...");
        }
        else if(trainingSkill == "DEFENSE" && skills.level(0) < 40) {
            combat.style(Combat.Style.ACCURATE);
            trainingSkill = "ATTACK";
            System.out.println("Switching to attack training...");
        }
        else if(trainingSkill == "DEFENSE" && skills.level(2) < 40) {
            combat.style(Combat.Style.AGGRESSIVE);
            trainingSkill = "STRENGTH";
            System.out.println("Switching to strength training...");
        }
        return;
    }
}
