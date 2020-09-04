package AutoMiner;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;

public class WalkPortSarimBox extends Task {

    public static final Tile[] pathToDepositBox = {new Tile(2969, 3241, 0), new Tile(2974, 3241, 0), new Tile(2979, 3241, 0), new Tile(2982, 3237, 0), new Tile(2986, 3234, 0), new Tile(2991, 3234, 0), new Tile(2996, 3236, 0), new Tile(3000, 3239, 0), new Tile(3005, 3239, 0), new Tile(3010, 3239, 0), new Tile(3015, 3241, 0), new Tile(3020, 3242, 0), new Tile(3025, 3242, 0), new Tile(3028, 3238, 0), new Tile(3033, 3237, 0), new Tile(3038, 3237, 0), new Tile(3043, 3236, 0)};

    private final Walker walker = new Walker(ctx);

    public WalkPortSarimBox(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() < 28 && pathToDepositBox[0].distanceTo(ctx.players.local()) > 6);
    }

    @Override
    public void execute() {
        if(ctx.movement.energyLevel() > 50 && !(ctx.movement.running(true))) {
            System.out.println("Turning on run");
            ctx.movement.running(true);
        }
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if(ctx.inventory.count() == 28) {
                walker.walkPath(pathToDepositBox);
                ctx.camera.angle(270);
                if(ctx.game.tab() != Game.Tab.INVENTORY) {
                    ctx.game.tab(Game.Tab.INVENTORY);
                }
            }
            else {
                walker.walkPathReverse(pathToDepositBox);
                ctx.camera.angle(90);
            }
        }
    }
}
