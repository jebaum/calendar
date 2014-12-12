package cs130.androidyamlcal;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
* Created by kevin on 12/3/14.
*/
public class WeekViewFragment extends Fragment
		implements WeekView.MonthChangeListener, EventView, WeekView.EventClickListener
{
	public static final String NUM_DAYS = "numDays";
	private static final String TAG = "WeekViewFragment";
	private WeekView _weekView;
	private CalendarDatabaseHelper _calendarDatabaseHelper;
	private int _numVisibleDays;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		_calendarDatabaseHelper = new CalendarDatabaseHelper(getActivity().getApplicationContext());
		_numVisibleDays = getArguments().getInt(NUM_DAYS, 1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_week_view, container, false);
		_weekView = (WeekView) v.findViewById(R.id.week_view);
		_weekView.setMonthChangeListener(this);
		_weekView.setOnEventClickListener(this);
		_weekView.setNumberOfVisibleDays(_numVisibleDays);
		_weekView.setHourHeight(80);
		return v;
	}

	@Override
	public List<WeekViewEvent> onMonthChange(int year, int month)
	{
		List<WeekViewEvent> events = new ArrayList<>();
		Log.d(TAG, "year: " + year + ", month: " + month);

		for (Event event : _calendarDatabaseHelper.getNonDeletedEvents())
		{
			if ((event.getStartTime().get(Calendar.YEAR) == year
					&& event.getStartTime().get(Calendar.MONTH) + 1 == month)
				|| (event.getEndTime().get(Calendar.YEAR) == year
					&& event.getEndTime().get(Calendar.MONTH) + 1 == month))
			{
				Log.d(TAG, event.getTitle() + ": " + String.valueOf(event.getStartTime().get
						(Calendar.MONTH)));
				WeekViewEvent weekViewEvent = new WeekViewEvent(event.getId(),
						event.getTitle(), event.getStartTime(), event.getEndTime());
				weekViewEvent.setColor(getResources().getColor(R.color.event_color_02));
				events.add(weekViewEvent);
			}
		}
		return events;
	}

	@Override
	public void onEventClick(WeekViewEvent selectedEvent, RectF eventRect)
	{
		Intent i = new Intent(getActivity(), AddEventActivity.class);
		Event event = _calendarDatabaseHelper.getEvent((int) selectedEvent.getId());
		i.putExtra(AddEventActivity.ID, event.getId());
		i.putExtra(AddEventActivity.TITLE, event.getTitle());
		i.putExtra(AddEventActivity.LOCATION, event.getLocation());
		i.putExtra(AddEventActivity.DESCRIPTION, event.getDescription());
		i.putExtra(AddEventActivity.CATEGORY, event.getCategory());
		i.putExtra(AddEventActivity.START_TIME, event.getStartTime().getTimeInMillis());
		i.putExtra(AddEventActivity.END_TIME, event.getEndTime().getTimeInMillis());
		getActivity().startActivityForResult(i, 1);
	}

	@Override
	public void updateEvents(Calendar eventTime)
	{
		_weekView.goToDate(eventTime);
	}
}
