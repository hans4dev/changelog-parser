package pdf.changelog;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChangeLogEntry {

	private Date date;
	private String title;
	private String description;
	private int page;
	private Set<String> tags;
	
	public ChangeLogEntry() {
		this.tags = new HashSet<String>();
	}

	public ChangeLogEntry(Date date, int page) {
		this();
		this.date = date;
		this.page = page;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	

	@Override
	public String toString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return String.format("[%s] %s: %s (p. %d)", df.format(date), title, description, page);
	}
	
	public void appendDescription(String appendText) {
		if (this.description == null || this.description.isEmpty()) {
			this.description = appendText;
		} else {
			this.description += appendText;
		}
	}
	
	public boolean isValid() {
		return (date != null && title != null && !title.isEmpty() && description != null && !description.isEmpty());
	}

	public void setTags(Collection<String> tags) {
		this.tags.clear();
		this.tags.addAll(tags);
	}
	public Set<String> getTags() {
		return tags;
	}
	
	public static Collection<String> parseTags(String tags) {
		String[] tagArray = tags.split(",");
		return Arrays.asList(tagArray);		
	}
	public static String formatTags(Collection<String> tags) {
		if (tags != null) {
			StringBuilder sb = new StringBuilder();
			for (String s : tags) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(s);
			} // for each tag
			return sb.toString();
		}
		return "";
	}
}
