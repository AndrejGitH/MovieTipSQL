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

    //set a database connection
    public DatabaseHandler(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection(url, username, password); //method from JDBC library
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //process query and store data to resultSet object. If query is invalid, catch exception and print error
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

    //close the database connection
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //create observable list, add all years to it, return this list
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
    //create observable list, add all actors names to it, return this list
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

    // returns established database connection
    public Connection getConnection() {
        return connection;
    }
}




