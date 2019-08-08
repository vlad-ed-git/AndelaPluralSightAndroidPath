package com.dev.vlad.travelmantics.Utils;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dev.vlad.travelmantics.TravelDealActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    private static final int RC_SIGN_IN = 1112;
    public static FirebaseFirestore firestoreDb;
    public static ArrayList<TravelDeals> travelDealsArrayList;
    private static FirebaseUtil firebaseUtil;
    public static CollectionReference travelDealsRef;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseAuth.AuthStateListener authStateListener;
    private static Activity callingActivity;
    public static boolean isAdmin;
    private static final String ADMINS_COLLECTION_NAME = "admins";
    private static FirebaseStorage firebaseStorage;

    private FirebaseUtil() {
    }

    public static void openFbDbRef(String ref, final Activity callerActivity) {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            firestoreDb = FirebaseFirestore.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            callingActivity = callerActivity;
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        FirebaseUtil.signIn();
                    }
                }
            };
        }
        travelDealsArrayList = new ArrayList<>();
        travelDealsRef = firestoreDb.collection(ref);
    }

    public static StorageReference connectStorage(String directory){
       FirebaseUtil.firebaseStorage =  FirebaseStorage.getInstance();
       return firebaseStorage.getReference().child(directory);
    }

    public static void checkIfAdmin(OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        FirebaseUtil.isAdmin = false;
        if(firebaseAuth.getCurrentUser() != null) {
            String uid = firebaseAuth.getCurrentUser().getUid();
            DocumentReference adminsReference = firestoreDb.collection(FirebaseUtil.ADMINS_COLLECTION_NAME).document(uid);
            adminsReference.get().addOnCompleteListener(onCompleteListener);

        }
    }

    public static void attachListener() {
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public static void detachListener() {
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private static void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        callingActivity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }
}
