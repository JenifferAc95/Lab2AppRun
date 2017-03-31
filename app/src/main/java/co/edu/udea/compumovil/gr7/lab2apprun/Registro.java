package co.edu.udea.compumovil.gr7.lab2apprun;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import co.edu.udea.compumovil.gr7.lab2apprun.data.DbHelper;
import co.edu.udea.compumovil.gr7.lab2apprun.data.StatusContract;

public class Registro extends AppCompatActivity {

    private TextView txt;
    private Button btn;
    private Bitmap picture;
    private boolean control=false;
    private static final int REQUEST_CODE_GALLERY=1;
    private static final int REQUEST_CODE_CAMERA=2;
    private ImageView targetImage;
    EditText[] txtValidate = new EditText[5];
    DbHelper dbH;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        picture = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_profile);
        txtValidate[4]=(EditText)findViewById(R.id.editTextConfirmarPass);
        txtValidate[2]=(EditText)findViewById(R.id.editTextContraseña);
        txtValidate[0]=(EditText)findViewById(R.id.editTextUsuario);
        txtValidate[3]=(EditText)findViewById(R.id.editTextAge);
        txtValidate[1]=(EditText)findViewById(R.id.editText5Email);
        dbH = new DbHelper(this);
        btn = (Button)findViewById(R.id.buttonRegistarse);
        btn.setEnabled(false);
        TextWatcher btnActivation = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(verificarVaciosSinMessage(txtValidate)){btn.setEnabled(true);}
                else{btn.setEnabled(false);}
            }
        };
        for (int n = 0; n < txtValidate.length; n++)
        {
            txtValidate[n].addTextChangedListener(btnActivation);
        }
        targetImage = (ImageView)findViewById(R.id.profilePicture);
        targetImage.setImageBitmap(picture);
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        if(control) {
            data.putExtra("user", txtValidate[0].getText().toString());
            data.putExtra("pass", txtValidate[2].getText().toString());
        } else{
            data.putExtra("user", ".");
            data.putExtra("pass", ".");
        }
        setResult(RESULT_OK,data);
        super.finish();
    }

    public void Validar(View v){
        View focusView=null;
        if (!verificarVacios(txtValidate)){
        }else if(!txtValidate[1].getText().toString().contains("@")){
                txtValidate[1].setError(getString(R.string.invalid_mail));
                focusView = txtValidate[1];
            }else{
            if (!txtValidate[4].getText().toString().equals(txtValidate[2].getText().toString())){
                txtValidate[4].setError(getString(R.string.pass_no_equals));
                focusView = txtValidate[4];
            }else if(!existNick(txtValidate[0].getText().toString())){
                db = dbH.getWritableDatabase();
                ContentValues values = new ContentValues();
                Cursor search = db.rawQuery("select count(*) from usuario", null);
                search.moveToFirst();
                int aux=Integer.parseInt(search.getString(0));
                values.put(StatusContract.Column_User.ID,(aux+1));
                values.put(StatusContract.Column_User.NICKNAME, txtValidate[0].getText().toString());
                values.put(StatusContract.Column_User.PASS, txtValidate[2].getText().toString());
                values.put(StatusContract.Column_User.MAIL, txtValidate[1].getText().toString());
                values.put(StatusContract.Column_User.AGE, txtValidate[3].getText().toString());
                values.put(StatusContract.Column_User.PICTURE, getBitmapAsByteArray(picture));
                db.insertWithOnConflict(StatusContract.TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                db.close();
                control=true;
                finish();
            }
            else
            {
                txtValidate[0].setError(getString(R.string.user_exists));
                focusView = txtValidate[0];
            }
        }

    }
    public boolean existNick(String nickName)
    {
        db = dbH.getWritableDatabase();
        Cursor nick=db.rawQuery("select * from "+StatusContract.TABLE_USER+" where nickname='"+nickName+"'", null);
        if (nick.moveToFirst()) {
            db.close();
            return true;
        }
        return false;
    }

    public boolean verificarVacios(EditText[] txtValidate)
    {
        View focus=null;
        for(int i=0; i<txtValidate.length;i++)
        {
            if((txtValidate[i].getText().toString()).isEmpty())
            {
                txtValidate[i].setError(getString(R.string.error_field_required));
                focus = txtValidate[i];
                return false;
            }
        }
        return true;
    }

    public boolean verificarVaciosSinMessage(EditText[] txtValidate)
    {
        View focus=null;
        for(int i=0; i<txtValidate.length;i++)
        {
            if((txtValidate[i].getText().toString()).isEmpty())
            {
                return false;
            }
        }
        return true;
    }
    public void ClickGallery(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }
    public void ClickCamera(View v) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode==REQUEST_CODE_GALLERY || requestCode==REQUEST_CODE_CAMERA)){
            try {
                Uri targetUri = data.getData();
                picture = redimensionarImagenMaximo(BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri)),350,350);
                targetImage.setImageBitmap(picture);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }
}
