package com.example.thomas.scoutranktracker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;

/**
 * Created by Thomas on 2/19/2017.
 */
public class Database extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "ScoutTracker.db";
    public static final String RANKS_TABLE_NAME = "ranks";
    public static final String RANKS_COLUMN_ID = "id";
    public static final String RANKS_COLUMN_SCOUT = "scout";
    public static final String RANKS_COLUMN_TENDERFOOT = "tenderfoot";
    public static final String RANKS_COLUMN_SECONDCLASS = "secondclass";
    public static final String RANKS_COLUMN_FIRSTCLASS = "firstclass";
    public static final String RANKS_COLUMN_STAR = "star";
    public static final String RANKS_COLUMN_LIFE = "life";
    public static final String RANKS_COLUMN_EAGLE = "eagle";

    public Database (Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table ranks "+
        "(id integer primary key, scout text, tenderfoot text, secondClass text, firstClass text, star text, life text, eagle text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS ranks");
        onCreate(db);
    }

    public boolean insertReg(int id, boolean reg)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        switch(id)
        {
            case 0: contentValues.put("scout",reg);
                    break;
            case 1: contentValues.put("tenderfoot",reg);
                    break;
            case 2: contentValues.put("secondclass", reg);
                    break;
            case 3: contentValues.put("firstclass", reg);
                    break;
            case 4: contentValues.put("star",reg);
                    break;
            case 5: contentValues.put("life", reg);
                    break;
            case 6: contentValues.put("eagle", reg);
                    break;
        }
        db.insert("ranks", null,contentValues);
        return true;
    }

    public void updateReg(int id, boolean reg, int regNum)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        switch(id)
        {
            case 0: contentValues.put("scout",reg);
                break;
            case 1: contentValues.put("tenderfoot",reg);
                break;
            case 2: contentValues.put("secondclass", reg);
                break;
            case 3: contentValues.put("firstclass", reg);
                break;
            case 4: contentValues.put("star",reg);
                break;
            case 5: contentValues.put("life", reg);
                break;
            case 6: contentValues.put("eagle", reg);
                break;
        }

        db.update("ranks", contentValues, "id = ?",new String[]{Integer.toString(regNum)});
    }

    public Cursor getData(int id, int reg){
        reg++;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from ranks where id=" + reg + "", null);
        /*switch (id) {
            case 0:
                res = db.rawQuery("select scout from ranks where scout like id=" + reg + "", null);
                break;
            case 1:
                res = db.rawQuery("select tenderfoot from ranks where tenderfoot like id=" + reg + "", null);
                break;
            case 2:
                res = db.rawQuery("select secondclass from ranks where secondclass like id=" + reg + "", null);
                break;
            case 3:
                res = db.rawQuery("select firstclass from ranks where firstclass like id=" + reg + "", null);
                break;
            case 4:
                res = db.rawQuery("select star from ranks where star like id=" + reg + "", null);
                break;
            case 5:
                res = db.rawQuery("select life from ranks where life like id=" + reg + "", null);
                break;
            case 6:
                res = db.rawQuery("select eagle from ranks where eagle like id=" + reg + "", null);
                break;
        }*/
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, RANKS_TABLE_NAME);
        return numRows;
    }

    public boolean tableExist() {
        File dbtest = new File("/data/data/com.example.thomas.scoutranktracker/databases/ScoutTracker.db");
        if (dbtest.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
