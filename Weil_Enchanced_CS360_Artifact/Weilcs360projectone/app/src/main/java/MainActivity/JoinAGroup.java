package MainActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weil_cs360_project_one.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JoinAGroup extends AppCompatActivity {

    //CREATE THE CLASS VARS
    private Button joinButton;
    private Button createButton;

    private Spinner spinnerDropdown;

    private AppDatabase db;

    private List<String> groupNameList;

    private int userId;

    /**
     * Used when this page is created for the first time
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_group);

        //get the passed userId
        userId = getIntent().getIntExtra("USER_ID", 0);

        //assign the buttons
        joinButton = findViewById(R.id.group_join_button);
        createButton = findViewById(R.id.group_create_button);

        //assign the spinner
        spinnerDropdown = findViewById(R.id.group_spinner);

        //create the database db (same database)
        db = AppDatabase.getDatabase(this);

        populateSpinner();

    }

    /**
     *  Return the string of whatever the spinner currently has selected
     * @return
     */
    private String getStringFromSpinner() {
        Object selectedObject = spinnerDropdown.getSelectedItem();

        return(selectedObject.toString());

    }

    /**
     * grab the names of all of the possible groups and add them to the spinner
     */
    private void populateSpinner() {

        //grab the lists of groups on the device
        groupNameList = getGroupNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDropdown.setAdapter(adapter);

    }

    /**
     * get the complete list of groups within the database
     * @return
     */
    private List<String> getGroupNames() {
        List<groups> groupsList = db.groupDao().getAllGroups();
        List<String> nameList = new ArrayList<>();

        System.out.println("In getGroupNames()");

        for(groups group : groupsList) {
            System.out.println("groupName = " + group.groupName);

            nameList.add((String) group.groupName);
            System.out.println("Added groupName to list " + group.groupName);

        }
        return(nameList);
    }

    /**
     * When the inventoryBtn is clicked just finish this screen to go back
     * @param v
     */
    public void inventoryBtnClicked(View v) {
        finish();
    }

    /**
     * When the user hits the createGroup button it will show the add_group_pop_up.xml
     * @param v
     */
    public void createGroup(View v) {
        showAddGroupPopUp();
        finish();   //leave the page
    }

    /**
     * When the user hits the join group button it will add the user to the currently selected group from the spinner
     * @param v
     */
    public void joinGroup(View v) {
        addUserToGroup(getStringFromSpinner());
        finish();   //leave the page
    }

    /**
     * Actually display the add_group_pop_up.xml and process the user input within it
     */
    private void showAddGroupPopUp() {
        //create the customLayout based on the add_group_pop_up.xml
        LayoutInflater inflater = getLayoutInflater();
        View customLayout = inflater.inflate(R.layout.add_group_pop_up, null);

        //Link the edit text fields so we can grab their values later
        final EditText groupNameField = customLayout.findViewById(R.id.add_group_group_name);

        //create the pop up dialog input box
        new AlertDialog.Builder(this)
                .setTitle("Enter New Item Information")
                .setView(customLayout)

                //when the user clicks ok grab the input
                .setPositiveButton("OK", (dialog, which) -> {
                    String groupName = groupNameField.getText().toString();



                    //if we successfully got all values, we can pass it along and create a group
                    addNewGroup(groupName);

                    //add the user to the new group
                    addUserToGroup(groupName);


                })
                //If the user hits cancel do nothing with the input and just close the page
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }

    /**
     * Addd a new group to the database
     * @param groupName
     */
    private void addNewGroup(String groupName) {
        //create a new group
        groups newGroup = new groups();
        newGroup.groupName = groupName;

        //add the group to the db
        db.groupDao().addGroup(newGroup);
    }

    /**
     * Within the groupUserBridge table, add the user to the passed group
     * @param groupName
     */
    private void addUserToGroup(String groupName) {
        //get the groupId based on the name
        groups currentGroup = db.groupDao().getGroupByName(groupName);

        //get a list of groups that the user is in
        List<groups> userInGroupList = db.groupDao().getGroupsUserIn(userId);

        for(groups group: userInGroupList) {
            //function from Dao to remove the group from the groupUserBridge
            db.groupDao().deleteUserFromGroup(userId, group.groupId);
        }

        //create the new bridge object to be added to the db and add it
        groupUserBridge bridge = new groupUserBridge(userId, currentGroup.groupId);
        db.groupDao().addUserToGroup(bridge);
    }
}
