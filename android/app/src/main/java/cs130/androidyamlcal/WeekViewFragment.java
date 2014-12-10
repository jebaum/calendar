package cs130.androidyamlcal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
* Created by kevin on 12/3/14.
*/
public class WeekViewFragment extends Fragment implements WeekView.MonthChangeListener, EventView
{
	private static final String TAG = "WeekViewFragment";
	private WeekView _weekView;
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
		View v = inflater.inflate(R.layout.fragment_week_view, container, false);
		_weekView = (WeekView) v.findViewById(R.id.week_view);
		_weekView.setMonthChangeListener(this);
		return v;
	}

	@Override
	public List<WeekViewEvent> onMonthChange(int year, int month)
	{
		List<WeekViewEvent> events = new ArrayList<>();
		Log.d(TAG, "year: " + year + ", month: " + month);
//		Calendar startTime = Calendar.getInstance();
//		startTime.set(Calendar.HOUR_OF_DAY, 3);
//		startTime.set(Calendar.MINUTE, 0);
//		startTime.set(Calendar.MONTH, month-1);
//		startTime.set(Calendar.YEAR, year);
//		Calendar endTime = (Calendar) startTime.clone();
//		endTime.add(Calendar.HOUR, 1);
//		endTime.set(Calendar.MONTH, month-1);
//		WeekViewEvent testEvent = new WeekViewEvent(1, "Test event", startTime, endTime);
//		testEvent.setColor(getResources().getColor(R.color.event_color_01));
//		events.add(testEvent);

		for (Event event : _calendarDatabaseHelper.getEvents())
		{
			if ((event.getStartTime().get(Calendar.YEAR) == year
					&& event.getStartTime().get(Calendar.MONTH) + 1 == month)
				|| (event.getEndTime().get(Calendar.YEAR) == year
					&& event.getEndTime().get(Calendar.MONTH) + 1 == month))
			{
				Log.d(TAG, event.getTitle() + ": " + String.valueOf(event.getStartTime().get
						(Calendar.MONTH)));
				WeekViewEvent weekViewEvent = new WeekViewEvent(1, event.getTitle(), event.getStartTime(), event.getEndTime());
				weekViewEvent.setColor(getResources().getColor(R.color.event_color_02));
				events.add(weekViewEvent);
			}
		}
		return events;
	}

	@Override
	public void updateEvents()
	{
		_weekView.goToToday();
	}
}
