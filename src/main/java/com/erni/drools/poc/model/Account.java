package com.erni.drools.poc.model;


import java.math.BigDecimal;

public class Account implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    private long balance;

    private long user;

    public Account() {
    }

    public long getBalance() {
        return this.balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public java.lang.Long getUser() {
        return this.user;
    }

    public void setUser(java.lang.Long user) {
        this.user = user;
    }

    public Account(long balance, long user) {
        this.balance = balance;
        this.user = user;
    }

    public void withdraw(long money) {
        balance -= money;
    }

}