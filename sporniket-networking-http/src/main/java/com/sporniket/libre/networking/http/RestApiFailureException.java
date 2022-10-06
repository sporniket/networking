package com.sporniket.libre.networking.http;

import org.apache.http.Header;
import org.apache.http.StatusLine;

/**
 * Thrown by the default handler of the rest api helper.
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
 * @version 20.04.00
 * @since 20.04.00
 */
public class RestApiFailureException extends ResponseHandlingException
{
	private static final long serialVersionUID = 8222220334463670669L;

	public RestApiFailureException(String body, Header[] headers, StatusLine statusLine)
	{
		super(body, headers, statusLine);
	}

	public RestApiFailureException(String message, String body, Header[] headers, StatusLine statusLine)
	{
		super(message, body, headers, statusLine);
	}

	public RestApiFailureException(String message, Throwable cause, String body, Header[] headers, StatusLine statusLine)
	{
		super(message, cause, body, headers, statusLine);
	}

	public RestApiFailureException(Throwable cause, String body, Header[] headers, StatusLine statusLine)
	{
		super(cause, body, headers, statusLine);
	}

}
