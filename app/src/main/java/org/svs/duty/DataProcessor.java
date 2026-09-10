package org.svs.duty;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class DataProcessor {
    private final Logger log = LoggerFactory.getLogger(DataProcessor.class);

    public void process(CommandLine cmdLine) throws AppException {

        var configFile = Path.of(cmdLine.getOptionValue(App.OPT_CONFIG));
        log.info("Config: [{}]", configFile.toAbsolutePath());
        if(!Files.exists(configFile)) {
            throw new AppException("File does not exist: [" + configFile.toAbsolutePath() + "]");
        }

        Properties config = new Properties();
        try(var is = Files.newInputStream(configFile)) {
            config.load(is);
        }
        catch (IOException | IllegalArgumentException ex) {
            log.debug("Failed", ex);
            throw new AppException("Failed to load ["
                    + configFile.toAbsolutePath()
                    + "]: "
                    + ex.getMessage());
        }

    }
}
