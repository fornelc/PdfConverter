package technology.converter;

import extractors.SpreadsheetExtractionAlgorithm;
import utils.Page;
import utils.PageUtils;
import utils.Table;
import writers.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {

    private static final String REGEX_PATTERN = "\\d*.[+]?\\d*(\\,\\d+)?";

    public static void main (String[] args) {
        try {
            String path = args[0];
            if (path.endsWith(".pdf")) {
                File file = new File(path);
                System.out.println("File pdf: " + file.getAbsolutePath());
                executeConvert(file);
            }else {
                File[] files = getPDFs(path);
                for (File file : files) {
                   executeConvert(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void executeConvert (File file) throws IOException{
        List<Table> tables = getTableTrading(file.getAbsolutePath());
        StringBuilder sb = new StringBuilder();
        (new CSVWriter()).write(sb, tables);
        String s = sb.toString();
        String[] lines = s.split("\\r?\\n");
        saveCsv(lines, file.getName());
    }

    private static File[] getPDFs(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        return listOfFiles;
    }

    private static List<Table> getTableTrading(String pdf) throws IOException {
        List<Page> pages = PageUtils.getAreasFromPages(pdf, 124.875f, 12.75f, 790.5f, 561f);
        SpreadsheetExtractionAlgorithm bea = new SpreadsheetExtractionAlgorithm();
        List<Table> tables = new ArrayList<>();
        for(Page page : pages)
            tables.add(bea.extract(page).get(0));
        return tables;
    }

    private static void saveCsv(String[] lines, String fileName) throws IOException {
        BufferedWriter br = new BufferedWriter(new FileWriter(buildOutput(fileName)));
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

    public static String checknumeric(String str){
        String numericString = null;
        String temp;
        if(str.startsWith("-")){ //checks for negative values
            temp=str.substring(1);
            if(temp.matches(REGEX_PATTERN)){
                numericString=str;
            }
        }
        if(str.matches(REGEX_PATTERN)) {
            numericString=str;
        }
        return numericString;
    }

    private static String buildOutput(String fileName) {
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

    private static void checkDirectory(String path) {
        File directory = new File(path);
        if(! directory.exists()) {
            directory.mkdir();
        }
    }

    private static String getDateFormat() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" dd-MM-yyyy HH:mm:ss");
        String formatDateTime = now.format(formatter);

        return formatDateTime;
    }
}
