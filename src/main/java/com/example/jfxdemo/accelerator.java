package com.example.jfxdemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

import static javafx.application.Application.launch;

public class accelerator extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(accelerator.class.getResource("accelerator.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 600);
        stage.setTitle("QDU accelerator");
        stage.setResizable(false);
        //stage.getIcons().add(new Image("C:\\Users\\Emblem\\IdeaProjects\\jfxdemo\\src\\main\\resources\\img\\icon.png"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}