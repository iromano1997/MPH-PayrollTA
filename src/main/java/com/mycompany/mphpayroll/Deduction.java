/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mphpayroll;

abstract class Deduction {
    private String name;

    public Deduction(String name) {
        this.name = name;
    }

    public abstract double calculate(double amount);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
