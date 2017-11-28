import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class SQLTable extends TableView<ObservableList<String>> {
	
	public SQLTable(ResultSet resultSet) {
		setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		setPrefWidth(500);
		try {
			ResultSetMetaData meta = resultSet.getMetaData();
			int colCt = meta.getColumnCount();
			for(int i = 1; i <= colCt; i++) {
				TableColumn<ObservableList<String>,String> column = new TableColumn<ObservableList<String>, String>(meta.getColumnLabel(i));
				final int j = i;
				column.setCellValueFactory(new Callback<CellDataFeatures<ObservableList<String>,String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<ObservableList<String>, String> arr) {
						return new SimpleStringProperty(arr.getValue().get(j-1).toString());
					}
				});
				getColumns().add(column);
			}
			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while(resultSet.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for(int i = 1; i <= colCt; i++)
					row.add(resultSet.getString(i));
				data.add(row);
			}
			setItems(data);
		}
		catch(SQLException e) {
			return;
		}
	}
}