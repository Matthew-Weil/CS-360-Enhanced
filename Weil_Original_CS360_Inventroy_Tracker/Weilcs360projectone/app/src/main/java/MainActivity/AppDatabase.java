package MainActivity;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Item.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    //Use the two created Daos to get information from the database back to the program
    public abstract UserDao userDao();
    public abstract ItemDao itemDao();

    private static volatile AppDatabase INSTANCE;

    /**
     * Create and build the AppDatabase, this is a singleton so only one will ever exist
     * @param context
     * @return
     */
    public static AppDatabase getDatabase(final android.content.Context context) {
        if(INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = androidx.room.Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "user_database")
                            .allowMainThreadQueries() //perform SQL queries dirctely from the main thread (Usually not a great idea, but for the size I am working with, it is fine)
                            .fallbackToDestructiveMigration() //Will destroy the previous data when I update the database to a new schema (I know it is depreciated, but for the purpose of this project it works great)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
