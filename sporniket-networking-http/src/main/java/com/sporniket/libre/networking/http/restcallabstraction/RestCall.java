/**
 *
 */
package com.sporniket.libre.networking.http.restcallabstraction ;

/**
 * Model of a Rest call.
 * 
 * <p>
 * Run mvn generate-sources to be able to compile.
 * </p>
 * 
 * <p>
 * Make a factory to instanciate this interface using your framework utility classes (e.g. RestEasy <code>Dispatcher</code>, <code>MockHttpRequest</code> and
 * <code>MockHttpResponse</code>) and use it throughout all your integrated tests.
 * </p>
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
 * <i>The Sporniket Networking Library &#8211; http</i> is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * <p>
 * <i>The Sporniket Networking Library &#8211; http</i> is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with <i>The Sporniket Networking Library &#8211; http</i>. If not, see
 * <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>. 2
 * 
 * <hr>
 * 
 * @author David SPORN
 * @version 20.04.00
 * @since 22.10.00
 */
public interface RestCall {

    RestResponse perform(RestRequest request) throws RestCallException ;
}
