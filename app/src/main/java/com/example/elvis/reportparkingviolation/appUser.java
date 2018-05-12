package com.example.elvis.reportparkingviolation;
/**
 * Elvis Gu, May 2018
 * set up a appUser class
 */
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class appUser extends BmobUser {


    private Integer age;
    private Boolean isPolice;
    private Boolean sex;
    BmobFile avatar;


    public Boolean getSex() {
        return sex;
    }
    public void setSex(Boolean sex) {
        this.sex = sex;
    }


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return getUsername()+"\n"+getObjectId()+"\n"+age+"\n"+getSessionToken()+"\n"+getEmailVerified();
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public Boolean getPolice() {
        return isPolice;
    }

    public void setPolice(Boolean police) {
        isPolice = police;
    }
}

