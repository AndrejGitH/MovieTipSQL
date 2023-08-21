package first.sqlapp.movietipsql;


import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MovieApp extends Application {
    private DatabaseHandler dbHandler;
    private TextArea displayArea = new TextArea();

    public static void main(String[] args) {
        launch(args);
    }

    public void init() throws Exception {
        AppInitializer appInitializer  = new AppInitializer();
        dbHandler = appInitializer.initialize();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Creating window and button
        HBox layout = new HBox(5);
        layout.setStyle("-fx-control-inner-background: lightblue; -fx-text-fill: black;");
        Button searchBt = new Button("Search");
        searchBt.setDisable(true);

        displayArea.setEditable(false);
        displayArea.setPrefWidth(400); // Set an appropriate width
        displayArea.setPrefHeight(300);
        displayArea.setStyle("-fx-control-inner-background: lightblue; -fx-text-fill: black;");

        // Choice box for search options
        ChoiceBox<String> searchOptions = new ChoiceBox<>();
        searchOptions.getItems().addAll("Search by Year", "Search by Actor");
        searchOptions.setValue(""); // Blank initial option

        // Create choices menu for year
        ChoiceBox<Integer> yearChoice = new ChoiceBox<>();
        ObservableList<Integer> yearList = dbHandler.getYears();
        yearChoice.setItems(yearList);
        yearChoice.setDisable(true);

        // Create choices menu for actors
        ChoiceBox<String> actorChoice = new ChoiceBox<>();
        ObservableList<String> actorsList = dbHandler.getActors();
        actorChoice.setItems(actorsList);
        actorChoice.setDisable(true);

        // Listener to enable/disable year and actor choices based on selected search option
        searchOptions.valueProperty().addListener((observable, oldValue, newValue) -> {
            yearChoice.setDisable(true);
            actorChoice.setDisable(true);
            searchBt.setDisable(true);

            if (newValue.equals("Search by Year")) {
                yearChoice.setDisable(false);
            } else if (newValue.equals("Search by Actor")) {
                actorChoice.setDisable(false);
            }
        });

        // Listener to enable search button when year or actor is selected
        yearChoice.valueProperty().addListener((observable, oldValue, newValue) -> {
            searchBt.setDisable(newValue == null && actorChoice.getValue() == null);
        });

        actorChoice.valueProperty().addListener((observable, oldValue, newValue) -> {
            searchBt.setDisable(newValue == null && yearChoice.getValue() == null);
        });

        // Button event handling
        searchBt.setOnAction(e -> {
            String selectedOpt = searchOptions.getValue();
            ResultSet resultSet = null;

            if (selectedOpt.equals("Search by Year")) {
                int selectedYear = yearChoice.getValue();
                resultSet = dbHandler.executeQuery("SELECT M.Title, M.ReleaseYear, CONCAT(A.FirstName, ' ', A.LastName) AS actorname " +
                        "FROM Movies M " +
                        "JOIN MovieActors MA ON M.MovieID = MA.MovieID " +
                        "JOIN actors A ON MA.ActorID = A.ActorID " +
                        "WHERE M.ReleaseYear = " + selectedYear);

            } else if (selectedOpt.equals("Search by Actor")) {
                String selectedActor = actorChoice.getValue();
                // Redefine the alias 'actorname' in this query
                resultSet = dbHandler.executeQuery("SELECT M.Title, M.ReleaseYear, CONCAT(A.FirstName, ' ', A.LastName) AS actorname " +
                        "FROM Movies M " +
                        "JOIN MovieActors MA ON M.MovieID = MA.MovieID " +
                        "JOIN actors A ON MA.ActorID = A.ActorID " +
                        "WHERE CONCAT(A.FirstName, ' ', A.LastName) = '" + selectedActor + "'");

            }
            displayResultSetData(resultSet, selectedOpt);
        });


        // Add components to layout
        layout.getChildren().addAll(searchOptions, yearChoice, actorChoice, searchBt, displayArea);

        // Scene setup
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.setTitle("Movie Tip Search");
        stage.show();
    }

    // Close database connection when app is stopped
    public void stop() throws Exception {
        dbHandler.closeConnection();
    }

    private void displayResultSetData(ResultSet resultSet, String selectedOption) {
        // Clear any previous data in your display area
        displayArea.clear();

        try {
            while (resultSet.next()) {
                String movieTitle = resultSet.getString("Title");
                int releaseYear = resultSet.getInt("ReleaseYear");
                String actorName = resultSet.getString("actorname");

                String movieInfo = movieTitle + ", " + releaseYear + ", Main Role: " + actorName;
                // Append the movie info to your display area (assuming you have a TextArea or similar component)
                displayArea.appendText(movieInfo + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
