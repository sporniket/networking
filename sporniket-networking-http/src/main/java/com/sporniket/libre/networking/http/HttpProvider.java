/**
 *
 */
package com.sporniket.libre.networking.http;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Helper to use http calls ; for authenticated calls, only basic authentication is supported.
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
public class HttpProvider
{
	/**
	 * The http client that will perform the requests.
	 */
	private HttpClient myHttpClient;

	public HttpProvider(NetworkingConfiguration networkingConfiguration)
	{
		super();
		myHttpClient = buildHttpBuilder(networkingConfiguration);
	}

	/**
	 * Prépare un builder de {@link HttpClient} doté d'un éventuel proxy.
	 *
	 * @return le builder.
	 */
	private HttpClient buildHttpBuilder(NetworkingConfiguration networkingConfiguration)
	{
		HttpClientBuilder _builder = HttpClientBuilder.create();
		_builder.setConnectionManager(networkingConfiguration.getConnectionManager());

		if (networkingConfiguration.isHttpProxyDefined())
		{
			final HttpHost _proxy = networkingConfiguration.getHttpProxy();
			_builder.setProxy(_proxy);
		}
		return _builder.build();
	}

	/**
	 * Execute la requête à l'aide d'un client HTTP personnalisé.
	 *
	 * @param request
	 *            la requête à exécuter.
	 * @param client
	 *            le client HTTP à utiliser pour exécuter la requête.
	 * @param httpContext
	 *            le contexte d'execution de la requête.
	 * @return la réponse.
	 * @throws NetworkingException
	 *             lorsqu'il y a eu un problème technique.
	 */
	private Response doPerform(Request request, HttpClient client, HttpProviderContext httpContext) throws NetworkingException
	{
		try
		{
			return prepareExecutor(client, httpContext).execute(request);
		}
		catch (ClientProtocolException e)
		{
			throw new NetworkingException(e);
		}
		catch (IOException e)
		{
			throw new NetworkingException(e);
		}
	}

	private Response doPerformSimpleRequest(Request request) throws NetworkingException
	{
		try
		{
			return Executor.newInstance(getHttpClient()).execute(request);
		}
		catch (ClientProtocolException e)
		{
			throw new NetworkingException(e);
		}
		catch (IOException e)
		{
			throw new NetworkingException(e);
		}
	}

	private HttpClient getHttpClient()
	{
		return myHttpClient;
	}

	/**
	 * Execute la requête à l'aide d'un client HTTP de base.
	 *
	 * @param request
	 *            la requête à exécuter.
	 * @return la réponse.
	 * @throws NetworkingException
	 *             lorsqu'il y a eu un problème technique.
	 */
	public Response perform(Request request) throws NetworkingException
	{
		return doPerformSimpleRequest(request);
	}

	/**
	 * Execute la requête à l'aide dans un contexte non trivial.
	 *
	 * @param request
	 *            la requête à exécuter.
	 * @param httpContext
	 *            le contexte d'execution de la requête.
	 * @return la réponse.
	 * @throws NetworkingException
	 *             lorsqu'il y a eu un problème technique.
	 */
	public Response perform(Request request, HttpProviderContext httpContext) throws NetworkingException
	{
		return doPerform(request, getHttpClient(), httpContext);
	}

	private Executor prepareExecutor(HttpClient client, HttpProviderContext clientContext)
	{
		final Executor _executor = Executor.newInstance(client);
		if (null != clientContext && null != clientContext.getCredentials() && null != clientContext.getAuthenticatedHost())
		{
			_executor//
					.auth(clientContext.getAuthenticatedHost(), clientContext.getCredentials())//
					.authPreemptive(clientContext.getAuthenticatedHost())//
			;
		}
		return _executor;
	}
}
