package mm.com.mytelpay.adapter.common.connector.httpcalling;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import rx.schedulers.Schedulers;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CommonServiceGenerator implements BaseServiceHttpGenerator {
    private static final Object LOCK = new Object();
    private static CommonServiceGenerator serviceAccess;
    private static CommonServiceGenerator serviceBusiness;
    public final ObjectMapper objectMapper;
    public Retrofit retrofitAccess;
    public Retrofit retrofitService = null;
    private HttpConfig httpConfig;

    public CommonServiceGenerator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }

    public Retrofit buildRetrofit(boolean isGetAccessToken) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = this.getUnsafeOkHttpClient().addInterceptor(interceptor);
        builder.addInterceptor(
                chain -> {
                    Request original = chain.request();
                    okhttp3.Request.Builder requestBuilder = original.newBuilder();
                    if (this.httpConfig.getHeaderMaps() != null
                            && this.httpConfig.getHeaderMaps().size() > 0) {

                        for (Map.Entry<String, String> stringStringEntry :
                                this.httpConfig.getHeaderMaps().entrySet()) {
                            requestBuilder.addHeader(
                                    stringStringEntry.getKey(), stringStringEntry.getValue());
                            log.info(
                                    stringStringEntry.getKey()
                                            + ":"
                                            + stringStringEntry.getValue());
                        }
                    }

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                });
        OkHttpClient okHttpClient =
                builder.connectTimeout(
                                this.httpConfig.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                        .readTimeout(this.httpConfig.getReadTimeout(), TimeUnit.MILLISECONDS)
                        .writeTimeout(this.httpConfig.getWriteTimeout(), TimeUnit.MILLISECONDS)
                        .build();
        RxJavaCallAdapterFactory rxAdapter =
                RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        return (new retrofit2.Retrofit.Builder())
                .baseUrl(this.httpConfig.getUrl())
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(JacksonConverterFactory.create(this.objectMapper))
                .client(okHttpClient)
                .build();
    }

    public Retrofit buildRetrofitWithLogeLevel(
            boolean isGetAccessToken, HttpLoggingInterceptor.Level loggingInterceptor) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(loggingInterceptor);
        OkHttpClient.Builder builder = this.getUnsafeOkHttpClient().addInterceptor(interceptor);
        builder.addInterceptor(
                chain -> {
                    Request original = chain.request();
                    okhttp3.Request.Builder requestBuilder = original.newBuilder();
                    if (this.httpConfig.getHeaderMaps() != null
                            && this.httpConfig.getHeaderMaps().size() > 0) {

                        for (Map.Entry<String, String> stringStringEntry :
                                this.httpConfig.getHeaderMaps().entrySet()) {
                            requestBuilder.addHeader(
                                    stringStringEntry.getKey(), stringStringEntry.getValue());
                            log.info(
                                    stringStringEntry.getKey()
                                            + ":"
                                            + stringStringEntry.getValue());
                        }
                    }

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                });
        OkHttpClient okHttpClient =
                builder.connectTimeout(
                                this.httpConfig.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                        .readTimeout(this.httpConfig.getReadTimeout(), TimeUnit.MILLISECONDS)
                        .writeTimeout(this.httpConfig.getWriteTimeout(), TimeUnit.MILLISECONDS)
                        .build();
        RxJavaCallAdapterFactory rxAdapter =
                RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        return (new retrofit2.Retrofit.Builder())
                .baseUrl(this.httpConfig.getUrl())
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(JacksonConverterFactory.create(this.objectMapper))
                .client(okHttpClient)
                .build();
    }

    public <S> S createServiceAccess(Class<S> serviceClass) {
        return this.retrofitAccess.create(serviceClass);
    }

    public <S> S createService(Class<S> serviceClass) {
        return this.retrofitService.create(serviceClass);
    }
}
