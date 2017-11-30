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
	
	public static ResultSet getWaiterShifts(LocalDate date, String service) {
        try {
	        String waiterQuery = "SELECT ID as WaiterID, FName, LName "
	                + "FROM EMPLOYEE "
	                + "WHERE Type = 'Waiter'";  
	        
	        String shiftQuery = "SELECT * "
	                + "FROM SERVES JOIN RESTAURANT_TABLE "
	                    + "ON TableNo = Number "
	                + "WHERE ShiftDate = '" + date.toString() + "' "
	                + "AND ShiftService = '" + service + "'";
	        
	        ResultSet rs = statement.executeQuery(
	                "SELECT WaiterID, FName, LName, COUNT(TableNo) as Tables, IFNULL(SUM(Max_Cap),0) as People "
	                + "FROM "
	                + "(" + waiterQuery + ") as W "
	                + "NATURAL LEFT OUTER JOIN "
	                + "(" + shiftQuery + ") as S "
	                + "GROUP BY WaiterId, FName, LName;");  
	        
	        //while(rs.next())
              //  System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3));
	        
	        return rs;
		}
        catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
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
					"SELECT I.Name as Ingredient, I.Amount as 'Amount (pounds)', Count(M.ID) as 'Used in # Recipes'\n"
					+ "FROM Ingredient as I "
					+ 	"LEFT JOIN (Requires as R JOIN Menu_Item as M on R.MenuItemID = M.ID) on I.ID = R.IngredientID\n"
					+ "GROUP BY I.Name, I.Amount");
			return rs;
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
