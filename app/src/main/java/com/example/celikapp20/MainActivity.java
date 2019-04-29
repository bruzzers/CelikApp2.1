package com.example.celikapp20;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.util.SortedList;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private EditText search;
    private Button search_button;
    private ListView listViewDatabase;
    private SharedPreferences pref;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        search=(EditText) findViewById(R.id.search_editText);
        search_button=(Button) findViewById(R.id.search_Button);
        listViewDatabase=(ListView) findViewById(R.id.ListViewDatabase);

        pref=getSharedPreferences("PREFS", MODE_PRIVATE);

        //set listener
        search_button.setOnClickListener(this);
        listViewDatabase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
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
                Intent open= new Intent(this, ListaSpesa.class);
                startActivity(open);
                Log.d("MainActivity", "APRO LISTA SPESA");
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

        } else if (id == R.id.nav_login) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_Button:
                display();
                break;

        }
    }

    private void display() {
            final String text = search.getText().toString();
            myRef=database.getReference();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                   ArrayList<Product> array= new ArrayList<>();
                   array.clear();
                   for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        Prodotto value = ds.getValue(Prodotto.class);
                        if (value.getMarca().equals(text) || value.getNome().contains(text) || value.getLocalita().equals(text)) {
                            Product p= new Product(value.getNome(), value.getMarca(),String.valueOf(value.getPrezzo()), value.getLocalita());
                            array.add(p);

                        }
                    }
                    Collections.sort(array, new Comparator<Product>() {
                        public int compare(Product o1, Product o2) {
                            return o1.getPrezzo().compareTo(o2.getPrezzo());
                        }
                    });
                    ProductListAdapter adapter=new ProductListAdapter(MainActivity.this, R.layout.adapter_view_layout, array );
                    listViewDatabase.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("tag", "Failed to read value.", error.toException());
                }
            });
        }
}
