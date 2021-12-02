package dal.Interfaces;

import be.Category;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.SQLException;

public interface ICategoriesDAO {
    int createNewCategory(String name) throws SQLException;

    void deleteCategory(Category category) throws SQLServerException, SQLException;

    int categoryOccurrences(Category category) throws SQLException;

    Category getCategoryById(int categoryId) throws SQLException;

    void updateCategory(Category category, String name) throws SQLException;
}
