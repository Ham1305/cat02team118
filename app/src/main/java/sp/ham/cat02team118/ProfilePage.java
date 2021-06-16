package sp.ham.cat02team118;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfilePage extends AppCompatActivity {

    public static final String TAG = "TAG";
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String UID;
    private TextView UserName, FirstName, LastName, PhoneNum, Email, Address;
    private ImageView setpfp;
    Uri imageUri;
    Button EditProfile;

    private static final int GALLERY_REQUEST = 1;

    private static final int CAMERA_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        getSupportActionBar().hide();

        UserName = findViewById(R.id.username);
        FirstName = findViewById(R.id.FirstName);
        LastName = findViewById(R.id.LastName);
        PhoneNum = findViewById(R.id.PhoneNum);
        Email = findViewById(R.id.Email);
        Address = findViewById(R.id.Address);
        setpfp = findViewById(R.id.userimage);
        setpfp.setOnClickListener(onClick);
        EditProfile = findViewById(R.id.imageButton5);
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,EditProfile.class);
                startActivity(i);
            }
        });

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        UID = auth.getCurrentUser().getUid();
        loadInfomation(UID);

        BottomNavigationView bottomNavigationView = findViewById(R.id.rewardsNavViewBar);

        bottomNavigationView.setSelectedItemId(R.id.profilepage);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profilepage:
                        return true;

                    case R.id.explore:
                        startActivity(new Intent(getApplicationContext(), ShopList.class));
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.business:
                        startActivity(new Intent(getApplicationContext(), ProfilepageOwners.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final CharSequence[] options = {"Open Camera","Select from Gallery","Cancel"};
            new AlertDialog.Builder(ProfilePage.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Pick profile Picture")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (options[which].equals("Open Camera")){
                                Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if(CameraIntent.resolveActivity(getPackageManager()) != null){
                                    startActivityForResult(CameraIntent, CAMERA_REQUEST);
                                }
                            }
                            else if (options[which].equals("Select from Gallery")){
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                            }
                            else if(options[which].equals("Cancel")){
                                dialog.dismiss();
                            }
                        }
                    })
                    .show();
        }
    };

    private void loadInfomation(String uid) {
        DocumentReference addRef = firestore.collection("users").document(uid);
        addRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String address = documentSnapshot.getString("address");
                            String email = documentSnapshot.getString("email");
                            String firstname = documentSnapshot.getString("firstname");
                            String lastname = documentSnapshot.getString("lastname");
                            String username = documentSnapshot.getString("username");
                            String phoneno = documentSnapshot.getString("phoneno");
                            UserName.setText("@"+username);
                            FirstName.setText(firstname);
                            LastName.setText(lastname);
                            PhoneNum.setText(phoneno);
                            Email.setText(email);
                            Address.setText(address);
                            Uri uri = auth.getCurrentUser().getPhotoUrl();
                            if (uri!=null){
                                Glide.with(ProfilePage.this)
                                        .load(uri)
                                        .into(setpfp);
                            }
                        } else{
                            Toast.makeText(ProfilePage.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfilePage.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: error");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                imageUri = data.getData();
                try {
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    setpfp.setImageBitmap(selectedImage);
                    handleUpload(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == CAMERA_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                setpfp.setImageBitmap(bitmap);
                handleUpload(bitmap);
            }
        }
    }

    private void handleUpload(Bitmap bitmap){
        ByteArrayOutputStream baos  = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,baos);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid + ".jpeg");

        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure:",e.getCause());
                    }
                });
    }

    private void getDownloadUrl(StorageReference reference){
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG,"onSuccess:" + uri);
                        setUserProfileUrl(uri);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Glide.with(ProfilePage.this)
                                .load(user.getPhotoUrl())
                                .into(setpfp);
                        Toast.makeText(ProfilePage.this, "Profile Picture\nUpdated Successfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfilePage.this, "Profile image failed...",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}