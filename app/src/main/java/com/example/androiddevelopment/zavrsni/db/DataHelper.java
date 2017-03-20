package com.example.androiddevelopment.zavrsni.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.androiddevelopment.zavrsni.db.model.Brojevi;
import com.example.androiddevelopment.zavrsni.db.model.Kontakt;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by androiddevelopment on 20.3.17..
 */

public class DataHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME    = "priprema.db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<Brojevi, Integer> mBrojeviDao = null;
    private Dao<Kontakt, Integer> mKontaktDao = null;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Brojevi.class);
            TableUtils.createTable(connectionSource, Kontakt.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Brojevi.class, true);
            TableUtils.dropTable(connectionSource, Kontakt.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Brojevi, Integer> getBrojeviDao() throws SQLException {
        if (mBrojeviDao == null) {
            mBrojeviDao = getDao(Brojevi.class);
        }

        return mBrojeviDao;
    }

    public Dao<Kontakt, Integer> getKontaktDao() throws SQLException {
        if (mKontaktDao == null) {
            mKontaktDao = getDao(Kontakt.class);
        }

        return mKontaktDao;
    }

    //obavezno prilikom zatvarnaj rada sa bazom osloboditi resurse
    @Override
    public void close() {
        mBrojeviDao = null;
        mKontaktDao = null;

        super.close();
    }
}
