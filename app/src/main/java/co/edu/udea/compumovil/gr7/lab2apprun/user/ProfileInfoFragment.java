package co.edu.udea.compumovil.gr7.lab2apprun.user;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import co.edu.udea.compumovil.gr7.lab2apprun.R;
import co.edu.udea.compumovil.gr7.lab2apprun.data.DbHelper;
import co.edu.udea.compumovil.gr7.lab2apprun.data.StatusContract;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileInfoFragment extends Fragment {
    private ImageView targetImageR;
    DbHelper dbH;
    SQLiteDatabase db;
    TextView[] txtValidateR = new TextView[4];
    View view;

    public ProfileInfoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbH=new DbHelper(getActivity().getBaseContext());

        view=inflater.inflate(R.layout.fragment_profile_info, container, false);
        txtValidateR[0]=(TextView)view.findViewById(R.id.viewNick);
        txtValidateR[1]=(TextView)view.findViewById(R.id.viewAge);
        txtValidateR[2]=(TextView)view.findViewById(R.id.viewMail);
        targetImageR=(ImageView)view.findViewById(R.id.viewProfile);
        db=dbH.getWritableDatabase();
        Cursor search = db.rawQuery("select * from " + StatusContract.TABLE_LOGIN, null);
        search.moveToFirst();
        String aux = search.getString(1);
        search = db.rawQuery("select * from "+StatusContract.TABLE_USER+ " where "+StatusContract.Column_User.NICKNAME+"='"+aux+"'", null);
        search.moveToFirst();
        txtValidateR[0].setText("Usuario: "+search.getString(1));
        txtValidateR[1].setText(search.getString(4) + " Years Old");
        txtValidateR[2].setText(search.getString(2));
        byte[] auxx=search.getBlob(5);
        Bitmap pict= BitmapFactory.decodeByteArray(auxx, 0, (auxx).length);
        targetImageR.setImageBitmap(pict);
        db.close();
        return view;
    }
}
