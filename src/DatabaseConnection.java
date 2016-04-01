import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
	public static final String url = "jdbc:mysql://cse.unl.edu/anooka";
	public static final String username = "anooka";
	public static final String password = "Zi:LB4";

	public static Connection getConnection() {
		//Establish Database COnnection
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			//System.out.println("InstantiationException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			//System.out.println("IllegalAccessException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			//System.out.println("ClassNotFoundException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		Connection conn;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println("SQLException: DatabaseInfo is incorrect");
			e.printStackTrace();
			throw new RuntimeException(e);
		}	
		return conn;
	}
}
