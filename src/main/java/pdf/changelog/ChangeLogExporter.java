package pdf.changelog;

import java.util.Collection;

public interface ChangeLogExporter {

	public abstract long export(Collection<ChangeLogEntry> changeLogEntries)
			throws ChangeLogExportException;

}