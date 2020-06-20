package pro.gravit.launchermodules.radon;

import pro.gravit.launcher.modules.LauncherInitContext;
import pro.gravit.launcher.modules.LauncherModule;
import pro.gravit.launcher.modules.LauncherModuleInfo;
import pro.gravit.launchserver.binary.tasks.AttachJarsTask;
import pro.gravit.launchserver.modules.events.LaunchServerFullInitEvent;
import pro.gravit.utils.Version;

public class ModuleImpl extends LauncherModule {
    public static final Version version = new Version(2, 0, 0, 2, Version.Type.LTS);
    public ModuleImpl() {
        super(new LauncherModuleInfo("Radon", version, new String[] { "LaunchServerCore"}));
    }

    @Override
    public void init(LauncherInitContext initContext) {
        registerEvent(this::finish, LaunchServerFullInitEvent.class);
    }

    public void finish(LaunchServerFullInitEvent event) {
        event.server.launcherBinary.add(t -> t instanceof AttachJarsTask, new RadonBuildTask(event.server, this));
    }
}
