/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package pdf.changelog.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import pdf.StyledText;
import pdf.StyledTextExtractionStrategy;
import pdf.TextStyle;
import pdf.changelog.ChangeLogEntry;

import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextRenderInfo;


/**
 * A simple text extraction renderer.
 * 
 * This renderer keeps track of the current Y position of each string.  If it detects
 * that the y position has changed, it inserts a line break into the output.  If the
 * PDF renders text in a non-top-to-bottom fashion, this will result in the text not
 * being a true representation of how it appears in the PDF.
 * 
 * This renderer also uses a simple strategy based on the font metrics to determine if
 * a blank space should be inserted into the output.
 * 
 * @since	2.1.5
 */
public class ChangeLogExtractionStrategy extends StyledTextExtractionStrategy {

	private static final Logger LOGGER = LogManager.getLogger(ChangeLogExtractionStrategy.class);
	
	private static final String DATE_REGEX = "\\d{2}\\.\\d{2}\\.\\d{4}";
	private static final String DATE_FORMAT = "dd.MM.yyyy";
	private DateFormat df;
	private Date currentDate;

	private int page;

	private ArrayList<ChangeLogEntry> changeLogEntries;
	private ChangeLogEntry currentEntry;

	
	public ChangeLogExtractionStrategy(RenderFilter renderFilter) {
		super(renderFilter);
		
		this.changeLogEntries = new ArrayList<ChangeLogEntry>();		
		
		this.df = new SimpleDateFormat(DATE_FORMAT);		
		this.currentDate = new Date();
		
		this.page = 0;
	}

	/**
     * Creates a new text extraction renderer.
     */
    public ChangeLogExtractionStrategy() {
    }

    
    /**
     * @since 5.0.1
     */
    public void beginTextBlock() {
    	LOGGER.trace("beginTextBlock");
    }

    /**
     * @since 5.0.1
     */
    public void endTextBlock() {
    	LOGGER.trace("endTextBlock");
    }
    

    /**
     * Captures text using a simplified algorithm for inserting hard returns and spaces
     * @param	renderInfo	render info
     */
    public void renderText(TextRenderInfo renderInfo) {
    	super.renderText(renderInfo);    	
    }
    	

    @Override
    protected void endTextLine() {    	
    	//super.endTextLine();
    	   
    	if (currentEntry == null) {
    		currentEntry = new ChangeLogEntry(currentDate, page);
    	}
    	
    	if (currentStyledText != null && currentStyledText.getText() != null && currentStyledText.getStyle() != null) {    		
    		
    		String text = currentStyledText.getText().toString().trim();
    		TextStyle style = currentStyledText.getStyle();
    		
    		// check date
    		if (text.matches(DATE_REGEX)) {
    			try {
					currentDate = df.parse(text);
					if (currentEntry.isValid()) {
						changeLogEntries.add(currentEntry);
						currentEntry = new ChangeLogEntry(currentDate, page);
					}
					currentEntry.setDate(currentDate);					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		} // if date
    		// check heading
    		else if (style.getFontSize() > 12 || style.getFontName().endsWith("Bold")) {
				if (currentEntry.isValid()) {
					changeLogEntries.add(currentEntry);
					currentEntry = new ChangeLogEntry(currentDate, page);
				}
				currentEntry.setTitle(text);
    		} // if heading
    		else {
				currentEntry.appendDescription(text);
    		} // normal text

    		currentStyledText = new StyledText();
    		
    	} // if text and style available
    }
    

    public void setPage(int page) {
    	this.page = page;
    }
    public int getPage() {
		return page;
	}    
    
    public List<ChangeLogEntry> getChangeLogEntries() {    	
    	return changeLogEntries;
	}
    
    

}