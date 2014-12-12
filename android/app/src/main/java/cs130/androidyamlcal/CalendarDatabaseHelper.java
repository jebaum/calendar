package cs130.androidyamlcal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
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
	private static final String EVENT_ID = "id";
	public static final String EVENT_TITLE = "title";
	public static final String EVENT_LOCATION = "location";
	public static final String EVENT_DESCRIPTION = "description";
	public static final String EVENT_CATEGORY = "category";
	public static final String EVENT_START_TIME = "startTime";
	public static final String EVENT_END_TIME = "endTime";
	public static final String EVENT_IS_CACHED = "isCached";
	public static final String EVENT_IS_DELETED = "isDeleted";
	private static final String SESSION = "Session";
	private static final String SESSION_ADDRESS = "address";

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
				EVENT_ID + " INTEGER PRIMARY KEY, " +
				EVENT_TITLE + " VARCHAR(50), " +
				EVENT_LOCATION + " VARCHAR(200), " +
				EVENT_DESCRIPTION + " VARCHAR(500), " +
				EVENT_CATEGORY + " VARCHAR(50), " +
				EVENT_START_TIME + " INTEGER, " +
				EVENT_END_TIME + " INTEGER, " +
				EVENT_IS_CACHED + " BOOLEAN, " +
				EVENT_IS_DELETED + " BOOLEAN, " +
				"UNIQUE (" +
					EVENT_TITLE + ")" +
//					EVENT_LOCATION + "," +
//					EVENT_DESCRIPTION + "," +
//					EVENT_CATEGORY + "," +
//					EVENT_START_TIME + "," +
//					EVENT_END_TIME + ")" +
				")"
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
		cv.put(EVENT_IS_DELETED, false);
		db.insert(EVENT, null, cv);
	}

	public void cachedUpdateEvent(Event event)
	{
		Event oldEvent = getEvent(event.getId());
		SQLiteDatabase db = getWritableDatabase();
		ContentValues oldValues = new ContentValues();
		oldValues.put(EVENT_ID, oldEvent.getId());
		oldValues.put(EVENT_TITLE, oldEvent.getTitle());
		oldValues.put(EVENT_LOCATION, oldEvent.getLocation());
		oldValues.put(EVENT_DESCRIPTION, oldEvent.getDescription());
		oldValues.put(EVENT_CATEGORY, oldEvent.getCategory());
		oldValues.put(EVENT_START_TIME, oldEvent.getStartTime().getTimeInMillis());
		oldValues.put(EVENT_END_TIME, oldEvent.getEndTime().getTimeInMillis());
		oldValues.put(EVENT_IS_CACHED, event.isCached());
		oldValues.put(EVENT_IS_DELETED, true);
		db.update(EVENT, oldValues, EVENT_ID + "=" + event.getId(), null);

		ContentValues newValues = new ContentValues();
		newValues.put(EVENT_TITLE, event.getTitle());
		newValues.put(EVENT_LOCATION, event.getLocation());
		newValues.put(EVENT_DESCRIPTION, event.getDescription());
		newValues.put(EVENT_CATEGORY, event.getCategory());
		newValues.put(EVENT_START_TIME, event.getStartTime().getTimeInMillis());
		newValues.put(EVENT_END_TIME, event.getEndTime().getTimeInMillis());
		newValues.put(EVENT_IS_CACHED, event.isCached());
		newValues.put(EVENT_IS_DELETED, false);
		db.insert(EVENT, null, newValues);
	}

	public int updateEvent(Event event)
	{
		ContentValues values = new ContentValues();
		values.put(EVENT_ID, event.getId());
		values.put(EVENT_TITLE, event.getTitle());
		values.put(EVENT_LOCATION, event.getLocation());
		values.put(EVENT_DESCRIPTION, event.getDescription());
		values.put(EVENT_CATEGORY, event.getCategory());
		values.put(EVENT_START_TIME, event.getStartTime().getTimeInMillis());
		values.put(EVENT_END_TIME, event.getEndTime().getTimeInMillis());
		values.put(EVENT_IS_CACHED, event.isCached());
		values.put(EVENT_IS_DELETED, false);
		return getWritableDatabase().update(EVENT, values, EVENT_ID + "=" + event.getId(), null);
	}

	public Event getEvent(int id)
	{
		Cursor cursor = getReadableDatabase().query(EVENT, null,
				EVENT_ID + "=" + id, null, null, null, null);
		return cursor.moveToFirst() ? packageEvent(cursor) : null;
	}

	public ArrayList<Event> getEvents()
	{
		return getEvents(null);
	}

	public ArrayList<Event> getCachedEvents()
	{
		return getEvents(EVENT_IS_CACHED);
	}

	public ArrayList<Event> getNonDeletedEvents()
	{
		return getEvents("NOT " + EVENT_IS_DELETED);
	}

	public ArrayList<Event> getEvents(String where)
	{
		ArrayList<Event> events = new ArrayList<Event>();
		Cursor cursor = getReadableDatabase().query(EVENT, null,
				where, null, null, null, null);

		while (cursor.moveToNext())
		{
			events.add(packageEvent(cursor));
		}
		return events;
	}

	public void deleteEvent(Event event)
	{
		Event oldEvent = getEvent(event.getId());
		ContentValues oldValues = new ContentValues();
		oldValues.put(EVENT_ID, oldEvent.getId());
		oldValues.put(EVENT_TITLE, oldEvent.getTitle());
		oldValues.put(EVENT_LOCATION, oldEvent.getLocation());
		oldValues.put(EVENT_DESCRIPTION, oldEvent.getDescription());
		oldValues.put(EVENT_CATEGORY, oldEvent.getCategory());
		oldValues.put(EVENT_START_TIME, oldEvent.getStartTime().getTimeInMillis());
		oldValues.put(EVENT_END_TIME, oldEvent.getEndTime().getTimeInMillis());
		oldValues.put(EVENT_IS_CACHED, true);
		oldValues.put(EVENT_IS_DELETED, true);
		SQLiteDatabase db = getWritableDatabase();
		db.update(EVENT, oldValues, EVENT_ID + "=" + event.getId(), null);
//		printEvents();
		Log.d(TAG, "deleted: " + oldEvent.getTitle() + ", " + oldEvent.getLocation() + ", " + oldEvent.getDescription() + ", " + oldEvent.getCategory() + ", " + oldEvent.getStartTime().getTimeInMillis() + ", " + oldEvent.getEndTime().getTimeInMillis());
	}

	public int deleteNonCachedEvents()
	{
		return getWritableDatabase().delete(EVENT, "NOT " + EVENT_IS_CACHED, null);
	}

	public int deleteEvents()
	{
		return getWritableDatabase().delete(EVENT, null, null);
	}

	public int removeDeletedEvents()
	{
		return getWritableDatabase().delete(EVENT, EVENT_IS_DELETED, null);
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

	private Event packageEvent(Cursor cursor)
	{
		Event event = new Event();
		event.setId(cursor.getInt(cursor.getColumnIndex(EVENT_ID)));
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
		return event;
	}

	public void printEvents()
	{
		Cursor cursor = getReadableDatabase().query(EVENT, null, null, null, null, null, null);
		DateFormat df = DateFormat.getDateTimeInstance();
		while (cursor.moveToNext())
		{
			Log.d(TAG, cursor.getInt(cursor.getColumnIndex(EVENT_ID)) + ", " +
				cursor.getString(cursor.getColumnIndex(EVENT_TITLE)) + ", " +
				cursor.getString(cursor.getColumnIndex(EVENT_LOCATION)) + ", " +
				cursor.getString(cursor.getColumnIndex(EVENT_DESCRIPTION)) + ", " +
				cursor.getString(cursor.getColumnIndex(EVENT_CATEGORY)) + ", " +
				df.format(new Date(cursor.getLong(cursor.getColumnIndex(EVENT_START_TIME)))) + ", " +
				df.format(new Date(cursor.getLong(cursor.getColumnIndex(EVENT_END_TIME)))) + ", " +
				cursor.getShort(cursor.getColumnIndex(EVENT_IS_CACHED)) + ", " +
				cursor.getShort(cursor.getColumnIndex(EVENT_IS_DELETED))
			);
		}
	}
}
