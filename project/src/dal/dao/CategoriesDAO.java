package dal.dao;

import be.Artist;
import be.Category;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.DatabaseConnector;
import dal.Interfaces.ICategoriesDAO;

import java.sql.*;

public class CategoriesDAO implements ICategoriesDAO {
    DatabaseConnector databaseConnector;

    public CategoriesDAO() {
        databaseConnector = new DatabaseConnector();
    }
    //looks for a given category and return its id, if not found just creates a new one and returns the generated key associated to it.

    @Override
    public int createNewCategory(String category) throws SQLException {
        String sql0 = "SELECT * FROM categories WHERE Category = ?";
        int id = 0;
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql0);
            preparedStatement.setString(1, category);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                id = resultSet.getInt("Id");
                return id;
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
    public void deleteCategory(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE Id = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        }
    }

    /**
     * this method returns how many songs a category is involved in. We need this to check while deleting a song.
     * If a category only belongs to one song that we want to delete, we just clear it from database.
     */
    @Override
    public int categoryOccurrences(int categoryId) throws SQLException {
        int occurrences = 0;
        String sql = "SELECT FROM songs WHERE Category = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                occurrences += 1;
            }
        }
        return occurrences;
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

    @Override
    public void updateCategory(int id, String name) throws SQLException {
        String sql = "UPDATE categories SET Category=? WHERE Id = ?";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

        }
    }
}
