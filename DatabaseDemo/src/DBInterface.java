import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DBInterface {
	private static Statement statement;
	
	public static void setStatement(Statement statement) {
		DBInterface.statement = statement;
	}
	
	public static ResultSet getDeliveries(LocalDate date) {
		try {
			return statement.executeQuery(
					  "SELECT TIME(time) AS Time, I.Name AS Ingredient, D.Amount AS 'Amount (pounds)', CONCAT('$',D.Cost) AS 'Cost'\n"
					+ "FROM DELIVERY AS D JOIN INGREDIENT AS I ON D.IngredientID = I.ID\n"
					+ "WHERE DATE(time) = '" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "'");
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
