package technology.converter.writers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import technology.converter.utils.RectangularTextContainer;
import technology.converter.utils.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVWriter implements Writer {

	public CSVWriter() {
		this(CSVFormat.EXCEL);
	}

	protected CSVWriter(CSVFormat format) {
		this.format = format;
	}

	private final CSVFormat format;

	@Override
	public void write(Appendable out, Table table) throws IOException {
		write(out, Collections.singletonList(table));
	}

	@Override
	public void write(Appendable out, List<Table> tables) throws IOException {
		try (CSVPrinter printer = new CSVPrinter(out, format)) {
			for (Table table : tables) {
				for (List<RectangularTextContainer> row : table.getRows()) {
					List<String> cells = new ArrayList<>(1);
					cells.add(row.get(4).getText());
					printer.printRecord(cells);
				}
			}
			printer.flush();
		}
	}

}
