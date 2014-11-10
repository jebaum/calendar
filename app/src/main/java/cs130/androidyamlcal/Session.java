package cs130.androidyamlcal;

/**
 * Created by kevin on 11/9/14.
 */
public class Session
{
	private boolean _offline;
	private String _address;

	public boolean isOffline()
	{
		return _offline;
	}

	public void setOffline(boolean offline)
	{
		_offline = offline;
	}

	public String getAddress()
	{
		return _address;
	}

	public void setAddress(String address)
	{
		_address = address;
	}
}
