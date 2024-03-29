package tech.misfit.ifarmer.model.configuration;


import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import tech.misfit.ifarmer.R;
import tech.misfit.ifarmer.model.utils.ShareInfo;



public abstract class ApiHandler {

    private String TAG = "ApiHandler";
    private Context context;

    public ApiHandler(Context context) {
        this.context = context;
    }

    public void httpRequest(String baseUrl, String path, String requestType, String requestId, HashMap hashMap) {
        try {
            startApiCall(requestId);
            Call<ResponseBody> bodyToCall = null;

            switch (requestType) {
                case "get":
                    bodyToCall = ApiClient.callRetrofit(context, baseUrl, requestId).getRequest(path, hashMap);
                    break;
                case "post":
                    bodyToCall = ApiClient.callRetrofit(context, baseUrl, requestId).postRequest(path, hashMap);
                    break;
            }

            bodyToCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e(TAG, "onResponse: " + response.code());
                    Log.e(TAG, "onResponse: " + String.valueOf(response.body()));
                    endApiCall(requestId);
                    if (response.code() == ResponseCode.SUCCESS_RESPONSE || response.code() == 201 || response.code() == 204) {
                        try {
                            successResponse(requestId, response.body(), baseUrl, path, requestType);
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: failResponse " + String.valueOf(e));
                            failResponse(requestId, ResponseCode.INVALID_JSON_RESPONSE, context.getString(R.string.invalid_json_response));
                        }
                    } else if (response.code() == 401) {
                        failResponse(requestId, ResponseCode.UNAUTHENTICATION, context.getString(R.string.access_deny));
                        ShareInfo.getInstance().logout(context);
                    } else {
                        failResponse(requestId, ResponseCode.UNAUTHENTICATION, context.getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Log.e(TAG, "onResponse: Error " + String.valueOf(throwable));
                    endApiCall(requestId);
                    if (throwable instanceof HttpException)
                        failResponse(requestId, ResponseCode.SERVER_ERROR, context.getString(R.string.invalid_request));
                    else if (throwable instanceof UnknownHostException)
                        failResponse(requestId, ResponseCode.SERVER_ERROR, context.getString(R.string.connectivity_error));
                    else if (throwable instanceof IOException)
                        failResponse(requestId, ResponseCode.SERVER_ERROR, context.getString(R.string.connectivity_error));
                    else
                        failResponse(requestId, ResponseCode.SERVER_ERROR, context.getString(R.string.unknown_error));
                }
            });
        } catch (Exception e) {
            failResponse(requestId, ResponseCode.UNKNOWN_ERROR, context.getString(R.string.unknown_error));
            Log.e(TAG, "onResponse: " + String.valueOf(e));
        }
    }

    public abstract void startApiCall(String requestId);

    public abstract void endApiCall(String requestId);

    public abstract void downloadProgress(String requestId, int progress);

    public abstract void successResponse(String requestId, ResponseBody responseBody, String baseUrl, String path, String requestType);

    public abstract void failResponse(String requestId, int responseCode, String message);
}

