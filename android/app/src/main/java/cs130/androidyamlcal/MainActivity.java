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
import android.support.v7.widget.Toolbar;
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
import java.util.ArrayList;
import java.util.Calendar;


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
	private PostEventsTask _postEventsTask;
	private FragmentManager _fragmentManager;
	private boolean _isOffline;
	private Menu _menu;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		_isOffline = false;
		setContentView(R.layout.activity_fragment);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		_fragmentManager = getSupportFragmentManager();
		Fragment fragment = getActiveFragment();

		if (fragment == null)
		{
			fragment = new MonthViewFragment();
			_fragmentManager.beginTransaction()
					.add(R.id.fragment_container, fragment)
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
		_menu = menu;
		getMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.month_view).setChecked(true);
		menu.findItem(R.id.offline).setChecked(_isOffline);
		return ret;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Bundle args;
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
				if (_isOffline)
				{
					if (_calendarDatabaseHelper.getSession().getAddress().isEmpty())
					{
						createNewAddressDialog();
					}
					else
					{
						setOffline(false);
						_fetchEventsTask = new FetchEventsTask();
						_fetchEventsTask.execute();
						showFetchProgressDialog();
					}
				}
				else
				{
					setOffline(true);
				}
				return true;
			case R.id.day_view:
				Log.d(TAG, "day view selected");
				item.setChecked(true);
				WeekViewFragment dayViewFragment = new WeekViewFragment();
				args = new Bundle();
				args.putInt(WeekViewFragment.NUM_DAYS, 1);
				dayViewFragment.setArguments(args);
				swapFragment(dayViewFragment);
				return true;
			case R.id.week_view:
				Log.d(TAG, "week view selected");
				item.setChecked(true);
				WeekViewFragment weekViewFragment = new WeekViewFragment();
				args = new Bundle();
				args.putInt(WeekViewFragment.NUM_DAYS, 5);
				weekViewFragment.setArguments(args);
				swapFragment(weekViewFragment);
				return true;
			case R.id.month_view:
				Log.d(TAG, "month view selected");
				item.setChecked(true);
				swapFragment(new MonthViewFragment());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.d(TAG, "onActivityResult");
		if (requestCode == 1)
		{
			if(resultCode == RESULT_OK)
			{
				Event event = new Event();

				event.setId(data.getIntExtra(AddEventActivity.ID, -1));
				event.setTitle(data.getStringExtra(AddEventActivity.TITLE));
				event.setLocation(data.getStringExtra(AddEventActivity.LOCATION));
				event.setDescription(data.getStringExtra(AddEventActivity.DESCRIPTION));
				event.setCategory(data.getStringExtra(AddEventActivity.CATEGORY));

				Calendar startTime = Calendar.getInstance();
				startTime.setTimeInMillis(data.getLongExtra(AddEventActivity.START_TIME, 0));
				event.setStartTime(startTime);

				Calendar endTime = Calendar.getInstance();
				endTime.setTimeInMillis(data.getLongExtra(AddEventActivity.END_TIME, 0));
				event.setEndTime(endTime);

				event.setCached(true);

				DateFormat df = DateFormat.getDateTimeInstance();

				if (event.getId() == -1)
				{
					Log.d(TAG, "adding event");
					_calendarDatabaseHelper.addEvent(event);
				}
				else if (data.getBooleanExtra(AddEventActivity.DELETE, false))
				{
					Log.d(TAG, "removing event");
					_calendarDatabaseHelper.deleteEvent(event);
//					_calendarDatabaseHelper.deleteNonCachedEvents();
//					((EventView) getActiveFragment()).updateEvents(null);
				}
				else
				{
					Log.d(TAG, "updating event");
					_calendarDatabaseHelper.cachedUpdateEvent(event);
				}

				if (!_isOffline)
				{
					_fetchEventsTask = new FetchEventsTask();
					_fetchEventsTask.execute();
					showFetchProgressDialog();
				}
				else
				{
					((EventView) getActiveFragment()).updateEvents(event.getStartTime());
				}
			}
			if (resultCode == RESULT_CANCELED)
			{
				Log.d(TAG, "result_canceled");
				//Write your code if there's no result
			}
		}
	}

	private void setOffline(boolean isOffline)
	{
		_isOffline = isOffline;
		if (_menu != null)
		{
			_menu.findItem(R.id.offline).setChecked(isOffline);
		}
	}

	private void swapFragment(Fragment fragment)
	{
		_fragmentManager.beginTransaction()
				.replace(R.id.fragment_container, fragment)
				.commit();
	}

	private Fragment getActiveFragment()
	{
		return _fragmentManager.findFragmentById(R.id.fragment_container);
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
							if (_postEventsTask != null)
							{
								_postEventsTask.cancel(true);
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
					+ ", startTime: " + df.format(event.getStartTime().getTime())
					+ ", endTime: " + df.format(event.getEndTime().getTime())
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
						setOffline(false);
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
						setOffline(true);
						Log.d(TAG, "offline");
					}
				})
				.setTitle("Set new Address")
				.setMessage("Could not connect to server, set new address or work offline");
		builder.show();
	}

	private class PostEventsTask extends AsyncTask<Void,Void,Void>
	{
		private boolean _failed = false;
		private ArrayList<Event> _events;

		public PostEventsTask(ArrayList<Event> events)
		{
			Log.d(TAG, "post events task oncreate");
			_events = new ArrayList<>();
			_events.addAll(events);
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			Log.d(TAG, "sending events");
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
				for (Event event : _events)
				{
					g.writeStartObject();
					g.writeStringField(CalendarDatabaseHelper.EVENT_TITLE, event.getTitle());
					g.writeStringField(CalendarDatabaseHelper.EVENT_LOCATION, event.getLocation());
					g.writeStringField(CalendarDatabaseHelper.EVENT_DESCRIPTION,
							event.getDescription());
					g.writeStringField(CalendarDatabaseHelper.EVENT_CATEGORY, event.getCategory());
					g.writeNumberField(CalendarDatabaseHelper.EVENT_START_TIME,
							event.getStartTime().getTimeInMillis());
					g.writeNumberField(CalendarDatabaseHelper.EVENT_END_TIME,
							event.getEndTime().getTimeInMillis());
					g.writeEndObject();
				}
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
			else
			{
				for (Event event : _events)
				{
					event.setCached(false);
					_calendarDatabaseHelper.updateEvent(event);
				}
				_calendarDatabaseHelper.removeDeletedEvents();
				((EventView) getActiveFragment()).updateEvents(Calendar.getInstance());
			}
			_postProgressDialog.cancel();
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

			_calendarDatabaseHelper.deleteNonCachedEvents();
			Log.d(TAG, "cached events");
			printEvents();
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
				URL url = new URL(fullAddress + ":4567/date_start/0/date_end/1577836800");
				connection = (HttpURLConnection) url.openConnection();

				InputStream in = connection.getInputStream();

				JsonFactory jsonFactory = new JsonFactory();
				JsonParser jp = jsonFactory.createParser(in);
				Log.d(TAG, "received events");
				if (jp.nextToken() == JsonToken.START_ARRAY)
				{
					while (jp.nextToken() == JsonToken.START_OBJECT)
					{
						Event event = new Event();
						while (jp.nextToken() != JsonToken.END_OBJECT)
						{
							fieldName = jp.getCurrentName();
							jp.nextToken();
							switch (fieldName)
							{
								case CalendarDatabaseHelper.EVENT_TITLE:
									event.setTitle(jp.getText());
									break;
								case CalendarDatabaseHelper.EVENT_LOCATION:
									event.setLocation(jp.getText());
									break;
								case CalendarDatabaseHelper.EVENT_DESCRIPTION:
									event.setDescription(jp.getText());
									break;
								case CalendarDatabaseHelper.EVENT_CATEGORY:
									event.setCategory(jp.getText());
									break;
								case CalendarDatabaseHelper.EVENT_START_TIME:
									Calendar startTime = Calendar.getInstance();
									startTime.setTimeInMillis(jp.getLongValue());
									event.setStartTime(startTime);
									break;
								case CalendarDatabaseHelper.EVENT_END_TIME:
									Calendar endTime = Calendar.getInstance();
									endTime.setTimeInMillis(jp.getLongValue());
									event.setEndTime(endTime);
							}
//							if (fieldName.equals("title"))
//							{
//								event.setTitle(jp.getText());
//							}
//							else if (fieldName.equals("location"))
//							{
//								event.setLocation(jp.getText());
//							}
//							else if (fieldName.equals("description"))
//							{
//								event.setDescription(jp.getText());
//							}
//							else if (fieldName.equals("category"))
//							{
//								event.setCategory(jp.getText());
//							}
//							else if (fieldName.equals("startTime"))
//							{
//								Calendar startTime = Calendar.getInstance();
//								startTime.setTimeInMillis(jp.getLongValue());
//								event.setStartTime(startTime);
//							}
//							else if (fieldName.equals("endTime"))
//							{
//								Calendar endTime = Calendar.getInstance();
//								endTime.setTimeInMillis(jp.getLongValue());
//								event.setEndTime(endTime);
//							}
						}
						Log.d(TAG, "added: " + event.getTitle() + ", " + event.getLocation() + ", " + event.getDescription() + ", " + event.getCategory() + ", " + event.getStartTime().getTimeInMillis() + ", " + event.getEndTime().getTimeInMillis());

						event.setCached(false);
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
			else if (!_calendarDatabaseHelper.getCachedEvents().isEmpty())
			{
				Log.d(TAG, "after fetch");
				printEvents();
				_postEventsTask = new PostEventsTask(_calendarDatabaseHelper.getNonDeletedEvents());
				_postEventsTask.execute();
				showPostProgressDialog();
			}
			else
			{
				_calendarDatabaseHelper.removeDeletedEvents();
			}
			((EventView) getActiveFragment()).updateEvents(Calendar.getInstance());
			_fetchProgressDialog.cancel();
		}
	}
}
