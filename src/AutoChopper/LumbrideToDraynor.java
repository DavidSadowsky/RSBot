package AutoChopper;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Skills;

public class LumbrideToDraynor extends Task {

    public static final Tile[] pathToDraynor = {new Tile(3194, 3217, 0), new Tile(3189, 3216, 0), new Tile(3184, 3216, 0), new Tile(3179, 3215, 0), new Tile(3174, 3215, 0), new Tile(3169, 3218, 0), new Tile(3166, 3222, 0), new Tile(3161, 3222, 0), new Tile(3157, 3225, 0), new Tile(3152, 3228, 0), new Tile(3147, 3229, 0), new Tile(3142, 3229, 0), new Tile(3137, 3229, 0), new Tile(3132, 3229, 0), new Tile(3127, 3228, 0), new Tile(3122, 3228, 0), new Tile(3117, 3228, 0), new Tile(3112, 3230, 0), new Tile(3108, 3234, 0), new Tile(3103, 3235, 0), new Tile(3102, 3240, 0)};


    private final Walker walker = new Walker(ctx);
    Skills skills = new Skills(ctx);

    public LumbrideToDraynor(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.skills.level(8) == 15;
    }

    @Override
    public void execute() {
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            walker.walkPath(pathToDraynor);
        }
    }
}
