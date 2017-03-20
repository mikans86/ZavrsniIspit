package com.example.androiddevelopment.zavrsni.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.androiddevelopment.zavrsni.R;
import com.example.androiddevelopment.zavrsni.db.DataHelper;
import com.example.androiddevelopment.zavrsni.db.model.Brojevi;
import com.example.androiddevelopment.zavrsni.db.model.Kontakt;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import static com.example.androiddevelopment.zavrsni.activity.FirstActivity.NOTIF_STATUS;
import static com.example.androiddevelopment.zavrsni.activity.FirstActivity.NOTIF_TOAST;

public class SecondActivity extends AppCompatActivity {

    private DataHelper databaseHelper;
    private Kontakt k;
    private SharedPreferences prefs;

    private EditText ime;
    private EditText prezime;
    private EditText adresa;
    private String slika;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int key = getIntent().getExtras().getInt(FirstActivity.ACTOR_KEY);

        try {
            k = getDatabaseHelper().getKontaktDao().queryForId(key);

            ime = (EditText) findViewById(R.id.Ime2);
            prezime = (EditText) findViewById(R.id.Prezime2);
            adresa = (EditText) findViewById(R.id.Adresa2);


            ime.setText(k.getmIme());
            prezime.setText(k.getmPrezime());
            adresa.setText(k.getmAdresa());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.brojevi);

        try {
            List<Brojevi> list = getDatabaseHelper().getBrojeviDao().queryBuilder()
                    .where()
                    .eq(Brojevi.FIELD_NAME_USER, k.getmId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Brojevi b = (Brojevi) listView.getItemAtPosition(position);
                    Toast.makeText(SecondActivity.this, b.getBBroj() + " " + b.getBTip() , Toast.LENGTH_SHORT).show();

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();

        }



    }


    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("Pripremni test");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        mBuilder.setLargeIcon(bm);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void showMessage(String message){
        boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
        boolean status = prefs.getBoolean(NOTIF_STATUS, false);

        if (toast){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if (status){
            showStatusMesage(message);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.dodaj_broj:
                //OTVORI SE DIALOG UNESETE INFORMACIJE
                final Dialog dialog = new Dialog(this);
                dialog.setTitle("Dodavanje broja telefona");
                dialog.setContentView(R.layout.broj_dijalog);


                Button add = (Button) dialog.findViewById(R.id.add);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText broj = (EditText) dialog.findViewById(R.id.broj2);
                        EditText tip = (EditText) dialog.findViewById(R.id.tip);



                        Brojevi b = new Brojevi();
                        b.setBBroj(broj.getText().toString());
                        b.setBTip(tip.getText().toString());
                        b.setBUser(k);



                        try {
                            getDatabaseHelper().getBrojeviDao().create(b);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        //URADITI REFRESH
                        refresh();

                        showMessage("Novi broj dodat");

                        dialog.dismiss();
                    }
                });
                final Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;
            case R.id.edit:
                //POKUPITE INFORMACIJE SA EDIT POLJA
                k.setmIme(ime.getText().toString());
                k.setmPrezime(prezime.getText().toString());
                k.setmAdresa(adresa.getText().toString());

                try {
                    getDatabaseHelper().getKontaktDao().update(k);

                    showMessage("Dodati podaci o kontaktu");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.remove:
                try {
                    getDatabaseHelper().getKontaktDao().delete(k);

                    showMessage("Kontakt izbrisan");

                    finish(); //moramo pozvati da bi se vratili na prethodnu aktivnost
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
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

