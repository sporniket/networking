package com.sporniket.libre.networking.http;

/**
 * A semantic enum for designating http status code groups.
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
public enum HttpStatusGroup
{
	SC_1XX_INFORMATIONAL(1),
	SC_2XX_SUCCESS(2),
	SC_3XX_REDIRECTION(3),
	SC_4XX_CLIENT_ERROR(4),
	SC_5XX_SERVER_ERROR(5);

	private static final HttpStatusGroup[] ORDERED_BY_GROUP_VALUE =
	{
			SC_1XX_INFORMATIONAL, SC_2XX_SUCCESS, SC_3XX_REDIRECTION, SC_4XX_CLIENT_ERROR, SC_5XX_SERVER_ERROR
	};

	public static HttpStatusGroup ofGroupValue(int value)
	{
		return ORDERED_BY_GROUP_VALUE[value - 1];
	}

	private final int myGroupValue;

	HttpStatusGroup(int groupValue)
	{
		this.myGroupValue = groupValue;
	}

	public int getGroupValue()
	{
		return myGroupValue;
	}

}
