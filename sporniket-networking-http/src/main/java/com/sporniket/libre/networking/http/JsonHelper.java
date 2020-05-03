/**
 *
 */
package com.sporniket.libre.networking.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

//FIXME write a lib
/**
 * Private helper for json parsing & serializing.
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
class JsonHelper
{

	private static final ObjectMapper DEFAULT_MAPPER;

	static
	{
		ObjectMapper _mapper = new ObjectMapper();

		// Configure mapper
		_mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		_mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		_mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		_mapper.setDefaultPropertyInclusion(Include.NON_NULL);
		DEFAULT_MAPPER = _mapper;
	}

	static String jsonFrom(Object object) throws IOException
	{
		return DEFAULT_MAPPER.writeValueAsString(object);
	}

	static <T> T objectFrom(InputStream input, String charset, Class<T> returnClass)
			throws JsonParseException, JsonMappingException, IOException
	{
		return DEFAULT_MAPPER.readValue(new InputStreamReader(input, charset), returnClass);
	}

	static <T> T objectFrom(String json, Class<T> returnClass) throws JsonParseException, JsonMappingException, IOException
	{
		return DEFAULT_MAPPER.readValue(json, returnClass);
	}
}
