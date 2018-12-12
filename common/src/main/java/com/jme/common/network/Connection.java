package com.jme.common.network;


import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

/**
 * Created by zhangzhongqiang on 2015/7/29.
 * Refactored by Yanmin on 2016/3/16
 */
public class Connection {

    static Collection<? extends Certificate> dzInoutCert;

    public static OkHttpClient getUnsafeOkHttpClient(Interceptor interceptor) {
        OkHttpClient okHttpClient = null;
        try {
            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;
            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{ /*trustManager*/ trustManagerForCertificates}, null/*new SecureRandom()*/);
            sslSocketFactory = sslContext.getSocketFactory();

            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.connectTimeout(60, TimeUnit.SECONDS);
            builder.readTimeout(60, TimeUnit.SECONDS);
            builder.writeTimeout(60, TimeUnit.SECONDS);
            builder.sslSocketFactory(sslSocketFactory, trustManager);
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request.Builder builder = request.newBuilder();
                    /*builder.addHeader(CommonConstants.HttpHeadConfig.TONCE, DateUtil.getCurrentSecondTime())
                            .addHeader(CommonConstants.HttpHeadConfig.DEVICE, String.valueOf(BuildConfig.VERSION_NAME))
                            .addHeader(CommonConstants.HttpHeadConfig.PLATFORM, "android")
                            .addHeader(CommonConstants.HttpHeadConfig.VERSIONCODE, String.valueOf(BuildConfig.VERSION_CODE))*/
                    Response response = chain.proceed(builder.build());
                    return response;
                }
            });

            builder.interceptors().add(new ReceivedCookiesInterceptor());
            builder.interceptors().add(interceptor);
            builder.interceptors().add(logInterceptor);
            builder.retryOnConnectionFailure(true);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }).build();
            okHttpClient = builder.build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static X509TrustManager trustManagerForCertificates = new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            /*return new java.security.cert.X509Certificate[]{};*/
            return new X509Certificate[0];
        }
    };

    static X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException {
        CertificateFactory cerficateFy = CertificateFactory.getInstance("X.509");
        dzInoutCert = cerficateFy.generateCertificates(in);

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : dzInoutCert) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    static InputStream trustedCertificatesInputStream() {
        // PEM files for root certificates of Comodo and Entrust. These two CAs are sufficient to view
        // https://publicobject.com (Comodo) and https://squareup.com (Entrust). But they aren't
        // sufficient to connect to most HTTPS sites including https://godaddy.com and https://visa.com.
        // Typically developers will need to get a PEM file from their organization's TLS administrator.
        final String DZ_INOUT_CERT = "-----BEGIN CERTIFICATE-----\n" +
                "MIIDIjCCAgoCCQDLWBYrY5bbJDANBgkqhkiG9w0BAQsFADBTMQswCQYDVQQGEwJD\n" +
                "TjETMBEGA1UECAwKU29tZS1TdGF0ZTEhMB8GA1UECgwYSW50ZXJuZXQgV2lkZ2l0\n" +
                "cyBQdHkgTHRkMQwwCgYDVQQDDANqbWUwHhcNMTQxMTI2MDc1MTUyWhcNMjQxMTIz\n" +
                "MDc1MTUyWjBTMQswCQYDVQQGEwJDTjETMBEGA1UECAwKU29tZS1TdGF0ZTEhMB8G\n" +
                "A1UECgwYSW50ZXJuZXQgV2lkZ2l0cyBQdHkgTHRkMQwwCgYDVQQDDANqbWUwggEi\n" +
                "MA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCYTPYFsxbKLcExuMEzuZhuvO90\n" +
                "MUUldmKPEmRA+AoeSEIcFDGB0ETMBEf/PmRRkP3ZE3kYD6i0xxeODOsdlddlnS1b\n" +
                "4PvIVFpGw46GaMUAZR9gP/BINaqt7cc1Kt0NfGDl6eqEiHNHYO8tk8tA0hhKV04x\n" +
                "+U60LdpLQNwEHl6mv4EotRL/C+k8y3zQSksfQMA3CJ+bdwQmkXd1d3TpYozS/o1X\n" +
                "wPxXB8nRa5InZbx9mbdPF2uQla52IvslQmll+o8gCGQhBZNpUxD32KVd1uwkTn2V\n" +
                "ieu/34hKj2ektQKMhG6UyjUvBDV/idVdF8qjUqo05FbWlHSTN8ayC35AN+EFAgMB\n" +
                "AAEwDQYJKoZIhvcNAQELBQADggEBABVGWDdDFTzhAvueQcwBJmJWpyuAp/78TMmB\n" +
                "KeXvTRFwxiI1d6t4RBCd1MxSyjmxanaDtB+1rQjDFpY3NvjIroqrGvSIowUS0Uxq\n" +
                "WASIf4k/lXqNrGe17wIIiEJ+Fu3rUx/LPhQ/0SWLlzy2Hyb4YO57uvvKsc1Bqc4v\n" +
                "SSFuGk24CaHK52FekfEB74vbcI8lM9l1Rix0izMtAG8GuIw9cfCxRaNzmv90uh/c\n" +
                "9VxUV6nlaIo/CsWphguBCq1PlXU4lF7Ts1xlYh8ti6mSJH6hdXcwAXZ3uWQa3Xn5\n" +
                "RPEzrH9+R+njIL0KJcsbKvAgELtkJRTCY5oVpzAr6dN0jNj+Kdk=\n" +
                "-----END CERTIFICATE-----\n";

        return new Buffer()
                .writeUtf8(DZ_INOUT_CERT)
                .inputStream();
    }

}