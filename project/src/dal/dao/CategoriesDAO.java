package dal.dao;

import be.Category;
import bll.exceptions.CategoriesException;
import dal.DatabaseConnector;
import dal.Interfaces.ICategoriesDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriesDAO implements ICategoriesDAO {
    DatabaseConnector databaseConnector;

    public CategoriesDAO() {
        databaseConnector = new DatabaseConnector();
    }

    //looks for a given category and return its id, if not found just creates a new one and returns the generated key associated to it.
    @Override
    public int createCategory(String category) throws SQLException, CategoriesException {
        categoryNameNotNull(category);
        String sql0 = "SELECT * FROM categories WHERE Category = ?";
        int id = 0;
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql0);
            preparedStatement.setString(1, category);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                id = resultSet.getInt("Id");
                checkCategoryName(id);
            }
            String sql1 = "INSERT INTO categories VALUES(?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            preparedStatement1.setString(1, category);
            preparedStatement1.executeUpdate();
            ResultSet resultSet1 = preparedStatement1.getGeneratedKeys();
            while (resultSet1.next()) {
                id = resultSet1.getInt(1);
            }

        }
        return id;
    }

    @Override
    public void deleteCategory(Category category) throws SQLException {
        String sql = "DELETE FROM categories WHERE Id = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, category.getId());
            preparedStatement.executeUpdate();

        }
    }

    @Override
    public Category getCategory(int categoryId) throws SQLException {
        String sql = "SELECT *  FROM categories WHERE Id=?";
        Category category = null;
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("Category");
                category = new Category(id, name);
            }
        }
        return category;
    }

    @Override
    public void updateCategory(Category category, String name) throws SQLException {
        String sql = "UPDATE categories SET Category=? WHERE Id = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, category.getId());
            preparedStatement.executeUpdate();

        }
    }

    @Override
    public List<Category> getAllCategories() throws SQLException {
        List<Category> allCategories = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try (Connection connection = databaseConnector.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("Category");
                Category category = new Category(id, name);
                allCategories.add(category);
            }
        }
        return allCategories;
    }

    private void checkCategoryName(int id) throws CategoriesException {
        Exception exception = new Exception();
        if (id != 0)
            throw new CategoriesException("Category already exists", exception, id);
    }

    private void categoryNameNotNull(String name) throws CategoriesException {
        Exception exception = new Exception();
        if (name.isEmpty())
            throw new CategoriesException("Please find a category for your song", exception);
    }
}
