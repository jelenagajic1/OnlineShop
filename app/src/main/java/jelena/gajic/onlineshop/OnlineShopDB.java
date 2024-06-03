package jelena.gajic.onlineshop;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;

public class OnlineShopDB extends SQLiteOpenHelper {
    private final String TABLE1_NAME = "korisnici";
    public static final String TABLE1_USERNAME = "username";
    public static final String TABLE1_MAIL = "mail";
    public static final String TABLE1_PASSWORD = "password";
    public static final String TABLE1_ID = "ID";
    public static final String TABLE1_ADMIN_STATUS = "isAdmin";

    private final String TABLE2_NAME = "stavke";
    public static final String TABLE2_IMAGE_NAME = "imageName";
    public static final String TABLE2_ITEM_NAME = "itemName";
    public static final String TABLE2_CATEGORY = "category";
    public static final String TABLE2_PRICE = "price";

    private final String TABLE3_NAME = "istorija_kupovine";
    public static final String TABLE3_STATUS = "status";
    public static final String TABLE3_USERNAME = "username";
    public static final String TABLE3_DATE = "date";
    public static final String TABLE3_PRICE = "price";

    public OnlineShopDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE1_NAME +
                " (" + TABLE1_USERNAME + " TEXT, " +
                TABLE1_MAIL + " TEXT, " +
                TABLE1_PASSWORD + " TEXT, " +
                TABLE1_ADMIN_STATUS + " INTEGER, " +
                TABLE1_ID + " TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE2_NAME +
                " (" + TABLE2_IMAGE_NAME + " BLOB, " +
                TABLE2_ITEM_NAME + " TEXT, " +
                TABLE2_CATEGORY + " TEXT, " +
                TABLE2_PRICE + " TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE3_NAME +
                " (" + TABLE3_STATUS + " TEXT, " +
                TABLE3_USERNAME + " TEXT, " +
                TABLE3_DATE + " TEXT, " +
                TABLE3_PRICE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertUser(Korisnik user, String id, boolean isAdmin) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE1_USERNAME, user.getUsername());
        values.put(TABLE1_MAIL, user.getMail());
        values.put(TABLE1_PASSWORD, user.getPassword());
        values.put(TABLE1_ID, id);
        if(isAdmin)
            values.put(TABLE1_ADMIN_STATUS, 1);
        else
            values.put(TABLE1_ADMIN_STATUS, 0);

        db.insert(TABLE1_NAME, null, values);
        close();
    }

    // Convert Drawable to Bitmap
    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    // Convert Bitmap to Byte Array
    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void insertItem(Model item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        Bitmap bitmap = drawableToBitmap(item.getSlika());
        byte[] imageBytes = bitmapToByteArray(bitmap);
        values.put(TABLE2_IMAGE_NAME, imageBytes);
        values.put(TABLE2_ITEM_NAME, item.getNaziv());
        values.put(TABLE2_CATEGORY, item.getKategorija());
        values.put(TABLE2_PRICE, item.getCena());

        db.insert(TABLE2_NAME, null, values);
        close();
    }

    public void insertItemToPurchaseHistory(Model item, String username) {
        SQLiteDatabase db = getWritableDatabase();

        // Get current date
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = sdf.format(new Date(currentTimeMillis));

        ContentValues values = new ContentValues();
        values.put(TABLE3_DATE, formattedDate);
        values.put(TABLE3_USERNAME, username);
        values.put(TABLE3_PRICE, item.getCena());
        values.put(TABLE3_STATUS, "DELIVERED");

        db.insert(TABLE3_NAME, null, values);
        close();
    }

    public void deleteUser(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE1_NAME, TABLE1_ID + " =?", new String[]{String.valueOf(id)});
        close();
    }

    //public void deleteItem(String itemName)

    public Korisnik[] readUsers() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE1_NAME, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }
        Korisnik[] users = new Korisnik[cursor.getCount()];
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            users[i++] = createUser(cursor);
        }

        close();
        return users;
    }

    public Model[] readItems(Context context) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE2_NAME, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }
        Model[] items = new Model[cursor.getCount()];
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            items[i++] = createItem(context, cursor);
        }

        close();
        return items;
    }

    public Korisnik readUser(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE1_NAME, null, TABLE1_ID + " =?", new String[] {String.valueOf(id)}, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        cursor.moveToFirst();

        Korisnik user = createUser(cursor);

        close();
        return user;
    }

    //public ShoppingItem readItem(String itemName)

    private Korisnik createUser(Cursor cursor) {
        String username = cursor.getString(cursor.getColumnIndexOrThrow(TABLE1_USERNAME));
        String mail = cursor.getString(cursor.getColumnIndexOrThrow(TABLE1_MAIL));
        String passoword = cursor.getString(cursor.getColumnIndexOrThrow(TABLE1_PASSWORD));

        return new Korisnik(username, mail, passoword);
    }

    private Drawable byteToDrawable(Context context, byte[] imageBytes) {
        if (imageBytes == null) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    private Model createItem(Context context, Cursor cursor) {
        String itemName = cursor.getString(cursor.getColumnIndexOrThrow(TABLE2_ITEM_NAME));
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(TABLE2_IMAGE_NAME));
        String price = cursor.getString(cursor.getColumnIndexOrThrow(TABLE2_PRICE));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(TABLE2_CATEGORY));

        Drawable drawable = byteToDrawable(context, imageBytes);

        return new Model(drawable, itemName, price, category);
    }

    public boolean correctUsernameAndPassword(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {TABLE1_ID};

        // Define the selection criteria
        String selection = TABLE1_USERNAME + " = ? AND " + TABLE1_PASSWORD + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {username, password};

        // Perform the query
        Cursor cursor = db.query(
                TABLE1_NAME,    // The table to query
                projection,     // The columns to return
                selection,      // The columns for the WHERE clause
                selectionArgs,  // The values for the WHERE clause
                null,           // Don't group the rows
                null,           // Don't filter by row groups
                null            // The sort order
        );

        // Check if any row was returned
        boolean correctCredentials = cursor.moveToFirst();

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return correctCredentials;
    }

    public String getEmailByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {TABLE1_MAIL};

        // Define the selection criteria
        String selection = TABLE1_USERNAME + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {username};

        // Perform the query
        Cursor cursor = db.query(
                TABLE1_NAME,    // The table to query
                projection,     // The columns to return
                selection,      // The columns for the WHERE clause
                selectionArgs,  // The values for the WHERE clause
                null,           // Don't group the rows
                null,           // Don't filter by row groups
                null            // The sort order
        );

        String email = null;
        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndexOrThrow(TABLE1_MAIL));
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return email;
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {TABLE1_ID};

        // Define the selection criteria
        String selection = TABLE1_USERNAME + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {username};

        // Perform the query
        Cursor cursor = db.query(
                TABLE1_NAME,    // The table to query
                projection,     // The columns to return
                selection,      // The columns for the WHERE clause
                selectionArgs,  // The values for the WHERE clause
                null,           // Don't group the rows
                null,           // Don't filter by row groups
                null            // The sort order
        );

        // Check if any row was returned
        boolean exists = cursor.moveToFirst();

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return exists;
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();

        // Define the ContentValues object to hold the new password value
        ContentValues values = new ContentValues();
        values.put(TABLE1_PASSWORD, newPassword);

        // Define the selection criteria
        String selection = TABLE1_USERNAME + " = ?";

        // Define the selection argument
        String[] selectionArgs = {username};

        // Perform the update operation
        int rowsAffected = db.update(TABLE1_NAME, values, selection, selectionArgs);
        close();

        return rowsAffected > 0;
    }

    public boolean correctPassword(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {TABLE1_ID};

        // Define the selection criteria
        String selection = TABLE1_USERNAME + " = ? AND " + TABLE1_PASSWORD + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {username, password};

        // Perform the query
        Cursor cursor = db.query(
                TABLE1_NAME,    // The table to query
                projection,     // The columns to return
                selection,      // The columns for the WHERE clause
                selectionArgs,  // The values for the WHERE clause
                null,           // Don't group the rows
                null,           // Don't filter by row groups
                null            // The sort order
        );

        // Check if any row was returned
        boolean correctPassword = cursor.moveToFirst();

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return correctPassword;
    }

    public String[] findCategories() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(true, TABLE2_NAME, new String[]{TABLE2_CATEGORY}, null, null, null, null, null, null);

        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }

        String[] categories = new String[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            categories[i++] = cursor.getString(cursor.getColumnIndexOrThrow(TABLE2_CATEGORY));
        }

        cursor.close();
        db.close();

        return categories;
    }

    public Model[] getItemsByCategory(Context context, String category) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE2_NAME, null, TABLE2_CATEGORY + " = ?", new String[]{category}, null, null, null);

        if (cursor == null || cursor.getCount() <= 0) {
            // No items found for the given category
            return null;
        }

        Model[] items = new Model[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(TABLE2_ITEM_NAME));
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(TABLE2_IMAGE_NAME));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(TABLE2_PRICE));

            Drawable drawable = byteToDrawable(context, imageBytes);
            items[i++] = new Model(drawable, itemName, price, category);
        }

        cursor.close();
        db.close();

        return items;
    }

    public PurchaseHistoryModel[] getAllPurchaseHistoryItems(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE3_NAME,
                null,
                TABLE3_USERNAME + " =? ",
                new String[]{username},
                null,
                null,
                null
        );

        if(!cursor.moveToFirst())
            return null;

        PurchaseHistoryModel[] purchases = new PurchaseHistoryModel[cursor.getCount()];
        int pos = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TABLE3_DATE));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(TABLE3_PRICE));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(TABLE3_STATUS));

            PurchaseHistoryModel.State state;
            switch(status){
                case "DELIVERED":
                    state = PurchaseHistoryModel.State.DELIVERED;
                    break;
                case "CANCELLED":
                    state = PurchaseHistoryModel.State.CANCELLED;
                    break;
                default:
                    state = PurchaseHistoryModel.State.WAITING_FOR_DELIVERY;
                    break;
            }
            purchases[pos] =  new PurchaseHistoryModel(state, Integer.valueOf(price), date);
            pos++;
        }
        return purchases;
    }

    public boolean isAdmin(String username){
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {TABLE1_ADMIN_STATUS};

        // Define the selection criteria
        String selection = TABLE1_USERNAME + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {username};

        // Perform the query
        Cursor cursor = db.query(
                TABLE1_NAME,    // The table to query
                projection,     // The columns to return
                selection,      // The columns for the WHERE clause
                selectionArgs,  // The values for the WHERE clause
                null,           // Don't group the rows
                null,           // Don't filter by row groups
                null            // The sort order
        );

        boolean isAdmin = false;
        if (cursor.moveToFirst()) {
            int isAdminValue = cursor.getInt(cursor.getColumnIndexOrThrow(TABLE1_ADMIN_STATUS));
            isAdmin = isAdminValue == 1;
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return isAdmin;
    }
}