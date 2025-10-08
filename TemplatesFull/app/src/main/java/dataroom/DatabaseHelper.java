package dataroom;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ExampleDBOLE.class}, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {
    public abstract ExampleDBOLEDao exampleDbOLE();

    //if app doesn't run fully on the ui thread (which is rare)
    //enableMultiInstanceInvalidation in the application instantiation of room db
    //AppDatabase db = Room.databaseBuilder(getApplicationContext(),
    //        AppDatabase.class, "database-name").build();
}
