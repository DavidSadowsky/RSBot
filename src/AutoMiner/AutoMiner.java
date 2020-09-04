package AutoMiner;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Script.Manifest(name="AutoMiner", description="Mines ores", properties = "author=David Sadowsky; topic=999; client=4;")

public class AutoMiner extends PollingScript<ClientContext> {
    List<Task> taskList = new ArrayList<Task>();

    long startTime = System.currentTimeMillis();
    int breakCount = 0;
    final static int ROCK_IDS[] = { 10943, 11161, 11361, 11360 };

    @Override
    public void start() {
        taskList.add(new Bank(ctx));
        taskList.add(new Walk(ctx));
        taskList.add(new Mine(ctx));
    }

    @Override
    public void stop() {
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
        else {
            System.out.println("Taking a 1-2 minute break");
            Condition.sleep(60000 + rand.nextInt(0, 60000));
        }
        startTime = System.currentTimeMillis();
        breakCount++;
        return;
    }

    @Override
    public void poll() {
        if(ctx.game.tab() != Game.Tab.INVENTORY) {
            System.out.println("Opening inventory");
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        if(ctx.movement.energyLevel() == 100 && !(ctx.movement.running(true))) {
            System.out.println("Turning on run");
            ctx.movement.running(true);
        }
        for(Task task : taskList) {
            if((new Date()).getTime() - startTime > 10*60*1000 && ctx.objects.id(ROCK_IDS).nearest().poll().tile().distanceTo(ctx.players.local()) < 10) {
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
