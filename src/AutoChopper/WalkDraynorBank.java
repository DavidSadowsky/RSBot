package AutoChopper;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import java.util.Random;

public class WalkDraynorBank extends Task {

    Area AREA = new Area(new Tile(3092, 3244,0), new Tile(3095, 3244,0), new Tile(3095, 3241,0), new Tile(3092, 3241,0));
    Area OAK_AREA = new Area(new Tile(3098,3246,0), new Tile(3098,3244,0), new Tile(3101,3245,0));

    public WalkDraynorBank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() == 0 && ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 3);
    }

    @Override
    public void execute() {
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if(ctx.inventory.count() == 28) {
                ctx.movement.step(AREA.getRandomTile());
                Condition.sleep(3000);
            }
            else {
                Random rand = new Random();
                ctx.movement.step(OAK_AREA.getRandomTile());
                Condition.sleep(3000);
            }
        }
    }
}
