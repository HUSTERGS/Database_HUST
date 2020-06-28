package net.samge.model;

public class User {

    private long uid;
    private long isAdmin;
    private String email;
    private String password;
    private String username;
    private String idCardNum;
    private String phoneNum;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }


    public long getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(long isAdmin) {
        this.isAdmin = isAdmin;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }


    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public User(long uid, long isAdmin, String email, String password, String username, String idCardNum, String phoneNum) {
        this.uid = uid;
        this.isAdmin = isAdmin;
        this.email = email;
        this.password = password;
        this.username = username;
        this.idCardNum = idCardNum;
        this.phoneNum = phoneNum;
    }


}
