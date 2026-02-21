package MainActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.weil_cs360_project_one.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryManagement extends AppCompatActivity {
    //make these variables class variables so all methods can reach them
    private int userId;
    private AppDatabase db;

    private Button selectButton;
    private Button removeButton;
    private TableLayout tableLayout;

    private final String CHANNEL_ID = "notifications";

    private List<TableRow> rowDeletionList;

    private int checkQtyVal;

    private boolean selectMode;

    private boolean isFirstLaunch = true;


    /**
     * Used when this page is created for the first time
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //System.out.println("Test print");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_information);

        //bring over the user id
        userId = getIntent().getIntExtra("USER_ID", 0);

        //assign the buttons
        selectButton = findViewById(R.id.inventory1_select_button);
        removeButton = findViewById(R.id.inventory1_remove_button);

        //Set the default value to 10
        checkQtyVal = 10;

        //create the database db (same database)
        db = AppDatabase.getDatabase(this);

        //create an ArrayList for stored deletion
        rowDeletionList = new ArrayList<>();



        //init vars for header text
        TextView headerTextView = findViewById(R.id.inventory1_header_text);
        String headerTextString = "";

        //if the user is not in a group
        if(db.groupDao().getGroupsUserIn(userId).isEmpty()) {
            // If we can successfully get the user from the ID, set the header of the inventory to their name's inventory
            User currentUser = db.userDao().getUserByID(userId);
            if (currentUser != null) {

                headerTextString = currentUser.username + "'s Inventory";

            }

        }
        else {
            //get the group the user is in and set the title to their group name
            List<groups> currentGroups = db.groupDao().getGroupsUserIn(userId);
            headerTextString = currentGroups.get(0).groupName + "'s Inventory";

        }


        //Actually set the header text
        headerTextView.setText(headerTextString);


        //default the items
        List<Item> items;

        //if the user is not in the default group change the items to the groups items
        if(!db.groupDao().getGroupsUserIn(userId).isEmpty()) {
            //get a list of all of the items the user currently has tracked
            items = db.itemDao().getUserGroupItems(userId);
        }
        //if the user is not in a group than get only their items
        else {
            items = db.itemDao().getItemsOfOnlyUser(userId);
        }



        tableLayout = findViewById(R.id.inventory1_table);

        for (Item item : items) {
            displayExistingItem(item);
        }


        createNotificationChannel();

        isFirstLaunch = true;
    }

    /**
     * When the inventory is resumed, reload the items
     */
    @Override
    protected void onResume() {
        super.onResume();



        if (isFirstLaunch) {
            isFirstLaunch = false; //flip the flag so it runs only when truly resumed
        } else {



            //init vars for header text
            TextView headerTextView = findViewById(R.id.inventory1_header_text);
            String headerTextString = "";

            //if the user is not in a group
            if(db.groupDao().getGroupsUserIn(userId).isEmpty()) {
                // If we can successfully get the user from the ID, set the header of the inventory to their name's inventory
                User currentUser = db.userDao().getUserByID(userId);
                if (currentUser != null) {

                    headerTextString = currentUser.username + "'s Inventory";

                }

            }
            else {
                //get the group the user is in and set the title to their group name
                List<groups> currentGroups = db.groupDao().getGroupsUserIn(userId);
                headerTextString = currentGroups.get(0).groupName + "'s Inventory";

            }


            //Actually set the header text
            headerTextView.setText(headerTextString);

            //get a count of all rows within the table
            int rowCount = tableLayout.getChildCount();

            //if there is more than just the header
            if(rowCount > 1) {
                //remove all rows except the first row
                tableLayout.removeViews(1, rowCount - 1);
            }


            //reload the items
            //default the items to only the users items
            List<Item> items;

            //if the user is not in the default group change the items to the groups items
            if(!db.groupDao().getGroupsUserIn(userId).isEmpty()) {
                //get a list of all of the items the user currently has tracked
                items = db.itemDao().getUserGroupItems(userId);
            }
            //if the user is not in a group than get only their items
            else {
                items = db.itemDao().getItemsOfOnlyUser(userId);
            }


            tableLayout = findViewById(R.id.inventory1_table);

            for (Item item : items) {
                displayExistingItem(item);
            }
        }
    }

    /**
     * Create the notification channel that will be used
     */
    private void createNotificationChannel() {
        CharSequence name = "Inventory Updates";
        String description = "Notifications for inventory changes";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        // Register the channel with the system
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * Display the passed item to the dataTable on the screen
     * @param item The passed item to be displayed
     */
    public void displayExistingItem(Item item) {
        //init needed assets
        TableRow row = new TableRow(this);
        TextView columnDateText = new TextView(this);
        EditText columnQtyText = new EditText(this);
        EditText columnItemText = new EditText(this);

        //set the color of the new EditTexts
        columnQtyText.setTextColor(Color.parseColor("#ADD8E6"));
        columnItemText.setTextColor(Color.parseColor("#ADD8E6"));

        //set IME options so the keyboard shows a done button
        columnQtyText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        columnQtyText.setInputType(InputType.TYPE_CLASS_NUMBER);
        columnItemText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        columnItemText.setInputType(InputType.TYPE_CLASS_TEXT);

        //If the user clicked on the item name then immediately save it to the EditTexts view
        columnItemText.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                v.setTag(((EditText) v).getText().toString());
            }
        });

        //Skipped using an override and just went straight to the column.setOnEditorActionListener(...)
        //If the user is done editing the text then save the new text as the items name
        columnItemText.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                String oldName = v.getTag().toString();
                String newName = v.getText().toString();

                //If the name is not empty and not the same as the old name then make the new itemName permanent
                if(!newName.isEmpty() && !newName.equals(oldName)) {
                    db.itemDao().updateItemName(oldName, newName, userId);
                    v.setTag(newName);
                }
                //Clear the focus on the object
                v.clearFocus();
                return(true);
            }
            return(false);
        });

        //Used an override here, found a better method for columnItemText above
        //If the user edits the qty of an item
        columnQtyText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //If the user is done with the edit or left it empty
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    //Get the parent row
                    TableRow parentRow = (TableRow) v.getParent();

                    //Get the name from the first column
                    EditText firstCol = (EditText) parentRow.getChildAt(0);
                    String itemName = firstCol.getText().toString();

                    //Get the new quantity
                    String newQtyString = v.getText().toString();
                    int newQty = newQtyString.isEmpty() ? 0 : Integer.parseInt(newQtyString);

                    //Update the rows associated item to the new qty
                    db.itemDao().updateItemQuantity(itemName, newQty, userId);

                    sendNotification(itemName, newQty);

                    // Clear focus to hide keyboard
                    v.clearFocus();
                    return(true);
                }
                return(false);
            }

        });







        //set the text of the text based items
        columnItemText.setText(item.itemName);
        columnQtyText.setText(String.valueOf(item.quantity));
        columnDateText.setText(item.date);

        //Access views in the inflated row and set data
        row.addView(columnItemText);
        row.addView(columnQtyText);
        row.addView(columnDateText);

        //make the row clickable
        row.setClickable(true);

        //Override the on click to store the row for deletion
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //only continue if we are in select mode
                if(selectMode) {
                    //if the row is not already selected
                    if(!rowDeletionList.contains(row)) {

                        //Change the background of the row to ensure it is VISIBLY selected
                        TypedValue typedValue = new TypedValue();
                        row.getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondaryVariant, typedValue, true);
                        row.setBackgroundColor(typedValue.data);

                        //Also change the background of the 3rd coloumn textView since it has an independent background color vs the row
                        TextView textView = (TextView) row.getChildAt(2);
                        textView.setBackgroundColor(typedValue.data);


                        //Add the row to the stored deletion list for if the user clicks the removeButton
                        rowDeletionList.add(row);
                    }
                    //if the row is already selected
                    else {
                        //Change the background of the row to ensure it is 'default'
                        row.setBackgroundColor(Color.parseColor("#1C2E4A"));

                        //Also change the background of the 3rd coloumn textView since it has an independent background color vs the row
                        TextView textView = (TextView) row.getChildAt(2);
                        textView.setBackgroundColor(Color.parseColor("#1C2E4A"));


                        //remove the row from the deletion list
                        rowDeletionList.remove(row);
                    }
                }
            }
        });

        //add the row to the tableLayout on the screen
        tableLayout.addView(row);

    }

    /**
     * The qty has dipped below the wanted user level
     * send notification to the user alerting them of the qty change
     * @param itemName
     * @param itemQty
     */
    public void sendNotification(String itemName, int itemQty) {
        //if we have permission from the user to send them notifications
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info) // Required icon
                    .setContentTitle("Qty below " + checkQtyVal)
                    .setContentText("The item " + itemName + "is now at qty: " + itemQty);

            //Display the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());
        }
    }


    /**
     * Add a new item to the database
     */
    public void addNewItem(String passedItemName, int passedItemQty) {

        List<groups> userGroups = db.groupDao().getGroupsUserIn(userId);

        int activeGroupId = 0;
        if (!userGroups.isEmpty()) {
            activeGroupId = userGroups.get(0).groupId;
        }

        //Create and set the item qualities
        Item newItem = new Item();
        newItem.userId = userId;
        newItem.itemName = passedItemName;
        newItem.quantity = passedItemQty;
        newItem.groupId = activeGroupId;
        newItem.date = LocalDate.now().toString();


        //Insert the item into the database
        db.itemDao().insertItem(newItem);

        //Display the new item on the screen
        displayExistingItem(newItem);
    }

    /**
     * Delete the item based on its name
     * @param itemName The name of the item
     */
    public void deleteAnItem(String itemName) {
        db.itemDao().deleteItemByName(itemName, userId);
    }

    /**
     * The add button was clicked so we need to add a new row to the tableLayout
     * @param v
     */
    public void addButtonClicked(View v) {
        showAddItemPopUp();

    }

    /**
     * Create a pop up dialog window for the user to enter the new item information
     * before it ever gets added to the database
     */
    private void showAddItemPopUp() {
        LayoutInflater inflater = getLayoutInflater();
        View customLayout = inflater.inflate(R.layout.add_item_pop_up, null);

        //Link the edit text fields so we can grab their values later
        final EditText itemNameField = customLayout.findViewById(R.id.add_item_item_name);
        final EditText itemQtyField = customLayout.findViewById(R.id.add_item_item_qty);

        //create the pop up dialog input box
        new AlertDialog.Builder(this)
                .setTitle("Enter New Item Information")
                .setView(customLayout)

                //when the user clicks ok grab the input
                .setPositiveButton("OK", (dialog, which) -> {
                    String itemName = itemNameField.getText().toString();

                    //try to convert the itemQty to an int
                    try {
                        int itemQty = Integer.parseInt(itemQtyField.getText().toString());

                        //if we successfully got all values, we can pass it along and create an item
                        addNewItem(itemName, itemQty);

                    } catch(NumberFormatException e) {
                        System.out.println("ERROR - INVALID QTY (NOT INT)");
                    }


                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }


    /**
     * The log out button was clicked so just leave the session and go back to the log in page
     * @param v
     */
    public void logOutButtonClicked(View v) {
        finish();
    }

    /**
     * start the notification settings page and switch to it
     * @param v
     */
    public void startNotificationSettingPage(View v) {
        Intent intent = new Intent(InventoryManagement.this, SmsNotification.class);
       // intent.putExtra("USER_ID", user.id);
        startActivity(intent);
    }

    public void startGroupSettingsPage(View v) {
        Intent intent = new Intent(InventoryManagement.this, JoinAGroup.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    /**
     * Toggle the selectMode Boolean variable
     * If we are switching to a FALSE state:
     *      - Changer the button color back to normal
     *      - Make the remove button invisible
     *      - Change the row colors back to normal
     *
     * If we are switch to a TRUE state:
     *      - Change the button color to the pressed look
     *      - Make the remove button visible
     */
    private void changeSelectMode() {
        //if the select mode is currently true, set it to false
        if(selectMode) {
            selectMode = false;

            //Change the button back to the normal look
            TypedValue typedValue = new TypedValue();
            selectButton.getContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
            selectButton.setBackgroundTintList(ColorStateList.valueOf(typedValue.data));

            //set the remove button to invisible
            removeButton.setVisibility(View.INVISIBLE);

            //set all the rows within the TableLayout back to the default color
            for(int i = 0; i < tableLayout.getChildCount(); i++) {
                View child = tableLayout.getChildAt(i);
                if (child instanceof TableRow) {
                    //get the row and change it to the default color
                    TableRow row = (TableRow) child;
                    row.setBackgroundColor(Color.parseColor("#1C2E4A"));

                    //Also change the background of the 3rd coloumn textView since it has an independent background color vs the row
                    TextView textView = (TextView) row.getChildAt(2);
                    textView.setBackgroundColor(Color.parseColor("#1C2E4A"));
                }
            }

            //Clear the row deletion list because it does not carry over inbetween 'select' instances
            rowDeletionList.clear();

        }
        //if the select mode is currently false, set it to true
        else {
            selectMode = true;

            //Change to pressed color
            TypedValue typedValue = new TypedValue();
            selectButton.getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
            selectButton.setBackgroundTintList(ColorStateList.valueOf(typedValue.data));

            //set the remove button to visible
            removeButton.setVisibility(View.VISIBLE);

        }
    }

    /**
     * Perform the row deletion from both the database and the TableLayout UI
     */
    private void performRowDeletion() {
        //System.out.println(rowDeletionList.size());

        for(TableRow row : rowDeletionList) {
            //System.out.println(((TextView) row.getChildAt(0)).getText().toString());

            //Actually delete the item from the database
            deleteAnItem(((TextView) row.getChildAt(0)).getText().toString());

            //Delete the row from the TableView
            ViewGroup container = ((ViewGroup) row.getParent());
            container.removeView(row);
        }

        //clear the arrayList
        rowDeletionList.clear();
    }

    /**
     * Called when the select button is clicked
     * @param v
     */
    public void SelectButtonClicked(View v) {
        //Toggle the selectMode variable (boolean)
        changeSelectMode();
    }

    /**
     * called when the remove button is clicked
     * @param v
     */
    public void RemoveButtonClicked(View v) {
        //perform the deletion
        performRowDeletion();

        //change the select mode back to normal
        changeSelectMode();
    }

}



