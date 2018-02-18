package extractors;

import utils.Page;
import utils.Table;

import java.util.List;

public interface ExtractionAlgorithm {

    List<? extends Table> extract(Page page);
    String toString();
    
}
