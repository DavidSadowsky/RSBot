package AutoSmither;

import AutoSmelter.Task;
import AutoSmelter.Walker;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class Walk extends Task {

    public static final Tile[] pathToBank = {new Tile(3187, 3425, 0), new Tile(3187, 3428, 0), new Tile(3184, 3431, 0), new Tile(3183, 3434, 0)};
    final static int IRON_BAR_ID = 2351;
    final static int IRON_PLATEBODY_ID = 1115;

    private final Walker walker = new Walker(ctx);

    public Walk(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.select().id(IRON_BAR_ID).count() == 0 && ctx.inventory.select().id(IRON_PLATEBODY_ID).count() > 0) || (ctx.inventory.select().id(IRON_BAR_ID).count() == 25 && pathToBank[0].distanceTo(ctx.players.local()) > 6);
    }

    @Override
    public void execute() {
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if(ctx.inventory.select().id(IRON_BAR_ID).count() == 0) {
                System.out.println("Walking to bank");
                if(ctx.movement.energyLevel() > 90) {
                    ctx.movement.running(true);
                }
                walker.walkPath(pathToBank);
            }
            else {
                System.out.println("Walking to anvil");
                if(ctx.movement.energyLevel() > 90) {
                    ctx.movement.running(true);
                }
                walker.walkPathReverse(pathToBank);
            }
        }
    }
}
