package integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.absent;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.sporniket.libre.networking.http.HttpStatusGroup.SC_2XX_SUCCESS;
import static org.apache.http.client.fluent.Request.Get;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.sporniket.libre.networking.http.HttpProvider;
import com.sporniket.libre.networking.http.HttpProviderContext;
import com.sporniket.libre.networking.http.HttpProviderContext_Builder;
import com.sporniket.libre.networking.http.NetworkingConfiguration;
import com.sporniket.libre.networking.http.ResponseHandlerBuilder;
import com.sporniket.libre.networking.http.ResponseHandlingException;

/**
 * Test suite for the general use of the lib.
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
 */
public class NetworkingIT
{

	private static class CallResult
	{
		private String data; // when error is not null ;

		private String error; // when there is an error ;

		private int httpStatus; // http status;
	}

	private static final String FORMAT_URL_BASE = "http://localhost:%d%s";

	private static final String URL_PATH = "/test";

	private static final ResponseHandlerBuilder.Handler<CallResult> PROCESS_ERROR = (b, h, s) -> {
		final CallResult _result = new CallResult();
		_result.error = b;
		_result.httpStatus = s.getStatusCode();
		return _result;
	};

	private static final ResponseHandlerBuilder.Handler<CallResult> PROCESS_ERROR_GONE = (b, h, s) -> {
		final CallResult _result = new CallResult();
		_result.error = "It is gone !!";
		_result.httpStatus = s.getStatusCode();
		return _result;
	};

	private static final ResponseHandlerBuilder.Handler<CallResult> PROCESS_OK = (b, h, s) -> {
		final CallResult _result = new CallResult();
		_result.data = b;
		_result.httpStatus = s.getStatusCode();
		return _result;
	};

	private String testUrl;

	private HttpProvider httpProvider = new HttpProvider(new NetworkingConfiguration());

	private final ResponseHandler<CallResult> responseHandler = new ResponseHandlerBuilder<CallResult>()//
			.whenStatusGroup(SC_2XX_SUCCESS, PROCESS_OK)//
			.whenStatusGroup(SC_2XX_SUCCESS, null)// useless, for coverage only
			.whenStatusGroup(null, PROCESS_OK)// useless, for coverage only
			.whenStatusCode(410, PROCESS_ERROR_GONE)//
			.whenStatusCode(200, null)// useless, for coverage only
			.otherwise(PROCESS_ERROR)//
			.done();

	private WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());

	@BeforeEach
	public void init()
	{
		server.start();
		testUrl = String.format(FORMAT_URL_BASE, server.port(), URL_PATH);
	}

	@Test
	public void shouldProcessOkResponse() throws Exception
	{
		server.stubFor(//
				get(URL_PATH).willReturn(//
						aResponse()//
								.withHeader("Content-Type", "text/plain; charset:iso-8859-1; what:ever")//
								.withBody("Done")//
				)//
		);

		CallResult result = httpProvider.perform(Get(testUrl)).handleResponse(responseHandler);

		then(result.httpStatus)
				.describedAs("{\n  httpStatus:%d,\n  data:%s,\n  error:%s\n}\n", result.httpStatus, result.data, result.error)
				.isEqualTo(200);
		then(result.data).isEqualTo("Done");
		then(result.error).isNull();
	}

	@Test
	public void shouldSupportResponseWithMultilineBody() throws Exception
	{
		server.stubFor(//
				get(URL_PATH).willReturn(//
						aResponse()//
								.withHeader("Content-Type", "text/plain; charset:iso-8859-1; what:ever")//
								.withBody("Line1\nLine2\r\nLine3\rLine4")//
				)//
		);

		CallResult result = httpProvider.perform(Get(testUrl)).handleResponse(responseHandler);

		then(result.httpStatus)
				.describedAs("{\n  httpStatus:%d,\n  data:%s,\n  error:%s\n}\n", result.httpStatus, result.data, result.error)
				.isEqualTo(200);
		then(result.data).isEqualTo("Line1\nLine2\nLine3\nLine4");
		then(result.error).isNull();
	}

	@Test
	public void shouldFailWhenCharsetIsBadlyFormed() throws Exception
	{
		server.stubFor(//
				get(URL_PATH).willReturn(//
						aResponse()//
								.withHeader("Content-Type", "text/plain; charset:")//
								.withBody("Done")//
				)//
		);

		thenThrownBy(() -> {
			httpProvider.perform(Get(testUrl)).handleResponse(responseHandler);
		})//
				.isInstanceOf(ResponseHandlingException.class)//
				.hasMessage("Error while retrieving charset");
	}

	@Test
	public void shouldFailWhenCharsetIsNotSupported() throws Exception
	{
		server.stubFor(//
				get(URL_PATH).willReturn(//
						aResponse()//
								.withHeader("Content-Type", "text/plain; charset:what-ever")//
								.withBody("Done")//
				)//
		);

		thenThrownBy(() -> {
			httpProvider.perform(Get(testUrl)).handleResponse(responseHandler);
		})//
				.isInstanceOf(ResponseHandlingException.class)//
				.hasMessage("Error while retrieving body");
	}

	@Test
	public void shouldProcessErrorResponse() throws Exception
	{
		server.stubFor(//
				get(URL_PATH).willReturn(//
						aResponse()//
								.withHeader("Content-Type", "text/plain")//
								.withBody("Done")//
								.withStatus(500)//
				)//
		);

		CallResult result = httpProvider.perform(Get(testUrl)).handleResponse(responseHandler);

		then(result.httpStatus)
				.describedAs("{\n  httpStatus:%d,\n  data:%s,\n  error:%s\n}\n", result.httpStatus, result.data, result.error)
				.isEqualTo(500);
		then(result.error).isEqualTo("Done");
		then(result.data).isNull();
	}

	@Test
	public void shouldUseSpecificHandlerWhenSpecified() throws Exception
	{
		server.stubFor(//
				get(URL_PATH).willReturn(//
						aResponse()//
								.withStatus(410))//
		);

		CallResult result = httpProvider.perform(Get(testUrl)).handleResponse(responseHandler);

		then(result.httpStatus)
				.describedAs("{\n  httpStatus:%d,\n  data:%s,\n  error:%s\n}\n", result.httpStatus, result.data, result.error)
				.isEqualTo(410);
		then(result.error)
				.describedAs("{\n  httpStatus:%d,\n  data:%s,\n  error:%s\n}\n", result.httpStatus, result.data, result.error)
				.isEqualTo("It is gone !!");
		;
	}

	@Test
	public void shouldProcessEmptyOkResponses() throws Exception
	{
		server.stubFor(//
				get(URL_PATH).willReturn(//
						aResponse()//
				)//
		);

		CallResult result = httpProvider.perform(Get(testUrl)).handleResponse(responseHandler);

		then(result.httpStatus)
				.describedAs("{\n  httpStatus:%d,\n  data:%s,\n  error:%s\n}\n", result.httpStatus, result.data, result.error)
				.isEqualTo(200);
		then(result.data)
				.describedAs("{\n  httpStatus:%d,\n  data:%s,\n  error:%s\n}\n", result.httpStatus, result.data, result.error)
				.isEqualTo("");
		;
	}

	@Test
	public void shouldProcess401Responses() throws Exception
	{
		server.stubFor(//
				get(URL_PATH)//
						.willReturn(//
								aResponse()//
										.withStatus(401))//
		);

		CallResult result = httpProvider.perform(Get(testUrl)).handleResponse(responseHandler);

		then(result.httpStatus)
				.describedAs("{\n  httpStatus:%d,\n  data:%s,\n  error:%s\n}\n", result.httpStatus, result.data, result.error)
				.isEqualTo(401);
		then(result.data).isNull();
		then(result.error).isEqualTo("");
	}

	@Test
	public void shouldThrowExceptionsByDefault() throws Exception
	{
		server.stubFor(//
				get(URL_PATH).willReturn(//
						aResponse()//
				)//
		);
		thenThrownBy(() -> {
			httpProvider.perform(Get(testUrl)).handleResponse(new ResponseHandlerBuilder<CallResult>().done());
		})//
				.isInstanceOf(ResponseHandlingException.class)//
				.hasFieldOrPropertyWithValue("body", "")//
		;
	}

	@Test
	public void shouldCallWithAuthentication() throws Exception
	{
		server.stubFor(//
				get(URL_PATH)//
						.withHeader("Authorization", absent())//
						.willReturn(//
								aResponse()//
										.withStatus(401)//
										.withHeader("WWW-Authenticate", "Basic realm=\"whatever\"")//
						)//
		);
		server.stubFor(//
				get(URL_PATH)//
						.withBasicAuth("foo", "bar")//
						.willReturn(//
								aResponse()//
										.withStatus(200)//
										.withBody("Done")//
						)//
		);

		final URI _testUri = new URI(testUrl);
		HttpProviderContext context = new HttpProviderContext_Builder()//
				.withAuthenticatedHost(new HttpHost(_testUri.getHost(), _testUri.getPort(), _testUri.getScheme()))//
				.withCredentials(new UsernamePasswordCredentials("foo", "bar"))//
				.done();
		CallResult result = httpProvider.perform(Get(testUrl), context).handleResponse(responseHandler);

		then(result.httpStatus)
				.describedAs("{\n  httpStatus:%d,\n  data:%s,\n  error:%s\n}\n", result.httpStatus, result.data, result.error)
				.isEqualTo(200);
		then(result.data).isEqualTo("Done");
		then(result.error).isNull();

		// test preemptive authentication
		final List<ServeEvent> _allServeEvents = server.getAllServeEvents();
		then(_allServeEvents)//
				.describedAs("Should call directly with authentication headers")//
				.hasSize(1);
	}

	@AfterEach
	public void tearDown()
	{
		server.stop();
	}
}
