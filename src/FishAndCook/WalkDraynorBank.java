package FishAndCook;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class WalkDraynorBank extends Task {

    public static final Tile[] pathToBank = {new Tile(3086, 3231, 0), new Tile(3087, 3234, 0), new Tile(3087, 3237, 0), new Tile(3087, 3240, 0), new Tile(3087, 3243, 0), new Tile(3087, 3246, 0), new Tile(3090, 3247, 0), new Tile(3092, 3244, 0)};
    final static int SHRIMP_ID = 317;

    private final Walker walker = new Walker(ctx);

    public WalkDraynorBank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ((ctx.inventory.select().id(SHRIMP_ID).count() == 0 && pathToBank[pathToBank.length - 1].distanceTo(ctx.players.local()) < 6) || (ctx.inventory.select().count() == 28 && pathToBank[pathToBank.length - 1].distanceTo(ctx.players.local()) > 6));
    }

    @Override
    public void execute() {
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if(ctx.inventory.select().count() == 28) {
                System.out.println("Walking to bank...");
                if(ctx.movement.energyLevel() > 90) {
                    ctx.movement.running(true);
                }
                walker.walkPath(pathToBank);
            }
            else {
                System.out.println("Walking to fishing spots...");
                if(ctx.movement.energyLevel() > 90) {
                    ctx.movement.running(true);
                }
                walker.walkPathReverse(pathToBank);
            }
        }
    }
}
