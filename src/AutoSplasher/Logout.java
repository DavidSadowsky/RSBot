package AutoSplasher;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;

public class Logout extends Task{
    public Logout(ClientContext ctx) {
        super(ctx);
    }

    final static int FIRE_RUNE = 1;
    final static int AIR_RUNE = 1;
    final static int MIND_RUNE = 1;

    @Override
    public boolean activate() {
        return ctx.inventory.select().id(FIRE_RUNE).isEmpty() || ctx.inventory.select().id(AIR_RUNE).isEmpty() || ctx.inventory.select().id(MIND_RUNE).isEmpty();
    }

    @Override
    public void execute() {
        Game game = new Game(ctx);
        game.logout();
    }
}
