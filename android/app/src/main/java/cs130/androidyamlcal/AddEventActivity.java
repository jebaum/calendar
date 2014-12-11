package cs130.androidyamlcal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by kevin on 11/10/14.
 */
public class AddEventActivity extends ActionBarActivity
{
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String LOCATION = "location";
	public static final String DESCRIPTION = "description";
	public static final String CATEGORY = "category";
	public static final String START_TIME = "start_time";
	public static final String END_TIME = "end_time";
	private static final String TAG = "AddEventActivity";
	private EditText _titleEditText;
	private EditText _locationEditText;
	private EditText _descriptionEditText;
	private EditText _categoryEditText;
	private DatePicker _startDatePicker;
	private TimePicker _startTimePicker;
	private DatePicker _endDatePicker;
	private TimePicker _endTimePicker;
	private int _id;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_event);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		_titleEditText = (EditText) findViewById(R.id.title_edit);
		_locationEditText = (EditText) findViewById(R.id.location_edit);
		_descriptionEditText = (EditText) findViewById(R.id.description_edit);
		_categoryEditText = (EditText) findViewById(R.id.category_edit);
		_startDatePicker = (DatePicker) findViewById(R.id.start_date_picker);
		_startTimePicker = (TimePicker) findViewById(R.id.start_time_picker);
		_endDatePicker = (DatePicker) findViewById(R.id.end_date_picker);
		_endTimePicker = (TimePicker) findViewById(R.id.end_time_picker);
		Button addButton = (Button) findViewById(R.id.add);

		Bundle extras;
		if ((extras = getIntent().getExtras()) != null)
		{
			setTitle(R.string.edit_event_title);
			_id = extras.getInt(ID);
			_titleEditText.setText(extras.getString(TITLE));
			_locationEditText.setText(extras.getString(LOCATION));
			_descriptionEditText.setText(extras.getString(DESCRIPTION));
			_categoryEditText.setText(extras.getString(CATEGORY));

			Calendar startTime = Calendar.getInstance();
			startTime.setTimeInMillis(extras.getLong(START_TIME));
			_startDatePicker.updateDate(startTime.get(Calendar.YEAR),
					startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH));
			_startTimePicker.setCurrentHour(startTime.get(Calendar.HOUR_OF_DAY));
			_startTimePicker.setCurrentMinute(startTime.get(Calendar.MINUTE));

			Calendar endTime = Calendar.getInstance();
			endTime.setTimeInMillis(extras.getLong(END_TIME));
			_endDatePicker.updateDate(endTime.get(Calendar.YEAR),
					endTime.get(Calendar.MONTH), endTime.get(Calendar.DAY_OF_MONTH));
			_endTimePicker.setCurrentHour(endTime.get(Calendar.HOUR_OF_DAY));
			_endTimePicker.setCurrentMinute(endTime.get(Calendar.MINUTE));
		}
		else
		{
			_id = -1;
		}

		addButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Calendar start = new GregorianCalendar(_startDatePicker.getYear(),
						_startDatePicker.getMonth(), _startDatePicker.getDayOfMonth(),
						_startTimePicker.getCurrentHour(), _startTimePicker.getCurrentMinute());
				Calendar end = new GregorianCalendar(_endDatePicker.getYear(),
						_endDatePicker.getMonth(), _endDatePicker.getDayOfMonth(),
						_endTimePicker.getCurrentHour(), _endTimePicker.getCurrentMinute());
				Intent i = new Intent();

				i.putExtra(ID, _id);
				i.putExtra(TITLE, _titleEditText.getText().toString());
				i.putExtra(LOCATION, _locationEditText.getText().toString());
				i.putExtra(DESCRIPTION, _descriptionEditText.getText().toString());
				i.putExtra(CATEGORY, _categoryEditText.getText().toString());
				i.putExtra(START_TIME, start.getTimeInMillis());
				i.putExtra(END_TIME, end.getTimeInMillis());
				finish();
				setResult(RESULT_OK, i);
			}
		});
	}
}
