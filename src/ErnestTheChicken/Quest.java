package ErnestTheChicken;

import Smither.Walker;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

import java.util.Random;
import java.util.concurrent.Callable;

public class Quest extends Task {

    public static final Tile[] veronicaToProf =  {new Tile(3110, 3330, 0), new Tile(3110, 3333, 0), new Tile(3109, 3336, 0), new Tile(3109, 3339, 0), new Tile(3109, 3342, 0), new Tile(3109, 3345, 0), new Tile(3109, 3348, 0), new Tile(3109, 3351, 0), new Tile(3108, 3354, 0), new Tile(3109, 3357, 0), new Tile(3109, 3360, 0), new Tile(3109, 3366, 1), new Tile(3106, 3366, 1), new Tile(3105, 3364, 2), new Tile(3108, 3364, 2)};
    public static final Tile[] profToFirstFloor = {new Tile(3110, 3366, 2), new Tile(3107, 3364, 2), new Tile(3106, 3363, 1), new Tile(3106, 3366, 1), new Tile(3108, 3361, 0)};
    public static final Tile[] firstFloorToBookCase = {new Tile(3106, 3368, 0), new Tile(3104, 3365, 0), new Tile(3103, 3362, 0), new Tile(3100, 3361, 0)};

    private final Smither.Walker walker = new Walker(ctx);

    public Quest(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return true;
    }

    public void startQuest() {
        Random rand = new Random();
        while(ctx.movement.energyLevel() < 100) {
            System.out.println("Restoring run energy...");
            ctx.camera.angle(ctx.camera.x() + rand.nextInt(30));
            Condition.sleep(15000);
        }
        ctx.camera.angle('n');
        ctx.camera.pitch(50);
        Condition.sleep(1000);
        final Npc VERONICA = ctx.npcs.select().id(3561).poll();
        VERONICA.interact("Talk-to");
        Condition.sleep(3000);
        while(!ctx.chat.canContinue()) {
            Condition.sleep(3000);
            VERONICA.interact("Talk-to");
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.chat.canContinue();
            }
        }, 250, 25);
        ctx.chat.clickContinue();
        Condition.sleep(2000);
        ChatOption chat = ctx.chat.select().text("Aha, sounds like a quest").poll();
        chat.select(true);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.chat.canContinue();
            }
        }, 250, 25);
        while(ctx.chat.canContinue()) {
            ctx.chat.clickContinue();
            Condition.sleep(750);
        }
        System.out.println("Quest started.");
        return;
    }

    public void talkToProf() {
        while(ctx.players.local().tile().floor() != 2) {
            walker.walkPath(veronicaToProf);
            Condition.sleep(1000);
            if(ctx.players.local().tile().floor() == 1) {
                ctx.camera.angle(180);
            }
        }
        final GameObject DOOR = ctx.objects.select().id(11540).nearest().poll();
        DOOR.interact("Open");
        ctx.camera.pitch(50);
        ctx.camera.angle(270);
        Condition.sleep(3000);
        final Npc PROF = ctx.npcs.select().id(3562).poll();
        PROF.interact("Talk-to");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.chat.canContinue();
            }
        }, 250, 25);
        ctx.chat.clickContinue();
        Condition.sleep(2000);
        ChatOption chat = ctx.chat.select().text("I'm looking for").poll();
        chat.select(true);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.chat.canContinue();
            }
        }, 250, 25);
        while(ctx.chat.canContinue()) {
            ctx.chat.clickContinue();
            Condition.sleep(750);
        }
        ChatOption chat2 = ctx.chat.select().text("Change him back").poll();
        chat2.select(true);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.chat.canContinue();
            }
        }, 250, 25);
        while(ctx.chat.canContinue()) {
            ctx.chat.clickContinue();
        }
        System.out.println("Professor dialogue complete.");
        ctx.camera.pitch(50);
        return;
    }

    public void walkToPuzzle() {
        while(ctx.players.local().tile().floor() != 0) {
            walker.walkPath(profToFirstFloor);
            Condition.sleep(500);
        }
        Tile doorTile = new Tile(3106, 3368, 0);
        ctx.movement.step(doorTile);
        Condition.sleep(5000);
        GameObject door = ctx.objects.select().id(11540).nearest().poll();
        door.interact("Open");
        Condition.sleep(1000);
        Tile bookcaseTile = new Tile(3098, 3359, 0);
        while(bookcaseTile.distanceTo(ctx.players.local()) > 3) {
            walker.walkPath(firstFloorToBookCase);
            if(ctx.players.local().tile().floor() == 0) {
                ctx.camera.angle(180);
            }
            Condition.sleep(500);
        }
        GameObject bookcase = ctx.objects.select().id(156).poll();
        bookcase.interact("Search");
        ctx.camera.angle('n');
        Condition.sleep(3000);
        GameObject ladder = ctx.objects.select().id(133).nearest().poll();
        ladder.interact("Climb-down");
        System.out.println("Starting puzzle...");
        Condition.sleep(3000);
        return;
    }

    public void pullLeverA(boolean up) {
        GameObject lever = up ? ctx.objects.select().id(11451).poll() : ctx.objects.select().id(11452).poll();
        lever.bounds(46, 83, -218, -153, -3, 22);
        lever.click();
        Condition.sleep(3000);
        while(up ? !ctx.objects.select().id(11452).poll().valid() : !ctx.objects.select().id(11451).poll().valid()){
            lever.click();
            Condition.sleep(2000);
        }
        System.out.println("Lever A pulled.");
        Condition.sleep(2000);
        return;
    }

    public void pullLeverB(boolean up) {
        GameObject lever = up ? ctx.objects.select().id(11453).poll() : ctx.objects.select().id(11454).poll();
        lever.bounds(101, 128, -220, -158, 53, 80);
        lever.click();
        Condition.sleep(3000);
        while(up ? !ctx.objects.select().id(11454).poll().valid() : !ctx.objects.select().id(11453).poll().valid()){
            lever.click();
            Condition.sleep(2000);
        }
        System.out.println("Lever B pulled.");
        Condition.sleep(2000);
        return;
    }

    public void pullLeverC(boolean up) {
        GameObject lever = up ? ctx.objects.select().id(11455).poll() : ctx.objects.select().id(11456).poll();
        lever.bounds(101, 129, -217, -165, 49, 78);
        lever.click();
        Condition.sleep(3000);
        while(up ? !ctx.objects.select().id(11456).poll().valid() : !ctx.objects.select().id(11455).poll().valid()){
            lever.click();
            Condition.sleep(2000);
        }
        System.out.println("Lever C pulled.");
        Condition.sleep(2000);
        return;
    }

    public void pullLeverD(boolean up) {
        GameObject lever = up ? ctx.objects.select().id(11457).poll() : ctx.objects.select().id(11458).poll();
        lever.bounds(42, 72, -226, -160, 96, 124);
        lever.click();
        Condition.sleep(3000);
        while(up ? !ctx.objects.select().id(11458).poll().valid() : !ctx.objects.select().id(11457).poll().valid()){
            lever.click();
            Condition.sleep(2000);
        }
        System.out.println("Lever D pulled.");
        Condition.sleep(2000);
        return;
    }

    public void pullLeverE(boolean up) {
        GameObject lever = up ? ctx.objects.select().id(11459).poll() : ctx.objects.select().id(11460).poll();
        lever.bounds(47, 79, -225, -171, 125, 98);
        lever.click();
        Condition.sleep(3000);
        while(up ? !ctx.objects.select().id(11460).poll().valid() : !ctx.objects.select().id(11459).poll().valid()){
            lever.click();
            Condition.sleep(2000);
        }
        System.out.println("Lever E pulled.");
        Condition.sleep(2000);
        return;
    }

    public void pullLeverF(boolean up) {
        GameObject lever = up ? ctx.objects.select().id(11461).poll() : ctx.objects.select().id(11462).poll();
        lever.bounds(29, 0, -217, -160, 43, 78);
        lever.click();
        Condition.sleep(3000);
        while(up ? !ctx.objects.select().id(11462).poll().valid() : !ctx.objects.select().id(11461).poll().valid()){
            lever.click();
            Condition.sleep(2000);
        }
        System.out.println("Lever F pulled.");
        Condition.sleep(2000);
        return;
    }

    public void pullExitLever() {
        final GameObject lever = ctx.objects.select().id(160).poll();
        ctx.camera.turnTo(lever.tile());
        Condition.sleep(1000);
        lever.bounds(105, 134, -204, -166, 55, 78);
        lever.click();
        while(ctx.players.local().tile().x() != 3098 && ctx.players.local().tile().y() != 3358) {
            Condition.sleep(3000);
            lever.click();
        }
    }

    public void puzzle() {
        Condition.sleep(3000);
        pullLeverB(true);
        Tile leverA = new Tile(3108, 9747, 0);
        ctx.movement.step(leverA);
        Condition.sleep(5000);
        pullLeverA(true);
        Tile DOOR_1_TILE = new Tile(3108, 9757,0);
        ctx.movement.step(DOOR_1_TILE);
        Condition.sleep(5000);
        GameObject DOOR_1 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_1.tile());
        Condition.sleep(1000);
        DOOR_1.interact("Open");
        Condition.sleep(2500);
        Tile LEVER_D_TILE = new Tile(3108,9766,0);
        ctx.movement.step(LEVER_D_TILE);
        Condition.sleep(5000);
        pullLeverD(true);
        Tile DOOR_2_TILE = new Tile(3106, 9760,0);
        ctx.movement.step(DOOR_2_TILE);
        Condition.sleep(5000);
        GameObject DOOR_2 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_2.tile());
        Condition.sleep(1000);
        DOOR_2.interact("Open");
        Condition.sleep(2500);
        Tile DOOR_3_TILE = new Tile(3102, 9759,0);
        ctx.movement.step(DOOR_3_TILE);
        Condition.sleep(3000);
        GameObject DOOR_3 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_3.tile());
        Condition.sleep(1000);
        DOOR_3.interact("Open");
        Condition.sleep(2500);
        ctx.movement.step(leverA);
        Condition.sleep(5000);
        pullLeverA(false);
        Tile LEVER_B_TILE = new Tile(3117,9752,0);
        ctx.movement.step(LEVER_B_TILE);
        Condition.sleep(5000);
        pullLeverB(false);
        Tile DOOR_4_TILE = new Tile(3102,9756,0);
        ctx.movement.step(DOOR_4_TILE);
        Condition.sleep(6000);
        GameObject DOOR_4 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_4.tile());
        Condition.sleep(1000);
        DOOR_4.interact("Open");
        Condition.sleep(2500);
        Tile DOOR_5_TILE = new Tile(3101,9760,0);
        ctx.movement.step(DOOR_5_TILE);
        Condition.sleep(2000);
        GameObject DOOR_5 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_5.tile());
        Condition.sleep(1000);
        DOOR_5.interact("Open");
        Condition.sleep(2500);
        Tile DOOR_6_TILE = new Tile(3097,9761,0);
        ctx.movement.step(DOOR_6_TILE);
        Condition.sleep(2000);
        GameObject DOOR_6 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_6.tile());
        Condition.sleep(1000);
        DOOR_6.interact("Open");
        Condition.sleep(2500);
        pullLeverF(true);
        pullLeverE(true);
        Tile DOOR_7_TILE = new Tile(3099,9765,0);
        ctx.movement.step(DOOR_7_TILE);
        Condition.sleep(2000);
        GameObject DOOR_7 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_7.tile());
        Condition.sleep(1000);
        DOOR_7.interact("Open");
        Condition.sleep(2500);
        Tile DOOR_8_TILE = new Tile(3104,9765,0);
        ctx.movement.step(DOOR_8_TILE);
        Condition.sleep(2000);
        GameObject DOOR_8 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_8.tile());
        Condition.sleep(1000);
        DOOR_8.interact("Open");
        Condition.sleep(2500);
        Tile LEVER_C_TILE = new Tile(3111,9760,0);
        ctx.movement.step(LEVER_C_TILE);
        Condition.sleep(5000);
        pullLeverC(true);
        Tile DOOR_9_TILE = new Tile(3106,9765,0);
        ctx.movement.step(DOOR_9_TILE);
        Condition.sleep(5000);
        GameObject DOOR_9 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_9.tile());
        Condition.sleep(1000);
        DOOR_9.interact("Open");
        Condition.sleep(2500);
        Tile DOOR_10_TILE = new Tile(3101,9765,0);
        ctx.movement.step(DOOR_10_TILE);
        Condition.sleep(2000);
        GameObject DOOR_10 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_10.tile());
        Condition.sleep(1000);
        DOOR_10.interact("Open");
        Condition.sleep(2500);
        pullLeverE(false);
        Tile DOOR_14_TILE = new Tile(3099,9765,0);
        ctx.movement.step(DOOR_14_TILE);
        Condition.sleep(2000);
        GameObject DOOR_14 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_14.tile());
        Condition.sleep(1000);
        DOOR_14.interact("Open");
        Condition.sleep(2500);
        Tile DOOR_11_TILE = new Tile(3102,9764,0);
        ctx.movement.step(DOOR_11_TILE);
        Condition.sleep(2500);
        GameObject DOOR_11 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_11.tile());
        Condition.sleep(1000);
        DOOR_11.interact("Open");
        Condition.sleep(2500);
        Tile DOOR_12_TILE = new Tile(3102,9759,0);
        ctx.movement.step(DOOR_12_TILE);
        Condition.sleep(2000);
        GameObject DOOR_12 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_12.tile());
        Condition.sleep(1000);
        DOOR_12.interact("Open");
        Condition.sleep(2500);
        Tile DOOR_13_TILE = new Tile(3101,9755,0);
        ctx.movement.step(DOOR_13_TILE);
        Condition.sleep(2000);
        GameObject DOOR_13 = ctx.objects.select().id(11450).nearest().poll();
        ctx.camera.turnTo(DOOR_13.tile());
        Condition.sleep(1000);
        DOOR_13.interact("Open");
        Condition.sleep(2500);
        Tile OIL_CAN_TILE = new Tile(3093,9755,0);
        ctx.movement.step(OIL_CAN_TILE);
        Condition.sleep(3000);
        GroundItem OIL_CAN = ctx.groundItems.select().id(277).nearest().poll();
        OIL_CAN.interact("Take");
        Condition.sleep(3000);
        System.out.println("Puzzle Completed.");
        Tile DOOR_15_TILE = new Tile(3098,9755,0);
        ctx.movement.step(DOOR_15_TILE);
        Condition.sleep(3000);
        GameObject DOOR_15 = ctx.objects.select().id(11450).nearest().poll();
        DOOR_15.interact("Open");
        Condition.sleep(2500);
        Tile LADDER_TILE = new Tile(3117,9753,0);
        ctx.movement.step(LADDER_TILE);
        Condition.sleep(5000);
        GameObject LADDER = ctx.objects.select().id(132).poll();
        LADDER.interact("Climb-up");
        Condition.sleep(3000);
        pullExitLever();
        Condition.sleep(3000);
        return;
    }

    public void poisonPiranhas() {
        Condition.sleep(3000);
        ctx.camera.pitch(50);
        ctx.movement.step(new Tile(3103,3363,0));
        Condition.sleep(5000);
        GameObject DOOR_1 = ctx.objects.select().id(11540).nearest().poll();
        ctx.camera.turnTo(DOOR_1);
        DOOR_1.interact("Open");
        Condition.sleep(2000);
        ctx.movement.step(new Tile(3106,3369,0));
        Condition.sleep(5000);
        GameObject DOOR_2 = ctx.objects.select().id(11540).nearest().poll();
        ctx.camera.turnTo(DOOR_2);
        DOOR_2.interact("Open");
        Condition.sleep(2000);
        ctx.movement.step(new Tile(3109,3361,0));
        Condition.sleep(5000);
        GameObject STAIR_1 = ctx.objects.select().id(11498).nearest().poll();
        ctx.camera.turnTo(STAIR_1);
        Condition.sleep(2000);
        STAIR_1.interact("Climb-up");
        Condition.sleep(1000);
        ctx.movement.step(new Tile(3113,3367,1));
        Condition.sleep(5000);
        GameObject DOOR_3 = ctx.objects.select().id(11540).nearest().poll();
        ctx.camera.turnTo(DOOR_3);
        Condition.sleep(2000);
        DOOR_3.interact("Open");
        Condition.sleep(1000);
        ctx.movement.step(new Tile(3116,3362,1));
        Condition.sleep(5000);
        GameObject DOOR_4 = ctx.objects.select().id(11540).nearest().poll();
        ctx.camera.turnTo(DOOR_4);
        Condition.sleep(2000);
        DOOR_4.interact("Open");
        Condition.sleep(1000);
        ctx.movement.step(new Tile(3111,3358,1));
        Condition.sleep(5000);
        GameObject DOOR_5 = ctx.objects.select().id(11540).nearest().poll();
        Condition.sleep(2000);
        DOOR_5.interact("Open");
        ctx.camera.angle(90);
        Condition.sleep(1000);
        GroundItem FISH_FOOD = ctx.groundItems.select().id(272).poll();
        final int inventCount = ctx.inventory.count();
        FISH_FOOD.interact("Take");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.count() != inventCount;
            }
        }, 250,25);
        ctx.movement.step(new Tile(3108,3366,1));
        Condition.sleep(5000);
        GameObject STAIR_2 = ctx.objects.select().id(11499).nearest().poll();
        ctx.camera.turnTo(STAIR_2);
        Condition.sleep(2000);
        STAIR_2.interact("Climb-down");
        Condition.sleep(1000);
        ctx.movement.step(new Tile(3106,3368,0));
        Condition.sleep(5000);
        GameObject DOOR_6 = ctx.objects.select().id(11540).nearest().poll();
        ctx.camera.turnTo(DOOR_6);
        Condition.sleep(2000);
        DOOR_6.interact("Open");
        Condition.sleep(1000);
        ctx.movement.step(new Tile(3102,3371,0));
        Condition.sleep(5000);
        GameObject DOOR_7 = ctx.objects.select().id(11540).nearest().poll();
        ctx.camera.turnTo(DOOR_7);
        Condition.sleep(2000);
        DOOR_7.interact("Open");
        Condition.sleep(1000);
        ctx.movement.step(new Tile(3099,3367,0));
        Condition.sleep(5000);
        GameObject DOOR_8 = ctx.objects.select().id(11540).nearest().poll();
        ctx.camera.turnTo(DOOR_8);
        Condition.sleep(5000);
        DOOR_8.interact("Open");
        Condition.sleep(1000);
        GroundItem POISON = ctx.groundItems.select().id(273).poll();
        final int inventCount2 = ctx.inventory.count();
        POISON.interact("Take");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.count() != inventCount2;
            }
        }, 250,25);
        Item fish_food = ctx.inventory.select().id(272).poll();
        Item poison = ctx.inventory.select().id(273).poll();
        if(ctx.game.tab() != Game.Tab.INVENTORY) {
            System.out.println("Opening inventory");
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        fish_food.interact("Use");
        poison.interact("Use");
        ctx.movement.step(new Tile(3111,3371,0));
        Condition.sleep(5000);
        ctx.movement.step(new Tile(3117,3356,0));
        Condition.sleep(5000);
        GameObject DOOR_9 = ctx.objects.select().id(11540).nearest().poll();
        ctx.camera.turnTo(DOOR_8);
        Condition.sleep(5000);
        DOOR_9.interact("Open");
        Condition.sleep(5000);
        ctx.movement.step(new Tile(3122,3359,0));
        Condition.sleep(5000);
        GroundItem SPADE = ctx.groundItems.select().id(952).poll();
        final int inventCount3 = ctx.inventory.count();
        SPADE.interact("Take");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.count() != inventCount3;
            }
        }, 250,25);
        GameObject DOOR_13 = ctx.objects.select().id(11539).nearest().poll();
        DOOR_13.interact("Open");
        Condition.sleep(2000);
        ctx.movement.step(new Tile(3118,3345,0));
        Condition.sleep(15000);
        ctx.movement.step(new Tile(3103,3339,0));
        Condition.sleep(15000);
        ctx.movement.step(new Tile(3087,3336,0));
        Condition.sleep(15000);
        Item POISONED_FISH_FOOD = ctx.inventory.select().id(274).poll();
        POISONED_FISH_FOOD.interact("Use");
        GameObject FOUNTAIN = ctx.objects.select().id(153).nearest().poll();
        FOUNTAIN.click();
        Condition.sleep(5000);
        FOUNTAIN.click();
        Condition.sleep(5000);
        while(ctx.chat.canContinue()) {
            ctx.chat.clickContinue();
            Condition.sleep(1000);
        }
        return;
    }

    public void rubberTube() {
        Condition.sleep(3000);
        ctx.movement.step(new Tile(3091,3347,0));
        Condition.sleep(5000);
        ctx.movement.step(new Tile(3086,3358,0));
        Condition.sleep(5000);
        GameObject COMPOST_HEAP = ctx.objects.select().id(152).poll();
        ctx.camera.turnTo(COMPOST_HEAP);
        Condition.sleep(2000);
        COMPOST_HEAP.click();
        Condition.sleep(5000);
        ctx.movement.step(new Tile(3095,3346,0));
        Condition.sleep(5000);
        ctx.movement.step(new Tile(3107,3353,0));
        Condition.sleep(30000);
        GameObject DOOR_1 = ctx.objects.select().id(11541).nearest().poll();
        ctx.camera.turnTo(DOOR_1);
        DOOR_1.interact("Open");
        Condition.sleep(3000);
        ctx.movement.step(new Tile(3109,3357,0));
        Condition.sleep(2000);
        GameObject DOOR_2 = ctx.objects.select().id(11540).nearest().poll();
        ctx.camera.turnTo(DOOR_2);
        DOOR_2.interact("Open");
        ctx.movement.step(new Tile(3107,3367,0));
        Condition.sleep(5000);
        while(ctx.movement.energyLevel() < 10 || ctx.combat.health() < ctx.combat.maxHealth()){
            System.out.println("Restoring run and HP...");
            Condition.sleep(15000);
        }
        if(!ctx.movement.running(true)) {
            ctx.movement.running(true);
        }
        GroundItem RUBBER_TUBE = ctx.groundItems.select().id(276).poll();
        GameObject SKELETON_DOOR = ctx.objects.select().id(11540).nearest().poll();
        ctx.camera.turnTo(SKELETON_DOOR);
        Condition.sleep(3000);
        SKELETON_DOOR.click();
        Condition.sleep(1000);
        RUBBER_TUBE.interact("Take");
        final int INVENTORY_COUNT = ctx.inventory.select().count();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return INVENTORY_COUNT != ctx.inventory.select().count();
            }
        },100, 50);
        SKELETON_DOOR.click();
        Condition.wait(new Callable<Boolean>() {
            final int x = ctx.players.local().tile().x();
            final int y = ctx.players.local().tile().y();
            @Override
            public Boolean call() throws Exception {
                return (x == 3107) && (y == 3367);
            }
        }, 250, 20);
        ctx.movement.step(new Tile(3109,3361,0));
        Condition.sleep(5000);
        GameObject STAIR_1 = ctx.objects.select().id(11487).nearest().poll();
        ctx.camera.turnTo(STAIR_1);
        STAIR_1.interact("Climb-up");
        Condition.sleep(2000);
        ctx.movement.step(new Tile(3105,3364,1));
        Condition.sleep(3000);
        GameObject STAIR_2 = ctx.objects.select().id(11511).nearest().poll();
        ctx.camera.turnTo(STAIR_2);
        STAIR_2.interact("Climb-up");
        Condition.sleep(2000);
        GameObject DOOR_4 = ctx.objects.select().id(11540).nearest().poll();
        DOOR_4.interact("Open");
        ctx.camera.angle(270);
        Condition.sleep(3000);
        Npc PROF = ctx.npcs.select().id(3562).poll();
        PROF.interact("Talk-to");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.chat.canContinue();
            }
        }, 250, 25);
        while(ctx.chat.canContinue()) {
            ctx.chat.clickContinue();
            Condition.sleep(750);
        }
        while(!ctx.chat.canContinue()) {
            System.out.println("Waiting for cutscene...");
            Condition.sleep(1000);
        }
        while(ctx.chat.canContinue()) {
            ctx.chat.clickContinue();
            Condition.sleep(750);
        }
        Condition.sleep(3000);
        System.out.println("Quest Completed");
        System.exit(0);
        return;
    }

    @Override
    public void execute() {
        startQuest();
        talkToProf();
        walkToPuzzle();
        puzzle();
        poisonPiranhas();
        rubberTube();
    }
}
