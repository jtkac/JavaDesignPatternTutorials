package data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLiteOriginalSetup extends SQLiteOpenHelper {
    private static final String dbName = "SomeDBName";
    private static final int dbVersion = 1;
    public SQLiteOriginalSetup(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table with example
        new ExampleDBEncaps().createExampleDBOLETable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //perform upgrade or drop tables
        db.execSQL("DROP TABLE IF EXISTS " + ExampleDBEncaps.TABLENAME);
    }
    //table name


    protected class ExampleDBEncaps {
        public static final String TABLENAME = "tblExample";
        public static final String ID = "Id";
        public static final String TITLE = "Name";
        public static final String DESCRIPTION = "Description";

        protected void createExampleDBOLETable(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLENAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT NOT NULL, " + DESCRIPTION + " TEXT);");
        }

        protected void upsertExampleOLE(ExampleDBOLE example) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(TITLE, example.getName());
            cv.put(DESCRIPTION, example.getDescription());
            cv.put(ID, example.getId());
            example.setId((long)db.insertWithOnConflict(TABLENAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE));


        }

        @SuppressLint("Range")
        protected ExampleDBOLE parseExample(Cursor cursor) {
            ExampleDBOLE example = new ExampleDBOLE();
            while (!cursor.isAfterLast()) {
                example.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                example.setName(cursor.getString(cursor.getColumnIndex(TITLE)));
                example.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                break;
            }
            return example;
        }

        protected List<ExampleDBOLE> parseMultipleExamples(Cursor cursor) {
            List<ExampleDBOLE> examples = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                examples.add(parseExample(cursor));
                cursor.moveToNext();
            }
            cursor.close();
            return examples;
        }

        protected List<ExampleDBOLE> getExamples() {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.query(TABLENAME, null, null, null, null, null, null);
            return parseMultipleExamples(c);
        }

        protected ExampleDBOLE getExampleById(long id) {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.query(TABLENAME, null, ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            return parseExample(c);
        }


        protected void deleteExampleById(long id) {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLENAME, ID + " = ?", new String[]{String.valueOf(id)});
        }

    }
}
