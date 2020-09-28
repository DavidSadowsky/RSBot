package AutoChopper;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Skills;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Script.Manifest(name="AutoChopper", description="Chops trees to level 50", properties = "author=David Sadowsky; topic=999; client=4;")

public class AutoChopper extends PollingScript<ClientContext> {
    List<Task> taskList = new ArrayList<Task>();

    long startTime = System.currentTimeMillis();
    int breakCount = 0;
    boolean logs = false;
    boolean oaks = false;
    boolean willows = false;

    Skills skills = new Skills(ctx);

    @Override
    public void start() {
        if(skills.level(8) < 15) {
            taskList.add(new Bank(ctx));
            taskList.add(new WalkLumbridgeBank(ctx));
            taskList.add(new ChopLogs(ctx));
            logs = true;
            System.out.println("Level 1-15 tasks loaded.");
        }
        else if(skills.level(8) < 31) {
            taskList.add(new Bank(ctx));
            taskList.add(new WalkDraynorBank(ctx));
            taskList.add(new ChopOak(ctx));
            logs = false;
            oaks = true;
            System.out.println("Level 15-31 tasks loaded.");
        }
        else if(skills.level(8) < 50) {
            System.out.println("Level 31-50 tasks loaded.");
            ctx.movement.step(new Tile(3090,3238,0));
            Condition.sleep(3000);
            taskList.add(new Bank(ctx));
            taskList.add(new WalkDraynorBankWillows(ctx));
            taskList.add(new ChopWillow(ctx));
            oaks = false;
            willows = true;
        }
    }

    @Override
    public void stop() {
        Random rand = new Random();
        if(breakCount == 36) {
            System.out.println("Logging out");
            ctx.game.logout();
            System.exit(0);
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
        while(ctx.chat.canContinue()) {
            ctx.chat.clickContinue();
        }
        if(ctx.game.tab() != Game.Tab.INVENTORY && !(ctx.bank.opened())) {
            System.out.println("Opening inventory");
            ctx.game.tab(Game.Tab.INVENTORY);
            ctx.camera.pitch(100);
        }
        if(skills.level(8) == 15 && logs) {
            taskList.clear();
            taskList.add(new LumbrideToDraynor(ctx));
        }
        if(skills.level(8) >= 15 && logs && ctx.players.local().tile().distanceTo(new Tile(3102, 3240, 0)) < 6) {
            taskList.clear();
            start();
        }
        if(skills.level(8) == 31 && oaks) {
            taskList.clear();
            start();
        }
        for(Task task : taskList) {
            if((new Date()).getTime() - startTime > 10*60*1000) {
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
