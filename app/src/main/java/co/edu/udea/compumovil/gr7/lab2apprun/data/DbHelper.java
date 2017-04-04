package co.edu.udea.compumovil.gr7.lab2apprun.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jeniffer Acosta on 18/03/2017.
 */
public class DbHelper extends SQLiteOpenHelper { //
    private static final String TAG = DbHelper.class.getSimpleName();

    public DbHelper(Context context) {
        super(context, StatusContract.DB_NAME, null, StatusContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlLogin = String.format(
          "create table %s(%s int primary key, %s text unique)",
                StatusContract.TABLE_LOGIN,
                StatusContract.Column_Login.ID,
                StatusContract.Column_Login.NICKNAME);
        db.execSQL(sqlLogin);
        String sqlUser = String
                .format("create table %s (%s int primary key, %s text unique, %s text, %s text, %s text, %s blob)",
                        StatusContract.TABLE_USER,
                        StatusContract.Column_User.ID,
                        StatusContract.Column_User.NICKNAME,
                        StatusContract.Column_User.MAIL,
                        StatusContract.Column_User.PASS,
                        StatusContract.Column_User.AGE,
                        StatusContract.Column_User.PICTURE);
        db.execSQL(sqlUser);
        String sqlEvent = String
                .format("create table %s (%s int primary key, %s text, %s text, %s text, %s text, %s text, %s text references %s(%s), %s blob)",
                        StatusContract.TABLE_EVENT,
                        StatusContract.Column_Event.ID,
                        StatusContract.Column_Event.NAME,
                        StatusContract.Column_Event.FIRST_DESCRIPTION,
                        StatusContract.Column_Event.INFORMATION,
                        StatusContract.Column_Event.PLACE,
                        StatusContract.Column_Event.DATE,
                        StatusContract.Column_Event.USER,
                        StatusContract.TABLE_USER,
                        StatusContract.Column_User.NICKNAME,
                        StatusContract.Column_Event.PICTURE);
        db.execSQL(sqlEvent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + StatusContract.TABLE_USER);
        db.execSQL("drop table if exists " + StatusContract.TABLE_EVENT);
        db.execSQL("drop table if exists " + StatusContract.TABLE_LOGIN);
    }
}
