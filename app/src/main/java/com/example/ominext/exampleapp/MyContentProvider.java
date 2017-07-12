package com.example.ominext.exampleapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Ominext on 7/12/2017.
 */

public class MyContentProvider extends ContentProvider{

    static final String PROVIDER_NAME="contacts";
    static final String URI="content://"+PROVIDER_NAME+"/students";
    static final Uri CONTENT_URI=Uri.parse(URI);

    static final String id="id";
    static final String name="name";
    static final String age="age";
    private static HashMap<String,String> STUDENTS_PROJECTION_MAP;
    static final int STUDENTS=1;
    static final int STUDENTS_ID=2;
    static final UriMatcher uriMatcher;
    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"students",STUDENTS);
        uriMatcher.addURI(PROVIDER_NAME,"students/#",STUDENTS_ID);
    }
    private SQLiteDatabase db;
    static final String DATABASE_NAME="COLLEGE";
    static final String TABLE_NAME="STUDENTS";
    static final int DATABASE_VERSION=1;
    static final String CREATE_DATABASE_TABLE="create table "+TABLE_NAME+" ("+id+" integer primary key autoincrement, "+name+" text not null, "+age+" int not null);";
    private static class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DATABASE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists "+TABLE_NAME);
            onCreate(db);
        }
    }
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count=0;
        switch (uriMatcher.match(uri)){
            case STUDENTS:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case STUDENTS_ID:
                count = db.delete(TABLE_NAME, id + "=" + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')': ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;

    }
}
