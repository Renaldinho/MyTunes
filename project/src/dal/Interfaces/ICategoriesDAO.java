package dal.Interfaces;

import be.Category;
import bll.exceptions.CategoriesException;

import java.sql.SQLException;
import java.util.List;

public interface ICategoriesDAO {
    int createNewCategory(String name) throws SQLException, CategoriesException;

    void deleteCategory(Category category) throws  SQLException;

    Category getCategoryById(int categoryId) throws SQLException;

    void updateCategory(Category category, String name) throws SQLException;

    List<Category> getAllCategories() throws SQLException;
}
