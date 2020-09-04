package AutoMiner;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class WalkLumbridgeBank extends Task {

    public static final Tile[] pathToBank = {new Tile(3227, 3148, 0), new Tile(3231, 3151, 0), new Tile(3231, 3156, 0), new Tile(3232, 3161, 0), new Tile(3232, 3166, 0), new Tile(3234, 3171, 0), new Tile(3234, 3176, 0), new Tile(3237, 3180, 0), new Tile(3239, 3185, 0), new Tile(3243, 3189, 0), new Tile(3242, 3194, 0), new Tile(3240, 3199, 0), new Tile(3236, 3203, 0), new Tile(3236, 3208, 0), new Tile(3236, 3213, 0), new Tile(3233, 3217, 0), new Tile(3228, 3218, 0), new Tile(3223, 3218, 0), new Tile(3218, 3218, 0), new Tile(3215, 3214, 0), new Tile(3211, 3211, 0), new Tile(3206, 3209, 0), new Tile(3205, 3209, 1), new Tile(3205, 3209, 2), new Tile(3205, 3214, 2), new Tile(3208, 3218, 2)};

    private final Walker walker = new Walker(ctx);

    public WalkLumbridgeBank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() < 28 && pathToBank[0].distanceTo(ctx.players.local()) > 6);
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
