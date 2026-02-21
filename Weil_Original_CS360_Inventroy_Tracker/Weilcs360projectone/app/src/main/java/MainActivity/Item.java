package MainActivity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "items",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE)) // If user is deleted, their items are too
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int itemId;

    public int userId;
    public String itemName;
    public int quantity;
    public String date;
}
