package com.raveltrips.contentcreator.Utils;

/**
 * Created by LENOVO on 23-05-2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LENOVO on 22-05-2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static  String DB_NAME = "offlineRavel";
    public static  String COL1 = "TRIP_NAME";
    public static  String COL2 = "TRIP_ID";
    public static  String COL3 = "ID";
    public static  String COL4 = "NAME";
    public static  String COL5 = "LATITUDE";
    public static  String COL6 = "LONGITUDE";
  //  public static  String COL7 = "TAGS";
    public static  String COL7 = "DESCRIPTION";
    public static  String COL8 = "WEBSITE";
    public static  String COL9 = "PHONE_NUMBER";
    public static  String COL10 = "CREATOR_ID";

    //Whenever DB is created.
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table MYTRIPS (TRIP_ID VARCHAR(100),CREATOR_ID VARCHAR(100),TRIP_NAME VARCHAR(100))");
        sqLiteDatabase.execSQL("create table HIDDENGEMS (TRIP_NAME VARCHAR(100),TRIP_ID VARCHAR(100),ID INTEGER,NAME VARCHAR(100),LATITUDE VARCHAR(100),LONGITUDE VARCHAR(100),DESCRIPTION VARCHAR(100),WEBSITE VARCHAR(100),PHONE_NUMBER VARCHAR(100),CREATOR_ID VARCHAR(100))");
        sqLiteDatabase.execSQL("create table RESTAURANTS (TRIP_NAME VARCHAR(100),TRIP_ID VARCHAR(100),ID INTEGER,NAME VARCHAR(100),LATITUDE VARCHAR(100),LONGITUDE VARCHAR(100),DESCRIPTION VARCHAR(100),WEBSITE VARCHAR(100),PHONE_NUMBER VARCHAR(100),CREATOR_ID VARCHAR(100))");
        sqLiteDatabase.execSQL("create table PAIDACTIVITIES (TRIP_NAME VARCHAR(100),TRIP_ID VARCHAR(100),ID INTEGER,NAME VARCHAR(100),LATITUDE VARCHAR(100),LONGITUDE VARCHAR(100),DESCRIPTION VARCHAR(100),WEBSITE VARCHAR(100),PHONE_NUMBER VARCHAR(100),CREATOR_ID VARCHAR(100))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MYTRIPS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS HIDDENGEMS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS RESTAURANTS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PAIDACTIVITIES");
        onCreate(sqLiteDatabase);
    }

    public boolean insertTripNameAndID(String tableName,String creatorID, String tripID , String tripName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,tripID);
        contentValues.put(COL10,creatorID);
        contentValues.put(COL1, tripName);
        long res = db.insert(tableName,null,contentValues);
        if (res!=-1){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean insert(String tableName, String tripID, String tripName, String name,
                          String lat, String longitude,  String description, String website, String phoneNumber,String creatorID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,tripName);
        contentValues.put(COL2,tripID);
        contentValues.put(COL4,name);
        contentValues.put(COL5,lat);
        contentValues.put(COL6,longitude);
        contentValues.put(COL7,description);
        contentValues.put(COL10,creatorID);
        if(tableName.equals("PAIDACTIVITIES") || tableName.equals("RESTAURANTS")){
            contentValues.put(COL8,website);
            contentValues.put(COL9,phoneNumber);
        }

        long res = db.insert(tableName,null,contentValues);
        if (res!=-1){
            return true;
        }
        else {
            return false;
        }
    }

    public Cursor getAll(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+tableName,null);
        return result;
    }
    public Cursor getAllByTripId(String tableName , String tripId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+tableName+" where TRIP_ID= '"+tripId+"'",null);
        return result;
    }
    public int getCount(String tableName) {
        String countQuery = "SELECT  * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public boolean updateData(String tableName,String name, String lat, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,name);
        contentValues.put(COL3,lat);
        contentValues.put(COL4,longitude);
        db.update(tableName,contentValues, "TRIP_ID = ?", new String[]{name});
        return true;
    }

    public void deleteData(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ tableName);
    }
    public void deleteDataByTripId(String tableName , String tripId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ tableName +" where TRIP_ID='"+tripId+"'");
    }
    public void dropTable(String tableName,String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+tableName);
    }

    public Cursor getUniqueTripsById(String tableName)
    {
        Cursor cursor=null;
        SQLiteDatabase db = this.getWritableDatabase();
        if (tableName.equals("RESTAURANTS")) {
            cursor = db.query(true, tableName, new String[]{"TRIP_ID","NAME","DESCRIPTION","WEBSITE","PHONE_NUMBER"}, null, null, "TRIP_ID", null, null, null);
        }
        else if (tableName.equals("PAIDACTIVITIES"))
        {
            cursor = db.query(true, tableName, new String[]{"TRIP_ID","NAME","DESCRIPTION","WEBSITE","PHONE_NUMBER"}, null, null, "TRIP_ID", null, null, null);
        }
        else if (tableName.equals("HIDDENGEMS"))
        {
            cursor = db.query(true, tableName, new String[]{"TRIP_ID","NAME","LATITUDE","LONGITUDE","DESCRIPTION","WEBSITE","PHONE_NUMBER"}, null, null, "TRIP_ID", null, null, null);
        }
        return cursor;
    }
}
