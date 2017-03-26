package pdf.changelog.parser;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import pdf.changelog.ChangeLogEntry;
import pdf.changelog.parser.ChangeLogExtractor;

public class ChangeLogExtractorTest {
	
	private static final String pdfName = "Change_Log_2_3.pdf";

	@Test
	public void extract_pages() throws IOException {

		InputStream pdfStream = getClass().getClassLoader().getResourceAsStream(pdfName);
		ChangeLogExtractor extractor = new ChangeLogExtractor(pdfStream);

		final int pageFrom = 53;
		final int pageTo = 251;
		final int entriesExpected = 1494;
		
		extractor.extractPages(pageFrom, pageTo);
		List<ChangeLogEntry> changeLogEntries = extractor.getChangeLogEntries();
		
		assertThat("changelogs found", changeLogEntries, hasSize(entriesExpected));
	}
	
	@Test
	public void extract_page() throws Exception {

		InputStream pdfStream = getClass().getClassLoader().getResourceAsStream(pdfName);
		ChangeLogExtractor extractor = new ChangeLogExtractor(pdfStream);

		final int pageNum = 53;
		final int entriesExpected = 4;
				
		extractor.extractPage(pageNum);
		List<ChangeLogEntry> changeLogEntries = extractor.getChangeLogEntries();
		
		assertThat("changelogs found", changeLogEntries, hasSize(entriesExpected));		
		
	}

}
