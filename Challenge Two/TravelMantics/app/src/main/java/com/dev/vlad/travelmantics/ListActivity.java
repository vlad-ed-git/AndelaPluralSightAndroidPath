package com.dev.vlad.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dev.vlad.travelmantics.Adapters.TravelDealsAdapter;
import com.dev.vlad.travelmantics.Utils.FirebaseUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;


public class ListActivity extends AppCompatActivity implements OnCompleteListener<DocumentSnapshot> {


    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_activity_menu, menu);
        this.menu = menu;
        return true;

    }

    //checking for admin privileges
    @Override
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document != null && document.exists()) {
                FirebaseUtil.isAdmin = true;
                toggleAdminPriviledges(true);
            }else{
                toggleAdminPriviledges(false);
            }
        } else {

            Log.d("Checking if admin :", "failed with exception " + task.getException());
        }
    }

    private void toggleAdminPriviledges(boolean isAdmin) {
        menu.findItem(R.id.insert_menu_item).setVisible(isAdmin);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.insert_menu_item:
                startActivity(new Intent(ListActivity.this, TravelDealActivity.class));
                return true;

            case R.id.logout_menu_item:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseUtil.attachListener(); //to log in screen when done
                            }
                        });
                FirebaseUtil.detachListener();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.openFbDbRef("TravelDeals", ListActivity.this);
        FirebaseUtil.attachListener();
        FirebaseUtil.checkIfAdmin(this);
        displayDeals();
    }

    private void displayDeals(){
        RecyclerView travel_deals_rv = findViewById(R.id.travel_deals_rv);

        TravelDealsAdapter travelDealsAdapter =  new TravelDealsAdapter();
        travel_deals_rv.setAdapter(travelDealsAdapter);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(ListActivity.this, RecyclerView.VERTICAL, false);
        travel_deals_rv.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListener();
    }
}
