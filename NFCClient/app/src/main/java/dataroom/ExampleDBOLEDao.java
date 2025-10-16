package dataroom;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExampleDBOLEDao {
    @Query("SELECT * FROM exampleDBOLE")
    List<ExampleDBOLE> getExamples();

    @Query("SELECT * FROM exampleDBOLE where id in (:ids)")
    List<ExampleDBOLE> getExamplesByIds(Long...ids); //expediency may make an array or backwards compat

    @Insert
    void insertAll(ExampleDBOLE...examples);

    @Delete
    void deleteAll(ExampleDBOLE...examples);

}
