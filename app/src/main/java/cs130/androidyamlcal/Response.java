package cs130.androidyamlcal;

/**
 * Created by kevin on 10/31/14.
 */
public class Response
{
	int _responseCode;
	byte[] _bytes;

	public int getResponseCode()
	{
		return _responseCode;
	}

	public void setResponseCode(int responseCode)
	{
		_responseCode = responseCode;
	}

	public byte[] getBytes()
	{
		return _bytes;
	}

	public void setBytes(byte[] bytes)
	{
		_bytes = bytes;
	}
}
