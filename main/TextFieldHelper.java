package main;

import javafx.scene.control.TextField;

public class TextFieldHelper {

    public static int convertTextToInt(String val){
        int result = 0;

        try{
            result = Integer.parseInt(val);
        } catch(Exception e){
            result = 0;
        }
        return result;
    }

    public static String convertIntToText(int val){
        return String.valueOf(val);
    }


    public static boolean isNumericFieldValid(TextField f, String fieldName, StringBuffer err){
        boolean result = true;
        String text = f.getText();

        if (isFieldValid(f, fieldName, err)){
            try{
                if(Integer.valueOf(text) == 0){
                    err.append(fieldName).append(text);
                    result = false;
                }
            } catch(Exception e){
                result = false;
                err.append(fieldName).append(text);
            }
        }
        return result;
    }

    public static boolean isFieldValid(TextField f, String fieldName, StringBuffer err){
        boolean result = true;
        String text = f.getText();

        if (text.compareTo("") == 0){
            err.append(fieldName).append(text);
            result = false;
        }

        return result;
    }

}
