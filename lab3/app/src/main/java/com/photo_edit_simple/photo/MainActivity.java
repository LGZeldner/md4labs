package com.photo_edit_simple.photo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 0;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 0;
    private static final int PICTURE_CROP = 1;
    private ImageView imageView;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

        /* проверяем разрешение и запрашиваем */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }
    }

    public void onClick(View view) { /* при клике на кнопку фото */
        Intent cameraIntent = new Intent();
        /* устанавливаем действие:
         вызвать стандартное приложение, чтобы сделать фото и вернуть его */
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) { /* если выполнилось */
            File file = null;
            try {
                file = createPhotoFile(); /* сохраняем файл */
            } catch (IOException error) {
                Toast.makeText(this, "Ошибка создания файла", Toast.LENGTH_LONG).show();
            }
            if (file != null){ /* если файл успешно создан */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    photoUri = FileProvider.getUriForFile(
                            this, "com.photo_edit_simple.android.provider", file);
                } else {
                    photoUri = Uri.fromFile(file); /* получаем URI */
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); /* добавляем данные в Intent */
                /* начинаем активность для редактирования фото */
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

        }
    }
    private File createPhotoFile() throws IOException { /* создание файла фото */
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = File.createTempFile(timeStamp, ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        return  file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == CAMERA_REQUEST) {
                imageView.setImageURI(photoUri);
                crop_photo();
            }
            if (requestCode == PICTURE_CROP) {
                Bundle extras = data.getExtras();
                Bitmap picture = extras.getParcelable("data");
                imageView.setImageBitmap(picture);
            }
        }
    }

    private void crop_photo() { /* обработка */
        try {
            Intent cropIntent = new Intent();
            cropIntent.setAction("com.android.camera.action.CROP");
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.setDataAndType(photoUri,"image/*");
            cropIntent.putExtra("crop", true);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("outputFormat", "JPEG" );
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PICTURE_CROP);
        } catch (ActivityNotFoundException error) {
            Toast.makeText(this, "Не поддерживается кадрирование", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
// If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//Start your camera handling here
                } else {
                    Toast.makeText(this, "Нет доступа к камере", Toast.LENGTH_LONG).show();
                }
        }
    }

    private void getPrePhoto() {
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void getFullPhoto() {

    }



}
