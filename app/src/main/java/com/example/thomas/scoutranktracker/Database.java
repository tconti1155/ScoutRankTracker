package com.example.thomas.scoutranktracker;

/* the following sites were used as references to create the app
    Expandable List View: http://theopentutorials.com/tutorials/android/listview/android-expandable-list-view-example/
    Selecting items in Database: http://zetcode.com/db/sqlite/select/
    SQLite database: https://www.tutorialspoint.com/android/android_sqlite_database.htm
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;

/**
 * Created by Thomas on 2/19/2017.
 */
public class Database extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "ScoutTracker.db";// creating database file
    public static final String RANKS_TABLE_NAME = "ranks";// naming the table
    public static final String RANKS_COLUMN_ID = "id";// creating the columns
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
    //creates the database
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table ranks "+
        "(id integer primary key, scout text, tenderfoot text, secondClass text, firstClass text, star text, life text, eagle text)");
    }

    @Override
    //for when the table needs to upgraded.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS ranks");
        onCreate(db);
    }

    // inserting requirements to table
    public boolean insertReg(int id, boolean reg)
    {
        SQLiteDatabase db = this.getWritableDatabase();// creating database SQlite Varaible
        ContentValues contentValues = new ContentValues();// creating a ContentsValues
        switch(id)// filling the values for a given id and reg.
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
        db.insert("ranks", null,contentValues);// inserting it into the table
        return true;
    }

    //updating the values in the table
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
        //updating the values in the table
        db.update("ranks", contentValues, "id = ?",new String[]{Integer.toString(regNum)});
    }

    // retrieving the data from the table
    public Cursor getData(int id, int reg){
        reg++;// increating the reg to match up with the table and array.
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from ranks where id=" + reg + "", null);// getting the information from table and putting into res

        return res;// returning res.
    }

    //needing to exist the table
    public boolean tableExist() {
        File dbtest = new File("/data/data/com.example.thomas.scoutranktracker/databases/ScoutTracker.db");
        if (dbtest.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
