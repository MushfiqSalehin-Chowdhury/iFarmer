package tech.misfit.ifarmer.model.configuration;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.misfit.ifarmer.BuildConfig;
import tech.misfit.ifarmer.model.utils.ShareInfo;

public class ApiClient {
    public static AllNetworkCalls callRetrofit(final Context context, String url, final String requestId) {
        try {
            OkHttpClient.Builder client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .followRedirects(true)
                    .followSslRedirects(true)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request().newBuilder()
                                    .addHeader("Content-Type", "application/json")
                                    //.addHeader("token","eyJhbGciOiJIUzI1NiJ9.eyJ1bmlxdWVfbmFtZSI6InNhbGVoaW5fbXVzaGZpcSIsInBob25lIjoiMDE3NDE0MjM0MzgiLCJpZCI6NiwiY190aW1lIjoiMjAxOS0wNC0yNSAwNDo0NDozMSArMDAwMCJ9.2YNWMtXO0MTa-6RRe2TWJJfF2u7Vgw9GHbXVIvAzQms")
                                    .addHeader("token", TextUtils.isEmpty(ShareInfo.getInstance().getAuthenticationToken(context)) ? "" : ShareInfo.getInstance().getAuthenticationToken(context))
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .addInterceptor(BuildConfig.DEBUG ? DefaultInterceptors.getHttpBodyLoggingInterceptor() : DefaultInterceptors.getHttpNoneLoggingInterceptor());

            client = enableTls12OnPreLollipop(client);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build();
            return retrofit.create(AllNetworkCalls.class);
        } catch (Exception e) {
            Log.e("ApiClient", "Exception: "+ String.valueOf(e));
            return null;
        }
    }

    private static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 17 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }

    public static class DefaultInterceptors {

        public static HttpLoggingInterceptor getHttpBodyLoggingInterceptor() {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            return interceptor;
        }

        public static HttpLoggingInterceptor getHttpNoneLoggingInterceptor() {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            return interceptor;
        }
    }

    public static class Tls12SocketFactory extends SSLSocketFactory {

        private static final String[] TLS_V12_ONLY = {"TLSv1.2"};

        final SSLSocketFactory delegate;

        public Tls12SocketFactory(SSLSocketFactory base) {
            this.delegate = base;
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            return patch(delegate.createSocket(s, host, port, autoClose));
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            return patch(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
            return patch(delegate.createSocket(host, port, localHost, localPort));
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            return patch(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            return patch(delegate.createSocket(address, port, localAddress, localPort));
        }

        private Socket patch(Socket s) {
            if (s instanceof SSLSocket) {
                ((SSLSocket) s).setEnabledProtocols(TLS_V12_ONLY);
            }
            return s;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isUserProxySet(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                String host = connectivityManager.getDefaultProxy().getHost();
                int port = connectivityManager.getDefaultProxy().getPort();
                if (host.length() > 0 || port > 0) return true;
                else return false;
            } else {
                Method method = ConnectivityManager.class.getMethod("getProxy");
                Object pp = method.invoke(connectivityManager);
                if (pp == null) return false;
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}