package cs130.androidyamlcal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Date;

/**
 * Created by kevin on 11/10/14.
 */
public class AddEventActivity extends ActionBarActivity
{
	private EditText _titleEditText;
	private EditText _locationEditText;
	private EditText _descriptionEditText;
	private EditText _categoryEditText;
	private DatePicker _startDatePicker;
	private TimePicker _startTimePicker;
	private DatePicker _endDatePicker;
	private TimePicker _endTimePicker;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_event);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		_titleEditText = (EditText) findViewById(R.id.title);
		_locationEditText = (EditText) findViewById(R.id.location);
		_descriptionEditText = (EditText) findViewById(R.id.description);
		_categoryEditText = (EditText) findViewById(R.id.category);
		_startDatePicker = (DatePicker) findViewById(R.id.start_date);
		_startTimePicker = (TimePicker) findViewById(R.id.start_time);
		_endDatePicker = (DatePicker) findViewById(R.id.end_date);
		_endTimePicker = (TimePicker) findViewById(R.id.end_time);
		Button addButton = (Button) findViewById(R.id.add);

		addButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Date start = new Date(_startDatePicker.getYear() - 1900, _startDatePicker.getMonth(),
						_startDatePicker.getDayOfMonth(), _startTimePicker.getCurrentHour(),
						_startTimePicker.getCurrentMinute(), 0);
				Date end = new Date(_endDatePicker.getYear() - 1900, _endDatePicker.getMonth(),
						_endDatePicker.getDayOfMonth(), _endTimePicker.getCurrentHour(),
						_endTimePicker.getCurrentMinute(), 0);
				Intent i = new Intent();
				i.putExtra(CalendarDatabaseHelper.EVENT_TITLE, _titleEditText.getText().toString());
				i.putExtra(CalendarDatabaseHelper.EVENT_LOCATION, _locationEditText.getText().toString());
				i.putExtra(CalendarDatabaseHelper.EVENT_DESCRIPTION, _descriptionEditText.getText().toString());
				i.putExtra(CalendarDatabaseHelper.EVENT_CATEGORY, _categoryEditText.getText().toString());
				i.putExtra(CalendarDatabaseHelper.EVENT_START_TIME, start.getTime());
				i.putExtra(CalendarDatabaseHelper.EVENT_END_TIME, end.getTime());
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}
}
