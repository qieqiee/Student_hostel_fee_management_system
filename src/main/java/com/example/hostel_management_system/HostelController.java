package com.example.hostel_management_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

public class HostelController {

    @FXML
    private TableColumn<HostelFeeRecord, Integer> ColMonths;

    @FXML
    private TableColumn<HostelFeeRecord, String> ColName;

    @FXML
    private ComboBox<Integer> ComboBoxMonth;

    @FXML
    private RadioButton RadioBtnDeluxe;

    @FXML
    private RadioButton RadioBtnStdRoom;

    @FXML
    private TableView<HostelFeeRecord> TableView;

    @FXML
    private Button btnCalc;

    @FXML
    private Button btnClear;
    
    @FXML
    private Button btnDelete;

    @FXML
    private CheckBox checkBoxInternetSubs;

    @FXML
    private TableColumn<HostelFeeRecord, Double> colDisc;

    @FXML
    private TableColumn<HostelFeeRecord, Double> colFinal;

    @FXML
    private TableColumn<HostelFeeRecord, String> colRoomType;

    @FXML
    private TableColumn<HostelFeeRecord, Double> colTotalMonthly;

    @FXML
    private TableColumn<HostelFeeRecord, Double> colTotalStay;

    @FXML
    private ToggleGroup roomGroup;

    @FXML
    private TextField txtSecurityDepo;

    @FXML
    private TextField txtStudentName;

    @FXML
    private TextField txtUsage;

    @FXML
    private TextField txtWaterCharges;
    
    // list that store all of the record to display in the table view
    private final ObservableList<HostelFeeRecord> recordList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        //month in the month staying combo box
        ComboBoxMonth.setItems(FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
        ));
        //connect every cloumn in table view with the property in the class
        ColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ColMonths.setCellValueFactory(new PropertyValueFactory<>("months"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colTotalMonthly.setCellValueFactory(new PropertyValueFactory<>("totalMonthly"));
        colTotalStay.setCellValueFactory(new PropertyValueFactory<>("totalStay"));
        colDisc.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colFinal.setCellValueFactory(new PropertyValueFactory<>("finalPayable"));

        TableView.setItems(recordList);
    }

    @FXML
    private void handleCalculate() {
        try {
            
            //fetch input from user
            String studentName = txtStudentName.getText().trim();
            Integer months = ComboBoxMonth.getValue();
            String roomType;
            
            //convert text input to number
            double electricityUsage = Double.parseDouble(txtUsage.getText().trim());
            double waterCharges = Double.parseDouble(txtWaterCharges.getText().trim());
            double securityDeposit = Double.parseDouble(txtSecurityDepo.getText().trim());
            //check whether internet subsription is selected
            boolean internetSelected = checkBoxInternetSubs.isSelected();

            if (studentName.isEmpty()) {
                showError("Student name cannot be empty.");
                return;
            }

            if (months == null) {
                showError("Please select number of months staying.");
                return;
            }

            if (!RadioBtnStdRoom.isSelected() && !RadioBtnDeluxe.isSelected()) {
                showError("Please select a room type.");
                return;
            }

            if (electricityUsage < 0 || waterCharges < 0 || securityDeposit < 0) {
                showError("Values cannot be negative.");
                return;
            }
            //Decide type of room based on the selected radio button
            if (RadioBtnStdRoom.isSelected()) {
                roomType = "Standard Room";
            } else {
                roomType = "Deluxe Room";
            }
            //calculation
            double totalMonthly = calculateMonthlyFee(roomType, electricityUsage, waterCharges, internetSelected);
            double totalStay = totalMonthly * months;
            double discount = calculateDiscount(totalStay, months);
            double finalPayable = calculateFinalPayable(totalStay, discount);
            //create object record to be inserted in the table view
            HostelFeeRecord record = new HostelFeeRecord(
                    studentName,
                    months,
                    roomType,
                    totalMonthly,
                    totalStay,
                    discount,
                    finalPayable
            );
            // add record in the table view list
            recordList.add(record);
            // display the result to user 
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Calculation Result");
            alert.setHeaderText("Hostel Fee Calculated Successfully");
            alert.setContentText(
                    "Student Name: " + studentName +
                    "\nMonthly Charges: RM " + String.format("%.2f", totalMonthly) +
                    "\nTotal Charges for Entire Stay: RM " + String.format("%.2f", totalStay) +
                    "\nDiscount Amount: RM " + String.format("%.2f", discount) +
                    "\nFinal Payable Amount: RM " + String.format("%.2f", finalPayable) +
                    "\nSecurity Deposit (Refundable): RM " + String.format("%.2f", securityDeposit)
            );
            alert.showAndWait();

        } catch (NumberFormatException e) {
            showError("Invalid input. Please enter numeric values correctly.");
        }
    }

    @FXML
    private void handleClear() {
        txtStudentName.clear();
        ComboBoxMonth.setValue(null);
        roomGroup.selectToggle(null);
        txtUsage.clear();
        txtWaterCharges.clear();
        txtSecurityDepo.clear();
        checkBoxInternetSubs.setSelected(false);
    }
    //method to calculate monthly charge hostel
    private double calculateMonthlyFee(String roomType, double electricityUsage, double waterCharges, boolean internetSelected) {
        double roomCharge = 0.0;
        double electricityCharge = electricityUsage * 0.25;
        double internetCharge = internetSelected ? 50.0 : 0.0;

        if (roomType.equals("Standard Room")) {
            roomCharge = 300.0;
        } else if (roomType.equals("Deluxe Room")) {
            roomCharge = 450.0;
        }

        return roomCharge + electricityCharge + waterCharges + internetCharge;
    }
    // calculate discount
    private double calculateDiscount(double totalStay, int months) {
        //discount 5% if stay more than 6 months
        if (months > 6) {
            return totalStay * 0.05;
        }
        return 0.0;
    }
    //calculate final pay after discount
    private double calculateFinalPayable(double totalStay, double discount) {
        return totalStay - discount;
    }
    //display error message to user
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    void handleDelete(ActionEvent event) {
    //select the row in the table view
    HostelFeeRecord selectedRecord = TableView.getSelectionModel().getSelectedItem();
    //if there are selected row, delete from table view
    if (selectedRecord != null) {
        recordList.remove(selectedRecord);
        TableView.getSelectionModel().clearSelection();
    } else {
        showError("Please select a row to delete.");
    }
    
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setContentText("Are you sure you want to delete this record?");
    }
}
    
  