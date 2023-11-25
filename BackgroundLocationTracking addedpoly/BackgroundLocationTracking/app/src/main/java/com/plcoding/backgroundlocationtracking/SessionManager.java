package com.plcoding.backgroundlocationtracking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;





public class SessionManager {

    SharedPreferences sharedprefernce;
    SharedPreferences.Editor editor;

    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "sharedcheckLogin";

    public static final String LANGUAGE = "mx_language";
    private static final String User_Id = "user_id";
    private static final String Lead_Id = "lead_id";
    private static final String Follow_Id = "follow_id";
    private static final String IS_LOGIN = "islogin";
    private static final String userPassword = "password";
    private static final String USERNAME = "username";
    private static final String USERPHONENUMBER = "userPhoneNumber";
    private static final String USEREMAIL = "userEmail";
    private static final String UserCity = "usercity";
    private static final String UserType = "usertype";
    private static final String SessionId = "sessionid";
    private static final String Photo = "Photo";

    private static final String employeetaggedid = "employeetaggedid";


    private static final String clock_status_check = "clock_status_check";

    private static final String BASE_URL = "BASE_URL";




    public SessionManager(Context context) {

        this.context = context;
        sharedprefernce = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedprefernce.edit();


    }




    public void setUserID(String user_id) {
        editor.putString(User_Id, user_id);
        editor.commit();
    }

    public String getUserID() {
        return sharedprefernce.getString(User_Id, "");
    }

    public void setLeadId(String lead_id) {

        editor.putString(Lead_Id, lead_id);
        editor.commit();

    }




    public String getClock_status_check() {
        return sharedprefernce.getString(clock_status_check, "");
    }

    public void setClock_status_check(String valueee_) {

        editor.putString(clock_status_check, valueee_);
        editor.commit();


    }





    public String getEmployeetaggedid() {
        return sharedprefernce.getString(employeetaggedid, "");
    }

    public void setEmployeetaggedid(String vaeee_) {

        editor.putString(employeetaggedid, vaeee_);
        editor.commit();


    }



    public String getLeadId() {

        return sharedprefernce.getString(Lead_Id, "");
    }

    public void setFollowId(String follow_id) {

        editor.putString(Follow_Id, follow_id);
        editor.commit();


    }

    public String getFollowId() {

        return sharedprefernce.getString(Follow_Id, "");
    }

    public void setUserName(String name) {
        editor.putString(USERNAME, name);
        editor.commit();

    }

    public String getUserName() {
        return sharedprefernce.getString(USERNAME, "DEFAULT");
    }

    public void setUserPhonenumber(String uphone) {
        editor.putString(USERPHONENUMBER, uphone);
        editor.commit();
    }

    public String getUserPhonenumber() {
        return sharedprefernce.getString(USERPHONENUMBER, "DEFAULT");
    }

    public void setUserEmail(String name) {
        editor.putString(USEREMAIL, name);
        editor.commit();
    }

    public String getUserEmail() {
        return sharedprefernce.getString(USEREMAIL, "DEFAULT");
    }

    public void setUSERcity(String ucity) {
        editor.putString(UserCity, ucity);
        editor.commit();
    }

    public String getUSERcity() {
        return sharedprefernce.getString(UserCity, "DEFAULT");
    }

    public void setUSERType(String utype) {
        editor.putString(UserType, utype);
        editor.commit();
    }

    public String getUSERType() {
        return sharedprefernce.getString(UserType, "DEFAULT");
    }

    public void setUserPassword(String userPass) {

        editor.putString(userPassword, userPass);
        editor.commit();

    }



    public String getUserPassword() {
        return sharedprefernce.getString(userPassword, "DEFAULT");
    }

    public void setSessionId(String sessionid) {

        editor.putString(SessionId, sessionid);
        editor.commit();

    }

    public String getSessionId() {
        return sharedprefernce.getString(SessionId, "DEFAULT");
    }

    public String getPhoto() {
        return sharedprefernce.getString(Photo, "DEFAULT");
    }

    public void setPhoto(String photo) {
        editor.putString(Photo, photo);
        editor.commit();

    }

}


