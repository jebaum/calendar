package cs130.androidyamlcal;

import java.util.Date;

/**
 * Created by kevin on 10/31/14.
 */
public class Event
{
	private String _title;
	private String _location;
	private String _description;
	private String _category;
	private Date _startTime;
	private Date _endTime;
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

	public Date getStartTime()
	{
		return _startTime;
	}

	public void setStartTime(Date startTime)
	{
		_startTime = startTime;
	}

	public Date getEndTime()
	{
		return _endTime;
	}

	public void setEndTime(Date endTime)
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

//	title: "this is my title",
//	location: "here i am",
//	description: "sup",
//	category: "important",
//	startTime: 1414000000000,
//	endTime: 1414005000000
}
