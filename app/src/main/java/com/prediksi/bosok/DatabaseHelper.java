package com.prediksi.bosok;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "prediksi_bosok.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE keluaran (id INTEGER PRIMARY KEY AUTOINCREMENT, pasaran TEXT, nomor TEXT, tanggal TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int o, int n) {
        db.execSQL("DROP TABLE IF EXISTS keluaran");
        onCreate(db);
    }

    public void tambahKeluaran(String pasaran, String nomor, String tanggal) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("pasaran", pasaran);
        cv.put("nomor", nomor);
        cv.put("tanggal", tanggal);
        db.insert("keluaran", null, cv);
    }

    public List<String[]> getByPasaran(String pasaran) {
        List<String[]> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, nomor, tanggal FROM keluaran WHERE pasaran=? ORDER BY id DESC", new String[]{pasaran});
        while (c.moveToNext()) {
            list.add(new String[]{c.getString(0), c.getString(1), c.getString(2)});
        }
        c.close();
        return list;
    }

    public List<String> getNomorOnly(String pasaran) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT nomor FROM keluaran WHERE pasaran=? ORDER BY id DESC LIMIT 30", new String[]{pasaran});
        while (c.moveToNext()) list.add(c.getString(0));
        c.close();
        return list;
    }

    public void hapus(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("keluaran", "id=?", new String[]{id});
    }
}
