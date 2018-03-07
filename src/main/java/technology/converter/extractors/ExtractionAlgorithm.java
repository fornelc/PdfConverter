package technology.converter.extractors;

import technology.converter.utils.Page;
import technology.converter.utils.Table;

import java.util.List;

public interface ExtractionAlgorithm {

    List<? extends Table> extract(Page page);
    String toString();
    
}
