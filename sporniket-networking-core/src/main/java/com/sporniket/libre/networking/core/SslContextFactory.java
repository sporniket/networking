package com.sporniket.libre.networking.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Factory for creating ssl contexts, with default settings for ease of use.
 * <p>
 * &copy; Copyright 2002-2022 David Sporn
 * </p>
 * <hr>
 * 
 * <p>
 * This file is part of <i>The Sporniket Core Library &#8211; lang</i>.
 * 
 * <p>
 * <i>The Sporniket Core Library &#8211; lang</i> is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * <p>
 * <i>The Sporniket Core Library &#8211; lang</i> is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with <i>The Sporniket Core Library &#8211;
 * lang</i>. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>. 2
 * 
 * <hr>
 * 
 * @author David SPORN
 * @version 22.11.00
 * @since 15.06.00
 */
public class SslContextFactory
{
    /**
     * Configuration object for creating the factory.
     * 
     * On creation, this configuration object use default values suitable as is, allowing to override only some of the configuration
     * values.
     * 
     * <p>
     * &copy; Copyright 2002-2022 David Sporn
     * </p>
     * <hr>
     * 
     * <p>
     * This file is part of <i>The Sporniket Core Library &#8211; lang</i>.
     * 
     * <p>
     * <i>The Sporniket Core Library &#8211; lang</i> is free software: you can redistribute it and/or modify it under the terms of
     * the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at
     * your option) any later version.
     * 
     * <p>
     * <i>The Sporniket Core Library &#8211; lang</i> is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
     * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
     * License for more details.
     * 
     * <p>
     * You should have received a copy of the GNU Lesser General Public License along with <i>The Sporniket Core Library &#8211;
     * lang</i>. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>. 2
     * 
     * <hr>
     * 
     * @author David SPORN
     * @version 22.11.00
     * @since 15.06.00
     */
    public static final class Configuration
    {
        /**
         * The algorithm of the key manager (default : default algorithm).
         */
        private String myAlgorithmKeyManagerFactory = ConfigurationDefaultValues.ALGORITHM__KEY_MANAGER_FACTORY;

        /**
         * The algorithm of the PRNG (default : SHA1PRNG).
         */
        private String myAlgorithmSecureRandom = ConfigurationDefaultValues.ALGORITHM__SECURE_RANDOM;

        /**
         * The algorithm of the trust manager (default : default algorithm).
         */
        private String myAlgorithmTrustManagerFactory = ConfigurationDefaultValues.ALGORITHM__TRUST_MANAGER_FACTORY;

        /**
         * The key store password (default : <code>changeit</code>).
         */
        private String myCaCertStorePassword = ConfigurationDefaultValues.PASSWORD;

        /**
         * The path to the ca cert key store of ca certificates (default : <code>JAVA_HOME/lib/security/cacerts</code>).
         */
        private String myCaCertStorePath = ConfigurationDefaultValues.CA_CERT_STORE_PATH;

        /**
         * The type of key store of ca certificates (default : the default keystore type).
         */
        private String myCaCertStoreType = ConfigurationDefaultValues.KEY_STORE_TYPE;

        /**
         * The protocol to use (default : TLS).
         */
        private String myProtocol = ConfigurationDefaultValues.PROTOCOL;

        public String getAlgorithmKeyManagerFactory()
        {
            return myAlgorithmKeyManagerFactory;
        }

        public String getAlgorithmSecureRandom()
        {
            return myAlgorithmSecureRandom;
        }

        public String getAlgorithmTrustManagerFactory()
        {
            return myAlgorithmTrustManagerFactory;
        }

        public String getCaCertStorePassword()
        {
            return myCaCertStorePassword;
        }

        public String getCaCertStorePath()
        {
            return myCaCertStorePath;
        }

        public String getCaCertStoreType()
        {
            return myCaCertStoreType;
        }

        public String getProtocol()
        {
            return myProtocol;
        }

        public void setAlgorithmKeyManagerFactory(String algorithmKeyManagerFactory)
        {
            myAlgorithmKeyManagerFactory = algorithmKeyManagerFactory;
        }

        public void setAlgorithmSecureRandom(String algorithmSecureRandom)
        {
            myAlgorithmSecureRandom = algorithmSecureRandom;
        }

        public void setAlgorithmTrustManagerFactory(String algorithmTrustManagerFactory)
        {
            myAlgorithmTrustManagerFactory = algorithmTrustManagerFactory;
        }

        public void setCaCertStorePassword(String caCertStorePassword)
        {
            this.myCaCertStorePassword = caCertStorePassword;
        }

        public void setCaCertStorePath(String caCertStorePath)
        {
            myCaCertStorePath = caCertStorePath;
        }

        public void setCaCertStoreType(String caCertStoreType)
        {
            myCaCertStoreType = caCertStoreType;
        }

        public void setProtocol(String protocol)
        {
            myProtocol = protocol;
        }

    }

    /**
     * Default values for this factory.
     * 
     * <p>
     * &copy; Copyright 2002-2022 David Sporn
     * </p>
     * <hr>
     * 
     * <p>
     * This file is part of <i>The Sporniket Core Library &#8211; lang</i>.
     * 
     * <p>
     * <i>The Sporniket Core Library &#8211; lang</i> is free software: you can redistribute it and/or modify it under the terms of
     * the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at
     * your option) any later version.
     * 
     * <p>
     * <i>The Sporniket Core Library &#8211; lang</i> is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
     * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
     * License for more details.
     * 
     * <p>
     * You should have received a copy of the GNU Lesser General Public License along with <i>The Sporniket Core Library &#8211;
     * lang</i>. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>. 2
     * 
     * <hr>
     * 
     * @author David SPORN
     * @version 22.11.00
     * @since 15.06.00
     */
    private static final class ConfigurationDefaultValues
    {
        public static final String ALGORITHM__KEY_MANAGER_FACTORY = KeyManagerFactory.getDefaultAlgorithm();

        public static final String ALGORITHM__SECURE_RANDOM = "SHA1PRNG";

        public static final String ALGORITHM__TRUST_MANAGER_FACTORY = TrustManagerFactory.getDefaultAlgorithm();

        public static final String CA_CERT_STORE_PATH = System.getProperty("java.home") + "/lib/security/cacerts";

        public static final String KEY_STORE_TYPE = KeyStore.getDefaultType();

        public static final String PASSWORD = "changeit";

        public static final String PROTOCOL = "TLS";
    }

    /**
     * Configuration for the default constructor.
     */
    private static final Configuration DEFAULT_CONFIG = new Configuration();

    private String myAlgorithmKeyManagerFactory;

    private String myAlgorithmSecureRandom;

    private String myAlgorithmTrustManagerFactory;

    private String myCaCertStorePassword;

    private String myCaCertStorePath;

    private String myCaCertStoreType;

    private KeyManagerFactory myKeyManagerFactory;

    private KeyStore myKeyStore;

    private boolean myNewKeyManagerFactoryRequired;

    private boolean myNewKeyStoreRequired;

    private boolean myNewTrustManagerRequired;

    private String myProtocol;

    private TrustManagerFactory myTrustManagerFactory;

    /**
     * 
     */
    public SslContextFactory()
    {
        this(DEFAULT_CONFIG);
    }

    /**
     * Constructor using custom parameters.
     * 
     * @param configuration
     *            custom parameters.
     */
    public SslContextFactory(Configuration configuration)
    {
        super();
        setProtocol(configuration.getProtocol());
        setCaCertStoreType(configuration.getCaCertStoreType());
        setCaCertStorePath(configuration.getCaCertStorePath());
        setCaCertStorePassword(configuration.getCaCertStorePassword());
        setAlgorithmSecureRandom(configuration.getAlgorithmSecureRandom());
        setAlgorithmKeyManagerFactory(configuration.getAlgorithmKeyManagerFactory());
        setAlgorithmTrustManagerFactory(configuration.getAlgorithmTrustManagerFactory());
    }

    /**
     * @throws KeyStoreException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws FileNotFoundException
     * @throws UnrecoverableKeyException
     */
    private void checkFactoryValidity() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException,
            FileNotFoundException, UnrecoverableKeyException
    {
        if (isNewKeyStoreRequired())
        {
            setKeyStore(KeyStore.getInstance(getCaCertStoreType()));
            getKeyStore().load(new FileInputStream(new File(getCaCertStorePath())), getCaCertStorePassword().toCharArray());
        }
        if (isNewKeyManagerFactoryRequired())
        {
            setKeyManagerFactory(KeyManagerFactory.getInstance(getAlgorithmKeyManagerFactory()));
            getKeyManagerFactory().init(getKeyStore(), getCaCertStorePassword().toCharArray());
        }
        if (isNewTrustManagerRequired())
        {
            setTrustManagerFactory(TrustManagerFactory.getInstance(getAlgorithmTrustManagerFactory()));
            getTrustManagerFactory().init(getKeyStore());
        }
        // reset state
        setNewTrustManagerRequired(false);
        setNewKeyManagerFactoryRequired(false);
        setNewKeyStoreRequired(false);
    }

    public String getAlgorithmKeyManagerFactory()
    {
        return myAlgorithmKeyManagerFactory;
    }

    public String getAlgorithmSecureRandom()
    {
        return myAlgorithmSecureRandom;
    }

    public String getAlgorithmTrustManagerFactory()
    {
        return myAlgorithmTrustManagerFactory;
    }

    public String getCaCertStorePassword()
    {
        return myCaCertStorePassword;
    }

    public String getCaCertStorePath()
    {
        return myCaCertStorePath;
    }

    public String getCaCertStoreType()
    {
        return myCaCertStoreType;
    }

    private KeyManagerFactory getKeyManagerFactory()
    {
        return myKeyManagerFactory;
    }

    private KeyStore getKeyStore()
    {
        return myKeyStore;
    }

    public String getProtocol()
    {
        return myProtocol;
    }

    public SSLContext getSslContext() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
            FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException
    {
        checkFactoryValidity();

        SSLContext _sslContext = SSLContext.getInstance(getProtocol());
        _sslContext.init(getKeyManagerFactory().getKeyManagers(), getTrustManagerFactory().getTrustManagers(),
                SecureRandom.getInstance(getAlgorithmSecureRandom()));

        return _sslContext;

    }

    private TrustManagerFactory getTrustManagerFactory()
    {
        return myTrustManagerFactory;
    }

    private boolean isNewKeyManagerFactoryRequired()
    {
        return myNewKeyManagerFactoryRequired || isNewKeyStoreRequired();
    }

    private boolean isNewKeyStoreRequired()
    {
        return myNewKeyStoreRequired;
    }

    private boolean isNewTrustManagerRequired()
    {
        return myNewTrustManagerRequired || isNewKeyManagerFactoryRequired();
    }

    public void setAlgorithmKeyManagerFactory(String algorithmKeyManagerFactory)
    {
        myAlgorithmKeyManagerFactory = algorithmKeyManagerFactory;
        setNewKeyManagerFactoryRequired(true);
    }

    public void setAlgorithmSecureRandom(String algorithmSecureRandom)
    {
        myAlgorithmSecureRandom = algorithmSecureRandom;
    }

    public void setAlgorithmTrustManagerFactory(String algorithmTrustManagerFactory)
    {
        myAlgorithmTrustManagerFactory = algorithmTrustManagerFactory;
        setNewTrustManagerRequired(true);
    }

    public void setCaCertStorePassword(String caCertStorePassword)
    {
        myCaCertStorePassword = caCertStorePassword;
        setNewKeyStoreRequired(true);
    }

    public void setCaCertStorePath(String caCertStorePath)
    {
        myCaCertStorePath = caCertStorePath;
        setNewKeyStoreRequired(true);
    }

    public void setCaCertStoreType(String caCertStoreType)
    {
        myCaCertStoreType = caCertStoreType;
        setNewKeyStoreRequired(true);
    }

    private void setKeyManagerFactory(KeyManagerFactory keyManagerFactory)
    {
        myKeyManagerFactory = keyManagerFactory;
    }

    private void setKeyStore(KeyStore keyStore)
    {
        myKeyStore = keyStore;
    }

    private void setNewKeyManagerFactoryRequired(boolean newKeyManagerFactoryRequired)
    {
        myNewKeyManagerFactoryRequired = newKeyManagerFactoryRequired;
    }

    private void setNewKeyStoreRequired(boolean newKeyStoreRequired)
    {
        myNewKeyStoreRequired = newKeyStoreRequired;
    }

    private void setNewTrustManagerRequired(boolean newTrustManagerRequired)
    {
        myNewTrustManagerRequired = newTrustManagerRequired;
    }

    public void setProtocol(String protocol)
    {
        myProtocol = protocol;
    }

    private void setTrustManagerFactory(TrustManagerFactory trustManagerFactory)
    {
        myTrustManagerFactory = trustManagerFactory;
    }

}
