package com.example.lab1gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.File;

public class MainPageController {

    @FXML
    Button createButton;
    @FXML
    public TextField deleteDatabase;
    @FXML
    public TextField openDatabase;
    @FXML
    public TextField addStudentName;
    @FXML
    public TextField addStudentVariant;
    @FXML
    public TextField editId;
    @FXML
    public TextField editName;
    @FXML
    public TextField editVariant;
    @FXML
    public TextField deleteId;
    @FXML
    public TextField deleteName;
    @FXML
    private TextField createDatabase;
    @FXML
    private TextField nameOfFile;
    @FXML
    private TextField variants;
    @FXML
    private Label errorLabel;
    @FXML
    private TableView<Student> table;
    @FXML
    private TableColumn<Student, Integer> idColumn;
    @FXML
    private TableColumn<Student, String> studentsNamesColumn;
    @FXML
    private TableColumn<Student, String> variantsColumn;
    private Database database = new Database();

    @FXML
    private void databaseCreateButton(ActionEvent event) {
        if (!database.create(createDatabase.getText())) {
            errorLabel.setText("Database already exist.\n Please enter new name!");
        } else {
            String path = nameOfFile.getText();
            if (!path.equals("")) {
                if (!database.getInformationFromFile(path)) {
                    errorLabel.setText("File not found");
                    database.delete();
                    return;
                }
            }
            String vars = variants.getText();
            if (!vars.equals("")) {
                database.setMaxVariant(Integer.parseInt(vars));
                database.createVariantsTable();
                database.createTestingTable();
                createDatabase.setDisable(true);
                nameOfFile.setDisable(true);
                variants.setDisable(true);
                createButton.setDisable(true);
                show();
            } else {
                errorLabel.setText("Please enter the number of variants!");
                database.delete();
            }
        }
    }


    private void show(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        studentsNamesColumn.setCellValueFactory(new PropertyValueFactory<>("Students"));
        variantsColumn.setCellValueFactory(new PropertyValueFactory<>("Variants"));
        table.setItems(database.getStudents());
    }
    @FXML
    private void saveButton(ActionEvent event) {
        database.save();
    }

    public void databaseDeleteButton(ActionEvent event) {
        String name = deleteDatabase.getText();
        File fileToDelete = new File("C:\\Programms\\Files\\Data bases\\Lab1GUI\\src\\main\\resources\\bases\\" + name + ".txt");
        if (fileToDelete.exists()) {
            if (name.equals(database.getName())) {
                database.delete();
                table.setItems(database.getStudents());
                show();
            } else {
                if (!fileToDelete.delete()) {
                    errorLabel.setText("delete error");
                }
            }
        } else {
            errorLabel.setText("File doesn't exist!");
        }
    }


    public void databaseOpenButton(ActionEvent event) {
        String name = openDatabase.getText();
        if (!name.equals("")) {
            if (!database.open(name)) {
                errorLabel.setText("Cannot find database");
            }
            show();
            openDatabase.setText("");
            createDatabase.setText(name);
            createDatabase.setDisable(true);
            nameOfFile.setDisable(true);
            variants.setText(String.valueOf(database.getMaxVariant()));
            variants.setDisable(true);
        } else {
            errorLabel.setText("Please enter the name of database\n that you want to open");
        }
    }

    public void backupButton(ActionEvent event) {
        database.open(database.getName());
    }

    public void addStudentsButton(ActionEvent event) {
        String name = addStudentName.getText();
        String var = addStudentVariant.getText();
        if (name.equals("")) {
            errorLabel.setText("Please enter name");
        } else {
            if (var.equals("")) {
                errorLabel.setText("Please enter variant or press automate variant");
            } else if (Integer.parseInt(var) > database.getMaxVariant()) {
                errorLabel.setText("Max variant is " + database.getMaxVariant());
            } else {
                if(database.getStudentsTableList().contains(name) && database.getTestingTableList().contains("Variant " + var)) {
                    errorLabel.setText("Student already exists");
                } else{
                    database.add(name, var);
                    addStudentName.setText("");
                    addStudentVariant.setText("");
                }
            }
        }

    }

    public void automateVariantButton(ActionEvent event) {
        int var = database.automateVariant();
        addStudentVariant.setText(String.valueOf(var));
    }

    public void editButton(ActionEvent event) {
        String id = editId.getText();
        String name = editName.getText();
        String var = editVariant.getText();
        if (id.equals("")) {
            errorLabel.setText("Please enter id");
        } else if (name.equals("") && var.equals("")) {
            errorLabel.setText("Please enter name, variant or both");
        } else if (Integer.parseInt(id) > database.getMaxId()) {
            errorLabel.setText("No such student");
        } else {
            database.edit(id, name, var);
            editId.setText("");
            editName.setText("");
            editVariant.setText("");
        }
    }

    public void databaseCreateMoreButton(ActionEvent event) {
        createDatabase.setDisable(false);
        nameOfFile.setDisable(false);
        variants.setDisable(false);
        createButton.setDisable(false);
        createDatabase.setText("");
        nameOfFile.setText("");
        variants.setText("");
    }

    public void deleteStudentButton(ActionEvent event) {
        String name = deleteName.getText();
        String id = deleteId.getText();
        if (name.equals("") && id.equals("")) {
            errorLabel.setText("Please enter id or name");
        } else if (!(name.equals("") || id.equals(""))) {
            errorLabel.setText("Please enter only one field");
        } else if (Checker.isNumeric(id) && (Integer.parseInt(id) > database.getMaxId())) {
            errorLabel.setText("No such student");
        } else {
            if (!database.deleteStudent(name, id)) {
                errorLabel.setText("No such student");
            }
            deleteName.setText("");
            deleteId.setText("");
        }
    }
}
