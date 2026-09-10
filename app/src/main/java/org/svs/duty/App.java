package org.svs.duty;

import com.google.common.base.StandardSystemProperty;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Properties;

public class App {

    private final static Logger log = LoggerFactory.getLogger(App.class);

    public final static String OPT_IN = "in";
    public final static String OPT_HELP = "h";

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

        Options options = new Options();

        options
                .addOption(Option.builder()
                        .option(OPT_IN)
                        .hasArg()
                        .required()
                        .desc("Input file: xml file from Xperia Companion backup, or vcf file")
                        .get())
                .addOption(Option.builder()
                        .option(OPT_HELP)
                        .longOpt("help")
                        .desc("Print usage")
                        .build());

        // Do not parse options for -h to avoid error on missing required arguments
        if(Arrays.stream(args).anyMatch(s -> "-h".equals(s) || "--help".equals(s))) {
            printUsage(options);
            return;
        }

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmdline = parser.parse(options, args);
            //new DataExtractor().process(cmdline);
            log.info("Done.");
        } catch (ParseException ex) {
            log.error("Invalid arguments: {}", ex.getMessage());
            printUsage(options);
            System.exit(1);
//        } catch (AppException ex) {
//            log.error("Failed: {}", ex.getMessage());
//            System.exit(1);
        } catch (Exception ex) {
            log.error("Failed", ex);
            System.exit(1);
        }
    }

    private static void printUsage(Options options){
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter pw = new PrintWriter(baos)) {
            var hf = HelpFormatter.builder()
                    .setPrintWriter(pw)
                    .get();
            hf.setOptionComparator(null);
            hf.printHelp("xperia-export", options, true);
            var text = baos.toString();
            log.info("");
            // this will print Arrays.java as a source file in the log
            //Arrays.asList(text.split(System.lineSeparator())).forEach(log::info);
            Arrays.asList(text.split(System.lineSeparator())).forEach(s -> log.info(s));
        }
        catch(IOException ex) {
            log.error("Failed", ex);
        }
    }
}
