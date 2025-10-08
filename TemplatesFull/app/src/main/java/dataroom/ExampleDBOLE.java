package dataroom;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "exampleDBOLE", indices = {@Index("name")})
//        @Index(value = {"last_name", "address"})})
//@Fts4(languageId = "lid")
//In cases where a table supports content in multiple languages, use the languageId option to specify the column that stores language information for each row:
public class ExampleDBOLE {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;

    //    @ColumnInfo(name = "optionalnaming")
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @Ignore
//    transient
    private String base64;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
