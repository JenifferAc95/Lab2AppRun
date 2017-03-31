package co.edu.udea.compumovil.gr7.lab2apprun.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LEONDAVID on 11/03/2016.
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
        String sqlRace = String
                .format("create table %s (%s int primary key, %s text, %s text, %s text, %s text, %s text, %s text references %s(%s), %s blob)",
                        StatusContract.TABLE_RACE,
                        StatusContract.Column_Race.ID,
                        StatusContract.Column_Race.NAME,
                        StatusContract.Column_Race.DESCRIPTION,
                        StatusContract.Column_Race.DISTANCE,
                        StatusContract.Column_Race.PLACE,
                        StatusContract.Column_Race.DATE,
                        StatusContract.Column_Race.USER,
                        StatusContract.TABLE_USER,
                        StatusContract.Column_User.NICKNAME,
                        StatusContract.Column_Race.PICTURE);
        db.execSQL(sqlRace);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + StatusContract.TABLE_USER);
        db.execSQL("drop table if exists " + StatusContract.TABLE_RACE);
        db.execSQL("drop table if exists " + StatusContract.TABLE_LOGIN);
    }
}
