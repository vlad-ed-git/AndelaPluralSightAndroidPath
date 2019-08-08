package com.dev.vlad.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dev.vlad.travelmantics.Utils.FirebaseUtil;
import com.dev.vlad.travelmantics.Utils.TravelDeals;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class TravelDealActivity extends AppCompatActivity implements OnCompleteListener<DocumentSnapshot> {

    private EditText title_et, price_et, descrption_et;
    private Button upload_img_btn;
    private ImageView deal_img_iv;
    public final static String PASSED_DEAL  = "clickedDeal";
    private TravelDeals travelDeal;
    private final  int DEAL_IMG_REQUEST_CODE = 42;
    private boolean uploading_image = false;
    private Menu menu;
    private Uri img_to_upload_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_deal);
        //check admin status
        FirebaseUtil.checkIfAdmin(this);

        title_et = findViewById(R.id.title_et);
        price_et = findViewById(R.id.price_et);
        descrption_et = findViewById(R.id.description_et);
        deal_img_iv = findViewById(R.id.deal_img_iv);

        travelDeal = getIntent().getParcelableExtra(PASSED_DEAL);
        if(travelDeal == null)
            travelDeal = new TravelDeals();

        title_et.setText(travelDeal.getTitle());
        descrption_et.setText(travelDeal.getDescription());
        price_et.setText(travelDeal.getPrice());
        String img_uri_str = travelDeal.getImageUri();
        if( img_uri_str != null ){
            Uri img_uri = Uri.parse(img_uri_str);
            Picasso.get().load(img_uri).into(deal_img_iv);
        }

        upload_img_btn = findViewById(R.id.upload_img_btn);
        upload_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!uploading_image) {
                    startActivityForResult(Intent.createChooser((new Intent(Intent.ACTION_GET_CONTENT)).setType("image/jpeg").putExtra(Intent.EXTRA_LOCAL_ONLY, true), "Insert Deal Image"), DEAL_IMG_REQUEST_CODE);
                }else{
                    Toast.makeText(TravelDealActivity.this, R.string.please_wait, Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.travel_deal_menu, menu);
        this.menu = menu;
        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu_item:
                if(!uploading_image) {
                    saveDeal();
                }else{
                    Toast.makeText(TravelDealActivity.this, R.string.please_wait, Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.delete_menu_item:
                if(!uploading_image) {
                    if (travelDeal.getImageUri() != null) {
                        deleteDealImage();
                        Toast.makeText(TravelDealActivity.this, R.string.deal_deleted_wait, Toast.LENGTH_LONG).show();
                    }else {
                        deleteDeal();
                    }
                }else{
                    Toast.makeText(TravelDealActivity.this, R.string.please_wait, Toast.LENGTH_LONG).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void saveDeal() {
            String title = title_et.getText().toString();
            String price = price_et.getText().toString();
            String desc = descrption_et.getText().toString();

          if(title.length() > 1 && price.length() > 1 && desc.length() > 1) {
              travelDeal.setTitle(title);
              travelDeal.setPrice(price);
              travelDeal.setDescription(desc);

              if (travelDeal.getId() == null) {
                  createNewDeal();
              } else {
                  if(img_to_upload_uri != null)
                      uploadImageToServer(img_to_upload_uri);
                  else
                      updateExistingDeal();
              }

          }else{
              Toast.makeText(TravelDealActivity.this, R.string.provide_deal_info, Toast.LENGTH_LONG).show();
          }
    }


    private void createNewDeal() {
        FirebaseUtil.openFbDbRef("TravelDeals", TravelDealActivity.this);
        DocumentReference newDealRef = FirebaseUtil.travelDealsRef.document();
        travelDeal.setId(newDealRef.getId());
        newDealRef.set(travelDeal,  SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TravelDealActivity.this, R.string.deal_saved_success, Toast.LENGTH_LONG).show();
                        if(img_to_upload_uri != null)
                            uploadImageToServer(img_to_upload_uri);
                        else
                            resetEditTexts();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TravelDealActivity.this, R.string.deal_saved_err, Toast.LENGTH_LONG).show();
                    }
                });

    }


    private void updateExistingDeal() {
        FirebaseUtil.openFbDbRef("TravelDeals", TravelDealActivity.this);
        DocumentReference newDealRef = FirebaseUtil.travelDealsRef.document(travelDeal.getId());
        newDealRef.set(travelDeal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TravelDealActivity.this, R.string.deal_saved_success, Toast.LENGTH_LONG).show();
                        resetEditTexts();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TravelDealActivity.this, R.string.deal_saved_err, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void deleteDeal() {

        FirebaseUtil.openFbDbRef("TravelDeals", TravelDealActivity.this);
        DocumentReference newDealRef = FirebaseUtil.travelDealsRef.document(travelDeal.getId());
        newDealRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                            Toast.makeText(TravelDealActivity.this, R.string.deal_deleted_success, Toast.LENGTH_LONG).show();
                            resetEditTexts();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TravelDealActivity.this, R.string.deal_deleted_err, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void deleteDealImage() {
        // Create a storage reference from our app
        final StorageReference travelDealsPicsRef =  FirebaseUtil.connectStorage("TravelDealsPics/deal_" + travelDeal.getId() + ".jpg");

        travelDealsPicsRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                deleteDeal();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
                Toast.makeText(TravelDealActivity.this, R.string.img_deleted_err, Toast.LENGTH_LONG).show();

            }
        });
    }


    private void resetEditTexts(){
        title_et.setText("");
        price_et.setText("");
        descrption_et.setText("");
        backToDealLists();
    }

    private void backToDealLists(){
        startActivity(new Intent(TravelDealActivity.this, ListActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DEAL_IMG_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                uploading_image = false;
                img_to_upload_uri = data.getData();
                Picasso.get().load(img_to_upload_uri).into(deal_img_iv);

            }
        }
    }

    private void uploadImageToServer(Uri img_uri){
        final StorageReference travelDealsPicsRef =  FirebaseUtil.connectStorage("TravelDealsPics/deal_" + travelDeal.getId() + ".jpg");
        UploadTask uploadTask = travelDealsPicsRef.putFile(img_uri);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task){
                if (!task.isSuccessful()) {
                    // Handle unsuccessful uploads
                    uploading_image = false;
                    if(task.getException() != null) {
                        Log.d("TravelDealActvity", "Uploading deal image exception : " + task.getException().getMessage());
                    }
                    Toast.makeText(TravelDealActivity.this, R.string.img_uploading_err, Toast.LENGTH_LONG).show();

                }

                // Continue with the task to get the download URL
                return travelDealsPicsRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploading_image = false;
                if (task.isSuccessful() && task.getResult() != null) {
                    travelDeal.setImageUri(task.getResult().toString());
                    Toast.makeText(TravelDealActivity.this, R.string.img_uploading_success, Toast.LENGTH_LONG).show();
                    updateExistingDeal();
                } else {
                    // Handle failures
                    if(task.getException() != null) {
                        Log.d("TravelDealActvity", "Uploading deal image exception : " + task.getException().getMessage());
                    }
                    Toast.makeText(TravelDealActivity.this, R.string.img_uploading_err, Toast.LENGTH_LONG).show();

                }
            }
        });


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });

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
        menu.findItem(R.id.delete_menu_item).setVisible(isAdmin);
        menu.findItem(R.id.save_menu_item).setVisible(isAdmin);
        title_et.setEnabled(isAdmin);
        price_et.setEnabled(isAdmin);
        descrption_et.setEnabled(isAdmin);
        upload_img_btn.setEnabled(isAdmin);
    }
}
