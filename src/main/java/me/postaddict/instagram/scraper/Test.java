package me.postaddict.instagram.scraper;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import me.postaddict.instagram.scraper.cookie.CookieHashSet;
import me.postaddict.instagram.scraper.cookie.DefaultCookieJar;
import me.postaddict.instagram.scraper.interceptor.ErrorInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgentInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgents;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class Test {
	public static final String ANDROID_4_2_1 = "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko; googleweblight) Chrome/38.0.1025.166 Mobile Safari/535.19";
	public static final String ANDROID_7_0 = "Mozilla/5.0 (Linux; Android 7.0; SM-G570M Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Mobile Safari/537.36";

	public static final String m = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1";
	
	public static void main(String[] args) {
		
        try {
			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        	OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new UserAgentInterceptor(m))
                    .addInterceptor(new ErrorInterceptor())
                    .cookieJar(new DefaultCookieJar(new CookieHashSet()))
                    .addInterceptor(loggingInterceptor)
                    .build();
            Instagram client = new Instagram(httpClient);
            client.basePage();
            client.login("feliixistda", "baker1997");
			client.basePage();
			
			System.out.println("LOGGED IN");

			client.uploadPost(new File("/home/felix/Pictures/photo.jpg"));
			
			System.out.println("Photo has been uploaded");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
