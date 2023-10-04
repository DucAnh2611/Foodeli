package com.example.foodeli.Activities.Find;

public class Filter {
    private String key;
    private float min, max;
    private int cid, page;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "key='" + key + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", cid=" + cid +
                ", page=" + page +
                '}';
    }
}
