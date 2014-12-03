package cs130.androidyamlcal;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kevin on 12/3/14.
 */
public class MonthViewFragment extends Fragment implements EventView
{
	private static final String TAG = "MonthViewFragment";
	private CalendarView _calendarView;
	private EventAdapter _eventAdapter;
	private ArrayList<Event> _dayEvents = new ArrayList<Event>();
	private CalendarDatabaseHelper _calendarDatabaseHelper;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		_calendarDatabaseHelper = new CalendarDatabaseHelper(getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.activity_main, container, false);
		Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
		((MainActivity) getActivity()).setSupportActionBar(toolbar);

		ListView eventsList = (ListView) v.findViewById(R.id.events_list);
		_eventAdapter = new EventAdapter(getActivity(), _dayEvents);
		eventsList.setAdapter(_eventAdapter);

		_calendarView = (CalendarView) v.findViewById(R.id.calendar);
		_calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
		{
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
			{
				updateEvents(year, month, dayOfMonth);
			}
		});
		return v;
	}

	private void updateEvents(int year, int month, int dayOfMonth)
	{
		Log.d(TAG, "selected date: year: " + String.valueOf(year) + ", month: "
				+ String.valueOf(month) + ", dayOfMonth: " + String.valueOf(dayOfMonth));
		_dayEvents.clear();
		for (Event event : _calendarDatabaseHelper.getEvents())
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

	public void updateEvents()
	{
		Date selectedDate = new Date(_calendarView.getDate());
		updateEvents(selectedDate.getYear() + 1900, selectedDate.getMonth(),
				selectedDate.getDate());
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
				convertView = getActivity().getLayoutInflater().inflate(_layout, null);
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
