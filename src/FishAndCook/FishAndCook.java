package FishAndCook;

import org.powerbot.script.Area;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Skills;

import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name="FishAndCook", description="Fishes and Cooks till both are level 50", properties = "author=David Sadowsky; topic=999; client=4;")


public class FishAndCook extends PollingScript<ClientContext> {
    List<Task> taskList = new ArrayList<Task>();

    Area LUMBRIDGE_BANK = new Area(new Tile(3207, 3220,2), new Tile(3210,3220,2), new Tile(3210,3214,2), new Tile(3207,3214,2));
    Area BARB_VILLAGE = new Area(new Tile(3103, 3437,0), new Tile(3103,3429,0), new Tile(3110,3429,0), new Tile(3110,3437,0));

    Skills skills = new Skills(ctx);

    boolean BEGINNER_FISHING_TASKS_REMOVED = false;
    boolean BEGINNER_COOKING_TASKS_REMOVED = false;
    boolean BARB_WALKING_TASKS_REMOVED = false;

    @Override
    public void start() {
        // Walk to Lumbridge to cook shrimp after level 20 fishing
        taskList.add(new DraynorToLumbridge(ctx));

        // Tasks for 1-20 fishing
        taskList.add(new BankShrimpFishing(ctx));
        taskList.add(new WalkDraynorBank(ctx));
        taskList.add(new FishShrimp(ctx));
        System.out.println("Tasks for 1-20 fishing loaded.");


        // todo: Tasks for level 20-50 fishing

        // todo: Tasks for level 30-50 cooking
    }

    @Override
    public void poll() {
        if(ctx.chat.canContinue()) {
            ctx.chat.clickContinue();
        }
        if(ctx.game.tab() != Game.Tab.INVENTORY && !(ctx.bank.opened())) {
            System.out.println("Opening inventory");
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        // Adds tasks for level 1-20 (ish) cooking
        if(skills.level(10) >= 20 && LUMBRIDGE_BANK.contains(ctx.players.local()) && ctx.players.local().tile().floor() == 2 && !BEGINNER_FISHING_TASKS_REMOVED) {

            // Clear old tasks
            taskList.clear();
            System.out.println("Tasks for 1-20 fishing unloaded.");

            // Add tasks for level 1-20 (ish) cooking
            taskList.add(new BankShrimpCooking(ctx));
            taskList.add(new WalkLumbridgeBank(ctx));
            taskList.add(new CookShrimp(ctx));
            System.out.println("Tasks for 1-20 cooking loaded.");

            BEGINNER_FISHING_TASKS_REMOVED = true;
        }
        if(LUMBRIDGE_BANK.contains(ctx.players.local()) && ctx.players.local().tile().floor() == 2 && skills.level(7) >= 20 && !BEGINNER_COOKING_TASKS_REMOVED) {

            // Clear old tasks
            taskList.clear();
            System.out.println("Tasks for 1-20 cooking unloaded.");

            // Add tasks to bank and walk to barb village fishing spot
            taskList.add(new BankBeforeBarbVillage(ctx));
            taskList.add(new WalkBarbVillage(ctx));
            System.out.println("Tasks for walking to barbarian village fishing spot loaded.");

            BEGINNER_COOKING_TASKS_REMOVED = true;
        }

        if(BARB_VILLAGE.contains(ctx.players.local()) && !BARB_WALKING_TASKS_REMOVED) {

            // Clear old tasks
            taskList.clear();
            System.out.println("Tasks for walking to barbarian village fishing spot unloaded.");

            // Add tasks for level 20-50 cooking and fishing
            taskList.add(new FlyFish(ctx));
            taskList.add(new CookTrout(ctx));
            taskList.add(new CookSalmon(ctx));
            taskList.add(new DropFish(ctx));
            System.out.println("Tasks for 20-50 fishing and cooking loaded.");

            BARB_WALKING_TASKS_REMOVED = true;
            ctx.camera.angle(180);
        }
        for(Task task : taskList) {
            if(task.activate()) {
                task.execute();
                break;
            }
        }
    }
}
