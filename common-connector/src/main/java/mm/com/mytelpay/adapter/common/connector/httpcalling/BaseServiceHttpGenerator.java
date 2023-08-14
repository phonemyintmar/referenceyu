package mm.com.mytelpay.adapter.common.connector.httpcalling;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.*;

public interface BaseServiceHttpGenerator {
    default OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            TrustManager[] trustAllCerts =
                    new TrustManager[] {
                        new X509TrustManager() {
                            public void checkClientTrusted(
                                    X509Certificate[] chain, String authType) {
                                // placeholder
                            }

                            public void checkServerTrusted(
                                    X509Certificate[] chain, String authType) {
                                // placeholder
                            }

                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }
                        }
                    };
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init((KeyManager[]) null, trustAllCerts, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            X509TrustManager trustManager = (X509TrustManager) trustAllCerts[0];
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, trustManager)
                    .hostnameVerifier((hostname, session) -> true);
            return builder;
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }

    Retrofit buildRetrofit(boolean isGetAccessToken);

    Retrofit buildRetrofitWithLogeLevel(
            boolean isGetAccessToken, HttpLoggingInterceptor.Level loggingInterceptor);
}
