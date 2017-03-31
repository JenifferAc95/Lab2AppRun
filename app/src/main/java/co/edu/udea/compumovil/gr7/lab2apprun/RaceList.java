package co.edu.udea.compumovil.gr7.lab2apprun;


import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import co.edu.udea.compumovil.gr7.lab2apprun.data.DbHelper;
import co.edu.udea.compumovil.gr7.lab2apprun.data.StatusContract;

public class RaceList extends ListFragment {
    DbHelper dbH;
    SQLiteDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        dbH=new DbHelper(getActivity().getBaseContext());
        ListarCarreras();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    public void ListarCarreras(){
        ArrayList<String> name = new ArrayList(),desc = new ArrayList(),dist = new ArrayList(),place = new ArrayList(),date = new ArrayList(),userMail = new ArrayList(),userPhone = new ArrayList();
        ArrayList picture = new ArrayList();
        boolean control=false;
        db=dbH.getReadableDatabase();
        Cursor test=db.rawQuery("select * from "+ StatusContract.TABLE_RACE+" order by "+StatusContract.Column_Race.NAME, null);
        if (test.moveToFirst()) {
            do{
                name.add(test.getString(1));
                desc.add(test.getString(2));
                dist.add(test.getString(3));
                place.add(test.getString(4));
                date.add(test.getString(5));
                Cursor subTest=db.rawQuery("select * from "+StatusContract.TABLE_USER+" where "+StatusContract.Column_User.NICKNAME+"='"+test.getString(6)+"'", null);
                if (subTest.moveToFirst()) {
                    do {
                        userMail.add(subTest.getString(2));
                        userPhone.add(subTest.getString(4));
                    } while (subTest.moveToNext());
                }
                picture.add(test.getBlob(7));
            }while(test.moveToNext());
            control=true;
        } else{
            Toast.makeText(getActivity().getBaseContext(),getString(R.string.no_race),Toast.LENGTH_LONG).show();
        }
        db.close();
        if(control) {
            ArrayList aList=new ArrayList();
            for (int i = 0; i < name.size(); i++) {
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("name", "Nombre: " + name.get(i));
                hm.put("desc", "Descripción : " + desc.get(i));
                hm.put("dist", "Distancia: " + dist.get(i));
                hm.put("place", "Lugar : " + place.get(i));
                hm.put("date", "Fecha : " + date.get(i));
                hm.put("userMail", "E-mail : " + userMail.get(i));
                hm.put("userPhone", "Teléfono : " + userPhone.get(i));
                hm.put("picture", BitmapFactory.decodeByteArray((byte[]) picture.get(i), 0, ((byte[]) picture.get(i)).length));
                aList.add(hm);
            }
            String from[];
            int to[];
            from = new String[]{"name", "desc", "dist", "place", "date", "userMail", "userPhone", "picture"};
            to = new int[]{R.id.name, R.id.desc, R.id.dist, R.id.place, R.id.date, R.id.userMail, R.id.userPhone, R.id.picture};
            ExtendedSimpleAdapter adapter = new ExtendedSimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);
            setListAdapter(adapter);
        }
    }
}
