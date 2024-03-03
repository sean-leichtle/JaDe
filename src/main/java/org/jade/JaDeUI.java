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

    private File targetDirectory;
    private TextField sourceFieldOne, sourceFieldTwo, targetField, directoryNameField;
    final private List<File> directories = new ArrayList<>(2);

    public JaDeUI() { }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JaDe: Java Deduplicator");
        primaryStage.setResizable(false);

        // Elements
        sourceFieldOne = new TextField();
        sourceFieldOne.setPromptText("Use search to find source directory.");
        Button sourceButtonOne = new Button("Source");

        sourceFieldTwo = new TextField();
        sourceFieldTwo.setPromptText("Use search to find source directory.");
        Button sourceButtonTwo = new Button("Source");

        targetField = new TextField();
        targetField.setPromptText("Use destination button to navigate to target directory.");
        Button targetButton = new Button("Target");

        directoryNameField = new TextField();
        directoryNameField.setPromptText("Please enter name of new directory which will be created within selected destination directory.");
        Button deduplicateButton = new Button("Deduplicate");

        // Style elements
        sourceFieldOne.setPrefSize(525, 30);
        sourceFieldTwo.setPrefSize(525, 30);
        targetField.setPrefSize(525, 30);
        directoryNameField.setPrefSize(525, 30);

        // Layout
        GridPane grid = new GridPane();

        HBox originPaneOne = new HBox(sourceFieldOne, sourceButtonOne);
        originPaneOne.setSpacing(20);

        HBox originPaneTwo = new HBox(sourceFieldTwo, sourceButtonTwo);
        originPaneTwo.setSpacing(20);

        HBox destinationPane = new HBox(targetField, targetButton);
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
        sourceButtonOne.setOnAction(event -> this.sourceButtonOneEvent(primaryStage));

        sourceButtonTwo.setOnAction(event -> this.sourceButtonTwoEvent(primaryStage));

        targetButton.setOnAction(event -> this.targetButtonEvent(primaryStage));

        deduplicateButton.setOnAction(event -> this.deduplicateButtonEvent());

        Platform.runLater(sourceFieldOne::requestFocus);

        // Display
        primaryStage.setScene(new Scene(grid));
        primaryStage.show();
    }

    public void sourceButtonOneEvent(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File sourceDirectoryOne = directoryChooser.showDialog(primaryStage);
        if(sourceDirectoryOne != null) {
            sourceFieldOne.setText(sourceDirectoryOne.getAbsolutePath());
            directories.add(sourceDirectoryOne);
        }
    }

    public void sourceButtonTwoEvent(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File sourceDirectoryTwo = directoryChooser.showDialog(primaryStage);
        if(sourceDirectoryTwo != null) {
            sourceFieldTwo.setText(sourceDirectoryTwo.getAbsolutePath());
            directories.add(sourceDirectoryTwo);
        }
    }

    public void targetButtonEvent(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        this.targetDirectory = directoryChooser.showDialog(primaryStage);
        if(this.targetDirectory != null) {
            targetField.setText(this.targetDirectory.getAbsolutePath());
        }
    }

    public void deduplicateButtonEvent() {
        this.targetDirectory = new File(targetField.getText() + "/" + directoryNameField.getText());
        if(!targetDirectory.exists() && !directoryNameField.getText().isEmpty() && !targetField.getText().isEmpty()) {
            if(!this.directories.isEmpty()) {
                targetDirectory.mkdir();
                JaDeFileProcessor fileProcessor = new JaDeFileProcessor(this.directories, targetDirectory);
                try {
                    fileProcessor.processFiles();
                } catch (
                        IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
