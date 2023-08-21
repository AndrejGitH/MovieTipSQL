package first.sqlapp.movietipsql;

import javafx.collections.FXCollections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;

public class DatabaseHandler {
    private Connection connection;

    public DatabaseHandler(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Integer> getYears() {
        ObservableList<Integer> yearsList = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT ReleaseYear FROM Movies");
            while (resultSet.next()) {
                int year = resultSet.getInt("ReleaseYear");
                yearsList.add(year);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return yearsList;
    }

    public ObservableList<String> getActors() {
        ObservableList<String> actorsList = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT concat(firstname, ' ', lastname) as actorname FROM actors");
            while (resultSet.next()) {
                String actor = resultSet.getString("actorname");
                actorsList.add(actor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actorsList;
    }

    public Connection getConnection() {
        return connection;
    }
}




