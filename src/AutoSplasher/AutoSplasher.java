package AutoSplasher;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Script.Manifest(name="AutoSplasher", description="Makes splashing just a little bit easier.", properties = "author=David Sadowsky; topic=999; client=4;")

public class AutoSplasher extends PollingScript<ClientContext> {

    List<Task> taskList = new ArrayList<Task>();
    long startTime = System.currentTimeMillis();
    Game.Tab tabs[] = { Game.Tab.ATTACK, Game.Tab.STATS, Game.Tab.QUESTS, Game.Tab.INVENTORY, Game.Tab.EQUIPMENT, Game.Tab.PRAYER, Game.Tab.MAGIC, Game.Tab.CLAN_CHAT, Game.Tab.FRIENDS_LIST, Game.Tab.ACCOUNT_MANAGEMENT, Game.Tab.IGNORED_LIST, Game.Tab.OPTIONS, Game.Tab.EMOTES, Game.Tab.MUSIC };
    Game game = new Game(ctx);

    @Override
    public void start() {
        taskList.add(new Logout(ctx));
        taskList.add(new Splash(ctx));
    }

    @Override
    public void poll() {
        if(System.currentTimeMillis() - startTime > 1000*60*10) {
            Random rand = new Random();
            ctx.game.tab(tabs[rand.nextInt(tabs.length - 1)]);
            startTime = System.currentTimeMillis();
        }
        if(!game.loggedIn()) {
            stop();
        }
        for(Task task : taskList) {
            if(task.activate()) {
                task.execute();
            }
        }
    }
}
