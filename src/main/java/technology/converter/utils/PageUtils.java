package technology.converter.utils;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageUtils {

    public static List<Page> getAreasFromPages(String path, float top, float left, float bottom, float right) throws IOException {
        List<Page> pages = getPages(path);
        List<Page> areasFromPages = new ArrayList<>();
        for(Page page : pages)
            areasFromPages.add(page.getArea(top, left, bottom, right));

        return areasFromPages;
    }

    public static List<Page> getPages(String path) throws IOException {
        ObjectExtractor oe = null;
        try {
            PDDocument document = PDDocument
                    .load(new File(path));
            oe = new ObjectExtractor(document);
            List<Page> pages = new ArrayList<>();
            for (int i = 1 ; i <= document.getNumberOfPages(); i++) {
                pages.add(oe.extract(i));
            }

            return pages;
        } finally {
            if (oe != null)
                oe.close();
        }
    }
}
