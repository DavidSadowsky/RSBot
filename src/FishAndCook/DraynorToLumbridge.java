package FishAndCook;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Skills;

public class DraynorToLumbridge extends Task {

    public static final Tile[] pathToLumbridge = {new Tile(3086, 3230, 0), new Tile(3091, 3230, 0), new Tile(3096, 3230, 0), new Tile(3101, 3229, 0), new Tile(3106, 3227, 0), new Tile(3111, 3228, 0), new Tile(3116, 3228, 0), new Tile(3121, 3228, 0), new Tile(3126, 3228, 0), new Tile(3131, 3228, 0), new Tile(3136, 3228, 0), new Tile(3141, 3228, 0), new Tile(3146, 3229, 0), new Tile(3151, 3229, 0), new Tile(3156, 3229, 0), new Tile(3161, 3228, 0), new Tile(3166, 3227, 0), new Tile(3171, 3226, 0), new Tile(3176, 3226, 0), new Tile(3181, 3226, 0), new Tile(3186, 3225, 0), new Tile(3191, 3223, 0), new Tile(3196, 3223, 0), new Tile(3198, 3218, 0), new Tile(3203, 3218, 0), new Tile(3206, 3214, 0), new Tile(3205, 3209, 1), new Tile(3205, 3209, 2), new Tile(3205, 3214, 2), new Tile(3208, 3218, 2)};


    private final Walker walker = new Walker(ctx);
    Skills skills = new Skills(ctx);

    public DraynorToLumbridge(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.skills.level(10) == 20;
    }

    @Override
    public void execute() {
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            walker.walkPath(pathToLumbridge);
        }
    }
}
