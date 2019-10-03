package pro.gravit.launchermodules.radon;

import pro.gravit.launcher.modules.LauncherInitContext;
import pro.gravit.launcher.modules.LauncherModule;
import pro.gravit.launchserver.binary.tasks.AttachJarsTask;
import pro.gravit.launchserver.binary.tasks.TaskUtil;
import pro.gravit.launchserver.modules.events.LaunchServerFullInitEvent;
import pro.gravit.launchserver.modules.impl.LaunchServerInitContext;
import pro.gravit.utils.Version;

public class ModuleImpl extends LauncherModule {
    public static final Version version = new Version(0, 1, 0, 0, Version.Type.BETA);

    @Override
    public void init(LauncherInitContext initContext) {
        registerEvent(this::finish, LaunchServerFullInitEvent.class);
        if(initContext != null)
        {
            if(initContext instanceof LaunchServerInitContext)
            {
                finish(new LaunchServerFullInitEvent(((LaunchServerInitContext) initContext).server));
            }
        }
    }
  
    public boolean certCheck = false;

    public void finish(LaunchServerFullInitEvent event) {
        certCheck = event.server.modulesManager.containsModule("JarSigner");
        TaskUtil.add(event.server.launcherBinary.tasks, t -> t instanceof AttachJarsTask, new RadonBuildTask(event.server));
    }

    public static void main(String[] args) {
        System.err.println("This is module, use with GravitLauncher`s LaunchServer.");
    }
}
