/**
 *
 */
package com.sporniket.libre.networking.http;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.apache.http.HttpHost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Stores the configuration for networking, like a proxy setting ; only unauthentified proxies are supported.
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
public class NetworkingConfiguration
{

	/**
	 * The connection manager.
	 */
	private final HttpClientConnectionManager myConnectionManager;

	/**
	 * The defined http proxy.
	 */
	private final HttpHost myHttpProxy;

	/**
	 * Http proxy host name.
	 */
	private final String myHttpProxyHost;

	/**
	 * Http proxy port.
	 */
	private final int myHttpProxyPort;

	/**
	 * Default configuration, no proxy.
	 */
	public NetworkingConfiguration()
	{
		this(null, 0);
	}

	/**
	 * Configuration with proxy.
	 * 
	 * @param httpProxyHost
	 *            the proxy host.
	 * @param httpProxyPort
	 *            the proxy port.
	 */
	public NetworkingConfiguration(String httpProxyHost, int httpProxyPort)
	{
		this(httpProxyHost, httpProxyPort, null);
	}

	/**
	 * Configuration with proxy and connection manager.
	 * 
	 * @param httpProxyHost
	 *            the proxy host.
	 * @param httpProxyPort
	 *            the proxy port.
	 * @param connectionManager
	 *            the connection manager.
	 */
	public NetworkingConfiguration(String httpProxyHost, int httpProxyPort, HttpClientConnectionManager connectionManager)
	{
		super();
		this.myHttpProxyHost = httpProxyHost;
		this.myHttpProxyPort = httpProxyPort;
		if (httpProxyPort > 0 && isNotBlank(httpProxyHost))
		{
			myHttpProxy = new HttpHost(httpProxyHost, httpProxyPort);
		}
		else
		{
			myHttpProxy = null;
		}
		if (null != connectionManager)
		{
			this.myConnectionManager = connectionManager;
		}
		else
		{
			this.myConnectionManager = new PoolingHttpClientConnectionManager();
		}
	}

	/**
	 * The connection manager.
	 * 
	 * @return the current value.
	 */
	public HttpClientConnectionManager getConnectionManager()
	{
		return myConnectionManager;
	}

	/**
	 * The defined http proxy.
	 * 
	 * @return the current value.
	 */
	public HttpHost getHttpProxy()
	{
		return myHttpProxy;
	}

	/**
	 * Http proxy host name.
	 * 
	 * @return the current value.
	 */
	public String getHttpProxyHost()
	{
		return myHttpProxyHost;
	}

	/**
	 * Http proxy port.
	 * 
	 * @return the current value.
	 */
	public int getHttpProxyPort()
	{
		return myHttpProxyPort;
	}

	/**
	 * @return <code>true</code> if there is a valid proxy definition.
	 */
	public boolean isHttpProxyDefined()
	{
		return null != myHttpProxy;
	}
}
