/**
 *
 */
package com.sporniket.libre.networking.http.restcallabstraction ;

/**
 * Exceptions thrown by a rest call.
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
public class RestCallException extends RuntimeException {

    private static final long serialVersionUID = 2110032978115662245L ;

    public RestCallException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public RestCallException(String arg0) {
        super(arg0) ;
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public RestCallException(Throwable arg0) {
        super(arg0) ;
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     * @param arg1
     */
    public RestCallException(String arg0, Throwable arg1) {
        super(arg0, arg1) ;
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     */
    public RestCallException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3) ;
        // TODO Auto-generated constructor stub
    }

}
