package com.sms.Controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;

public class NotifyAppointmentsChanges {
    private static final IntegerProperty appointmentCountProperty = new SimpleIntegerProperty();

    public static IntegerProperty appointmentCountProperty() {
        return appointmentCountProperty;
    }

    public static void setAppointmentCount(int count) {
        appointmentCountProperty.set(count);
    }

    public static void incrementAppointmentCount() {
        appointmentCountProperty.set(appointmentCountProperty.get() + 1);
    }

    public static void decrementAppointmentCount() {
        appointmentCountProperty.set(appointmentCountProperty.get() - 1);
    }
}
