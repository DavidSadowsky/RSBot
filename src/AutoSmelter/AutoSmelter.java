package AutoSmelter;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Script.Manifest(name="AutoSmelter", description="Smelts ores", properties = "author=David Sadowsky; topic=999; client=4;")

public class AutoSmelter extends PollingScript<ClientContext> {
    List<Task> taskList = new ArrayList<Task>();

    long startTime = System.currentTimeMillis();
    int breakCount = 0;
    final static int FURNACE_ID = 24012;

    @Override
    public void start() {

        String smeltOptions[] = {"Bronze", "Iron"};
        String smeltChoice = (String) "" + JOptionPane.showInputDialog(null, "Select bar to smelt","AutoSmelter", JOptionPane.PLAIN_MESSAGE, null, smeltOptions, smeltOptions[0]);

        taskList.add(new Bank(ctx));
        taskList.add(new Walk(ctx));
        if(smeltChoice == "Bronze") {
            taskList.add(new SmeltBronze(ctx));
        }
        if(smeltChoice == "Iron") {
            taskList.add(new SmeltIron(ctx));
        }
    }

    @Override
    public void stop() {
        System.out.println("Calculating break...");
        Random rand = new Random();
        if(breakCount == 36) {
            System.out.println("Logging out");
            ctx.game.logout();
            ctx.controller.stop();
        }
        else if(breakCount % 3 == 0 && breakCount != 0) {
            System.out.println("Taking a 5-10 minute break");
            Condition.sleep(300000 + rand.nextInt(0, 300000));
        }
        else if(breakCount != 0) {
            System.out.println("Taking a 1-2 minute break");
            Condition.sleep(60000 + rand.nextInt(0, 60000));
        }
        startTime = System.currentTimeMillis();
        breakCount++;
        return;
    }

    @Override
    public void poll() {
        if(ctx.game.tab() != Game.Tab.INVENTORY && !(ctx.bank.opened())) {
            System.out.println("Opening inventory");
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        for(Task task : taskList) {
            if((new Date()).getTime() - startTime > 10*60*1000 && ctx.objects.id(FURNACE_ID).nearest().poll().tile().distanceTo(ctx.players.local()) < 10) {
                stop();
                break;
            }
            if(task.activate()) {
                task.execute();
                break;
            }
        }
    }
}
