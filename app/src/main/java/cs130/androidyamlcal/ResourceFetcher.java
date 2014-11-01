package cs130.androidyamlcal;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kevin on 10/31/14.
 */
public class ResourceFetcher
{
	private static final String TAG = "ResourceFetcher";

	public static Response getResponse(String method, String urlSpec, String jsonBody)
	{
		Response response = new Response();
		byte[] bodyBytes = jsonBody.getBytes();
		HttpURLConnection connection = null;
		try
		{
			URL url = new URL(urlSpec);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			if (!method.equals("GET") && bodyBytes.length == 0)
			{
				connection.addRequestProperty("Content-Length", "0");
			}

			if (bodyBytes.length > 0)
			{
				connection.addRequestProperty("Content-Length", Integer.toString(bodyBytes
						.length));
				connection.addRequestProperty("Content-Type", "application/json");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.connect();

				OutputStream outputStream = connection.getOutputStream();
				outputStream.write(bodyBytes);
				outputStream.flush();
				outputStream.close();
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			Log.i(TAG, "Response Code: " + Integer.toString(connection.getResponseCode()));
			response.setResponseCode(connection.getResponseCode());

			InputStream in = connection.getInputStream();

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
			{
				return null;
			}

			int bytesRead;
			byte[] buffer = new byte[1024];
			while ((bytesRead = in.read(buffer)) > 0)
			{
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			response.setBytes(out.toByteArray());
		}
		catch (IOException ioe)
		{
			Log.e(TAG, "Failed to fetch resource", ioe);
		}
		finally
		{
			if (connection != null)
			{
				connection.disconnect();
			}
		}
		return response;
	}

	public static byte[] getUrlBytes(String method, String urlSpec, String jsonBody)
	{
		return getResponse(method, urlSpec, jsonBody).getBytes();
	}

	public static String getUrl(String method, String urlSpec, String jsonBody) throws IOException
	{
		return new String(getUrlBytes(method, urlSpec, jsonBody));
	}
}
