package fpt.android.com.appnauan;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AdminUploadImageActivity extends AppCompatActivity {

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads");
    private StorageTask uploadTask;
    private EditText imageIndex;

    int[] arrayImgs = {
            R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4, R.drawable.img_5,
            R.drawable.img_6, R.drawable.img_7, R.drawable.img_8, R.drawable.img_9, R.drawable.img_10,
            R.drawable.img_11, R.drawable.img_12, R.drawable.img_13, R.drawable.img_14, R.drawable.img_15,
            R.drawable.img_16, R.drawable.img_17, R.drawable.img_18, R.drawable.img_19, R.drawable.img_20,
            R.drawable.img_21, R.drawable.img_22, R.drawable.img_23, R.drawable.img_24, R.drawable.img_25,
            R.drawable.img_26, R.drawable.img_27, R.drawable.img_28, R.drawable.img_29, R.drawable.img_30,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload_image);

        final Button button = (Button) findViewById(R.id.btnUpload);
        imageIndex = (EditText) findViewById(R.id.imageIndex);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = Integer.parseInt(imageIndex.getText().toString());
                Uri imageUri = Uri.parse("android.resource://fpt.android.com.appnauan/" + arrayImgs[index - 1]);
                updateImage(imageUri, index);
            }
        });
    }

    private class UploadImagesThread extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            for (int i = 1; i <= 20; i++) {
                Uri imageUri = Uri.parse("android.resource://fpt.android.com.appnauan/" + arrayImgs[i - 1]);
                updateImage(imageUri, i);

                try {
                    Toast.makeText(AdminUploadImageActivity.this, "Sleep for 2s for task: " + i, Toast.LENGTH_SHORT).show();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    /**
     * Get real type of image (png, jpg)
     *
     * @param uri
     * @return
     */
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = this.getContentResolver();
        // Mime: A standard type to send over the Internet
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /**
     * Update image for each dish
     *
     * @param imageUri
     * @param foodId
     */
    private void updateImage(Uri imageUri, final int foodId) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading image...");
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(String.valueOf(foodId));

            uploadTask = fileReference.putFile(imageUri);
            // continueWithTask: return a task you created
            // continueWith: returns a Task that WRAPS YOUR RETURNED VALUE of then method (Task<Task<Uri>)
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("food").child(String.valueOf(foodId));
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("imageUrl", uri.toString()); //  just put String to DB not all the object uri
                        databaseReference.updateChildren(params);
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(AdminUploadImageActivity.this, "Upload image failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminUploadImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "No image selected or image error", Toast.LENGTH_SHORT).show();
        }
    }
}
