package pdf.changelog;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnetcionFactory {
	public static final String JDBC_DRIVER = "org.sqlite.JDBC";
	public static final String JDBC_CONN_STRING = "jdbc:sqlite:/%s";

	private final static String user = "";
	private final static String password = "";
	
	public static Connection createSQLiteConnection(File sqliteFile) throws ConnectionException {
		try {
			Class<Driver> driverClass = (Class<Driver>) Class.forName(JDBC_DRIVER);
			Driver driver = (Driver) driverClass.newInstance();
			DriverManager.registerDriver( driver);
			String jdbcUrl = String.format(JDBC_CONN_STRING, sqliteFile.getAbsolutePath());
			return DriverManager.getConnection(jdbcUrl, user, password);
		} catch (ClassNotFoundException e) {
			throw new ConnectionException(e);
		} catch (SQLException e) {
			throw new ConnectionException(e);
		} catch (InstantiationException e) {
			throw new ConnectionException(e);
		} catch (IllegalAccessException e) {
			throw new ConnectionException(e);
		}
	}
}
