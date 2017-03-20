package com.example.androiddevelopment.zavrsni.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by androiddevelopment on 20.3.17..
 */
@DatabaseTable(tableName = Kontakt.TABLE_NAME_USERS)
public class Kontakt {
    public static final String TABLE_NAME_USERS = "actor";
    public static final String FIELD_NAME_ID     = "id";
    public static final String FIELD_NAME_IME = "ime";
    public static final String FIELD_NAME_PREZIME = "prezime";
    public static final String FIELD_NAME_ADRESA = "adresa";
    public static final String FIELD_NAME_SLIKA = "slika";
    public static final String FIELD_NAME_BROJEVI = "brojevi";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_IME)
    private String mIme;

    @DatabaseField(columnName = FIELD_NAME_PREZIME)
    private String mPrezime;

    @DatabaseField(columnName = FIELD_NAME_SLIKA)
    private String mSlika;

    @DatabaseField(columnName = FIELD_NAME_ADRESA)
    private String mAdresa;

    @ForeignCollectionField(columnName = Kontakt.FIELD_NAME_BROJEVI, eager = true)
    private ForeignCollection<Brojevi> brojevi;

    public Kontakt (){

    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmIme() {
        return mIme;
    }

    public void setmIme(String mIme) {
        this.mIme = mIme;
    }

    public String getmPrezime() {
        return mPrezime;
    }

    public void setmPrezime(String mPrezime) {
        this.mPrezime = mPrezime;
    }

    public String getmAdresa() {
        return mAdresa;
    }

    public String getmSlika() {
        return mSlika;
    }

    public void setmSlika(String mSlika) {
        this.mSlika = mSlika;
    }

    public void setmAdresa(String mAdresa) {
        this.mAdresa = mAdresa;
    }

    public ForeignCollection<Brojevi> getBrojevi() {
        return brojevi;
    }

    public void setBrojevi(ForeignCollection<Brojevi> brojevi) {
        this.brojevi = brojevi;
    }

    @Override
    public String toString() {
        return mIme;
    }
}
