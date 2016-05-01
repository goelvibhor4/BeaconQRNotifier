package com.example.restaurantnotification;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.estimote.sdk.SystemRequirementsChecker;

import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private Toolbar mToolbar;

    private RecyclerView recyclerView;
    private MenuAdapter cAdapter;
    private List<Item> productsList ;

    //Variable to see if we have entered "onResume" by clicking on the notification or because we only re-opened the application
    private boolean seen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.restaurant_menu_toolbar);
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        int upToDate = getIntent().getIntExtra("notify", 0);
        Log.i(TAG, "upTodDate = " + upToDate);

        if(upToDate==1 && seen==false){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

            // set title
            alertDialogBuilder.setTitle(" WELCOME TO MSIT CANTEEN");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Welcome in our canteen. Read the daily menu to find out what " +
                            "fantastic dishes you can taste here. Thank you for downloading the app, " +
                            "at the time of payment notify the promotional code -CHAI-GARAM to " +
                            "claim your free chai.")
                    .setCancelable(false)
                    .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            updateUI();
                        }
                    })

                    .setNegativeButton("I don't care", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //setContentView(R.layout.activity_main);
                            onBackPressed();
                            seen=true;
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }

        /* With the following line we go through a predefined checklist and makes sure that everything
        is accounted for, including the runtime permissions (and also things like, is Bluetooth on,
        is Location on, etc.)
         */
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    // Other methods -------------------------------------------------------------------------------

    private void updateUI(){
        Log.i(TAG, "updateUI");
        setContentView(R.layout.dishes_of_the_day);

        mToolbar = (Toolbar) findViewById(R.id.restaurant_menu_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Dishes of the day");

        recyclerView = (RecyclerView) findViewById(R.id.restaurant_menu_recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        productsList = new LinkedList<>();

        /*Obviously at this point, you can replace the following lines with an AsyncTask that
        recovers from the cloud the daily menu. */
        productsList.add(new Item(R.drawable.antipasto,"CHOLE BHATURE"," Rs 25"));
        productsList.add(new Item(R.drawable.fritti,"FRENCH FRIES","Rs 25"));
        productsList.add(new Item(R.drawable.pasta,"Pasta","Rs 40"));
        productsList.add(new Item(R.drawable.pizza,"Pizza","Rs 100"));
        productsList.add(new Item(R.drawable.panino,"Sandwich","Rs 25"));
        productsList.add(new Item(R.drawable.insalata,"Salad","Rs 50"));
        productsList.add(new Item(R.drawable.gelato,"Ice cream"," Rs 20"));

        cAdapter = new MenuAdapter(productsList,MainActivity.this);

        recyclerView.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();
    }
}
