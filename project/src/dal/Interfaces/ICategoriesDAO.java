package dal.Interfaces;

import be.Category;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.SQLException;

public interface ICategoriesDAO {
    int createNewCategory(String name) throws SQLException;

    void deleteCategory(int id) throws SQLServerException, SQLException;

    int categoryOccurrences(int categoryId) throws SQLException;

    Category getCategoryById(int categoryId) throws SQLException;

    void updateCategory(int id, String name) throws SQLException;
}
