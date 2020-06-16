package in.specialsoft.artificaljewellery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import in.specialsoft.artificaljewellery.Prevalent.PreValent;
import io.paperdb.Paper;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView settings_profile_image;
    private TextView settings_profile_image_btn;
   private EditText settings_phone,settings_name,settings_address;
   private TextView settings_close,settings_update;

   private Uri imageUri;
   private String myUrl="";
   private StorageTask uploadTask;
   private StorageReference storageProfilePictureReference;
   private String checker ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureReference = FirebaseStorage.getInstance().getReference().child("ProfilePictures");

        settings_profile_image = findViewById(R.id.settings_profile_image);
        settings_profile_image_btn = findViewById(R.id.settings_profile_image_btn);
        settings_phone = findViewById(R.id.settings_phone);
        settings_name = findViewById(R.id.settings_name);
        settings_address = findViewById(R.id.settings_address);

        settings_close = findViewById(R.id.settings_close);
        settings_update = findViewById(R.id.settings_update);

        userInfoDisplay(settings_profile_image,settings_phone,settings_name,settings_address);

        settings_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        settings_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

        settings_profile_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker ="clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data != null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            settings_profile_image.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Problem in selecting image !!!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }
    }

    private void updateOnlyUserInfo() {

        if (TextUtils.isEmpty(settings_name.getText().toString()) || TextUtils.isEmpty(settings_phone.getText().toString()) || TextUtils.isEmpty(settings_address.getText().toString()))
        {
            Toast.makeText(this, "Don't leave any text field Empty !!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
            HashMap<String,Object> userMap = new HashMap<>();
            userMap.put("name",settings_name.getText().toString());
            userMap.put("phoneOrder",settings_phone.getText().toString());
            userMap.put("address",settings_address.getText().toString());

            ref.child(PreValent.onlineUser.getPhone()).updateChildren(userMap);
            Toast.makeText(SettingsActivity.this, "Profile Updated succesfully", Toast.LENGTH_SHORT).show();
            Paper.book().destroy();
            Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Information..");
        progressDialog.setMessage("...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (imageUri != null)
        {
            final  StorageReference fileRef = storageProfilePictureReference.child(PreValent.onlineUser.getPhone() +".jpg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful())
                            {
                                Uri downladUrl = task.getResult();
                                myUrl = downladUrl.toString();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                                HashMap<String,Object> userMap = new HashMap<>();
                                userMap.put("name",settings_name.getText().toString());
                                userMap.put("phoneOrder",settings_phone.getText().toString());
                                userMap.put("address",settings_address.getText().toString());
                                userMap.put("image",myUrl);

                                ref.child(PreValent.onlineUser.getPhone()).updateChildren(userMap);

                                progressDialog.dismiss();

                                Toast.makeText(SettingsActivity.this, "Profile Updated succesfully", Toast.LENGTH_SHORT).show();
                                Paper.book().destroy();
                                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(SettingsActivity.this, "Error :", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Image is not selected !!", Toast.LENGTH_SHORT).show();
        }

    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(settings_name.getText().toString()) || TextUtils.isEmpty(settings_phone.getText().toString()) || TextUtils.isEmpty(settings_address.getText().toString()))
        {
            Toast.makeText(this, "Don't leave any text field Empty !!", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void userInfoDisplay(final CircleImageView settings_profile_image, final EditText settings_phone, final EditText settings_name, final EditText settings_address) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(PreValent.onlineUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if user exist
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("image").exists()  && dataSnapshot.child("address").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phoneOrder").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(settings_profile_image);

                        settings_name.setText(name);
                        settings_address.setText(address);
                        settings_phone.setText(phone);
                    }
                    else if (!dataSnapshot.child("image").exists() && !dataSnapshot.child("address").exists())
                    {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phoneOrder").getValue().toString();

                        settings_name.setText(name);
                        settings_phone.setText(phone);
                    }
                    else if (!dataSnapshot.child("image").exists())
                    {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phoneOrder").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();


                        settings_name.setText(name);
                        settings_address.setText(address);
                        settings_phone.setText(phone);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}