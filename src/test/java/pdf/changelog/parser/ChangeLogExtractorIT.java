package pdf.changelog.parser;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import pdf.changelog.ChangeLogEntry;
import pdf.changelog.ChangeLogExportException;
import pdf.changelog.ChangeLogExporter;
import pdf.changelog.SqliteChangeLogExporter;
import pdf.changelog.parser.ChangeLogExtractor;

public class ChangeLogExtractorIT {

	private static final String pdfName = "Change_Log_2_3.pdf";
	private static final String sqliteName = "changeLogs.sqlite";

	@Test
	public void extract_and_store() throws IOException, SQLException, URISyntaxException, ChangeLogExportException {
		InputStream pdfStream = getClass().getClassLoader().getResourceAsStream(pdfName);
		ChangeLogExtractor extractor = new ChangeLogExtractor(pdfStream);

		final int pageFrom = 53;
		final int pageTo = 251;
		final int entriesExpected = 1494;
		
		extractor.extractPages(pageFrom, pageTo);
		List<ChangeLogEntry> changeLogEntries = extractor.getChangeLogEntries();
		
		assertThat("changelogs found", changeLogEntries, hasSize(entriesExpected));
		
		URL url = Thread.currentThread().getContextClassLoader().getResource(sqliteName);
		File sqliteFile = new File(url.getPath());
		ChangeLogExporter exporter = new SqliteChangeLogExporter(sqliteFile );
		
		long exportedCount = exporter.export(changeLogEntries);
		System.out.println("exported: " + exportedCount);
		
		assertThat("all chanelogs exported", exportedCount, is( (long) changeLogEntries.size()) );
	}

}
