package dal.dao;

import be.Artist;
import be.Category;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.DatabaseConnector;
import dal.Interfaces.ICategoriesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriesDAO implements ICategoriesDAO {
    DatabaseConnector databaseConnector;

    public CategoriesDAO() {
        databaseConnector = new DatabaseConnector();
    }

    @Override
    public int createNewCategory(String name) throws SQLException {
        String sql0 = "SELECT FROM categories WHERE Name = ?";
        int id = 0;
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql0);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                id = resultSet.getInt("Id");
                return id;
            }
            String sql1 = "INSERT INTO categories VALUES(?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setString(1, name);
            preparedStatement1.executeUpdate();
            ResultSet resultSet1 = preparedStatement1.getGeneratedKeys();
            while (resultSet1.next()) {
                id = resultSet1.getInt(1);
            }

        }
        return id;
    }

    @Override
    public void deleteCategory(String name) throws SQLException {
        String sql = "DELETE FROM categories WHERE Name = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();

        }
    }

    @Override
    public Category getCategoryById(int categoryId) throws SQLException {
        String sql = "SELECT FROM categories WHERE Id=?";
        Category category = null;
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("name");
                category = new Category(id, name);
            }
        }
        return category;
    }
}
