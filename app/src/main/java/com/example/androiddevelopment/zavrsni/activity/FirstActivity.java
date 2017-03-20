package com.example.androiddevelopment.zavrsni.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
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
import com.example.androiddevelopment.zavrsni.db.model.Kontakt;
import com.example.androiddevelopment.zavrsni.dialog.AboutDialog;
import com.example.androiddevelopment.zavrsni.preferenc.Preferences;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {

    private DataHelper databaseHelper;
    private SharedPreferences prefs;

    public static String ACTOR_KEY = "ACTOR_KEY";
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_statis";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final ListView listView = (ListView) findViewById(R.id.kontakti_list);

        try {
            List<Kontakt> list = getDatabaseHelper().getKontaktDao().queryForAll();

            ListAdapter adapter = new ArrayAdapter<>(FirstActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Kontakt k = (Kontakt) listView.getItemAtPosition(position);

                    Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                    intent.putExtra(ACTOR_KEY, k.getmId());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("Zavrsni");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        mBuilder.setLargeIcon(bm);

        mNotificationManager.notify(1, mBuilder.build());
    }



    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.kontakti_list);

        if (listview != null){
            ArrayAdapter<Kontakt> adapter = (ArrayAdapter<Kontakt>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Kontakt> list = getDatabaseHelper().getKontaktDao().queryForAll();

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
            case R.id.dodaj_kotakt:
                //DIALOG ZA UNOS PODATAKA
                final Dialog dialog = new Dialog(this);
                dialog.setTitle("Dodavanje kontakta");
                dialog.setContentView(R.layout.kotakt_dijalog);


                final Spinner imagesSpinner = (Spinner) dialog.findViewById(R.id.Slika);
                List<String> imagesList = new ArrayList<String>();
                imagesList.add("mehanicar.jpg");
                imagesList.add("rocky.jpg");
                imagesList.add("terminator.jpg");
                ArrayAdapter<String> imagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, imagesList);
                imagesSpinner.setAdapter(imagesAdapter);
                imagesSpinner.setSelection(0);

                Button add = (Button) dialog.findViewById(R.id.add);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText ime = (EditText) dialog.findViewById(R.id.Ime);
                        EditText prezime = (EditText) dialog.findViewById(R.id.Prezime);
                        EditText adresa = (EditText) dialog.findViewById(R.id.Adresa);
                        String slika = (String) imagesSpinner.getSelectedItem();


                        Kontakt k = new Kontakt();
                        k.setmIme(ime.getText().toString());
                        k.setmPrezime(prezime.getText().toString());
                        k.setmAdresa(adresa.getText().toString());
                        k.setmSlika(slika);

                        try {
                            getDatabaseHelper().getKontaktDao().create(k);

                            //provera podesavanja
                            boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
                            boolean status = prefs.getBoolean(NOTIF_STATUS, false);

                            if (toast){
                                Toast.makeText(FirstActivity.this, "Dodat novi kontakt", Toast.LENGTH_SHORT).show();
                            }

                            if (status){
                                showStatusMesage("Dodat novi kontakt");
                            }

                            //REFRESH
                            refresh();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

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
            case R.id.about:

                AlertDialog alertDialog = new AboutDialog(this).prepareDialog();
                alertDialog.show();
                break;
            case R.id.preferences:
                startActivity(new Intent(FirstActivity.this, Preferences.class));
                break;
        }

        return super.onOptionsItemSelected(item);
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
