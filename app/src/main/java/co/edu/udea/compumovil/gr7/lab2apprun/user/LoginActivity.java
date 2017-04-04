package co.edu.udea.compumovil.gr7.lab2apprun.user;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import co.edu.udea.compumovil.gr7.lab2apprun.NavDrawer;
import co.edu.udea.compumovil.gr7.lab2apprun.R;
import co.edu.udea.compumovil.gr7.lab2apprun.data.DbHelper;
import co.edu.udea.compumovil.gr7.lab2apprun.data.StatusContract;

/**
 * Created by Jeniffer Acosta on 28/03/2017.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE=1;
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    DbHelper dbH;
    SQLiteDatabase db;

    @Override
    protected void onResume() {
        super.onResume();
        mUserView.setError(null);
        mPasswordView.setError(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserView = (AutoCompleteTextView) findViewById(R.id.user);
        mPasswordView = (EditText) findViewById(R.id.password);
        //this.deleteDatabase(StatusContract.DB_NAME);
        dbH=new DbHelper(this);
        db = dbH.getWritableDatabase();
        //db.execSQL("delete from " + StatusContract.TABLE_LOGIN);
        //db.execSQL("delete from "+StatusContract.TABLE_USER);
        //db.execSQL("delete from "+StatusContract.TABLE_EVENT);
        Cursor search=db.rawQuery("select * from "+ StatusContract.TABLE_LOGIN, null);
        if(search.moveToFirst()){
            Intent newActivity = new Intent(this, NavDrawer.class);
            startActivity(newActivity);
        }
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String u="",p="";
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            if(data.hasExtra("user") && data.hasExtra("pass")) {
                u = data.getExtras().getString("user");
                p = data.getExtras().getString("pass");
            }
            if(!u.equals(".")) {
                mUserView.setText(u);
                mPasswordView.setText(p);
                Toast.makeText(this, getString(R.string.good_register), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,getString(R.string.register_cancelled),Toast.LENGTH_LONG).show();
            }
        }
    }

    public void register(View v){
        Intent newActivity = new Intent(this, Registro.class );
        startActivityForResult(newActivity, REQUEST_CODE);
    }

    public void signInLogin(View v) throws InterruptedException {
        String user = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(user)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        } else if(TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPassword(user,password)) {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                focusView = mPasswordView;
                cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            register();
        }
    }

    private boolean isPassword(String nickname,String pass) {
        db = dbH.getWritableDatabase();
        Cursor search=db.rawQuery("select * from "+StatusContract.TABLE_USER+" where nickname='"+nickname+"'", null);
        String validation="";
        if(search.moveToFirst()){
            do {
                validation = search.getString(3);
            } while(search.moveToNext());
        }
        db.close();
        if(pass.equals(validation)){return true;}
        return false;
    }

    private void register() throws InterruptedException {
        db = dbH.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StatusContract.Column_Login.ID,(1));
        values.put(StatusContract.Column_Login.NICKNAME, mUserView.getText().toString());
        db.insertWithOnConflict(StatusContract.TABLE_LOGIN, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        Intent newActivity = new Intent(this, NavDrawer.class);
        startActivity(newActivity);
    }
}

