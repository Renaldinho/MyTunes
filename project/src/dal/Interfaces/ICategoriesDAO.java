package dal.Interfaces;

import be.Category;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.SQLException;

public interface ICategoriesDAO {
    int createNewCategory(String name) throws SQLException;
    void deleteCategory(String name) throws SQLServerException, SQLException;

    Category getCategoryById(int categoryId) throws SQLException;
}