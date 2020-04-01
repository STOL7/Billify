package com.example.billify;

public class friend_history
{
    private String user_id;
    private String history_id;
    private String opp_id;

    public String getOpp_id() {
        return opp_id;
    }

    public void setOpp_id(String opp_id) {
        this.opp_id = opp_id;
    }

    friend_history()
    {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHistory_id() {
        return history_id;
    }

    public void setHistory_id(String history_id) {
        this.history_id = history_id;
    }

    public long getPaid() {
        return paid;
    }

    public void setPaid(long paid) {
        this.paid = paid;
    }

    private long paid;
}
