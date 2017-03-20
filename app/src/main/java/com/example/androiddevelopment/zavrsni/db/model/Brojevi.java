package com.example.androiddevelopment.zavrsni.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by androiddevelopment on 20.3.17..
 */
@DatabaseTable(tableName = Brojevi.TABLE_NAME_USERS)
public class Brojevi {

    public static final String TABLE_NAME_USERS = "brojevi";
    public static final String FIELD_NAME_ID   = "id";
    public static final String FIELD_NAME_TIP = "tip";
    public static final String FIELD_NAME_BROJ = "broj";
    public static final String FIELD_NAME_USER  = "user";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int BId;

    @DatabaseField(columnName = FIELD_NAME_TIP)
    private String BTip;

    @DatabaseField(columnName = FIELD_NAME_BROJ)
    private String BBroj;

    @DatabaseField(columnName = FIELD_NAME_USER, foreign = true, foreignAutoRefresh = true)
    private Kontakt BUser;

    public Brojevi (){

    }

    public int getBId() {
        return BId;
    }

    public void setBId(int BId) {
        this.BId = BId;
    }

    public String getBTip() {
        return BTip;
    }

    public void setBTip(String BTip) {
        this.BTip = BTip;
    }

    public String getBBroj() {
        return BBroj;
    }

    public void setBBroj(String BBroj) {
        this.BBroj = BBroj;
    }

    public Kontakt getBUser() {
        return BUser;
    }

    public void setBUser(Kontakt BUser) {
        this.BUser = BUser;
    }

    @Override
    public String toString() {
        return BBroj;
    }
}
