package com.fifteentec.yoko;

import android.text.TextUtils;

import org.w3c.dom.Text;

public class InfoValidate {

    /**
     * 判断一个字符串的位数
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    public static boolean isPhoneValid(String phone){
        /*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0〜9的数字，有9位。
        if (TextUtils.isEmpty(phone))
            return false;
        else
            return phone.matches(telRegex);
    }

    public static boolean isUsernameValid(String username){
        String usernameRegex = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2Da-zA-Z\\d~!@#$%^&*()_+`\\-={}:\\\";'<>?,.\\\\/]{1,16}$";
        if(TextUtils.isEmpty(username))
            return false;
        else
            return username.matches(usernameRegex);
    }

    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        //String pswRegex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+`\\-={}:\";'<>?,.\\\\/]).{4,16}$";
        String pswRegex = "^(?=.*[a-zA-Z])(?=.*\\d).{6,16}$";
        if(TextUtils.isEmpty(password))
            return false;
        else
            return password.matches(pswRegex);
    }
}
