package vn.hoanguyen.stargithubsearch.service

import android.text.TextUtils
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.hoanguyen.stargithubsearch.BuildConfig
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by Hoa Nguyen on Oct 09 2019.
 */
abstract class BaseNetworkAPI<API>(baseUrl: String) {
    private val networkAPI: API
    private val okHttpClient: OkHttpClient
    private fun createGsonConverter(gsonBuilder: GsonBuilder): GsonConverterFactory {
        return GsonConverterFactory.create(gsonBuilder.create())
    }

    protected abstract fun createNetworkAPI(retrofit: Retrofit): API
    private fun buildHttpClient(): OkHttpClient {
        val builder: OkHttpClient.Builder = okHttpBuilder
        builder.connectTimeout(10, TimeUnit.SECONDS)
        builder.readTimeout(30, TimeUnit.SECONDS)
        builder.writeTimeout(30, TimeUnit.SECONDS)
        builder.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                //this is where we will add whatever we want to our request headers.
                val basicRequest: Request = chain.request()
                val requestBuilder: Request.Builder = basicRequest.newBuilder()
                val authParam = buildAuthorizationParam()
                if (!TextUtils.isEmpty(authParam)) requestBuilder.addHeader(
                    HEADER_AUTHORIZATION,
                    authParam
                )
                return chain.proceed(requestBuilder.build())
            }
        })
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(interceptor)
        }

        return builder.build()
    }

    private val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

    protected fun buildAuthorizationParam(): String {
        //Fixme or override
//        String username = "mobile";
//        String password = "something";
//        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
//            String credentials = username + ":" + password;
//            return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//        }
        return ""
    }

    fun api(): API {
        return networkAPI
    }

    companion object {
        private const val HEADER_CONTENT_TYPE = "Content-Type"
        private const val HEADER_AUTHORIZATION = "Authorization"
    }

    init {
        val gsonBuilder = GsonBuilder()
        okHttpClient = buildHttpClient()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(createGsonConverter(gsonBuilder))
            .client(okHttpClient)
            .build()
        networkAPI = createNetworkAPI(retrofit)
    }
}