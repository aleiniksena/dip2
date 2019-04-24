package main;

import javafx.scene.control.ComboBox;

public class ComboBoxFieldHelper<T> {

    public  boolean isFieldValid(ComboBox<T> f, String fieldName, StringBuffer err){
        boolean result = (f.getSelectionModel().getSelectedItem() != null);

        if (! result){
            err.append(fieldName).append(" null");
        }

        return result;
    }
}
