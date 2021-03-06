package cs130.androidyamlcal;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kevin on 10/31/14.
 */
public class Event
{
	private int _id;
	private String _title;
	private String _location;
	private String _description;
	private String _category;
	private Calendar _startTime;
	private Calendar _endTime;
	private boolean _isCached;

	public String getTitle()
	{
		return _title;
	}

	public void setTitle(String title)
	{
		_title = title;
	}

	public String getLocation()
	{
		return _location;
	}

	public void setLocation(String location)
	{
		_location = location;
	}

	public String getDescription()
	{
		return _description;
	}

	public void setDescription(String description)
	{
		_description = description;
	}

	public String getCategory()
	{
		return _category;
	}

	public void setCategory(String category)
	{
		_category = category;
	}

	public Calendar getStartTime()
	{
		return _startTime;
	}

	public void setStartTime(Calendar startTime)
	{
		_startTime = startTime;
	}

	public Calendar getEndTime()
	{
		return _endTime;
	}

	public void setEndTime(Calendar endTime)
	{
		_endTime = endTime;
	}

	public boolean isCached()
	{
		return _isCached;
	}

	public void setCached(boolean isCached)
	{
		_isCached = isCached;
	}

	public int getId()
	{
		return _id;
	}

	public void setId(int id)
	{
		_id = id;
	}

//	title: "this is my title",
//	location: "here i am",
//	description: "sup",
//	category: "important",
//	startTime: 1414000000000,
//	endTime: 1414005000000
}
