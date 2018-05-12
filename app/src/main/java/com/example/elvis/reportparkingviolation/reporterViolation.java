package com.example.elvis.reportparkingviolation;

/**
 * Elvis Gu, May 2018
 */
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
/**
 * set up a reporterVioaltion class which is pointed to appUser
 * */
public class reporterViolation extends BmobObject {

    private static final long serialVersionUID = 1L;

    private String reportTitleandType;

    private String plateNumber;

    private BmobGeoPoint LLlocation;

    private String delatedLoc;

    private appUser reporter;

    private Boolean isDealt;

    public appUser getReporter() {
        return reporter;
    }

    public void setReporter(appUser reporter) {
        this.reporter = reporter;
    }



    public BmobGeoPoint getLLlocation() {
        return LLlocation;
    }

    public void setLLlocation(BmobGeoPoint LLlocation) {
        this.LLlocation = LLlocation;
    }

    public String getReportTitleandType() {
        return reportTitleandType;
    }

    public void setReportTitleandType(String reportTitleandType) {
        this.reportTitleandType = reportTitleandType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Boolean getDealt() {
        return isDealt;
    }

    public void setDealt(Boolean dealt) {
        isDealt = dealt;
    }

    public String getDelatedLoc() {
        return delatedLoc;
    }

    public void setDelatedLoc(String delatedLoc) {
        this.delatedLoc = delatedLoc;
    }
}


