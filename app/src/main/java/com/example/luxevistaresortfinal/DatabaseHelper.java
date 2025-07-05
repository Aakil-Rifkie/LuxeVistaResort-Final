package com.example.luxevistaresortfinal;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LuxeVista Resort";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_ROLE = "role";

    private static final String COLUMN_PREFERRED_ROOM = "preferred_room";
    private static final String COLUMN_PREFERRED_SERVICE = "preferred_service";
    private static final String COLUMN_TRAVEL_START_DATE = "travel_start_date";
    private static final String COLUMN_TRAVEL_END_DATE = "travel_end_date";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_AGE + " INTEGER," +
                COLUMN_ROLE + " TEXT," +
                COLUMN_PREFERRED_ROOM + " TEXT," +
                COLUMN_PREFERRED_SERVICE + " TEXT," +
                COLUMN_TRAVEL_START_DATE + " TEXT," +
                COLUMN_TRAVEL_END_DATE + " TEXT" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_ROOMS_TABLE = "CREATE TABLE rooms (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "room_name TEXT," +
                "description TEXT," +
                "price REAL" +
                ")";
        db.execSQL(CREATE_ROOMS_TABLE);

        String CREATE_BOOKINGS_TABLE = "CREATE TABLE bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "room_id INTEGER," +
                "user_id INTEGER," +
                "check_in TEXT," +
                "check_out TEXT," +
                "FOREIGN KEY (room_id) REFERENCES rooms(id)," +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ")";
        db.execSQL(CREATE_BOOKINGS_TABLE);

        String CREATE_SERVICES_TABLE = "CREATE TABLE services (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "service_name TEXT," +
                "description TEXT," +
                "price REAL" +
                ")";
        db.execSQL(CREATE_SERVICES_TABLE);

        String CREATE_SERVICE_BOOKINGS_TABLE = "CREATE TABLE service_bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "service_id INTEGER," +
                "user_id INTEGER," +
                "date TEXT," +
                "FOREIGN KEY (service_id) REFERENCES services(id)," +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ")";
        db.execSQL(CREATE_SERVICE_BOOKINGS_TABLE);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS rooms");
        db.execSQL("DROP TABLE IF EXISTS bookings");
        db.execSQL("DROP TABLE IF EXISTS services");
        db.execSQL("DROP TABLE IF EXISTS service_bookings");
        onCreate(db);
    }

    public boolean addUser(String email, String password, String username, int age) {
        if (emailExist(email)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_ROLE, "user");

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1;
    }

    public boolean emailExist(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=?", new String[]{email},
                null, null, null);

        boolean exists = (cursor != null && cursor.moveToFirst());
        if (cursor != null) cursor.close();
        return exists;
    }

    public String getUserRole(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ROLE},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }

        return null;
    }

    public boolean insertRoom(String roomName, String description, double price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("room_name", roomName);
        values.put("description", description);
        values.put("price", price);

        long result = db.insert("rooms", null, values);
        db.close();
        return result != -1;
    }

    public Cursor getAllRooms() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM rooms", null);
    }

    public boolean isRoomAvailable(int roomId, String checkIn, String checkOut) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM bookings WHERE room_id = ? AND (" +
                "(date(check_in) <= date(?) AND date(check_out) >= date(?)) OR " +
                "(date(check_in) <= date(?) AND date(check_out) >= date(?)) OR " +
                "(date(?) <= date(check_in) AND date(?) >= date(check_out))" +
                ")";
        Cursor cursor = db.rawQuery(query, new String[]{
                String.valueOf(roomId),
                checkOut, checkIn,
                checkOut, checkIn,
                checkIn, checkOut
        });

        boolean available = !cursor.moveToFirst();
        cursor.close();
        return available;
    }

    public boolean addBooking(int roomId, int userId, String checkIn, String checkOut) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("room_id", roomId);
        values.put("user_id", userId);
        values.put("check_in", checkIn);
        values.put("check_out", checkOut);

        long result = db.insert("bookings", null, values);
        db.close();
        return result != -1;
    }

    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, "email = ?", new String[]{email}, null, null, null);
    }

    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID}, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            cursor.close();
            return userId;
        }
        return -1;
    }

    public Cursor getAllServices() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM services", null);
    }

    public boolean isServiceAvailable(int serviceId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM service_bookings WHERE service_id = ? AND date = ?",
                new String[]{String.valueOf(serviceId), date});

        boolean available = !cursor.moveToFirst();
        cursor.close();
        return available;
    }

    public boolean addServiceBooking(int serviceId, int userId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("service_id", serviceId);
        values.put("user_id", userId);
        values.put("date", date);

        long result = db.insert("service_bookings", null, values);
        db.close();
        return result != -1;
    }

    public boolean updateUserProfile(String email, String preferredRoom, String preferredService, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("preferred_room", preferredRoom);
        values.put("preferred_service", preferredService);
        values.put("travel_start_date", startDate);
        values.put("travel_end_date", endDate);

        int result = db.update("users", values, "email = ?", new String[]{email});
        db.close();
        return result > 0;
    }


    public ArrayList<String> getUserBookingHistory(int userId) {
        ArrayList<String> history = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor roomCursor = db.rawQuery(
                "SELECT * FROM bookings INNER JOIN rooms ON bookings.room_id = rooms.id WHERE bookings.user_id = ?",
                new String[]{String.valueOf(userId)});

        while (roomCursor.moveToNext()) {
            String roomName = roomCursor.getString(roomCursor.getColumnIndexOrThrow("room_name"));
            String checkIn = roomCursor.getString(roomCursor.getColumnIndexOrThrow("check_in"));
            String checkOut = roomCursor.getString(roomCursor.getColumnIndexOrThrow("check_out"));
            history.add("Room: " + roomName + "\nCheck-in: " + checkIn + "\nCheck-out: " + checkOut);
        }
        roomCursor.close();

        Cursor serviceCursor = db.rawQuery(
                "SELECT * FROM service_bookings INNER JOIN services ON service_bookings.service_id = services.id WHERE service_bookings.user_id = ?",
                new String[]{String.valueOf(userId)});

        while (serviceCursor.moveToNext()) {
            String serviceName = serviceCursor.getString(serviceCursor.getColumnIndexOrThrow("service_name"));
            String date = serviceCursor.getString(serviceCursor.getColumnIndexOrThrow("date"));
            history.add("Service: " + serviceName + "\nDate: " + date);
        }
        serviceCursor.close();

        return history;
    }


}