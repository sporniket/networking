/**
 *
 */
package com.sporniket.libre.networking.http;

import static com.sporniket.libre.networking.http.JsonHelper.objectFrom;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;

/**
 * Build {@link ResponseHandler} using a fluent syntax to add handlers for a specific status code, group, or override the generic
 * handler.
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
public class ResponseHandlerBuilder<ResponseType>
{

	public interface Handler<ResponseType>
	{

		ResponseType perform(String body, Header[] headers, StatusLine statusLine) throws NetworkingException;
	}

	static final String RESPONSE_DEFAULT_ENCODING = "utf-8";

	/**
	 * Simple, naïve, response handler for typical rest service using json representations.
	 * 
	 * @param <TypeResource>
	 *            Type of the resource to parse when the service send a successfull response (http code 2xx).
	 * @param <TypeError>
	 *            Type of the error to parse when the service send a not successfull.
	 * @param typeResource
	 *            Type of the resource to parse when the service send a successfull response (http code 2xx).
	 * @param typeError
	 *            Type of the error to parse when the service send a not successfull.
	 * @return the response handler.
	 */
	public static <TypeResource, TypeError> ResponseHandler<RestApiResponse<TypeResource, TypeError>> buildSimpleHandlerForRestApi(
			Class<TypeResource> typeResource, Class<TypeError> typeError)
	{
		return new ResponseHandlerBuilder<RestApiResponse<TypeResource, TypeError>>()//
				.whenStatusGroup(HttpStatusGroup.SC_2XX_SUCCESS, (body, headers, status) -> {
					try
					{
						return new RestApiResponse_Builder<TypeResource, TypeError>()//
								.withBody(body)//
								.withHeaders(headers)//
								.withStatus(status)//
								.withResponse(isNotBlank(body) ? objectFrom(body, typeResource) : (TypeResource) null)//
								.withSuccess(true)//
								.done();
					}
					catch (IOException _error)
					{
						throw new JsonParseException(body, _error);
					}
				})//
				.whenStatusGroup(HttpStatusGroup.SC_4XX_CLIENT_ERROR, (body, headers, status) -> {
					try
					{
						return new RestApiResponse_Builder<TypeResource, TypeError>()//
								.withBody(body)//
								.withHeaders(headers)//
								.withStatus(status)//
								.withError(isNotBlank(body) ? objectFrom(body, typeError) : (TypeError) null)//
								.done();
					}
					catch (IOException _error)
					{
						throw new JsonParseException(body, _error);
					}
				})//
				.whenStatusGroup(HttpStatusGroup.SC_5XX_SERVER_ERROR, (body, headers, status) -> {
					try
					{
						return new RestApiResponse_Builder<TypeResource, TypeError>()//
								.withBody(body)//
								.withHeaders(headers)//
								.withStatus(status)//
								.withError(isNotBlank(body) ? objectFrom(body, typeError) : (TypeError) null)//
								.done();
					}
					catch (IOException _error)
					{
						throw new JsonParseException(body, _error);
					}
				})//
				.otherwise((body, headers, status) -> {
					throw new RestApiFailureException(body, headers, status);
				})//
				.done();
	}

	private static ResponseHandlingException createResponseHandlingException(final String message, Exception cause,
			HttpEntity source)
	{
		Header[] _headers =
		{
				source.getContentEncoding(), source.getContentType()
		};
		final ResponseHandlingException _responseHandlingException = new ResponseHandlingException(message, cause, null, _headers,
				null);
		return _responseHandlingException;
	}

	private static String formatErrorMessage(StatusLine st)
	{
		return String.format("%s (%s)", st.getStatusCode(), st.getReasonPhrase());
	}

	private static String getBody(HttpEntity entity) throws ResponseHandlingException
	{
		if (null == entity)
		{
			return null;
		}
		final String _encoding = getCharset(entity);
		try (InputStream _is = entity.getContent(); BufferedReader _ir = new BufferedReader(new InputStreamReader(_is, _encoding)))
		{
			StringBuilder _b = new StringBuilder();
			String _line = _ir.readLine();
			while (null != _line)
			{
				if (_b.length() > 0)
				{
					_b.append('\n');
				}
				_b.append(_line);
				_line = _ir.readLine();
			}
			return _b.toString();
		}
		catch (IOException | UnsupportedOperationException _error)
		{
			throw createResponseHandlingException("Error while retrieving body", _error, entity);
		}
	};

	private static String getCharset(HttpEntity entity) throws ResponseHandlingException
	{
		try
		{
			String _result = RESPONSE_DEFAULT_ENCODING;
			if (null != entity.getContentType())
			{
				final HeaderElement[] _elements = entity.getContentType().getElements();
				for (HeaderElement _element : _elements)
				{
					for (NameValuePair _parameter : _element.getParameters())
					{
						final String _name = _parameter.getName();
						if (_name.startsWith("charset"))
						{
							String _extract = _name.substring(_name.indexOf(':') + 1).trim();

							if (isBlank(_extract))
							{
								throw new IllegalArgumentException("empty charset");
							}
							_result = _extract;
						}
					}
				}
			}
			return _result;
		}
		catch (Exception _error)
		{
			throw createResponseHandlingException("Error while retrieving charset", _error, entity);
		}
	}

	private final Map<Integer, Handler<ResponseType>> myHandlersByStatusCode = new TreeMap<Integer, ResponseHandlerBuilder.Handler<ResponseType>>();

	private final Map<Integer, Handler<ResponseType>> myHandlersByStatusGroup = new TreeMap<Integer, ResponseHandlerBuilder.Handler<ResponseType>>();

	/**
	 * Define a default handler.
	 */
	private Handler<ResponseType> myDefaultHandler = (b, h, s) -> {
		throw new ResponseHandlingException(formatErrorMessage(s), b, h, s);
	};

	public ResponseHandler<ResponseType> done()
	{
		final Handler<ResponseType> _handlerDefault = myDefaultHandler;
		return response -> {
			final StatusLine _statusLine = response.getStatusLine();
			final Header[] _headers = response.getAllHeaders();
			final String _body = getBody(response.getEntity());

			final int _statusCode = _statusLine.getStatusCode();
			if (myHandlersByStatusCode.containsKey(_statusCode))
			{
				return myHandlersByStatusCode.get(_statusCode).perform(_body, _headers, _statusLine);
			}

			final int _statusGroup = _statusCode / 100;
			if (myHandlersByStatusGroup.containsKey(_statusGroup))
			{
				return myHandlersByStatusGroup.get(_statusGroup).perform(_body, _headers, _statusLine);
			}

			return _handlerDefault.perform(_body, _headers, _statusLine);
		};
	}

	public ResponseHandlerBuilder<ResponseType> otherwise(Handler<ResponseType> handler)
	{
		myDefaultHandler = handler;
		return this;
	}

	public ResponseHandlerBuilder<ResponseType> whenStatusCode(int statusCode, Handler<ResponseType> handler)
	{
		if (null != handler)
		{
			myHandlersByStatusCode.put(statusCode, handler);
		}
		return this;
	}

	public ResponseHandlerBuilder<ResponseType> whenStatusGroup(HttpStatusGroup statusGroup, Handler<ResponseType> handler)
	{
		if (null != statusGroup && null != handler)
		{
			myHandlersByStatusGroup.put(statusGroup.getGroupValue(), handler);
		}
		return this;
	}
}
