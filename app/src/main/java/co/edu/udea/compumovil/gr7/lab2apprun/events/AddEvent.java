package co.edu.udea.compumovil.gr7.lab2apprun.events;

/**
 * Created by Jeniffer Acosta on 4/04/2017.
 */

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;

import co.edu.udea.compumovil.gr7.lab2apprun.R;
import co.edu.udea.compumovil.gr7.lab2apprun.data.DbHelper;
import co.edu.udea.compumovil.gr7.lab2apprun.data.StatusContract;
public class AddEvent extends Fragment {
    Bitmap pict;
    private static final int REQUEST_CODE_GALLERY=1;
    private static final int REQUEST_CODE_CAMERA=2;
    private ImageView targetImageR;
    DbHelper dbH;
    SQLiteDatabase db;
    Calendar calendar = Calendar.getInstance();
    static final int PICK_REQUEST =1337;
    Uri contact = null;
    Button btnR;
    EditText[] txtValidateR = new EditText[4];
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbH=new DbHelper(getActivity().getBaseContext());
        view=inflater.inflate(R.layout.fragment_add_event,container,false);
        pict = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.calendar);
        targetImageR = (ImageView)view.findViewById(R.id.eventImage);
        targetImageR.setImageBitmap(pict);
        txtValidateR[0]=(EditText)view.findViewById(R.id.editTextEventName);
        txtValidateR[1]=(EditText)view.findViewById(R.id.editTextEventDescription);
        txtValidateR[2]=(EditText)view.findViewById(R.id.editTextOtherInformation);
        txtValidateR[3]=(EditText)view.findViewById(R.id.editTextEventPlace);
        btnR = (Button)view.findViewById(R.id.buttonEvent);
        btnR.setEnabled(false);
        TextWatcher btnActivation = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(verificarVaciosSinMessageR(txtValidateR)){btnR.setEnabled(true);}
                else{btnR.setEnabled(false);}
            }
        };
        for (int n = 0; n < txtValidateR.length; n++)
        {
            txtValidateR[n].addTextChangedListener(btnActivation);
        }
        return view;
    }

    public void DateClic()
    {
        setDate();
    }
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        }
    };
    public void setDate()
    {
        new DatePickerDialog(getActivity(),d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PICK_REQUEST){
            if(resultCode == getActivity().RESULT_OK){
                contact = data.getData();
            }
        }
        if (resultCode == getActivity().RESULT_OK && (requestCode==REQUEST_CODE_GALLERY || requestCode==REQUEST_CODE_CAMERA)){
            try {
                Uri targetUri = data.getData();
                pict = redimensionarImagenMaximo(BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri)),400,350);
                targetImageR.setImageBitmap(pict);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean verificarVaciosSinMessageR(EditText[] txtValidate)
    {
        for(int i=0; i<txtValidate.length;i++)
        {
            if((txtValidate[i].getText().toString()).isEmpty())
            {
                return false;
            }
        }
        return true;
    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
    public void onClickEvent() {
        db = dbH.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor search = db.rawQuery("select count(*) from " + StatusContract.TABLE_EVENT, null);
        search.moveToFirst();
        int aux=Integer.parseInt(search.getString(0));
        values.put(StatusContract.Column_Event.ID,(aux+1));
        values.put(StatusContract.Column_Event.NAME,txtValidateR[0].getText().toString());
        values.put(StatusContract.Column_Event.FIRST_DESCRIPTION,txtValidateR[1].getText().toString());
        values.put(StatusContract.Column_Event.INFORMATION,txtValidateR[2].getText().toString());
        values.put(StatusContract.Column_Event.PLACE, txtValidateR[3].getText().toString());
        String dAux = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))+" / "+Integer.toString(calendar.get(Calendar.MONTH))+" / "+Integer.toString(calendar.get(Calendar.YEAR));;;
        values.put(StatusContract.Column_Event.DATE,dAux);
        search = db.rawQuery("select * from " + StatusContract.TABLE_LOGIN, null);
        search.moveToFirst();
        values.put(StatusContract.Column_Event.USER,search.getString(1));
        values.put(StatusContract.Column_Event.PICTURE,getBitmapAsByteArray(pict));
        db.insertWithOnConflict(StatusContract.TABLE_EVENT, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }
    public void ClickGalleryR() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }
    public void ClickCameraR() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
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
