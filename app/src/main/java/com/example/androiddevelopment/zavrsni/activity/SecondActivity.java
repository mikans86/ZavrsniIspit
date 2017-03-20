package com.example.androiddevelopment.zavrsni.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.androiddevelopment.zavrsni.R;
import com.example.androiddevelopment.zavrsni.db.DataHelper;
import com.example.androiddevelopment.zavrsni.db.model.Brojevi;
import com.example.androiddevelopment.zavrsni.db.model.Kontakt;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private DataHelper databaseHelper;
    private Kontakt k;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.brojevi);

        if (listview != null){
            ArrayAdapter<Brojevi> adapter = (ArrayAdapter<Brojevi>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Brojevi> list = getDatabaseHelper().getBrojeviDao().queryBuilder()
                            .where()
                            .eq(Brojevi.FIELD_NAME_USER, k.getmId())
                            .query();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public DataHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DataHelper.class);
        }
        return databaseHelper;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}

