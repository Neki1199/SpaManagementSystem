package com.sms.Controllers.Calendar;

import com.sms.Models.Appointment;
import javafx.scene.control.Label;

public class WeekView {
    public WeekView(){}

    public void initializeWeekView(){
        // Add appointments into ListView
//        listView.setItems(FXCollections.observableArrayList());
//        listView.getItems().clear();
//        List<Appointment> appointments = Appointment.getAppointmentForDate("ENTER DATA PICKER");
//        datePicker.setValue(LocalDate.now());
//
//        // load appointments for initial date
//        onDateSelected();
//        // add listener to DatePicker
//        datePicker.setOnAction(event -> onDateSelected());
//        for (Appointment appointment : appointments) {
//            // Add appointment details to ListView
//            Label labelList = new Label();
//            labelList.setText(appointment.toString());
//            isPaidColor(appointment, labelList);
//            listView.getItems().add(labelList);
//        }
        // CREAR LISTENING A  DATEPICKER1

        //        List<Employee> employees = getEmployeesFromDatabase();
//        // To create the labels at the bottom of the calendar, with colors
//        int colorIndex = 0;
//        for (Employee employee : employees) {
//            Label label = new Label(employee.getName());
//            label.setStyle("-fx-background-color: " + getColor(colorIndex) + "; -fx-padding: 5;");
//            FontAwesomeIconView icon = new FontAwesomeIconView();
//            icon.setGlyphSize(20);
//            icon.setGlyphName("USER");
//            label.setGraphic(icon);
//            hbox.getChildren().add(label);
//            colorIndex++;
    }
    private void isPaidColor(Appointment appointment, Label lbl) {
        String paid = appointment.getStatus();
        // Add green color to listview value
        if (paid.equals("Paid")) {
            lbl.setStyle("-fx-background-color: rgba(0,110,0,0.55)");
        } else {
            lbl.setStyle("-fx-background-color: rgba(115,3,3,0.49)");
        }
    }
}
