package cs130.androidyamlcal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity
{
	private static final String TAG = "MainActivity";
//	private ArrayList<Event> _dayEvents = new ArrayList<Event>();
//	private EventAdapter _eventAdapter;
//	private CalendarView _calendarView;
	private View _newAddressDialogView;
//	private View _addEventDialogView;
	private Session _session;
	private FetchEventsTask _fetchEventsTask;
	private ProgressDialog _fetchProgressDialog;
	private ProgressDialog _postProgressDialog;
	private CalendarDatabaseHelper _calendarDatabaseHelper;
	private PostEventTask _postEventTask;
	private FragmentManager _fragmentManager;
	private boolean _isOffline;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		_isOffline = false;
		setContentView(R.layout.activity_fragment);
		_fragmentManager = getSupportFragmentManager();
		Fragment fragment = getActiveFragment();

		if (fragment == null)
		{
			fragment = new MonthViewFragment();
			_fragmentManager.beginTransaction()
					.add(android.R.id.content, fragment)
					.commit();
		}

		_calendarDatabaseHelper = new CalendarDatabaseHelper(getApplicationContext());
		_session = _calendarDatabaseHelper.getSession();

		if (!_isOffline)
		{
			if (_session.getAddress().isEmpty())
			{
				createNewAddressDialog();
			}
			else
			{
				_fetchEventsTask = new FetchEventsTask();
				_fetchEventsTask.execute();
				showFetchProgressDialog();
			}
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
	}

	@Override
	public void onDestroy()
	{
		if (_fetchProgressDialog != null)
		{
			_fetchProgressDialog.cancel();
		}
		if (_postProgressDialog != null)
		{
			_postProgressDialog.cancel();
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		boolean ret = super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return ret;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.add_event:
				Log.d(TAG, "add event!!!!!");
//				createAddEventDialog();
				Intent i = new Intent(MainActivity.this, AddEventActivity.class);
				startActivityForResult(i, 1);
				return true;
			case R.id.offline:
				Log.d(TAG, "toggle offline");
				_isOffline = !_isOffline;
				item.setChecked(_isOffline);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 1)
		{
			if(resultCode == RESULT_OK)
			{
				Event event = new Event();
				event.setTitle(data.getStringExtra(CalendarDatabaseHelper.EVENT_TITLE));
				event.setLocation(data.getStringExtra(CalendarDatabaseHelper.EVENT_LOCATION));
				event.setDescription(data.getStringExtra(CalendarDatabaseHelper.EVENT_DESCRIPTION));
				event.setCategory(data.getStringExtra(CalendarDatabaseHelper.EVENT_CATEGORY));
				event.setStartTime(new Date(data.getLongExtra(CalendarDatabaseHelper
						.EVENT_START_TIME, 0)));
				event.setEndTime(new Date(data.getLongExtra(CalendarDatabaseHelper.EVENT_END_TIME,
						0)));
				event.setCached(_isOffline);

				DateFormat df = DateFormat.getDateTimeInstance();
				Log.d(TAG, "new event start time: " + df.format(event.getStartTime()));

				if (!_isOffline)
				{
					_postEventTask = new PostEventTask(event);
					_postEventTask.execute();
					showPostProgressDialog();
				}
				else
				{
					_calendarDatabaseHelper.addEvent(event);
					((EventView) getActiveFragment()).updateEvents();
				}
			}
			if (resultCode == RESULT_CANCELED)
			{
				Log.d(TAG, "result_canceled");
				//Write your code if there's no result
			}
		}
	}

	private Fragment getActiveFragment()
	{
		return _fragmentManager.findFragmentById(android.R.id.content);
	}

	private void showPostProgressDialog()
	{
		if (_postProgressDialog == null)
		{
			_postProgressDialog = ProgressDialog.show(
					MainActivity.this,
					null,
					"Sending Event",
					true,
					true,
					new DialogInterface.OnCancelListener()
					{
						@Override
						public void onCancel(DialogInterface dialog)
						{
							if (_postEventTask != null)
							{
								_postEventTask.cancel(true);
							}
						}
					}
			);
		}
		else
		{
			_postProgressDialog.show();
		}
	}

	private void showFetchProgressDialog()
	{
		if (_fetchProgressDialog == null)
		{
			_fetchProgressDialog = ProgressDialog.show(
					MainActivity.this,
					null,
					"Loading Events",
					true,
					true,
					new DialogInterface.OnCancelListener()
					{
						@Override
						public void onCancel(DialogInterface dialog)
						{
							Log.d(TAG, "canceled");
							if (_fetchEventsTask != null)
							{
								_fetchEventsTask.cancel(true);
							}
						}
					}
			);
		}
		else
		{
			_fetchProgressDialog.show();
		}
	}

	private void updateSession(String address)
	{
		_session.setAddress(address);
		_calendarDatabaseHelper.updateSession(address);
	}

	private void printEvents()
	{
		DateFormat df = DateFormat.getDateTimeInstance();
		for (Event event : _calendarDatabaseHelper.getEvents())
		{
			Log.d(TAG, "title: " + event.getTitle()
					+ ", location: " + event.getLocation()
					+ ", description: " + event.getDescription()
					+ ", category: " + event.getCategory()
					+ ", startTime: " + df.format(event.getStartTime())
					+ ", endTime: " + df.format(event.getEndTime())
					+ ", isCached: " + event.isCached());
		}
	}

	private void createNewAddressDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		// Get the layout inflater
		LayoutInflater inflater = getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		_newAddressDialogView = inflater.inflate(R.layout.dialog_new_address, null);
		builder.setView(_newAddressDialogView)
				// Add action buttons
				.setPositiveButton("Set", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						EditText addressEditText = (EditText) _newAddressDialogView.findViewById(R.id.address);
						updateSession(addressEditText.getText().toString());
						_isOffline = false;
						_fetchEventsTask = new FetchEventsTask();
						_fetchEventsTask.execute();
						showFetchProgressDialog();
						Log.d(TAG, "address: " + addressEditText.getText().toString());
					}
				})
				.setNegativeButton("Work Offline", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						_isOffline = true;
						Log.d(TAG, "offline");
					}
				})
				.setTitle("Set new Address")
				.setMessage("Could not connect to server, set new address or work offline");
		builder.show();
	}

	private class PostEventTask extends AsyncTask<Void,Void,Void>
	{
		private boolean _failed = false;
		private Event _event;

		public PostEventTask(Event event)
		{
			_event = event;
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			HttpURLConnection connection;
			String fullAddress;
			try
			{
				// currently using static value for start and end dates and ip
				if (_session.getAddress().matches("[a-z]+://.*"))
				{
					fullAddress = _session.getAddress();
				}
				else
				{
					fullAddress = "http://" + _session.getAddress();
				}
				URL url = new URL(fullAddress + ":4567");
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);

				OutputStream out = connection.getOutputStream();

				JsonFactory jsonFactory = new JsonFactory();
				JsonGenerator g = jsonFactory.createGenerator(out);

				g.writeStartArray();
				g.writeStartObject();
				g.writeStringField(CalendarDatabaseHelper.EVENT_TITLE, _event.getTitle());
				g.writeStringField(CalendarDatabaseHelper.EVENT_LOCATION, _event.getLocation());
				g.writeStringField(CalendarDatabaseHelper.EVENT_DESCRIPTION,
						_event.getDescription());
				g.writeStringField(CalendarDatabaseHelper.EVENT_CATEGORY, _event.getCategory());
				g.writeNumberField(CalendarDatabaseHelper.EVENT_START_TIME,
						_event.getStartTime().getTime());
				g.writeNumberField(CalendarDatabaseHelper.EVENT_END_TIME, _event.getEndTime().getTime());
				g.writeEndObject();
				g.writeEndArray();
				g.close();

				InputStream in = connection.getInputStream();

				String response = "";
				byte[] buffer = new byte[1024];

				while(in.read(buffer) > 0)
				{
					response += new String(buffer);
				}
				Log.d(TAG, response);
				in.close();
			}
			catch (IOException ioe)
			{
				Log.e(TAG, "error", ioe);
				_failed = true;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args)
		{
			if (_failed)
			{
				createNewAddressDialog();
			}
//			_calendarDatabaseHelper.addEvent(_event);
//			printEvents();
//			((EventView) getActiveFragment()).updateEvents();
			_postProgressDialog.cancel();
			_fetchEventsTask = new FetchEventsTask();
			_fetchEventsTask.execute();
		}
	}

	private class FetchEventsTask extends AsyncTask<Void,Void,Void>
	{
		boolean _failed = false;

		@Override
		protected Void doInBackground(Void... params)
		{
			HttpURLConnection connection;
			String fieldName;
			String fullAddress;
			_calendarDatabaseHelper.deleteEvents();
			try
			{
				// currently using static value for start and end dates and ip
				if (_session.getAddress().matches("[a-z]+://.*"))
				{
					fullAddress = _session.getAddress();
				}
				else
				{
					fullAddress = "http://" + _session.getAddress();
				}
				URL url = new URL(fullAddress + ":4567/date_start/123/date_end/234");
				connection = (HttpURLConnection) url.openConnection();

				InputStream in = connection.getInputStream();

				JsonFactory jsonFactory = new JsonFactory();
				JsonParser jp = jsonFactory.createParser(in);
				if (jp.nextToken() == JsonToken.START_ARRAY)
				{
					while (jp.nextToken() == JsonToken.START_OBJECT)
					{
						Event event = new Event();
						while (jp.nextToken() != JsonToken.END_OBJECT)
						{
							fieldName = jp.getCurrentName();
							Log.d(TAG, jp.nextToken().toString());
							if (fieldName.equals("title"))
							{
								event.setTitle(jp.getText());
							}
							else if (fieldName.equals("location"))
							{
								event.setLocation(jp.getText());
							}
							else if (fieldName.equals("description"))
							{
								event.setDescription(jp.getText());
							}
							else if (fieldName.equals("category"))
							{
								event.setCategory(jp.getText());
							}
							else if (fieldName.equals("startTime"))
							{
								event.setStartTime(new Date(jp.getLongValue()));
							}
							else if (fieldName.equals("endTime"))
							{
								event.setEndTime(new Date(jp.getLongValue()));
							}
						}
						_calendarDatabaseHelper.addEvent(event);
					}
				}
			}
			catch (IOException ioe)
			{
				Log.e(TAG, "error", ioe);
				_failed = true;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args)
		{
			if (_failed)
			{
				createNewAddressDialog();
			}
			printEvents();
			((EventView) getActiveFragment()).updateEvents();
			_fetchProgressDialog.cancel();
		}
	}
}
