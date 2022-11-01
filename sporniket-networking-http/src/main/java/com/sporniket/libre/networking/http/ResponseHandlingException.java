package com.sporniket.libre.networking.http;

import org.apache.http.Header;
import org.apache.http.StatusLine;

/**
 * Thrown when there is a problem while handling the response, gathering the http response body, status and headers.
 *
 * <p>
 * &copy; Copyright 2020-2022 David Sporn
 * </p>
 * <hr>
 * 
 * <p>
 * This file is part of <i>The Sporniket Networking Library &#8211; http</i>.
 * 
 * <p>
 * <i>The Sporniket Networking Library &#8211; http</i> is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 * 
 * <p>
 * <i>The Sporniket Networking Library &#8211; http</i> is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with <i>The Sporniket Networking Library &#8211;
 * http</i>. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>. 2
 * 
 * <hr>
 * 
 * @author David SPORN
 * @version 22.11.00
 * @since 20.04.00
 */
public class ResponseHandlingException extends NetworkingException
{
	private static final long serialVersionUID = -2151046325538043889L;

	private final String myBody;

	private final Header[] myHeaders;

	private final StatusLine myStatusLine;

	public ResponseHandlingException(String body, Header[] headers, StatusLine statusLine)
	{
		super();
		this.myBody = body;
		this.myHeaders = headers;
		this.myStatusLine = statusLine;
	}

	public ResponseHandlingException(String message, String body, Header[] headers, StatusLine statusLine)
	{
		super(message);
		this.myBody = body;
		this.myHeaders = headers;
		this.myStatusLine = statusLine;
	}

	public ResponseHandlingException(String message, Throwable cause, String body, Header[] headers, StatusLine statusLine)
	{
		super(message, cause);
		this.myBody = body;
		this.myHeaders = headers;
		this.myStatusLine = statusLine;
	}

	public ResponseHandlingException(Throwable cause, String body, Header[] headers, StatusLine statusLine)
	{
		super(cause);
		this.myBody = body;
		this.myHeaders = headers;
		this.myStatusLine = statusLine;
	}

	public String getBody()
	{
		return myBody;
	}

	public Header[] getHeaders()
	{
		return myHeaders;
	}

	public StatusLine getStatusLine()
	{
		return myStatusLine;
	}

}
