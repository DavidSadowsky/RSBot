package AutoChopper;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class WalkLumbridgeBank extends Task {

    public static final Tile[] pathToBank = {new Tile(3189, 3219, 0), new Tile(3194, 3217, 0), new Tile(3199, 3218, 0), new Tile(3203, 3214, 0), new Tile(3208, 3212, 0), new Tile(3205, 3209, 1), new Tile(3205, 3209, 2), new Tile(3205, 3214, 2), new Tile(3208, 3219, 2)};

    private final Walker walker = new Walker(ctx);

    public WalkLumbridgeBank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() == 0 && pathToBank[0].distanceTo(ctx.players.local()) > 6);
    }

    @Override
    public void execute() {
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if(ctx.inventory.count() == 28) {
                walker.walkPath(pathToBank);
            }
            else {
                walker.walkPathReverse(pathToBank);
            }
        }
    }
}
