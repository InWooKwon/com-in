package com.example.comin;


import java.io.Serializable;
import java.util.ArrayList;

class Coverage implements Serializable{
    String type;
    int amount;
    String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
public class Insurance implements Serializable {

    private int idx;
    private String productName;
    private String company;
    private String productType;
    private int minAge;
    private int maxAge;
    private int price;
    private double score;
    private ArrayList<Coverage> coverageList;

    public ArrayList<Coverage> getCoverageList() {
        return coverageList;
    }

    public void setCoverageList(ArrayList<Coverage> coverageList) {
        this.coverageList = coverageList;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }



}
