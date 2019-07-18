package com.jme.lsgoldtrade.util;

import android.widget.EditText;

public class EidtTextInputUtil {

    //限制eidttext输入浮动数据步骤1：消除小数点后超过两位的字符​
    public static void limitInputPointPlaces(EditText editText, CharSequence charSequence, int pointPlaces) {
        String inputStr = charSequence.toString().trim();
        if (inputStr.contains(".")) {
            if (inputStr.endsWith(".")) {
                charSequence = inputStr + "00";
                editText.setText(charSequence);
                editText.setSelection(charSequence.length());//光标移到最后
            } else if (inputStr.endsWith(".0")) {
                charSequence = inputStr.subSequence(0,inputStr.indexOf("."));
                editText.setText(charSequence);
                editText.setSelection(charSequence.length());//光标移到最后
            }
//            if ((inputStr.length() -1) - inputStr.indexOf(".") > pointPlaces) {
//                charSequence = inputStr.subSequence(0,inputStr.indexOf(".") + (pointPlaces +1));
//                editText.setText(charSequence);
//                editText.setSelection(charSequence.length());//光标移到最后
//            }
        }
    }

    //限制eidttext输入浮动数据步骤2：输入的第一个字符为小数点时，自动在小数点前面不一个零   
    public static void limitInputAddStratOneZero(EditText editText, CharSequence charSequence){
        String inputStr = charSequence.toString().trim();
        if (inputStr.substring(0).equals(".")) {
            charSequence ="0" + inputStr;
            editText.setText(charSequence);
            editText.setSelection(charSequence.length());//光标移到最后   
        }
    }

    //限制eidttext输入浮动数据步骤3：如果输入的第一个为0，且第二位跟的不是".",则无法后续输入
    public static void limitInputClearStartMultiZero(EditText editText, CharSequence charSequence){
        String inputStr = charSequence.toString().trim();
        if (inputStr.startsWith("0") && inputStr.length() >1) {
            if (!inputStr.substring(1,2).equals(".")) {
                charSequence = inputStr.subSequence(0,1);
                editText.setText(charSequence);
                editText.setSelection(charSequence.length());
            }
        }
    }
}
