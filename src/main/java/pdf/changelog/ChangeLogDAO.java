package pdf.changelog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChangeLogDAO {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private static Connection connection;
	
	private static final int SQL_COL_ID = 1;
	private static final int SQL_COL_DATE = 2;
	private static final int SQL_COL_TITLE = 3;
	private static final int SQL_COL_TEXT = 4;
	private static final int SQL_COL_PAGE = 5;
	private static final int SQL_COL_TAGS = 6;
	
	private static final String SQL_CREATE = "CREATE TABLE ChangeLogEntries (id INTEGER, date TEXT, title TEXT, text TEXT, page INTEGER, tags TEXT);";
	private static final String SQL_MAXID = "SELECT MAX(id) FROM ChangeLogEntries";
	private static final String SQL_SELECT = "SELECT id, date, title, text, page, tags FROM ChangeLogEntries";
	private static final String SQL_INSERT = "INSERT INTO ChangeLogEntries VALUES(?, ?, ?, ?, ?, ?)";
	
	
	private static PreparedStatement psMAXID;
	private static PreparedStatement psSELECT;	
	private static PreparedStatement psINSERT;
		
	public ChangeLogDAO(Connection connection) throws SQLException {
		this.connection = connection;
		
		try {
			connection.createStatement().executeQuery(SQL_MAXID);
		} catch (SQLException e) {
			connection.createStatement().execute(SQL_CREATE);
		}
		
		prepareStatements();
	}
	
	private void prepareStatements() throws SQLException {
		psMAXID = connection.prepareStatement(SQL_MAXID);
		psSELECT = connection.prepareStatement(SQL_SELECT);
		psINSERT = connection.prepareStatement(SQL_INSERT);
	}
	
	public int saveChangeLogEntry(ChangeLogEntry changeLogEntry) throws SQLException {
		int newId = getNextId();
		psINSERT.setInt(SQL_COL_ID, newId);
		psINSERT.setString(SQL_COL_DATE, dateFormat.format( changeLogEntry.getDate().getTime() ));
		psINSERT.setString(SQL_COL_TITLE, changeLogEntry.getTitle());
		psINSERT.setString(SQL_COL_TEXT, changeLogEntry.getDescription());
		psINSERT.setInt(SQL_COL_PAGE, changeLogEntry.getPage());
		psINSERT.setString(SQL_COL_TAGS, ChangeLogEntry.formatTags(changeLogEntry.getTags()));
		
		return psINSERT.executeUpdate();		
	}

	private static int getNextId() throws SQLException {
		ResultSet rs = psMAXID.executeQuery();
		if (rs != null && rs.next()) {
			return rs.getInt(SQL_COL_ID) + 1;
		}
		return 0;
	}
	
	public List<ChangeLogEntry> getChangeLogEntries() throws SQLException {
		ResultSet rs = psSELECT.executeQuery();
		List<ChangeLogEntry> list = new  ArrayList<ChangeLogEntry>();
		while(rs.next()) {
			ChangeLogEntry entry = new ChangeLogEntry();
			try {
				entry.setDate(dateFormat.parse( rs.getString(SQL_COL_DATE) ));
			} catch (ParseException e) {
				throw new SQLException("unable to parse date column: " + e.getMessage(), e);
			}
			entry.setTitle(rs.getString(SQL_COL_TITLE));
			entry.setDescription(rs.getString(SQL_COL_TEXT));
			entry.setPage(rs.getInt(SQL_COL_PAGE));
			entry.setTags(ChangeLogEntry.parseTags( rs.getString(SQL_COL_TAGS) ));
			
			list.add(entry);
		}
		return list;
	}
}
