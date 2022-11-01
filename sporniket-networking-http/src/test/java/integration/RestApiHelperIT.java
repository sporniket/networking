package integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.sporniket.libre.networking.http.ResponseHandlerBuilder.buildSimpleHandlerForRestApi;
import static org.apache.http.client.fluent.Request.Get;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import org.apache.http.client.ResponseHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.sporniket.libre.networking.http.HttpProvider;
import com.sporniket.libre.networking.http.JsonParseException;
import com.sporniket.libre.networking.http.NetworkingConfiguration;
import com.sporniket.libre.networking.http.RestApiFailureException;
import com.sporniket.libre.networking.http.RestApiResponse;

/**
 * Test suite for the rest api helper.
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
public class RestApiHelperIT
{
	/**
	 * Sample resource model for testing simple rest api.
	 */
	private static class Item
	{
		public String id;

		public String label;
	}

	/**
	 * Sample error model for testing simple rest api.
	 */
	private static class Error
	{
		public String code;

		public String reason;
	}

	private static final String FORMAT_URL_BASE = "http://localhost:%d%s";

	private static final String URL_PATH = "/test";

	private String testUrl;

	private HttpProvider httpProvider = new HttpProvider(new NetworkingConfiguration());

	private WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());

	private final ResponseHandler<RestApiResponse<Item, Error>> mySimpleHandlerForRestApi = buildSimpleHandlerForRestApi(Item.class,
			Error.class);

	@BeforeEach
	public void init()
	{
		server.start();
		testUrl = String.format(FORMAT_URL_BASE, server.port(), URL_PATH);
	}

	@Test
	public void shouldGetParsedObjectWhenResponseOk() throws Exception
	{
		server.stubFor(//
				get(URL_PATH).willReturn(//
						aResponse()//
								.withHeader("Content-Type", "text/plain")//
								.withBody("{\"id\":\"foo\",\"label\":\"bar\"}")//
				)//
		);

		RestApiResponse<Item, Error> result = httpProvider.perform(Get(testUrl)).handleResponse(mySimpleHandlerForRestApi);

		then(result.isSuccess()).isTrue();
		Item apiResponse = result.getResponse();
		then(apiResponse.id).isEqualTo("foo");
		then(apiResponse.label).isEqualTo("bar");
	}

	@Test
	public void shouldGetNullWhenResponseOkAndBodyIsBlank() throws Exception
	{
		server.stubFor(//
				get(URL_PATH).willReturn(//
						aResponse()//
								.withHeader("Content-Type", "text/plain")//
								.withBody("")//
				)//
		);

		RestApiResponse<Item, Error> result = httpProvider.perform(Get(testUrl)).handleResponse(mySimpleHandlerForRestApi);

		then(result.isSuccess()).isTrue();
		then(result.getResponse()).isNull();
	}

	@ParameterizedTest
	@ValueSource(ints =
	{
			200, 400, 500
	})
	public void shouldGetParsingFailureWhenResponseBodyIsNotJson(int status) throws Exception
	{
		server.stubFor(//
				get(URL_PATH).willReturn(//
						aResponse()//
								.withStatus(status)//
								.withHeader("Content-Type", "text/plain")//
								.withBody("Done")//
				)//
		);

		thenThrownBy(() -> {
			httpProvider.perform(Get(testUrl)).handleResponse(mySimpleHandlerForRestApi);
		}).isInstanceOf(JsonParseException.class);
	}

	@Test
	public void shouldGetRestApiFailureWhenStatusIsNotSuccessNorError() throws Exception
	{
		server.stubFor(//
				get(URL_PATH).willReturn(//
						aResponse()//
								.withStatus(600)//
								.withHeader("Content-Type", "text/plain")//
								.withBody("Done")//
				)//
		);

		thenThrownBy(() -> {
			httpProvider.perform(Get(testUrl)).handleResponse(mySimpleHandlerForRestApi);
		}).isInstanceOf(RestApiFailureException.class);
	}

	@ParameterizedTest
	@ValueSource(ints =
	{
			400, 500
	})
	public void shouldGetRestApiFailureWhenResponseIsClientError(int status) throws Exception
	{
		server.stubFor(//
				get(URL_PATH).willReturn(//
						aResponse()//
								.withStatus(status)//
								.withHeader("Content-Type", "text/plain")//
								.withBody("{\"code\":\"1000\", \"reason\":\"whatever\"}")//
				)//
		);

		final RestApiResponse<Item, Error> result = httpProvider.perform(Get(testUrl)).handleResponse(mySimpleHandlerForRestApi);

		then(result.isSuccess()).isFalse();
		Error apiResponse = result.getError();
		then(apiResponse.code).isEqualTo("1000");
		then(apiResponse.reason).isEqualTo("whatever");
	}

	@AfterEach
	public void tearDown()
	{
		server.stop();
	}
}
