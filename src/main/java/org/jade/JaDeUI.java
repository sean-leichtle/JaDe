package org.jade;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class JaDeUI extends Application {

    private File selectedDirectoryOne, selectedDirectoryTwo, destinationDirectory;
    private List<File> directories = new ArrayList<>(2);

    public JaDeUI() { }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JaDe: Java Deduplicator");
        primaryStage.setResizable(false);

        // Elements
        TextField originFieldOne = new TextField();
        originFieldOne.setPromptText("Use search to find origin directory or enter it manually here.");
        Button originButtonOne = new Button("Search");

        TextField originFieldTwo = new TextField();
        originFieldTwo.setPromptText("Use search to find origin directory or enter it manually here.");
        Button originButtonTwo = new Button("Search");

        TextField destinationField = new TextField();
        destinationField.setPromptText("Use destination button to navigate to directory or enter it here.");
        Button destinationButton = new Button("Destination");

        TextField directoryNameField = new TextField();
        directoryNameField.setPromptText("Please enter name of new directory which will be created within selected destination directory.");
        Button deduplicateButton = new Button("Deduplicate");

        // Style elements
        originFieldOne.setPrefSize(525, 30);
        originFieldTwo.setPrefSize(525, 30);
        destinationField.setPrefSize(525, 30);
        directoryNameField.setPrefSize(525, 30);

        // Layout
        GridPane grid = new GridPane();

        HBox originPaneOne = new HBox(originFieldOne, originButtonOne);
        originPaneOne.setSpacing(20);

        HBox originPaneTwo = new HBox(originFieldTwo, originButtonTwo);
        originPaneTwo.setSpacing(20);

        HBox destinationPane = new HBox(destinationField, destinationButton);
        destinationPane.setSpacing(20);

        HBox createDirectoryPane = new HBox(directoryNameField, deduplicateButton);
        createDirectoryPane.setSpacing(20);

        grid.add(originPaneOne, 0, 0);
        grid.add(originPaneTwo, 0, 1);
        grid.add(destinationPane, 0, 2);
        grid.add(createDirectoryPane, 0, 3);

        grid.setPrefSize(700, 200);
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Logic
        originButtonOne.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            this.selectedDirectoryOne = directoryChooser.showDialog(primaryStage);
            if(this.selectedDirectoryOne != null) {
                originFieldOne.setText(selectedDirectoryOne.getAbsolutePath());
                directories.add(selectedDirectoryOne);
            }
        });

        originButtonTwo.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            this.selectedDirectoryTwo = directoryChooser.showDialog(primaryStage);
            if(this.selectedDirectoryTwo != null) {
                originFieldTwo.setText(this.selectedDirectoryTwo.getAbsolutePath());
                directories.add(selectedDirectoryTwo);
            }
        });

        destinationButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            this.destinationDirectory = directoryChooser.showDialog(primaryStage);
            if(this.destinationDirectory != null) {
                destinationField.setText(this.destinationDirectory.getAbsolutePath());
            }
        });

        deduplicateButton.setOnAction(event -> {
            this.destinationDirectory = new File(destinationField.getText() + "/" + directoryNameField.getText());
            if(!destinationDirectory.exists() && !directoryNameField.getText().isEmpty() && !destinationField.getText().isEmpty()) {
                destinationDirectory.mkdir();
                if(!this.directories.isEmpty()) {
                    JaDeFileProcessor fileProcessor = new JaDeFileProcessor(this.directories, destinationDirectory);
                    try {
                        fileProcessor.processFiles();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        Platform.runLater(originFieldOne::requestFocus);

        // Display
        primaryStage.setScene(new Scene(grid));
        primaryStage.show();
    }
}
