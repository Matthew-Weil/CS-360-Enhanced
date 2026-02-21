package MainActivity;

import androidx.room.Entity;

@Entity(primaryKeys = {"userId", "groupId"})
public class groupUserBridge {
    public int userId;
    public int groupId;

    /**
     * Constructor to make a new bridge
     * @param userId
     * @param groupId
     */
    public groupUserBridge(int userId, int groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }
}
