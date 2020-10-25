package com.android.watersprinkle;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User  {

    private String CompanyName;
    private String Address;
    private String MobileNo;
    private String selSt;
    private String selCy;
    public Map<String, Boolean> stars = new HashMap<>();
    public User(){
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("companyName", CompanyName);
        result.put("address", Address);
        result.put("mobileNo", MobileNo);
        result.put("selCy",selCy);
        result.put("selSt",selSt);
        result.put("stars", stars);

        return result;
    }
    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getAddress() {
        return Address;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public String getSelSt() {
        return selSt;
    }

    public void setSelSt(String selSt) {
        this.selSt = selSt;
    }

    public String getSelCy() {
        return selCy;
    }

    public void setSelCy(String selCy) {
        this.selCy = selCy;
    }

    public User(String CompanyName, String Address, String MobileNo, String selSt, String selCy){
        this.CompanyName = CompanyName;
        this.Address = Address;
        this.MobileNo = MobileNo;
        this.selSt=selSt;
        this.selCy=selCy;
    }
    public User(String CompanyName, String Address, String MobileNo){
        this.CompanyName = CompanyName;
        this.Address = Address;
        this.MobileNo = MobileNo;
     }
}
