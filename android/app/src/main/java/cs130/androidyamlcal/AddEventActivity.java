package cs130.androidyamlcal;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
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
	public static final String DELETE = "delete";
	private static final String TAG = "AddEventActivity";
	private EditText _titleEditText;
	private EditText _locationEditText;
	private EditText _descriptionEditText;
	private EditText _categoryEditText;
	private DatePickerDialog _startDatePickerDialog;
	private TimePickerDialog _startTimePickerDialog;
	private DatePickerDialog _endDatePickerDialog;
	private TimePickerDialog _endTimePickerDialog;
	private Button _startDateButton;
	private Button _startTimeButton;
	private Button _endDateButton;
	private Button _endTimeButton;
	private Calendar _startTime;
	private Calendar _endTime;
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

		_startDateButton = (Button) findViewById(R.id.start_date_button);
		_startTimeButton = (Button) findViewById(R.id.start_time_button);
		_endDateButton = (Button) findViewById(R.id.end_date_button);
		_endTimeButton = (Button) findViewById(R.id.end_time_button);

		_startTime = Calendar.getInstance();
		_endTime = Calendar.getInstance();

		Bundle extras;
		if ((extras = getIntent().getExtras()) != null)
		{
			setTitle(R.string.edit_event_title);
			_id = extras.getInt(ID);
			_titleEditText.setText(extras.getString(TITLE));
			_locationEditText.setText(extras.getString(LOCATION));
			_descriptionEditText.setText(extras.getString(DESCRIPTION));
			_categoryEditText.setText(extras.getString(CATEGORY));
			_startTime.setTimeInMillis(extras.getLong(START_TIME));
			_endTime.setTimeInMillis(extras.getLong(END_TIME));
		}
		else
		{
			_id = -1;
		}

		_startDateButton.setText(DateFormat.getDateInstance().format(_startTime.getTime()));
		_startTimeButton.setText(DateFormat.getTimeInstance().format(_startTime.getTime()));
		_endDateButton.setText(DateFormat.getDateInstance().format(_endTime.getTime()));
		_endTimeButton.setText(DateFormat.getTimeInstance().format(_endTime.getTime()));

		_startDateButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				_startDatePickerDialog = new DatePickerDialog(AddEventActivity.this,
						new DatePickerDialog.OnDateSetListener()
				{
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
					                      int dayOfMonth)
					{
						_startTime.set(Calendar.YEAR, year);
						_startTime.set(Calendar.MONTH, monthOfYear);
						_startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						_startDateButton.setText(DateFormat.getDateInstance().format(_startTime.getTime()));
					}
				}, _startTime.get(Calendar.YEAR), _startTime.get(Calendar.MONTH),
						_startTime.get(Calendar.DAY_OF_MONTH));
				_startDatePickerDialog.show();
			}
		});

		_startTimeButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				_startTimePickerDialog = new TimePickerDialog(AddEventActivity.this,
						new TimePickerDialog.OnTimeSetListener()
				{
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute)
					{
						_startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
						_startTime.set(Calendar.MINUTE, minute);
						_startTimeButton.setText(DateFormat.getTimeInstance().format(_startTime.getTime()));
					}
				}, _startTime.get(Calendar.HOUR_OF_DAY), _startTime.get(Calendar.MINUTE), false);
				_startTimePickerDialog.show();
			}
		});

		_endDateButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				_endDatePickerDialog = new DatePickerDialog(AddEventActivity.this,
						new DatePickerDialog.OnDateSetListener()
				{
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
					{
						_endTime.set(Calendar.YEAR, year);
						_endTime.set(Calendar.MONTH, monthOfYear);
						_endTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						_endDateButton.setText(DateFormat.getDateInstance().format(_endTime.getTime()));
					}
				}, _endTime.get(Calendar.YEAR), _endTime.get(Calendar.MONTH),
						_endTime.get(Calendar.DAY_OF_MONTH));
				_endDatePickerDialog.show();
			}
		});

		_endTimeButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				_endTimePickerDialog = new TimePickerDialog(AddEventActivity.this,
						new TimePickerDialog.OnTimeSetListener()
						{
							@Override
							public void onTimeSet(TimePicker view, int hourOfDay, int minute)
							{
								_endTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
								_endTime.set(Calendar.MINUTE, minute);
								_endTimeButton.setText(DateFormat.getTimeInstance().format
										(_endTime.getTime()));
							}
						}, _endTime.get(Calendar.HOUR_OF_DAY), _endTime.get(Calendar.MINUTE),
						false);
				_endTimePickerDialog.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		boolean ret = super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.add_event, menu);
		if (_id == -1)
		{
			menu.findItem(R.id.remove).setVisible(false);
		}
		return ret;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent i;
		switch (item.getItemId())
		{
			case R.id.save:
				i = new Intent();
				i.putExtra(ID, _id);
				i.putExtra(TITLE, _titleEditText.getText().toString());
				i.putExtra(LOCATION, _locationEditText.getText().toString());
				i.putExtra(DESCRIPTION, _descriptionEditText.getText().toString());
				i.putExtra(CATEGORY, _categoryEditText.getText().toString());
				i.putExtra(START_TIME, _startTime.getTimeInMillis());
				i.putExtra(END_TIME, _endTime.getTimeInMillis());
				setResult(RESULT_OK, i);
				finish();
				return true;
			case R.id.remove:
				i = new Intent();
				i.putExtra(ID, _id);
				i.putExtra(DELETE, true);
				setResult(RESULT_OK, i);
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
