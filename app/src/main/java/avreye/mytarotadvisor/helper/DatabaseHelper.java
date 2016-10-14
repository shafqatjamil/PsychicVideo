package avreye.mytarotadvisor.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import avreye.mytarotadvisor.Object.Message;
import avreye.mytarotadvisor.Object.OrderHistoryItemObject;

public class DatabaseHelper extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "user_data";

	// hand design table name
	private static final String TABLE_MESSAGE = "message";

	// foot design tbale name

	private static final String KEY_ID = "id";
	private static final String KEY_SENDER_ID = "sender_id";
	private static final String KEY_SENDER_DISPLAY_NAME = "sender_displayname";
	private static final String KEY_RECIEVER_ID = "reciever_id";
	private static final String KEY_RECIEVER_DISPLAY_NAME = "reciever_displayname";
	private static final String KEY_TEXT = "text";
	private static final String KEY_DATE= "send_receive_date";
	private static final String KEY_STATUS = "status";
	private static final String KEY_URL = "url";
	private static final String KEY_TYPE = "type";
	private static final String KEY_SENDER_TYPE = "sender_type";
	private static final String KEY_RECIEVER_TYPE = "reciever_type";
	private static final String KEY_REVIEW_STATUS = "review_status";
	private static final String KEY_MESSAGE_REVIEW_ID = "message_review_id";
    private static final String KEY_RID = "rid";
    private static final String KEY_DOB = "dob";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_FAVOURITE_TABLE = "CREATE TABLE " + TABLE_MESSAGE + "("
				+ KEY_ID + " INTEGER,"// PRIMARY KEY AUTOINCREMENT
				+ KEY_SENDER_ID + " TEXT,"
                + KEY_SENDER_DISPLAY_NAME + " TEXT,"
                + KEY_RECIEVER_ID + " TEXT,"
                + KEY_RECIEVER_DISPLAY_NAME + " TEXT,"
                + KEY_TEXT+ " TEXT,"
                + KEY_DATE + " VARCHAR(100),"
                + KEY_STATUS + " INTEGER,"
                + KEY_URL + " VARCHAR(100),"
                + KEY_TYPE + " VARCHAR(10),"
                + KEY_SENDER_TYPE + " VARCHAR(10),"
                + KEY_RECIEVER_TYPE + " VARCHAR(10),"
                + KEY_REVIEW_STATUS + " VARCHAR(10),"
				+ KEY_MESSAGE_REVIEW_ID + " VARCHAR(50),"
                + KEY_RID + " VARCHAR(50),"
                + KEY_DOB + " VARCHAR(15)"
				+ " )";

		db.execSQL(CREATE_FAVOURITE_TABLE);
	}
	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
		onCreate(db);
	}

	public boolean insertMessageToDB(Message message) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(KEY_ID, message.getId());
        cv.put(KEY_SENDER_ID, message.getSender_id());
        cv.put(KEY_SENDER_DISPLAY_NAME, message.getSender_display_name());
        cv.put(KEY_RECIEVER_ID, message.getReciever_id());
        cv.put(KEY_RECIEVER_DISPLAY_NAME, message.getReciever_display_name());
        cv.put(KEY_TEXT, message.getText());
        cv.put(KEY_DATE, message.getDate());
        cv.put(KEY_STATUS, message.getStatus());
        cv.put(KEY_URL, message.getUrl());
        cv.put(KEY_TYPE, message.getType());
        cv.put(KEY_SENDER_TYPE, message.getSender_type());
        cv.put(KEY_RECIEVER_TYPE, message.getReciever_type());
        cv.put(KEY_REVIEW_STATUS, message.getReview_status());
		cv.put(KEY_MESSAGE_REVIEW_ID, message.getMessage_review_id());
		cv.put(KEY_RID, message.getRid());
		cv.put(KEY_DOB, message.getDob());
		db.insert(TABLE_MESSAGE, null, cv);

		db.close();
		return true;
	}

	public boolean deleteMessage(Message message) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_MESSAGE, KEY_ID + "=" + message.getId(), null);
		Log.e("DAtabaseHelper", "deleted from data base " + message.getId());
		db.close();
		return true;

	}


	@SuppressLint("UseSparseArrays")
	public HashMap<Integer , Integer> getMessages() {

		SQLiteDatabase db = this.getReadableDatabase();

		HashMap<Integer , Integer> myFavourites = new HashMap<Integer ,Integer>();
		Cursor cursor = db.query(TABLE_MESSAGE, null, null, null, null, null,
				null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			for (int i = 0; i < cursor.getCount(); i++) {
				myFavourites.put(cursor.getInt(1) , cursor.getInt(1));

				cursor.moveToNext();
			}
			db.close();
		}
		return myFavourites;

	}

	@SuppressLint("UseSparseArrays")
	public ArrayList<Message> getAllMessages(String id) {

		SQLiteDatabase db = this.getReadableDatabase();

		ArrayList<Message> myFavourites = new ArrayList<>();
		String WhereCluase = KEY_SENDER_ID+"="+id+" OR "+KEY_RECIEVER_ID+"="+id;
		Cursor cursor = db.query(TABLE_MESSAGE, null, WhereCluase, null, null, null,null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			for (int i = 0; i < cursor.getCount(); i++) {
				Message message = new Message();
				message.setId(cursor.getInt(0));
				message.setSender_id(cursor.getString(1));
				message.setSender_display_name(cursor.getString(2));
				message.setReciever_id(cursor.getString(3));
				message.setReciever_display_name(cursor.getString(4));
				message.setText(cursor.getString(5));
				message.setDate(cursor.getString(6));
				message.setStatus(Integer.parseInt(cursor.getString(7)));
				message.setUrl(cursor.getString(8));
				message.setType(cursor.getString(9));
				message.setSender_type(cursor.getString(10));
				message.setReciever_type(cursor.getString(11));
				message.setReview_status(cursor.getString(12));
				message.setMessage_review_id(cursor.getString(13));
				message.setDob(cursor.getString(14));
				myFavourites.add(i,message);
				cursor.moveToNext();
			}
			db.close();
		}
		return myFavourites;

	}
	public ArrayList<Message> GetChatList(int id)
	{
		ArrayList<Message> ChatList = new ArrayList<Message>();
        SQLiteDatabase db = this.getReadableDatabase();
        String WhereCluase = KEY_ID  + " in(select max("+KEY_ID+") from "+TABLE_MESSAGE+" WHERE "+KEY_SENDER_ID+"="+id+" OR "+KEY_RECIEVER_ID+"="+id+" group by "+KEY_RID +")";

        Cursor cursor = db.query(TABLE_MESSAGE, null, WhereCluase, null, null, null,KEY_DATE+" DESC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        for (int i = 0; i < cursor.getCount(); i++) {
            Message message = new Message();
			message.setId(cursor.getInt(0));
			message.setSender_id(cursor.getString(1));
			message.setSender_display_name(cursor.getString(2));
			message.setReciever_id(cursor.getString(3));
			message.setReciever_display_name(cursor.getString(4));
			message.setText(cursor.getString(5));
			message.setDate(cursor.getString(6));
			message.setStatus(Integer.parseInt(cursor.getString(7)));
			message.setUrl(cursor.getString(8));
			message.setType(cursor.getString(9));
			message.setSender_type(cursor.getString(10));
			message.setReciever_type(cursor.getString(11));
			message.setReview_status(cursor.getString(12));
			message.setMessage_review_id(cursor.getString(13));
			message.setDob(cursor.getString(14));

            ChatList.add(i,message);
            cursor.moveToNext();
        }
        cursor.close();
        Log.e("ChatListLength ",""+ChatList.size());
        return ChatList;
	}
	public ArrayList<Message> GetChatListforAdvisor(int id)
	{
		ArrayList<Message> ChatList = new ArrayList<Message>();
		SQLiteDatabase db = this.getReadableDatabase();
		String WhereCluase = KEY_ID  + " in(select max("+KEY_ID+") from "+TABLE_MESSAGE+" WHERE ("+KEY_SENDER_ID+"="+id+" OR "+KEY_RECIEVER_ID+"="+id+") AND sender_type = 'client'"  + " group by "+KEY_RID +")";
		Log.e("query",WhereCluase);
		Cursor cursor = db.query(TABLE_MESSAGE, null, WhereCluase, null, null, null,KEY_DATE+" DESC");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
		}
		for (int i = 0; i < cursor.getCount(); i++) {
			Message message = new Message();
			message.setId(cursor.getInt(0));
			message.setSender_id(cursor.getString(1));
			message.setSender_display_name(cursor.getString(2));
			message.setReciever_id(cursor.getString(3));
			message.setReciever_display_name(cursor.getString(4));
			message.setText(cursor.getString(5));
			message.setDate(cursor.getString(6));
			message.setStatus(Integer.parseInt(cursor.getString(7)));
			message.setUrl(cursor.getString(8));
			message.setType(cursor.getString(9));
			message.setSender_type(cursor.getString(10));
			message.setReciever_type(cursor.getString(11));
			message.setReview_status(cursor.getString(12));
			message.setMessage_review_id(cursor.getString(13));
			message.setDob(cursor.getString(15));
			Log.e("DOBDB",cursor.getString(15));
			ChatList.add(i,message);
			cursor.moveToNext();
		}
		cursor.close();
		Log.e("ChatListLength ",""+ChatList.size());
		return ChatList;
	}
	public void FlushDatabase()
	{
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+ TABLE_MESSAGE);
	}
	public void UpdateReview(int id, String rating)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(KEY_REVIEW_STATUS,rating);
		db.update(TABLE_MESSAGE, cv, "id="+id, null);


	}
	public void UpdateMessageStatus(int id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(KEY_STATUS,"1");
		db.update(TABLE_MESSAGE, cv, "id="+id, null);


	}
	public ArrayList<Message> GetOrderHistory(String id)
	{
		ArrayList<Message> ChatList = new ArrayList<Message>();
		SQLiteDatabase db = this.getReadableDatabase();
		String WhereCluase = "("+KEY_SENDER_ID+"="+id+" OR reciever_id = "+ id + ") AND type = 'video' ";
		Log.e("query",WhereCluase);
		Cursor cursor = db.query(TABLE_MESSAGE, null, WhereCluase, null, null, null,KEY_DATE);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
		}
		for (int i = 0; i < cursor.getCount(); i++) {
			Message message = new Message();
			message.setId(cursor.getInt(0));
			message.setSender_id(cursor.getString(1));
			message.setSender_display_name(cursor.getString(2));
			message.setReciever_id(cursor.getString(3));
			message.setReciever_display_name(cursor.getString(4));
			message.setText(cursor.getString(5));

			String[] strs = cursor.getString(6).split(" ");
			message.setDate(strs[0]);
			message.setStatus(Integer.parseInt(cursor.getString(7)));
			message.setUrl(cursor.getString(8));
			message.setType(cursor.getString(9));
			message.setSender_type(cursor.getString(10));
			message.setReciever_type(cursor.getString(11));
			message.setReview_status(cursor.getString(12));
			message.setMessage_review_id(cursor.getString(13));
			message.setDob(cursor.getString(15));
			//Log.e("DOBDB",cursor.getString(15));
			//Log.e("ChatListLength ",""+cursor.getString(6));
		//	String[] strs = cursor.getString(6).split(" ");
		//	orderHistoryItemObject.setOrderDate(strs[0]);
		//	orderHistoryItemObject.setAdvisorName(cursor.getString(4));
			//orderHistoryItemObject.setDeliverd("Pending");
			ChatList.add(i,message);
			cursor.moveToNext();
		}
		cursor.close();
		Log.e("ChatListLength ",""+ChatList.size());
		return ChatList;
	}
	public boolean isPending(String id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		String WhereCluase = KEY_SENDER_ID+"="+id+" AND status = 0";
		Log.e("query",WhereCluase);
		Cursor cursor = db.query(TABLE_MESSAGE, null, WhereCluase, null, null, null,null);
		if (cursor.getCount() > 0) {
			return  true;
		}
		else
		return false;
	}
	public String getLatestMessageId()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_MESSAGE, null, null, null, null, null,KEY_ID +" DESC", "1");
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			return  cursor.getInt(0) + " ";
		}
		return "0";
	}
   /* public String isChatExist(String sender_id, String reciever_id)
    {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] args={sender_id,reciever_id,reciever_id,sender_id};
       // db.q
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_MESSAGE+" WHERE ("+KEY_SENDER_ID+" = ? AND "+KEY_RECIEVER_ID+" = ?) OR ("+KEY_RECIEVER_ID+" = ? AND "+KEY_SENDER_ID+" = ?) LIMIT 1", args);
       // SELECT * FROM `mesage_template` WHERE (sender_id = $sender AND receiver_id = $receiver) OR (sender_id = $receiver AND receiver_id = $sender)
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        for (int i = 0; i < cursor.getCount(); i++) {
            Log.e("data check","mil gya hy");
            Log.e("data check",""+cursor.getString(5));
            cursor.moveToNext();
        }
        return "";
    }*/
}