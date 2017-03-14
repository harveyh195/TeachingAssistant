package com.hmsdevelopment.teachingassistant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Validation {
    
    private String username;
    private String password;
    private final int DB_USERNAME_COLUMN = 2;
    private final int DB_PASSWORD_COLUMN = 3;
    private final int DB_TYPE_COLUMN = 4;
    private ResultSet results;
    
    public Validation(String username, String password) {
        this.username = username;
        this.password = password;
        
        try {
            connectToDB();
        } catch (Exception ex) {
            Logger.getLogger(Validation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void connectToDB() throws Exception {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/teachingassistant", "root", "");
        Statement test = con.createStatement();
        results = test.executeQuery("select * from credentials");
        results.first();
        
        // Check username
        if (!checkUsername(DB_USERNAME_COLUMN, username)) {
            validationFailure();
        } else {
            // Check password
            if (!results.getString(DB_PASSWORD_COLUMN).equalsIgnoreCase(password)) {
                validationFailure();
            } else {
                // Check user type
                if (results.getString(DB_TYPE_COLUMN).equalsIgnoreCase("S")) {
                    System.out.println("Valid Credentials - User is a Student");
                    /*
                    StudentScreen student = new StudentScreen(username);
                    student.setVisible(true);
                    */
                } else if (results.getString(DB_TYPE_COLUMN).equalsIgnoreCase("T")) {
                    System.out.println("Valid Credentials - User is a Teacher");
                    /*
                    TeacherScreen teacher = new TeacherScreen(username);
                    teacher.setVisible(true);
                    */
                }
            }
        }
        con.close();
    }
    
    private boolean checkUsername(int usernameColumn, String username) throws Exception {
        while (!results.isAfterLast()) {
            if (results.getString(usernameColumn).equalsIgnoreCase(username)) {
                return true;
            } else {
                results.next();
            }
        }
        return false;
    }
        
    private void validationFailure() {
        LoginScreen loginScreen = new LoginScreen(false);
        loginScreen.setVisible(true);
    }
    
}
