package org.svs.duty;

import org.apache.commons.cli.CommandLine;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.IntStream;

public class DataProcessor {
    private final Logger log = LoggerFactory.getLogger(DataProcessor.class);

    public void process(CommandLine cmdLine) throws AppException {

        var configFile = Path.of(cmdLine.getOptionValue(App.OPT_CONFIG));
        log.info("Config: [{}]", configFile.toAbsolutePath());
        if(!Files.exists(configFile)) {
            throw new AppException("File does not exist: [" + configFile.toAbsolutePath() + "]");
        }

        Properties config = new Properties();
        try(var br = Files.newBufferedReader(configFile)) {
            config.load(br);
        }
        catch (IOException | IllegalArgumentException ex) {
            log.debug("Failed", ex);
            throw new AppException("Failed to load ["
                    + configFile.toAbsolutePath()
                    + "]: "
                    + ex.getMessage());
        }

        config.entrySet().forEach(e -> log.debug("= {}: {}", e.getKey(), e.getValue()));

        var excelFile = Path.of(cmdLine.getOptionValue(App.OPT_FILE));
        log.info("Data: [{}]", excelFile.toAbsolutePath());
        if(!Files.exists(excelFile)) {
            throw new AppException("File does not exist: [" + excelFile.toAbsolutePath() + "]");
        }

        try (Workbook wb = WorkbookFactory.create(excelFile.toFile())) { // TODO saves back to this file? use inputstream?
            // https://poi.apache.org/components/spreadsheet/quick-guide.html#CellContents
            var sheet = wb.getSheetAt(0);
            int colIndex = CellReference.convertColStringToIndex("I");
            IntStream.rangeClosed(3, 62).forEach(i -> {
                var cell = sheet.getRow(i).getCell(colIndex);
                var val = cell.getStringCellValue();
                log.debug("= {}", val);
                cell.setCellValue(val + " u");
            });

            try(var os = Files.newOutputStream(Path.of("c:\\tmp\\staff duty\\out.xlsx"))) {
                wb.write(os);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
