package MainActivity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Facilitate the needed SQLite Queries to get information back to the main java files
 */
@Dao
public interface groupDao {
    /**
     * add a user to a group with a created bridge
     * @param bridge
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addUserToGroup(groupUserBridge bridge);

    /**
     * get a list of all groups that the user is currently in
     * @param userId
     * @return List of groups
     */
    @Query("SELECT `groups`.* FROM `groups` " +
            "INNER JOIN groupUserBridge ON `groups`.groupId = groupUserBridge.groupId " +
            "WHERE groupUserBridge.userId = :userId")
    List<groups> getGroupsUserIn(int userId);

    /**
     * delete a user from a specific group
     * @param userId
     * @param groupId
     */
    @Query("DELETE FROM groupUserBridge WHERE userId = :userId AND groupId = :groupId")
    void deleteUserFromGroup(int userId, int groupId);

    /**
     * add a group to the groups database
     * @param group
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addGroup(groups group);

    /**
     * get a list of all group members within a specific group
     * @param groupId
     * @return List of users
     */
    @Query("SELECT * FROM users INNER JOIN GroupUserBridge ON users.userId = GroupUserBridge.userId WHERE GroupUserBridge.groupId = :groupId")
    List<User> getGroupMembers(int groupId);

    /**
     * get a list of all groups within the database
     * @return list of groups
     */
    @Query("SELECT * FROM 'groups'")
    List<groups> getAllGroups();

    /**
     * get a group based on their name
     * @param groupName
     * @return one group
     */
    @Query("SELECT * FROM 'groups' WHERE groupName = :groupName")
    groups getGroupByName(String groupName);
}
