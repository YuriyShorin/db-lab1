package com.example.lab1gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;

public class Database {

    private File fullTable;
    private ArrayList<String> studentsTableList = new ArrayList<>();
    private ArrayList<String> variantsTableList = new ArrayList<>();
    private ArrayList<String> testingTableList = new ArrayList<>();
    ObservableList<Student> students = FXCollections.observableArrayList();
    private String name;
    private int maxId = 0;
    private int maxVariant = 0;

    public boolean create(String name) {
        clear();
        this.name = name;
        fullTable = new File("C:\\Programms\\Files\\Databases\\Lab1GUI\\src\\main\\resources\\bases\\" + name + ".txt");
        if (!fullTable.exists()) {
            try {
                if (!fullTable.createNewFile()) {
                    System.out.println("Error");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean getInformationFromFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            try (FileReader FileReader = new FileReader(path)) {
                BufferedReader reader = new BufferedReader(FileReader);
                String line = reader.readLine();
                while (line != null) {
                    studentsTableList.add(line);
                    Student student = new Student();
                    student.setStudents(line);
                    student.setId(maxId + 1);
                    students.add(student);
                    line = reader.readLine();
                    ++maxId;
                }
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean open(String name) {
        clear();
        File fileToOpen = new File("C:\\Programms\\Files\\Databases\\Lab1GUI\\src\\main\\resources\\bases\\" + name + ".txt");
        if (!fileToOpen.exists()) {
            return false;
        } else {
            fullTable = fileToOpen;
            try (FileReader FileReader = new FileReader("C:\\Programms\\Files\\Databases\\Lab1GUI\\src\\main\\resources\\bases\\" + name + ".txt")) {
                BufferedReader reader = new BufferedReader(FileReader);
                String line = reader.readLine();
                while (line != null) {
                    String[] splitLine = line.split(" ");
                    String studentName, variant;
                    int variantInt;
                    if(splitLine.length == 3){
                        studentName = splitLine[0];
                        variant = splitLine[1] + " " + splitLine[2];
                        variantInt = Integer.parseInt(splitLine[2]);
                    } else if(splitLine.length == 4){
                        studentName = splitLine[0] + " " + splitLine[1];
                        variant = splitLine[2] + " " + splitLine[3];
                        variantInt = Integer.parseInt(splitLine[3]);
                    } else if(splitLine.length == 5){
                        studentName = splitLine[0] + " " + splitLine[1] + " " + splitLine[2];
                        variant = splitLine[3] + " " + splitLine[4];
                        variantInt = Integer.parseInt(splitLine[4]);
                    } else{
                        return false;
                    }
                    if (variantInt > maxVariant) {
                        maxVariant = variantInt;
                    }
                    studentsTableList.add(studentName);
                    testingTableList.add(variant);
                    Student student = new Student();
                    student.setId(maxId + 1);
                    student.setStudents(studentName);
                    student.setVariant(variant);
                    students.add(student);
                    line = reader.readLine();
                    ++maxId;
                }
                createVariantsTable();
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
                return false;
            }
            return true;
        }
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(fullTable)) {
            BufferedWriter writer = new BufferedWriter(fileWriter);
            for (int i = 0; i < studentsTableList.size(); ++i) {
                writer.write(studentsTableList.get(i) + " " + testingTableList.get(i) + '\n');
                writer.flush();
            }
            writer.close();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void add(String name, String variant) {
        maxId++;
        studentsTableList.add(name);
        testingTableList.add("Variant " + variant);
        try (FileWriter fileWriter = new FileWriter(fullTable, true)) {
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(name + "Variant " + variant);
            writer.flush();
            writer.close();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        Student newStudent = new Student();
        newStudent.setId(maxId);
        newStudent.setStudents(name);
        newStudent.setVariant("Variant " + variant);
        students.add(newStudent);
    }

    public void edit(String id, String name, String variant) {
        int idInt = Integer.parseInt(id);
        Student editStudent = students.get(idInt - 1);
        if (!variant.equals("")) {
            editStudent.setVariant("Variant " + variant);
            testingTableList.set(idInt - 1, "Variant " + variant);
        }
        if (!name.equals("")) {
            editStudent.setStudents(name);
            studentsTableList.set(idInt - 1, name);
        }
        students.set(idInt - 1, editStudent);
    }

    public boolean deleteStudent(String name, String id) {
        if (!id.equals("")) {
            int idInt = Integer.parseInt(id);
            students.remove(idInt - 1);
            studentsTableList.remove(idInt - 1);
            testingTableList.remove(idInt - 1);
            maxId--;
            for (int i = idInt - 1; i < maxId; ++i) {
                students.get(i).setId(students.get(i).getId() - 1);
            }
            return true;
        }
        if (!name.equals("")) {
            for (int i = 0; i < maxId; ++i) {
                if (studentsTableList.get(i).equals(name)) {
                    students.remove(i);
                    studentsTableList.remove(i);
                    testingTableList.remove(i);
                    maxId--;
                    for (int j = i; j < maxId; ++j) {
                        students.get(j).setId(students.get(j).getId() - 1);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void delete() {
        clear();
        fullTable.delete();
    }

    private void clear() {
        maxId = 0;
        maxVariant = 0;
        name = "";
        studentsTableList.clear();
        variantsTableList.clear();
        testingTableList.clear();
        students.clear();
    }

    public void createVariantsTable() {
        for (int i = 1; i <= maxVariant; ++i) {
            variantsTableList.add("Variant " + i);
        }
    }

    public void createTestingTable() {
        for (int i = 0; i < studentsTableList.size(); ++i) {
            String var = variantsTableList.get((int) (Math.random() * variantsTableList.size()));
            testingTableList.add(var);
            students.get(i).setVariant(var);
        }
    }

    public int automateVariant() {
        return (int) (Math.random() * variantsTableList.size() + 1);
    }

    public void setMaxVariant(int maxVariant) {
        this.maxVariant = maxVariant;
    }


    public int getMaxId() {
        return maxId;
    }

    public String getName() {
        return name;
    }

    public int getMaxVariant() {
        return maxVariant;
    }

    public ArrayList<String> getStudentsTableList() {
        return studentsTableList;
    }

    public ArrayList<String> getTestingTableList(){
        return testingTableList;
    }

    public ObservableList<Student> getStudents() {
        return students;
    }
}
