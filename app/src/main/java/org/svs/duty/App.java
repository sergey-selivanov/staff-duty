package org.svs.duty;

import com.google.common.base.StandardSystemProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App {

    private final static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        Properties version = new Properties();
        try(InputStream is = App.class.getResourceAsStream("/version.properties")) {
            version.load(is);
        } catch(IOException ex) {
            log.warn("cannot load properties", ex);
        }

        log.info("=================================================================");
        log.info("Staff Duty {}", version.getProperty("version", "unknown version"));
        log.info("rev      {} {}", version.getProperty("git.commit", "unknown"), version.getProperty("git.date", ""));
        log.info("built    {}", version.getProperty("build.date", "unknown"));
        log.info("built by {}", version.getProperty("build.builtby", "unknown"));
        log.info("{} {} {}", StandardSystemProperty.JAVA_VM_NAME.value(), StandardSystemProperty.JAVA_VM_VERSION.value(), StandardSystemProperty.JAVA_HOME.value());
        log.info("-----------------------------------------------------------------");

    }
}
