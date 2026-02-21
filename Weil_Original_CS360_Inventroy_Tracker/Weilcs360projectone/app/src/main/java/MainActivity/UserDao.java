package MainActivity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    // Query to check if a user exists for login
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User login(String username, String password);

    // Query to check if a username is already taken during registration
    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername(String username);

    // Query to return a user based off of the user ID
    @Query("SELECT * FROM users WHERE id = :userID")
    User getUserByID(int userID);
}
