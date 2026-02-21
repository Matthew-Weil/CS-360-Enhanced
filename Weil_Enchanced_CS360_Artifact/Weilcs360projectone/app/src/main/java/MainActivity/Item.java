package MainActivity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "items",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE)) // If user is deleted, their items are too
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int itemId;
    public int userId;
    //This group id is used to determine which group the item belongs in
    public int groupId;
    public String itemName;
    public int quantity;
    public String date;
}
