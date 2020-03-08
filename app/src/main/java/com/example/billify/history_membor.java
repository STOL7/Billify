package com.example.billify;

public class history_membor
{
    String id;
    String paid;
    String expense;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    history_membor()
    {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }
}
