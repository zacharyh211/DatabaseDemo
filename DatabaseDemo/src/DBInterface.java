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
	
	public static boolean existDeliveries(LocalDate date) {
		try {
			ResultSet rs = statement.executeQuery(
					"SELECT 1\n"
					+ "FROM DELIVERY AS D JOIN INGREDIENT AS I ON D.INGREDIENTID = I.ID\n"
					+ "WHERE DATE(time) = '" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "'");
			return rs.next();
		}
		catch(Exception e) {
			return false;
		}
	}

	public static ResultSet getIntegredients() {
		try {
			ResultSet rs = statement.executeQuery( //Add Next delivery time column
					"SELECT I.Name as Ingredient, I.Amount as 'Amount (pounds)', Count(DISTINCT M.ID) as 'Used in # Recipes', MIN(D.Time) as 'Next Delivery'\n"
					+ "FROM Ingredient as I "
					+ 	"LEFT JOIN (Requires as R JOIN Menu_Item as M on R.MenuItemID = M.ID) on I.ID = R.IngredientID\n"
					+ 	"JOIN Delivery as D on I.ID = D.IngredientID\n"
					+ "GROUP BY I.Name, I.Amount");
			return rs;
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
