package dal.Interfaces;

import be.Category;
import bll.exceptions.CategoriesException;

import java.sql.SQLException;
import java.util.List;

public interface ICategoriesDAO {
    int createCategory(String name) throws SQLException, CategoriesException;

    void deleteCategory(Category category) throws  SQLException;

    Category getCategory(int categoryId) throws SQLException;

    void updateCategory(Category category, String name) throws SQLException;

    List<Category> getAllCategories() throws SQLException;
}



