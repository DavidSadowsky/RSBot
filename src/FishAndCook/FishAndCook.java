package FishAndCook;

import org.powerbot.script.Area;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;

import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name="FishAndCook", description="Fishes and Cooks till both are level 50", properties = "author=David Sadowsky; topic=999; client=4;")


public class FishAndCook extends PollingScript<ClientContext> {
    List<Task> taskList = new ArrayList<Task>();

    Area LUMBRIDGE_BANK = new Area(new Tile(3207, 3220,2), new Tile(3210,3220,2), new Tile(3210,3214,2), new Tile(3207,3214,2));
    boolean BEGINNER_FISHING_TASKS_REMOVED = false;

    @Override
    public void start() {
        // Walk to Lumbridge to cook shrimp after level 20 fishing
        taskList.add(new DraynorToLumbridge(ctx));

        // Tasks for 1-20 fishing
        taskList.add(new BankShrimpFishing(ctx));
        taskList.add(new WalkDraynorBank(ctx));
        taskList.add(new FishShrimp(ctx));


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
        if(LUMBRIDGE_BANK.contains(ctx.players.local()) && ctx.players.local().tile().floor() == 2 && !BEGINNER_FISHING_TASKS_REMOVED) {
            taskList.clear();

            // Tasks for level 1-20 (ish) cooking
            taskList.add(new BankShrimpCooking(ctx));
            taskList.add(new WalkLumbridgeBank(ctx));
            taskList.add(new CookShrimp(ctx));


            BEGINNER_FISHING_TASKS_REMOVED = true;
        }
        for(Task task : taskList) {
            if(task.activate()) {
                task.execute();
                break;
            }
        }
    }
}
