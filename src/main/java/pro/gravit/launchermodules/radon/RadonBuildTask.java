package pro.gravit.launchermodules.radon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import me.itzsomebody.radon.Radon;
import me.itzsomebody.radon.config.Configuration;
import me.itzsomebody.radon.config.ObfuscationConfiguration;
import pro.gravit.launchserver.LaunchServer;
import pro.gravit.launchserver.binary.tasks.LauncherBuildTask;
import pro.gravit.utils.helper.IOHelper;
import pro.gravit.utils.helper.UnpackHelper;

public class RadonBuildTask implements LauncherBuildTask {
    private final LaunchServer srv;
    public final Path config;

    public RadonBuildTask(LaunchServer srv) {
        this.srv = srv;
        config = this.srv.dir.resolve("radon.yml");
    }

    @Override
    public String getName() {
        return "Radon";
    }

    @Override
    public Path process(Path inputFile) throws IOException {
    	Path outputFile = srv.launcherBinary.nextLowerPath(this);
    	Files.deleteIfExists(outputFile);
    	if (!IOHelper.isFile(config)) UnpackHelper.unpack(this.getClass().getClassLoader().getResource("radonCfg.yml"), config);
    	Configuration p = new Configuration(IOHelper.newInput(config));
    	ObfuscationConfiguration info = ObfuscationConfiguration.from(p);
        info.setInput(inputFile.toFile());
        info.setOutput(outputFile.toFile());
        List<File> libs = srv.launcherBinary.coreLibs.stream().map(e -> e.toFile()).collect(Collectors.toList());
        libs.addAll(srv.launcherBinary.addonLibs.stream().map(e -> e.toFile()).collect(Collectors.toList()));
        info.setLibraries(libs);
        Radon r = new Radon(info);
        r.run();
    	return outputFile;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }
}
