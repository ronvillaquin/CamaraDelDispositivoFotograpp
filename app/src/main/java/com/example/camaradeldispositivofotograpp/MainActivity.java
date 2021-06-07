package com.example.camaradeldispositivofotograpp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


//luego de hacer el diseño vamos a la carpeta y al archivo manifest para agregar los ermisos
// ecesarios para usar a camara
//<uses-feature android:name="android.hardware.camera2" android:required="true" />
//    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
// en el mismo archivo manifest dentr de la etiqueta aplication hay que colcoar otro codigo
// si sale error o en rojo hayq ue darle alt+enter y darle crear xml
// luego en el archiivo que se creo xml filepath vamos a modficarlos desde el codigo
// ya no deberia saltar error porque le asignamos la rota donde guardara las fotos

public class MainActivity extends AppCompatActivity {

    private ImageView img_foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_foto = (ImageView)findViewById(R.id.img_foto);

        // ahora tenemos que validar los permisos que asignamos si sale error con alt+enter para importar la clase
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission
                    .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }
    }

    // fuera de on create pegamos el metodo para que cree los nombres unico de las fotos y la ruta en una variable
    //se improtan als clases con alt+enter
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Backup_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //este metodo nos permite tomar la fotografia y craer el archivo xq el cog de arriba es solo para el nombre
    static final int REQUEST_TAKE_PHOTO = 1;
    // cmbiamos el nombre al metodo por el que desesmos en este caso tomar foto y publico para poder asignar al oncli
    public void tomarFoto(View vista) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                //cuando le coloco el toStrring al final muestra miniatura ero guarda archvio vacio
                //y cuando no se lo pongo cierra app pero si guardar archivo con la imagen
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI.toString());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //con este metodo obtenemos una miniatura o una vsta previa d ela imagen
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img_foto.setImageBitmap(imageBitmap); // el img es el de la variable o id que declaramos arriba
        }
    }

}
