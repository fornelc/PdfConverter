package technology.converter.writers;

import technology.converter.utils.Table;

import java.io.IOException;
import java.util.List;

public interface Writer {
    void write(Appendable out, Table table) throws IOException;
    void write(Appendable out, List<Table> tables) throws IOException;
}
