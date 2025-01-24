package com.example.madassignment1;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {

    public static final String DBASE_NAME = "records";
    public static final String DBASE_TABLE2 = "tasks";
    public static final String DBASE_TABLE = "students";
    public static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE " + DBASE_TABLE + " (id INTEGER PRIMARY KEY, student_firstname TEXT, student_lastname TEXT, student_age INTEGER, student_gender TEXT, course_study TEXT, address TEXT);";
    private static final String CREATE_TABLE2 = "CREATE TABLE " + DBASE_TABLE2 + " (task_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, location TEXT, status TEXT);";

    public DatabaseManager (Context c) {
        super(c, DBASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.w("Students table", "dropping table and recreating.");
        db.execSQL("DROP TABLE IF EXISTS " + DBASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBASE_TABLE2);
        onCreate(db);
    }

    public void clearStudentRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DBASE_TABLE, null, null);
    }

    public void clearTaskRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DBASE_TABLE2, null, null);
    }

    public boolean addStudentRow(Integer id, String fn, String ln, Integer age, String gen, String cs, String adrs) {

        ContentValues newStudent = new ContentValues();
        newStudent.put("id", id);
        newStudent.put("student_firstname", fn);
        newStudent.put("student_lastname", ln);
        newStudent.put("student_age", age);
        newStudent.put("student_gender", gen);
        newStudent.put("course_study", cs);
        newStudent.put("address", adrs);

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.insertOrThrow(DBASE_TABLE, null, newStudent);
        } catch (Exception e) {
            Log.e("Error in inserting rows", e.toString());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean addTaskRow(String tname, String tloc, String tstat) {
        ContentValues newTask = new ContentValues();
        //   newTask.put("task_id", tid);
        newTask.put("name", tname);
        newTask.put("location", tloc);
        newTask.put("status", tstat);

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.insertOrThrow(DBASE_TABLE2, null, newTask);
        } catch (Exception e) {
            Log.e("Error in inserting rows", e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<String> retrieveStudentRows() {
        ArrayList<String> studentRows = new ArrayList<String>();
        String[] columns = new String[]{"id", "student_firstname", "student_lastname", "student_age", "student_gender", "course_study", "address TEXT"};

        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = db.query(DBASE_TABLE, columns, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                studentRows.add(cursor.getInt(0) + ", " + cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3) + ", " + cursor.getString(4) + ", " + cursor.getString(5));
                cursor.moveToNext();
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("Error in retrieving rows", e.toString());
            e.printStackTrace();
        }

        return studentRows;
    }

    public ArrayList<String> retrieveTaskRows() {
        ArrayList<String> taskRows = new ArrayList<String>();
        String[] columns = new String[]{"task_id", "name", "location", "status"};

        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = db.query(DBASE_TABLE2, columns, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                taskRows.add(cursor.getInt(0) + ", " + cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3));
                cursor.moveToNext();
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("Error in retrieving rows", e.toString());
            e.printStackTrace();
        }

        return taskRows;
    }


    public String selectStudentRecord(String wantedID) {
        String record = "";

        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor studCurs = db.rawQuery("SELECT * FROM TABLE " + DBASE_TABLE + " WHERE id = " + wantedID.trim(), null);
            studCurs.moveToFirst();
            while (!studCurs.isAfterLast()) {
                record = studCurs.toString();
                studCurs.moveToNext();
            }
            if (!studCurs.isClosed()) {
                studCurs.close();
            }
        } catch (Exception e) {
            Log.e("Error in retrieving rows", e.toString());
            e.printStackTrace();
        }

        return record;
    }


    public String selectTaskRecord(String wantedTask) {
        String record = "";

        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor taskCurs = db.rawQuery("SELECT * FROM TABLE " + DBASE_TABLE2 + " WHERE name = " + wantedTask.trim(), null);
            taskCurs.moveToFirst();
            while (!taskCurs.isAfterLast()) {
                taskCurs.moveToNext();
            }
            if (!taskCurs.isClosed()) {
                taskCurs.close();
            }
        } catch (Exception e) {
            Log.e("Error in retrieving rows", e.toString());
            e.printStackTrace();
        }

        return record;
    }

    public void updateStudentRecord(String originalStudent, String newStudent) {
        ContentValues updatedValues = new ContentValues();

        String[] studSplit = newStudent.split(", ");
        String[] originalSplit = originalStudent.split(", ");
        updatedValues.put("student_firstname", studSplit[1]);
        updatedValues.put("student_lastname", studSplit[2]);
        updatedValues.put("student_age", studSplit[3]);
        updatedValues.put("student_gender", studSplit[4]);
        updatedValues.put("course_study", studSplit[5]);
        updatedValues.put("address", studSplit[6]);

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.update(DBASE_TABLE, updatedValues, "name=?", new String[]{originalSplit[1]});
        } catch (Exception e) {
            Log.e("Error in updating rows", e.toString());
            e.printStackTrace();
        }
    }

    public void updateTaskRecord(String originalTask, String newTask) {
        ContentValues newTasks = new ContentValues();
        String[] taskSplit = newTask.split(", ");
        String[] oldTaskSplit = originalTask.split(", ");

        newTasks.put("name", taskSplit[1]);
        newTasks.put("location", taskSplit[2]);

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.update(DBASE_TABLE2, newTasks, "name=?", new String[]{oldTaskSplit[1]});
        } catch (Exception e) {
            Log.e("Error in updating rows", e.toString());
            e.printStackTrace();
        }
    }
}
