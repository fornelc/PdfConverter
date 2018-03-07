package technology.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.converter.extractors.SpreadsheetExtractionAlgorithm;
import technology.converter.utils.Page;
import technology.converter.utils.PageUtils;
import technology.converter.utils.Table;
import technology.converter.writers.CSVWriter;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Converter {
    private static final Logger LOG = LoggerFactory.getLogger(Converter.class);

    private static final String REGEX_PATTERN = "^-?\\d*.[+]?\\d*(\\,\\d+)?";

    public void executeConvert (File file) throws IOException {
        List<Table> tables = getTableTrading(file.getAbsolutePath());
        StringBuilder sb = new StringBuilder();
        (new CSVWriter()).write(sb, tables);
        String s = sb.toString();
        String[] lines = s.split("\\r?\\n");
        saveCsv(lines, file.getName());
    }

    public File[] getPDFs() {
        String path = getPath();

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        return listOfFiles;
    }

    private String getPath() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = Converter.class.getResourceAsStream("/config.properties");

            // load a properties file
            prop.load(input);

            return prop.getProperty("path");
        } catch (IOException ex) {
            LOG.error("Exception getting properties file", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOG.error("Exception closing the input stream", e);
                }
            }
        }

        return "";
    }

    private List<Table> getTableTrading(String pdf) throws IOException {
        List<Page> pages = PageUtils.getAreasFromPages(pdf, 124.875f, 12.75f, 790.5f, 561f);
        SpreadsheetExtractionAlgorithm bea = new SpreadsheetExtractionAlgorithm();
        List<Table> tables = new ArrayList<>();
        for(Page page : pages)
            tables.add(bea.extract(page).get(0));
        return tables;
    }

    private void saveCsv(String[] lines, String fileName) throws IOException {
        String output = buildOutput(fileName);
        BufferedWriter br = new BufferedWriter(new FileWriter(output));
        StringBuilder sb = new StringBuilder();
        for (String element : lines) {
            try {
                String value = element.split(" ")[0];
                value = value.replace("\"", "");
                String number = checknumeric(value);
                if(number != null) {
                    sb.append(value);
                    sb.append("\n");
                }
            } catch (Exception ex) {
                System.out.println("error");
            }

        }

        br.write(sb.toString());
        br.close();
    }

    private String checknumeric(String str){
        String numericString = null;
        String temp;
        /*if(str.startsWith("-")){ //checks for negative values
            temp=str.substring(1);
            if(temp.matches(REGEX_PATTERN)){
                numericString=str;
            }
        }*/
        if(str.matches(REGEX_PATTERN)) {
            numericString=str;
        }
        return numericString;
    }

    private String buildOutput(String fileName) {
        String path = System.getProperty("user.home") + "/outputPDFs/";
        checkDirectory(path);
        StringBuilder sBuilder = new StringBuilder(fileName);
        sBuilder.deleteCharAt(fileName.indexOf(".pdf"));
        String formatDateTime = getDateFormat();
        sBuilder.append(formatDateTime).append(".txt");
        String output = path + sBuilder;
        System.out.println(output);
        return output;
    }

    private void checkDirectory(String path) {
        File directory = new File(path);
        if(! directory.exists()) {
            directory.mkdir();
        }
    }

    private String getDateFormat() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" dd-MM-yyyy HH:mm:ss");
        String formatDateTime = now.format(formatter);

        return formatDateTime;
    }

}
