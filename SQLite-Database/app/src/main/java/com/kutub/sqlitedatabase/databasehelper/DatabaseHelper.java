package com.kutub.sqlitedatabase.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kutub.sqlitedatabase.model.Student;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "sqlite59";
    public static final String VERSION = "1";
    public static final String TABLE_STUDENT = "studens";
    public static final String COULMN_ID = "id";
    public static final String COULMN_NAME = "name";
    public static final String COULMN_EMAIL = "email";


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_STUDENT + " ("
                + COULMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COULMN_NAME + " TEXT, "
                + COULMN_EMAIL + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);

    }

    public boolean createStudent(Student s) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COULMN_NAME, s.getName());
        contentValues.put(COULMN_EMAIL, s.getEmail());
        long status = database.insert(TABLE_STUDENT, null, contentValues);

        return status != -1;


    }

    public List<Student> getAllStudent() {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENT, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COULMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COULMN_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COULMN_EMAIL));

                Student student = new Student(id, name, email);
                studentList.add(student);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return studentList;
    }

    public boolean updateStudent(Student s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COULMN_NAME, s.getName());
        contentValues.put(COULMN_EMAIL, s.getEmail());
        int result = db.update(TABLE_STUDENT, contentValues, COULMN_ID + "=?", new String[]{String.valueOf(s.getId())});
        return result > 0;
    }

    public boolean deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_STUDENT, COULMN_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }


    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }
}
