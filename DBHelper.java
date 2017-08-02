package com.amurthy.todolist;

/**
 * Created by macbookpro on 3/19/17.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public final class DBHelper {
    private static final String LOGTAG = "DBHelper";

    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "todolist";
    // Column Names
    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "Task Title";
    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_DATE = "Due Date";
    public static final String KEY_INFO = "Information";
    // Column indexes
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_TITLE = 1;
    public static final int COLUMN_DESCRIPTION = 2;
    public static final int COLUMN_DATE = 3;
    public static final int COLUMN_INFO = 4;

    private Context context;
    private SQLiteDatabase db;

    private SQLiteStatement insertStmt;
    private static final String INSERT =
            "INSERT INTO " + TABLE_NAME + "(" +
                    KEY_TITLE + ", " +
                    KEY_DESCRIPTION + ", " +
                    KEY_DATE + ", " +
                    KEY_INFO + ") values (?, ?, ?, ?)";

    public DBHelper (Context context) throws Exception
    {
        this.context = context;
        try {
            OpenHelper openHelper = new OpenHelper(this.context);
            // Open a database for reading and writing
            db = openHelper.getWritableDatabase();
            // compile a sqlite insert statement into re-usable statement object.
            insertStmt = db.compileStatement(INSERT);
        } catch (Exception e) {
            Log.e(LOGTAG, " DBHelper constructor: could not get database " + e);
            throw (e);
        }
    }

    public long insert (Todolist todolist)
    {
        // bind values to the pre-compiled SQL statement "inserStmt"
        insertStmt.bindString(COLUMN_TITLE, todolist.getTasktitle());
        insertStmt.bindString(COLUMN_DESCRIPTION, todolist.getDescription());
        insertStmt.bindString(COLUMN_DATE, todolist.getDuedate());
        insertStmt.bindString(COLUMN_INFO, todolist.getInformation());

        long value =-1;
        try {
            // Execute the sqlite statement.
            value = insertStmt.executeInsert();
        } catch (Exception e) {
            Log.e(LOGTAG, " executeInsert problem: " + e);
        }
        Log.d (LOGTAG, "value="+value);
        return value;
    }

    // Helper class for DB creation/update
    // SQLiteOpenHelper provides getReadableDatabase() and getWriteableDatabase() methods
    // to get access to an SQLiteDatabase object; either in read or write mode.
    // A key defined as INTEGER PRIMARY KEY will autoincrement.
    private static class OpenHelper extends SQLiteOpenHelper
    {
        private static final String LOGTAG = "OpenHelper";

        private static final String CREATE_TABLE =
                "CREATE TABLE " +
                        TABLE_NAME +
                        "(" + KEY_ID + " INTEGER PRIMARY KEY, " +
                        KEY_TITLE + " TEXT, " +
                        KEY_DESCRIPTION + " TEXT, " +
                        KEY_DATE + " TEXT, " +
                        KEY_INFO + " TEXT);";


        OpenHelper (Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        /**
         * Creates the tables.
         * This function is only run once or after every Clear Data
         */
        @Override
        public void onCreate (SQLiteDatabase db)
        {
            Log.d(LOGTAG, " onCreate");
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e ) {
                Log.e(LOGTAG, " onCreate: Could not create SQL database: " + e);
            }
        }
        /**
         * called, if the database version is increased in your application code.
         * This method updating an existing database schema or dropping the existing database
         * and recreating it via the onCreate() method.
         */

        @Override
        public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(LOGTAG,"Upgrading database, this will drop tables and recreate.");
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
            } catch (Exception e ) {
                Log.e(LOGTAG, " onUpgrade: Could not update SQL database: " + e);
            }

            // Technique to add a column rather than recreate the tables.
            // String upgradeQuery_ADD_AREA =
            // "ALTER TABLE "+ TABLE_NAME + " ADD COLUMN " + KEY_AREA + " TEXT ";
            // if(oldVersion <2 ){
            // db.execSQL(upgradeQuery_ADD_AREA);
        }
    }

    public void deleteAll()
    {
        db.delete(TABLE_NAME, null, null);
    }

    // delete a row in the database
    public boolean deleteRecord(long rowId)
    {
        return db.delete(TABLE_NAME, KEY_ID + "=" + rowId, null) > 0;
    }

    public List<Todolist> selectAll()
    {
        List<Todolist> list = new ArrayList<Todolist>();


        // query takes the following parameters
        // dbName : the table name
        // columnNames: a list of which table columns to return
        // whereClause: filter of selection of data; null selects all data
        // selectionArg: values to fill in the ? if any are in the whereClause
        // group by: Filter specifying how to group rows, null means no grouping
        // having: filter for groups, null means none
        // orderBy: Table columsn used to order the data, null means no order.
        // A Cursor provides read-write access to the result set returned by a database query.
        // A Cursor represents the result of the query and points to one row of the query result.
        Cursor cursor = db.query(TABLE_NAME,
                new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE, KEY_INFO },
                null, null, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do
            {
                Todolist todolist = new Todolist();
                todolist.setTasktitle(cursor.getString(COLUMN_TITLE));
                todolist.setDescription(cursor.getString(COLUMN_DESCRIPTION));
                todolist.setDuedate(cursor.getString(COLUMN_DATE));
                todolist.setDuedate(cursor.getString(COLUMN_INFO));
                todolist.setId(cursor.getLong(COLUMN_ID));
                list.add(todolist);
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }
        return list;
    }

}