package co.edu.udea.compumovil.gr7.lab2apprun.data;

/**
 * Created by Jeniffer Acosta on 30/03/2017
 */
import android.provider.BaseColumns;

public class StatusContract {

    public static final String DB_NAME = "lab2apprun.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_USER= "usuario";
    public static final String TABLE_RACE= "carrera";
    public static final String TABLE_LOGIN="logeado";

    public class Column_Login {
        public static final String ID = BaseColumns._ID;
        public static final String NICKNAME = "nickname";
    }
    public class Column_User {
        public static final String ID = BaseColumns._ID;
        public static final String NICKNAME = "nickname";
        public static final String MAIL = "mail";
        public static final String PASS = "password";
        public static final String AGE = "age";
        public static final String PICTURE = "picture";
    }
    public class Column_Race {
        public static final String ID = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String DISTANCE = "distance";
        public static final String PLACE = "place";
        public static final String DATE = "date";
        public static final String USER = "user";
        public static final String PICTURE = "picture";
    }
}
