package dal.Interfaces;

import be.Category;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.SQLException;
import java.util.List;

public interface ICategoriesDAO {
    int createNewCategory(String name) throws Exception;

    void deleteCategory(Category category) throws Exception;

    Category getCategoryById(int categoryId) throws Exception;

    void updateCategory(Category category, String name) throws Exception;

    List<Category> getAllCategories() throws Exception;
}
