package technology.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main (String[] args) {
        Converter converter = new Converter();
        File[] files = converter.getPDFs();

        try {
            for(File file : files) {
                if(file.getName().endsWith(".pdf"))
                    converter.executeConvert(file);
            }
        } catch (IOException e) {
           LOG.error("Exception converting files.", e);
        }

        LOG.info("Converter process finished succesfully.");
    }


}
