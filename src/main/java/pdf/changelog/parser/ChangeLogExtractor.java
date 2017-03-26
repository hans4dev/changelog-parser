package pdf.changelog.parser;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pdf.MarginRegionFilter;
import pdf.changelog.ChangeLogEntry;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;

public class ChangeLogExtractor {

	public static int DEFAULT_X = 90;
	public static int DEFAULT_Y = 65;	
	
	private PdfReader reader;
	private PdfReaderContentParser parser;
	private RegionTextRenderFilter regionTextRenderFilter;
	private ChangeLogExtractionStrategy extractionStrategy;
	
	public ChangeLogExtractor(InputStream pdfStream, int x, int y) throws IOException {
		reader = new PdfReader(pdfStream);
		parser = new PdfReaderContentParser(reader);
		regionTextRenderFilter = MarginRegionFilter.create(reader.getPageSize(1), x, y);
		
		extractionStrategy = new ChangeLogExtractionStrategy(regionTextRenderFilter);
	}

	public ChangeLogExtractor(InputStream pdfStream) throws IOException {
		this(pdfStream, DEFAULT_X, DEFAULT_Y);
	}

	public void extractPage(int pageNum) throws IOException {
		extractionStrategy.setPage(pageNum);
		extractionStrategy = parser.processContent(pageNum, extractionStrategy);
	}
	
	public void extractPages(int pageFrom, int pageTo) throws IOException {
		for (int pageNum = pageFrom; pageNum <= pageTo; pageNum++) {
			extractPage(pageNum);
		} // for pages	
	}

	public List<ChangeLogEntry> getChangeLogEntries() {
		List<ChangeLogEntry> list = new ArrayList<ChangeLogEntry>();
		list.addAll( extractionStrategy.getChangeLogEntries() );		
		System.out.println("Found entries: " + list.size());
		
		return list;
	}
	
	public static void main(String[] args) throws IOException, SQLException {
				
	}

}
