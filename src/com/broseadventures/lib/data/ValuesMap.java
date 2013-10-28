package com.broseadventures.lib.data;

import java.util.ArrayList;

public class ValuesMap {

    public ValuesMap() {
        this.list = new ArrayList<StrValue>();
    }

    private ArrayList<StrValue> list;

    public StrValue[] GetAll() {
        return this.list.toArray(new StrValue[this.list.size()]);
    }

    public <T> void Add(String k, T v) {
        this.list.add(new StrValue(k, v.toString()));
    }

    public void Add(String k, String v) {
        this.list.add(new StrValue(k, v));
    }

    public void Add(StrValue tuple) {
        this.list.add(tuple);
    }

    @Override
    public String toString() {
        String r = "";
        for (StrValue v : this.list) {
            r += "( " + v.key + ", " + v.value + " ) ";
        }
        return r;
    }

}
