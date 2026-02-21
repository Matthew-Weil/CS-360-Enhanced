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

    @Query("SELECT * FROM items WHERE userId = :currentUserId")
    List<Item> getUserItems(int currentUserId);

    @Update
    void updateItem(Item item);

    @Query("DELETE FROM items WHERE itemName = :name AND userId = :currentUserId")
    void deleteItemByName(String name, int currentUserId);

    @Query("UPDATE items SET quantity = :newQty WHERE itemName = :itemName AND userId = :userId")
    void updateItemQuantity(String itemName, int newQty, int userId);

    @Query("UPDATE items SET itemName = :newName WHERE itemName = :oldName AND userId = :userId")
    void updateItemName(String oldName, String newName, int userId);
}