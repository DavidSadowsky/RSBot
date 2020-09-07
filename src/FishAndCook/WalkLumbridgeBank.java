package FishAndCook;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class WalkLumbridgeBank extends Task {

    public static final Tile[] pathToBank = {new Tile(3211, 3215, 0), new Tile(3208, 3211, 0), new Tile(3205, 3209, 1), new Tile(3205, 3209, 2), new Tile(3205, 3214, 2), new Tile(3208, 3219, 2)};

    final static int SHRIMP_ID = 317;

    private final Walker walker = new Walker(ctx);

    public WalkLumbridgeBank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ((ctx.inventory.select().id(SHRIMP_ID).count() == 28 && pathToBank[0].distanceTo(ctx.players.local()) > 6) || (ctx.inventory.select().id(SHRIMP_ID).count() == 0 && pathToBank[pathToBank.length - 1].distanceTo(ctx.players.local()) > 6));
    }

    @Override
    public void execute() {
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if(ctx.inventory.select().id(SHRIMP_ID).count() == 0) {
                System.out.println("Walking to bank...");
                if(ctx.movement.energyLevel() > 90) {
                    ctx.movement.running(true);
                }
                walker.walkPath(pathToBank);
            }
            else {
                System.out.println("Walking to range...");
                if(ctx.movement.energyLevel() > 90) {
                    ctx.movement.running(true);
                }
                walker.walkPathReverse(pathToBank);
            }
        }
    }
}
