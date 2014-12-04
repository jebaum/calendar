package cs130.androidyamlcal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
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
	public static final String EVENT_IS_CACHED = "is_cached";
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
				SESSION_ADDRESS + " VARCHAR(100), PRIMARY KEY(" +
					SESSION_ADDRESS +"))"
		);
		db.execSQL("CREATE TABLE " + EVENT + "(" +
				EVENT_TITLE + " VARCHAR(50), " +
				EVENT_LOCATION + " VARCHAR(200), " +
				EVENT_DESCRIPTION + " VARCHAR(500), " +
				EVENT_CATEGORY + " VARCHAR(50), " +
				EVENT_START_TIME + " INTEGER, " +
				EVENT_END_TIME + " INTEGER, " +
				EVENT_IS_CACHED + " BOOLEAN, PRIMARY KEY(" +
					EVENT_TITLE + "," +
					EVENT_LOCATION + "," +
					EVENT_DESCRIPTION + "," +
					EVENT_CATEGORY + "," +
					EVENT_START_TIME + "," +
					EVENT_END_TIME + "," +
					EVENT_IS_CACHED + "))"
		);
		ContentValues cv = new ContentValues();
		cv.put(SESSION_ADDRESS, "");
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
		cv.put(EVENT_TITLE, event.getTitle());
		cv.put(EVENT_LOCATION, event.getLocation());
		cv.put(EVENT_DESCRIPTION, event.getDescription());
		cv.put(EVENT_CATEGORY, event.getCategory());
		cv.put(EVENT_START_TIME, event.getStartTime().getTimeInMillis());
		cv.put(EVENT_END_TIME, event.getEndTime().getTimeInMillis());
		cv.put(EVENT_IS_CACHED, event.isCached());
		db.insert(EVENT, null, cv);
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

			Calendar startTime = Calendar.getInstance();
			startTime.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(EVENT_START_TIME)));
			event.setStartTime(startTime);

			Calendar endTime = Calendar.getInstance();
			endTime.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(EVENT_END_TIME)));
			event.setEndTime(endTime);

			event.setCached(cursor.getShort(cursor.getColumnIndex(EVENT_IS_CACHED)) == 1);
			events.add(event);
		}
		return events;
	}

	public int deleteEvents()
	{
		return getReadableDatabase().delete(EVENT, null, null);
	}

	public int updateSession(String address)
	{
		ContentValues cv = new ContentValues();
		cv.put(SESSION_ADDRESS, address);
		return getWritableDatabase().update(SESSION, cv, null, null);
	}

	public Session getSession()
	{
		Session session = new Session();
		Cursor cursor = getReadableDatabase().query(SESSION, null, null, null, null, null, null);
		cursor.moveToNext();
		session.setAddress(cursor.getString(cursor.getColumnIndex(SESSION_ADDRESS)));
		return session;
	}
}
