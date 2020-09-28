package AutoChopper;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import java.util.Random;

public class WalkDraynorBankWillows extends Task {

    Area AREA = new Area(new Tile(3092, 3244,0), new Tile(3095, 3244,0), new Tile(3095, 3241,0), new Tile(3092, 3241,0));
    Area WILLOW_AREA = new Area(new Tile(3090,3238,0), new Tile(3090,3230,0), new Tile(3086,3230,0), new Tile(3086,3238,0));

    public WalkDraynorBankWillows(ClientContext ctx) {
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
                ctx.movement.step(WILLOW_AREA.getRandomTile());
                Condition.sleep(3000);
            }
        }
    }
}
