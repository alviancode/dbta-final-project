package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieListController{
    public Button refreshButton;
    Connection connect = new Connection();
    ObservableList<ModelTableMovie> oblist = FXCollections.observableArrayList();
    public String movietitle;
    public String username;

    @FXML
    private TableView<ModelTableMovie> movieTable;

    public void refreshButton(){

    }

    public void setUsername(String username){
        this.username = username;
    }

    public void getVal() {
        ModelTableMovie movie = movieTable.getSelectionModel().getSelectedItem();
        movietitle = movie.getMovie();
    }

    public void getMovie(){
        //ModelTableMovie movie = movieTable.getSelectionModel().getSelectedItem();
        getVal();
        //Parent root = null;
        try{



            FXMLLoader loader = new FXMLLoader(getClass().getResource("Rating.fxml"));
            Parent root = loader.load();
            RatingController rCon = loader.getController();
            rCon.setUsername(username);
            rCon.setMovieTitle(movietitle);
            rCon.loadFirst();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            Stage closeWindow = (Stage) movieTable.getScene().getWindow();
            closeWindow.close();
        } catch (IOException e) {
            System.out.println("File Not Found!");
        }
    }

    public void showTable() {
        try {
            PreparedStatement prepStat = connect.getPrepStat("SELECT * FROM movies;");
            ResultSet rs = prepStat.executeQuery();

            while (rs.next()) {
                oblist.add(new ModelTableMovie(rs.getString("title"), rs.getInt("movie_rating")));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void loadFirst() {
        showTable();

        TableColumn movCol = new TableColumn("Movie");
        movCol.setMinWidth(550);
        movCol.setCellValueFactory(
                new PropertyValueFactory<ModelTableMovie, String>("movie"));

        TableColumn ratCol = new TableColumn("Rating");
        ratCol.setMinWidth(100);
        ratCol.setCellValueFactory(
                new PropertyValueFactory<ModelTableMovie, Integer>("rating"));
        movieTable.setItems(oblist);
        movieTable.getColumns().addAll(movCol, ratCol);

    }

}
