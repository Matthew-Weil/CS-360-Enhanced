package MainActivity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "groups",
        indices = {@Index(value = {"groupName"}, unique = true)}
)
public class groups {
    @PrimaryKey(autoGenerate = true)
    public int groupId;
    public String groupName;
}
