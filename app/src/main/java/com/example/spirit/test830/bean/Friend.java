package com.example.spirit.test830.bean;

import android.support.annotation.NonNull;

import com.example.spirit.test830.utils.SpellUtil;

public class Friend implements Comparable<Friend> {
    private String name;
    private String pinYin;

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public Friend(String name) {
        this.name = name;

        setPinYin(SpellUtil.getSpell(name));
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Friend o) {
        return getPinYin().compareTo(o.getPinYin());
    }
}
