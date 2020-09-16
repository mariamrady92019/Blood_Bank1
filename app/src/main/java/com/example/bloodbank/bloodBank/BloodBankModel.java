package com.example.bloodbank.bloodBank;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BloodBankModel implements Serializable {

    private String id;
    private String name;
    private String adress;
    private String city;
    private String area;
    private boolean exist;
    private long numberOf_O;
    private long numberOf_AB;
    private long numberOf_A;
    private long numberOf_B;
    private long numberOf_O2;
    private long numberOf_AB2;
    private long numberOf_A2;
    private long numberOf_B2;
    private List<Map<String, Object>> requests;
    private List<String> donors;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return adress;
    }

    public void setAddress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public long getNumberOf_O() {
        return numberOf_O;
    }

    public void setNumberOf_O(long numberOf_O) {
        this.numberOf_O = numberOf_O;
    }

    public long getNumberOf_AB() {
        return numberOf_AB;
    }

    public void setNumberOf_AB(long numberOf_AB) {
        this.numberOf_AB = numberOf_AB;
    }

    public long getNumberOf_A() {
        return numberOf_A;
    }

    public void setNumberOf_A(long numberOf_A) {
        this.numberOf_A = numberOf_A;
    }

    public long getNumberOf_B() {
        return numberOf_B;
    }

    public void setNumberOf_B(long numberOf_B) {
        this.numberOf_B = numberOf_B;
    }

    public long getNumberOf_O2() {
        return numberOf_O2;
    }

    public void setNumberOf_O2(long numberOf_O2) {
        this.numberOf_O2 = numberOf_O2;
    }

    public long getNumberOf_AB2() {
        return numberOf_AB2;
    }

    public void setNumberOf_AB2(long numberOf_AB2) {
        this.numberOf_AB2 = numberOf_AB2;
    }

    public long getNumberOf_A2() {
        return numberOf_A2;
    }

    public void setNumberOf_A2(long numberOf_A2) {
        this.numberOf_A2 = numberOf_A2;
    }

    public long getNumberOf_B2() {
        return numberOf_B2;
    }

    public void setNumberOf_B2(long numberOf_B2) {
        this.numberOf_B2 = numberOf_B2;
    }


    public List<Map<String, Object>> getRequests() {
        return requests;
    }

    public void setRequests(List<Map<String, Object>> requests) {
        this.requests = requests;
    }

    public List<String> getDonors() {
        return donors;
    }

    public void setDonors(List<String> donors) {
        this.donors = donors;
    }
}
