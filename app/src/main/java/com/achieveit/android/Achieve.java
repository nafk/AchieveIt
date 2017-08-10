package com.achieveit.android;

import org.litepal.crud.DataSupport;

/**
 * Created by YT on 17/8/10/010.
 */

public class Achieve extends DataSupport{

    private String name;
    private int done;
    private int total;
    private String startDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
