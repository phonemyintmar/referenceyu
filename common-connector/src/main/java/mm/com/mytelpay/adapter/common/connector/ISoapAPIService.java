package mm.com.mytelpay.adapter.common.connector;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ISoapAPIService {
    @Headers({"Content-Type:text/xml"})
    @POST
    Call<ResponseBody> call(@Url String url, @Body RequestBody soapString);
}
