package cs130.androidyamlcal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kevin on 11/9/14.
 */
public class CalendarDatabaseHelper extends SQLiteOpenHelper
{
	private static final String DB_NAME = "calendar";
	private static final int VERSION = 1;
	private static final String TAG = "CalendarDatabaseHelper";
	private static final String EVENT = "Event";
	public static final String EVENT_TITLE = "title";
	public static final String EVENT_LOCATION = "location";
	public static final String EVENT_DESCRIPTION = "description";
	public static final String EVENT_CATEGORY = "category";
	public static final String EVENT_START_TIME = "start_time";
	public static final String EVENT_END_TIME = "end_time";
	private static final String SESSION = "Session";
	private static final String SESSION_OFFLINE = "offline";
	private static final String SESSION_ADDRESS = "address";
	private static final String SESSION_ID = "id";

	public CalendarDatabaseHelper(Context context)
	{
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.d(TAG, "creating tables");
		db.execSQL("CREATE TABLE Session(" +
				SESSION_OFFLINE + " BOOLEAN, " +
				SESSION_ADDRESS + " VARCHAR(100), PRIMARY KEY(" +
					SESSION_OFFLINE + "," +
					SESSION_ADDRESS +"))"
		);
		db.execSQL("CREATE TABLE " + EVENT + "(" +
				EVENT_TITLE + " VARCHAR(50), " +
				EVENT_LOCATION + " VARCHAR(200), " +
				EVENT_DESCRIPTION + " VARCHAR(500), " +
				EVENT_CATEGORY + " VARCHAR(50), " +
				EVENT_START_TIME + " INTEGER, " +
				EVENT_END_TIME + " INTEGER, PRIMARY KEY(" +
					EVENT_TITLE + "," +
					EVENT_LOCATION + "," +
					EVENT_DESCRIPTION + "," +
					EVENT_CATEGORY + "," +
					EVENT_START_TIME + "," +
					EVENT_END_TIME + "))"
		);
		ContentValues cv = new ContentValues();
		cv.put(SESSION_OFFLINE, false);
		db.insert(SESSION, null, cv);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}

	public void addEvent(Event event)
	{
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("title", event.getTitle());
		cv.put("location", event.getLocation());
		cv.put("description", event.getDescription());
		cv.put("category", event.getCategory());
		cv.put("start_time", event.getStartTime().getTime());
		cv.put("end_time", event.getEndTime().getTime());
		db.insert("Event", null, cv);
	}

	public ArrayList<Event> getEvents(Date date)
	{
		ArrayList<Event> events = new ArrayList<Event>();
		Date today = new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
		Date tomorrow = new Date(date.getYear(), date.getMonth(), date.getDate() + 1, 0, 0, 0);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query("Event", null,
			EVENT_START_TIME + " >= " + today.getTime() + " AND " + EVENT_START_TIME + " < " + tomorrow.getTime(),
			null, null, null, null);

		while (cursor.moveToNext())
		{
			Event event = new Event();
			event.setTitle(cursor.getString(cursor.getColumnIndex(EVENT_TITLE)));
			event.setLocation(cursor.getString(cursor.getColumnIndex(EVENT_LOCATION)));
			event.setDescription(cursor.getString(cursor.getColumnIndex(EVENT_DESCRIPTION)));
			event.setCategory(cursor.getString(cursor.getColumnIndex(EVENT_CATEGORY)));
			event.setStartTime(new Date(cursor.getInt(cursor.getColumnIndex(EVENT_START_TIME))));
			event.setEndTime(new Date(cursor.getInt(cursor.getColumnIndex(EVENT_END_TIME))));
			events.add(event);
		}
		return events;
	}

	public ArrayList<Event> getEvents()
	{
		ArrayList<Event> events = new ArrayList<Event>();
		Cursor cursor = getReadableDatabase().query(EVENT, null, null, null, null, null, null);

		while (cursor.moveToNext())
		{
			Event event = new Event();
			event.setTitle(cursor.getString(cursor.getColumnIndex(EVENT_TITLE)));
			event.setLocation(cursor.getString(cursor.getColumnIndex(EVENT_LOCATION)));
			event.setDescription(cursor.getString(cursor.getColumnIndex(EVENT_DESCRIPTION)));
			event.setCategory(cursor.getString(cursor.getColumnIndex(EVENT_CATEGORY)));
			Log.d(TAG, "db start_time:" + String.valueOf(cursor.getLong(cursor.getColumnIndex
					(EVENT_START_TIME))));
			event.setStartTime(new Date(cursor.getLong(cursor.getColumnIndex(EVENT_START_TIME))));
			event.setEndTime(new Date(cursor.getLong(cursor.getColumnIndex(EVENT_END_TIME))));
			events.add(event);
		}
		return events;
	}

	public int deleteEvents()
	{
		return getReadableDatabase().delete(EVENT, null, null);
	}

	public int updateSession(boolean offline, String address)
	{
		ContentValues cv = new ContentValues();
		cv.put(SESSION_OFFLINE, offline);
		cv.put(SESSION_ADDRESS, address);
		return getWritableDatabase().update(SESSION, cv, null, null);
	}

	public Session getSession()
	{
		Session session = new Session();
		Cursor cursor = getReadableDatabase().query(SESSION, null, null, null, null, null, null);
		cursor.moveToNext();
		session.setOffline(cursor.getShort(cursor.getColumnIndex(SESSION_OFFLINE)) != 0);
		session.setAddress(cursor.getString(cursor.getColumnIndex(SESSION_ADDRESS)));
		return session;
	}
}
