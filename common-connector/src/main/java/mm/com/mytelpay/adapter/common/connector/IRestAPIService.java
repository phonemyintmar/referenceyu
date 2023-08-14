package mm.com.mytelpay.adapter.common.connector;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

import java.util.Map;

public interface IRestAPIService {
    @POST
    Call<ResponseBody> callPOSTToPartner(
            @Url String url,
            @HeaderMap Map<String, String> headers,
            @Body RequestBody requestContent);

    @POST
    Call<ResponseBody> getMethod(
            @Url String url,
            @HeaderMap Map<String, String> headers,
            @Body RequestBody requestContent);
}
