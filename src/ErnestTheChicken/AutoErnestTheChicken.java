package ErnestTheChicken;

import ErnestTheChicken.Task;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Script.Manifest(name="AutoErnestTheChicken", description="Completes The Ernest the Chicken Quest", properties = "author=David Sadowsky; topic=999; client=4;")

public class AutoErnestTheChicken extends PollingScript<ClientContext>  {
    List<Task> taskList = new ArrayList<Task>();
    boolean hasStarted = false;

    @Override
    public void start() {
        taskList.add(new Quest(ctx));
    }

    @Override
    public void poll() {
        if(!ctx.movement.running(true)) {
            ctx.movement.running(true);
            System.out.println("Turning on run...");
        }
        for(Task task : taskList) {
            if(!hasStarted) {
                hasStarted = true;
                task.execute();
            }
        }
    }
}
