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


    public static boolean isFieldValid(TextField f){
        boolean result = true;
        String text = f.getText();

        if (text.compareTo("") == 0){
            result = false;
        }else{
            try{
                if(Integer.valueOf(text) == 0){
                    result = false;
                }
            } catch(Exception e){
                result = false;
            }
        }
        return result;
    }

}
