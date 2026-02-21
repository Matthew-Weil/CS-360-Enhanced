package MainActivity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.weil_cs360_project_one.R;

import java.time.LocalDate;
import java.util.List;

public class InventoryManagement extends AppCompatActivity {
    //make these variables class variables so all methods can reach them
    private int userId;
    private AppDatabase db;

    private TableLayout tableLayout;

    private final String CHANNEL_ID = "notifications";

    private int checkQtyVal;


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
        setContentView(R.layout.inventory_information);

        //bring over the user id
        userId = getIntent().getIntExtra("USER_ID", 0);

        //Set the default value to 10
        checkQtyVal = 10;

        //create the database db (same database)
        db = AppDatabase.getDatabase(this);

        // If we can successfully get the user from the ID, set the header of the inventory to their name's inventory
        User currentUser = db.userDao().getUserByID(userId);
        if(currentUser != null) {
            TextView headerTextView = findViewById(R.id.inventory1_header_text);
            String headerTextString = currentUser.username + "'s Inventory";
            headerTextView.setText(headerTextString);
        }
        //get a list of all of the items the user currently has tracked
        List<Item> items = db.itemDao().getUserItems(userId);


        tableLayout = findViewById(R.id.inventory1_table);

        for (Item item : items) {
            displayExistingItem(item);
        }


        createNotificationChannel();
    }

    /**
     * Create the noticiatin channel that will be used
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
                    //TODO check if qty is over under set by user
                    sendNotification(itemName, newQty);

                    // Clear focus to hide keyboard
                    v.clearFocus();
                    return(true);
                }
                return(false);
            }

        });

        //create the new removeButton and associate its image
        ImageButton removeButton = new ImageButton(this);
        removeButton.setImageResource(android.R.drawable.ic_delete);



        //When the removeButton is click, remove the row that it is in
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup row = (ViewGroup) v.getParent();

                View itemNameView = row.getChildAt(0);
                String itemName = ((EditText) itemNameView).getText().toString();

                ViewGroup container = ((ViewGroup) row.getParent());
                container.removeView(row);
                container.invalidate();


                deleteAnItem(itemName);
            }
        });

        //set the text of the text based items
        columnItemText.setText(item.itemName);
        columnQtyText.setText(String.valueOf(item.quantity));
        columnDateText.setText(item.date);

        // Access views in the inflated row and set data
        row.addView(columnItemText);
        row.addView(columnQtyText);
        row.addView(columnDateText);
        row.addView(removeButton);

        // add the row to the tableLayout on the screen
        tableLayout.addView(row);

    }


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
    public void addNewItem() {
        // get num for new row item name
        int newRowNum = tableLayout.getChildCount();

        //Create and set the item qualities
        Item newItem = new Item();
        newItem.userId = userId;
        newItem.itemName = "item " + newRowNum;
        newItem.quantity = 0;
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
        addNewItem();
    }

    public void logOutButtonClicked(View v) {
        finish();
    }

    public void startNotificationSettingPage(View v) {
        Intent intent = new Intent(InventoryManagement.this, SmsNotification.class);
       // intent.putExtra("USER_ID", user.id);
        startActivity(intent);
    }
}



