package com.sporniket.libre.networking.http;

import org.apache.http.Header;
import org.apache.http.StatusLine;

/**
 * Wrap a response from a rest api along with the return headers and the status line.
 *
 * <p>
 * &copy; Copyright 2020 David Sporn
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
 * 
 * @param <TypeResponse>
 *            the type of the response object.
 * @param <TypeError>
 *            the type of the error object.
 */
class RestApiResponse_Raw<TypeResponse, TypeError>
{
	/**
	 * The http response body, for reference, especially when the response object is <code>null</code>.
	 */
	String body;

	/**
	 * The error object, may be <code>null</code>.
	 */
	TypeError error;

	/**
	 * The http response headers, may contains exploitable data.
	 */
	Header[] headers;

	/**
	 * The response object, may be <code>null</code>.
	 */
	TypeResponse response;

	/**
	 * The http status line.
	 * 
	 * <ul>
	 * <li>a 2xx status tells that this should be a success response, that may be null,</li>
	 * <li>a 4xx or 5xx status indicate that should be a failure response, that may be null</li>
	 * </ul>
	 */
	StatusLine status;

	/**
	 * Indicate that one should look at the response object when <code>true</code>, or the error object when <code>false</code>.
	 */
	boolean success;
}
