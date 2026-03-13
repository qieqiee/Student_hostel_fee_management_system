/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.hostel_management_system;

public class HostelFeeRecord {
    private String name;
    private int months;
    private String roomType;
    private double totalMonthly;
    private double totalStay;
    private double discount;
    private double finalPayable;

    public HostelFeeRecord(String name, int months, String roomType,double totalMonthly, double totalStay,double discount, double finalPayable) {
        this.name = name;
        this.months = months;
        this.roomType = roomType;
        this.totalMonthly = totalMonthly;
        this.totalStay = totalStay;
        this.discount = discount;
        this.finalPayable = finalPayable;
    }

    public String getName() {
        return name;
    }

    public int getMonths() {
        return months;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getTotalMonthly() {
        return totalMonthly;
    }

    public double getTotalStay() {
        return totalStay;
    }

    public double getDiscount() {
        return discount;
    }

    public double getFinalPayable() {
        return finalPayable;
    }
}