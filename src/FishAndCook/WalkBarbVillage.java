package FishAndCook;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Skills;

public class WalkBarbVillage extends Task {

    public static final Tile[] pathToBarbVillage = {new Tile(3208, 3220, 2), new Tile(3206, 3215, 2), new Tile(3206, 3210, 2), new Tile(3206, 3208, 1), new Tile(3206, 3208, 0), new Tile(3207, 3213, 0), new Tile(3203, 3216, 0), new Tile(3198, 3218, 0), new Tile(3195, 3222, 0), new Tile(3191, 3225, 0), new Tile(3187, 3229, 0), new Tile(3182, 3230, 0), new Tile(3177, 3233, 0), new Tile(3173, 3237, 0), new Tile(3168, 3240, 0), new Tile(3163, 3241, 0), new Tile(3160, 3245, 0), new Tile(3156, 3249, 0), new Tile(3151, 3249, 0), new Tile(3148, 3253, 0), new Tile(3143, 3255, 0), new Tile(3142, 3260, 0), new Tile(3139, 3265, 0), new Tile(3136, 3269, 0), new Tile(3134, 3274, 0), new Tile(3134, 3279, 0), new Tile(3134, 3284, 0), new Tile(3134, 3289, 0), new Tile(3130, 3292, 0), new Tile(3126, 3295, 0), new Tile(3124, 3300, 0), new Tile(3124, 3305, 0), new Tile(3123, 3310, 0), new Tile(3123, 3315, 0), new Tile(3125, 3320, 0), new Tile(3130, 3323, 0), new Tile(3133, 3328, 0), new Tile(3135, 3333, 0), new Tile(3135, 3338, 0), new Tile(3135, 3343, 0), new Tile(3134, 3348, 0), new Tile(3135, 3353, 0), new Tile(3132, 3357, 0), new Tile(3131, 3362, 0), new Tile(3131, 3367, 0), new Tile(3130, 3372, 0), new Tile(3127, 3376, 0), new Tile(3124, 3380, 0), new Tile(3120, 3384, 0), new Tile(3116, 3387, 0), new Tile(3112, 3390, 0), new Tile(3107, 3392, 0), new Tile(3105, 3397, 0), new Tile(3104, 3402, 0), new Tile(3104, 3407, 0), new Tile(3101, 3411, 0), new Tile(3098, 3415, 0), new Tile(3098, 3420, 0), new Tile(3099, 3425, 0), new Tile(3101, 3430, 0), new Tile(3106, 3433, 0)};

    final int FLY_FISHING_ROD = 309;
    final int FEATHER = 314;

    private final Walker walker = new Walker(ctx);
    Skills skills = new Skills(ctx);

    public WalkBarbVillage(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.skills.level(7) >= 20 && ctx.inventory.select().id(FLY_FISHING_ROD).count() == 1 && ctx.inventory.select().id(FEATHER).count() > 0;
    }

    @Override
    public void execute() {
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            walker.walkPath(pathToBarbVillage);
        }
    }
}
