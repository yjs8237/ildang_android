package com.jscompany.ildang.Common;

public class UserLoginInfo {

    private static String cell_no;
    private static String user_pwd;
    private static String user_name;
    private static String user_type;
    private static String user_nick;
    private static String address;
    private static String com_name;

    public static String getCell_no() {
        return cell_no;
    }

    public static void setCell_no(String cell_no) {
        UserLoginInfo.cell_no = cell_no;
    }

    public static String getUser_pwd() {
        return user_pwd;
    }

    public static void setUser_pwd(String user_pwd) {
        UserLoginInfo.user_pwd = user_pwd;
    }

    public static String getUser_name() {
        return user_name;
    }

    public static void setUser_name(String user_name) {
        UserLoginInfo.user_name = user_name;
    }

    public static String getUser_type() {
        return user_type;
    }

    public static void setUser_type(String user_type) {
        UserLoginInfo.user_type = user_type;
    }

    public static String getUser_nick() {
        return user_nick;
    }

    public static void setUser_nick(String user_nick) {
        UserLoginInfo.user_nick = user_nick;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        UserLoginInfo.address = address;
    }

    public static String getCom_name() {
        return com_name;
    }

    public static void setCom_name(String com_name) {
        UserLoginInfo.com_name = com_name;
    }
}
