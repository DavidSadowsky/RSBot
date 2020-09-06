package AutoSmelter;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class WalkBronze extends Task {

    public static final Tile[] pathToBank = {new Tile(3225, 3254, 0), new Tile(3224, 3249, 0), new Tile(3224, 3244, 0), new Tile(3224, 3239, 0), new Tile(3227, 3235, 0), new Tile(3230, 3231, 0), new Tile(3231, 3226, 0), new Tile(3232, 3221, 0), new Tile(3227, 3219, 0), new Tile(3222, 3219, 0), new Tile(3217, 3219, 0), new Tile(3215, 3214, 0), new Tile(3211, 3211, 0), new Tile(3206, 3209, 0), new Tile(3205, 3209, 1), new Tile(3205, 3209, 2), new Tile(3205, 3214, 2), new Tile(3208, 3218, 2)};

    private final Walker walker = new Walker(ctx);

    public WalkBronze(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.select().count() == 14 && ctx.inventory.select().id(2349).count() > 13) || (ctx.inventory.select().count() == 28 && pathToBank[0].distanceTo(ctx.players.local()) > 6);
    }

    @Override
    public void execute() {
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if(ctx.inventory.count() == 14) {
                System.out.println("Walking to bank");
                if(ctx.movement.energyLevel() > 90) {
                    ctx.movement.running(true);
                }
                walker.walkPath(pathToBank);
            }
            else {
                System.out.println("Walking to furnace");
                if(ctx.movement.energyLevel() > 90) {
                    ctx.movement.running(true);
                }
                walker.walkPathReverse(pathToBank);
            }
        }
    }
}
