package tech.misfit.ifarmer.model.configuration;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;

public interface AllNetworkCalls {


    @GET("{url}")
    Call<ResponseBody> getRequest(
            @Path(value = "url", encoded = true) String path,
            @QueryMap Map<String, String> hashMap
    );

    @FormUrlEncoded
    @POST("{url}")
    Call<ResponseBody> postRequest(
            @Path(value = "url", encoded = true) String path,
            @FieldMap Map<String, String> hashMap
    );

    @GET("{url}")
    Call<ResponseBody> getRequest(
            @Path(value = "url", encoded = true) String path
    );

    @Headers("Content-Type: application/json")
    @POST("{url}")
    Call<ResponseBody> postRequest(
            @Path(value = "url", encoded = true) String path,
            @Body String body
    );



}
