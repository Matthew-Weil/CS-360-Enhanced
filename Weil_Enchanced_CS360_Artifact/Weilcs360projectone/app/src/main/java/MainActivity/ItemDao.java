package MainActivity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insertItem(Item item);

    @Update
    void updateItem(Item item);

    @Query("DELETE FROM items WHERE itemName = :name AND groupId IN " +
            "(SELECT groupId FROM groupUserBridge WHERE userId = :userId)")
    void deleteItemByName(String name, int userId);


    @Query("UPDATE items SET quantity = :newQty " +
            "WHERE itemName = :name AND groupId IN " +
            "(SELECT groupId FROM groupUserBridge WHERE userId = :userId)")
    void updateItemQuantity(String name, int newQty, int userId);


    @Query("UPDATE items SET itemName = :newName " +
            "WHERE itemName = :oldName AND groupId IN " +
            "(SELECT groupID FROM groupUserBridge WHERE userId = :userId)")
    void updateItemName(String oldName, String newName, int userId);

    @Query("SELECT items.* FROM items " +
            "INNER JOIN groupUserBridge ON items.groupId = groupUserBridge.groupId " +
            "WHERE groupUserBridge.userId = :userId")
    List<Item> getUserGroupItems(int userId);

    @Query("SELECT * FROM items WHERE userId = :userId")
    List<Item> getItemsOfOnlyUser(int userId);
}