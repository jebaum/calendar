package cs130.androidyamlcal;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends ActionBarActivity
{

	private static final String TAG = "MainActivity";
	private ArrayList<Event> _events = new ArrayList<Event>();
	private ArrayList<Event> _dayEvents = new ArrayList<Event>();
	private EventAdapter _eventAdapter;
	private CalendarView _calendarView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		_eventAdapter = new EventAdapter(MainActivity.this, _dayEvents);
		setContentView(R.layout.activity_main);
		ListView eventsList = (ListView) findViewById(R.id.events_list);
		eventsList.setAdapter(_eventAdapter);

		_calendarView = (CalendarView) findViewById(R.id.calendar);
		_calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
		{
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
			{
				updateEvents(year, month, dayOfMonth);
			}
		});

		new FetchEventsTask().execute();
	}

	private void printEvents()
	{
		DateFormat df = DateFormat.getDateTimeInstance();
		for (Event event : _events)
		{
			Log.d(TAG, "title: " + event.getTitle()
					+ ", location: " + event.getLocation()
					+ ", description: " + event.getDescription()
					+ ", category: " + event.getCategory()
					+ ", startTime: " + df.format(event.getStartTime())
					+ ", endTime: " + df.format(event.getEndTime()));
		}
	}

	private void updateEvents(int year, int month, int dayOfMonth)
	{
		Log.d(TAG, "selected date: year: " + String.valueOf(year) + ", month: "
				+ String.valueOf(month) + ", dayOfMonth: " + String.valueOf(dayOfMonth));
		_dayEvents.clear();
		for (Event event : _events)
		{
			Date startTime = event.getStartTime();
			Log.d(TAG, "year: " + startTime.getYear()
					+ ", month: " + startTime.getMonth()
					+ ", dayOfMonth: " + startTime.getDate());
			if (startTime.getDate() == dayOfMonth &&
					startTime.getMonth() == month &&
					startTime.getYear() + 1900 == year)
			{
				_dayEvents.add(event);
			}
		}
		_eventAdapter.notifyDataSetChanged();
	}

	private void updateEvents()
	{
		Date selectedDate = new Date(_calendarView.getDate());
		updateEvents(selectedDate.getYear() + 1900, selectedDate.getMonth(),
				selectedDate.getDate());
	}

	private class FetchEventsTask extends AsyncTask<Void,Void,Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{
			HttpURLConnection connection;
			String fieldName;
			try
			{
				// currently using static value for start and end dates and ip
				URL url = new URL("http://131.179.39.181:4567/date_start/123/date_end/234");
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
						_events.add(event);
					}
				}
			}
			catch (IOException ioe)
			{
				Log.e(TAG, "error", ioe);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args)
		{
			printEvents();
			updateEvents();
		}
	}

	private class EventAdapter extends ArrayAdapter<Event>
	{
		private int _layout;

		public EventAdapter(Context context, ArrayList<Event> events)
		{
			super(context, R.layout.event, events);
			_layout = R.layout.event;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = getLayoutInflater().inflate(_layout, null);
			}
			Event event = _dayEvents.get(position);
			DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

			TextView titleView = (TextView) convertView.findViewById(R.id.title);
			TextView locationView = (TextView) convertView.findViewById(R.id.location);
			TextView startTimeView = (TextView) convertView.findViewById(R.id.start_time);
			TextView endTimeView = (TextView) convertView.findViewById(R.id.end_time);

			titleView.setText(event.getTitle());
			locationView.setText(event.getLocation());
			startTimeView.setText(timeFormat.format(event.getStartTime()));
			endTimeView.setText(timeFormat.format(event.getEndTime()));

			return convertView;
		}
	}
}
