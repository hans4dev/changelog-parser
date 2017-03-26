package pdf.changelog;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class SqliteChangeLogExporter implements ChangeLogExporter {

	private File sqliteFile;
	
	public SqliteChangeLogExporter(File sqliteFile) throws SQLException {
		this.sqliteFile = sqliteFile;
	}
	
	/* (non-Javadoc)
	 * @see pdf.changelog.ChangeLogExporter#export(java.util.Collection)
	 */
	public long export(Collection<ChangeLogEntry> changeLogEntries) throws ChangeLogExportException {
		try {
			Connection connection = ConnetcionFactory.createSQLiteConnection(sqliteFile);
			
			ChangeLogDAO dao = new ChangeLogDAO(connection);
			long exportedCount = 0;
			for (ChangeLogEntry changeLogEntry : changeLogEntries) {
				System.out.println("\t" + changeLogEntry);
				exportedCount += dao.saveChangeLogEntry(changeLogEntry);
			}			
			connection.close();
			
			return exportedCount;
			
		} catch (ConnectionException e) {
			throw new ChangeLogExportException(e);
		} catch (SQLException e) {
			throw new ChangeLogExportException(e);
		}		
		
	}
}
