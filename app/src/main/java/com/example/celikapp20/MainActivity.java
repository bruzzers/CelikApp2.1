package com.example.celikapp20;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ListView listViewDatabase;
    private SearchView sv;
    private LinearLayout ll;
    private TextView tv;
    private ProgressBar pb;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    MapsDB db= new MapsDB(this);

    private static final int PREZZO=0;
    private static final int NOME=1;
    private static final int LOCALITA=2;
    private static final int TUTTO=0;
    private static final int NOME1=1;
    private static final int LOCALITA1=2;
    private static final int MARCA1=3;


    private SharedPreferences pref;
    private int ordine=PREZZO;
    private int metodo=TUTTO;


    @Override
    protected void onResume() {
        super.onResume();
        ordine=Integer.parseInt(pref.getString("pref_ordine", "0"));
        metodo=Integer.parseInt(pref.getString("pref_ricerca", "0"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String newString=null;
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            newString=extras.getString("nome");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sv=(SearchView) findViewById(R.id.searchview);
        listViewDatabase=(ListView) findViewById(R.id.ListViewDatabase);
        ll=(LinearLayout) findViewById(R.id.table);
        tv=(TextView) findViewById(R.id.TextViewEmpty);
        pb=(ProgressBar) findViewById(R.id.progress_bar);
        sv.setQuery(newString, false);

        PreferenceManager.setDefaultValues(this, R.xml.activity_settings, false);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        db.clean();





        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (InternetConnection.haveInternetConnection(MainActivity.this)) {

                    display();

                }
                else {
                    Dialog alertd=new Dialog(MainActivity.this);
                    alertd.setTitle("Nessuna Connessione a Internet");
                    alertd.setCancelable(false);
                    alertd.setContentView(R.layout.no_internet_dialog_content);
                    alertd.show();
                    Button b=(Button) alertd.findViewById(R.id.dialog_button);
                    b.setOnClickListener(
                             new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent p=new Intent(MainActivity.this, MainActivity.class);
                            startActivity(p);
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                Intent open=new Intent(this, Settings.class);
                startActivity(open);
                break;
            case R.id.action_about:
                Dialog d=new Dialog(this);
                        d.setTitle("About");
                        d.setCancelable(true);
                        d.setContentView(R.layout.dialog_content);
                        d.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lista_spesa) {
            Intent open= new Intent(this, ListaSpesa.class);
            startActivity(open);
        } else if (id == R.id.nav_impostazioni) {
            Intent open=new Intent(this, Settings.class);
            startActivity(open);
        } else if (id==R.id.nav_barcode){
            Intent open= new Intent(this, BarcodeReader.class);
            startActivity(open);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void display() {
            final String text = sv.getQuery().toString();
            tv.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
            myRef=database.getReference();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                   final ArrayList<Product> array= new ArrayList<>();
                   array.clear();
                   for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        Prodotto value = ds.getValue(Prodotto.class);
                        if (metodo == TUTTO) {
                            if (value.getMarca().contains(text) || value.getNome().contains(text) || value.getLocalita().contains(text) || String.valueOf(value.getCodice()).equals(text)) {
                                Product p = new Product(value.getNome(), value.getMarca(), String.valueOf(value.getPrezzo()), value.getLocalita(), String.valueOf(value.getCodice()), String.valueOf(value.getLatitudine()), String.valueOf(value.getLongitudine()));
                                array.add(p);

                            }
                        }
                        else if(metodo==NOME1){
                            if (value.getNome().contains(text)) {
                                Product p = new Product(value.getNome(), value.getMarca(), String.valueOf(value.getPrezzo()), value.getLocalita(), String.valueOf(value.getCodice()), String.valueOf(value.getLatitudine()), String.valueOf(value.getLongitudine()));
                                array.add(p);
                            }
                        }
                        else if(metodo==LOCALITA1){
                            if (value.getLocalita().contains(text)) {
                                Product p = new Product(value.getNome(), value.getMarca(), String.valueOf(value.getPrezzo()), value.getLocalita(), String.valueOf(value.getCodice()), String.valueOf(value.getLatitudine()), String.valueOf(value.getLongitudine()));
                                array.add(p);
                            }
                        }
                        else if(metodo==MARCA1){
                            if (value.getMarca().contains(text)) {
                                Product p = new Product(value.getNome(), value.getMarca(), String.valueOf(value.getPrezzo()), value.getLocalita(), String.valueOf(value.getCodice()), String.valueOf(value.getLatitudine()), String.valueOf(value.getLongitudine()));
                                array.add(p);
                            }
                        }
                    }
                   if(ordine==PREZZO) {
                       Collections.sort(array, new Comparator<Product>() {
                           public int compare(Product o1, Product o2) {
                               return o1.getPrezzo().compareTo(o2.getPrezzo());
                           }
                       });
                   }
                   else if(ordine==NOME){
                       Collections.sort(array, new Comparator<Product>() {
                           @Override
                           public int compare(Product o1, Product o2) {
                               return o1.getNome().compareTo(o2.getNome());
                           }
                       });
                   }
                   else if(ordine==LOCALITA){
                       Collections.sort(array, new Comparator<Product>() {
                           @Override
                           public int compare(Product o1, Product o2) {
                               return o1.getLocalita().compareTo(o2.getLocalita());
                           }
                       });
                   }
                    if(array.isEmpty()){
                        Toast.makeText(MainActivity.this, "Nessun prodotto trovato", Toast.LENGTH_SHORT).show();
                        tv.setVisibility(View.GONE);
                        pb.setVisibility(View.GONE);
                    }
                    else{
                    tv.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                    ProductListAdapter adapter=new ProductListAdapter(MainActivity.this, R.layout.adapter_view_layout, array );
                    pb.setVisibility(View.GONE);
                    listViewDatabase.setAdapter(adapter);

                    listViewDatabase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            db.insert(array.get(position).getNome(),array.get(position).getLatitudine(), array.get(position).getLongitudine(), array.get(position).getLocalita() );
                            Intent open=new Intent(MainActivity.this, MapsActivity.class);
                            startActivity(open);
                        }
                    });}
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("tag", "Failed to read value.", error.toException());
                }
            });

        }

}
