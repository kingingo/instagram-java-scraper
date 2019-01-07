package me.postaddict.instagram.scraper.cookie;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.xml.ws.Response;

import okhttp3.Cookie;
import okhttp3.ResponseBody;

public class CookieUtil {	
	private static void toOutput(Cookie cookie, DataOutputStream out) throws IOException {
		out.writeUTF(cookie.name());
		out.writeUTF(cookie.value());
		out.writeLong(cookie.expiresAt());
		out.writeUTF(cookie.domain());
		out.writeUTF(cookie.path());
		out.writeBoolean(cookie.secure());
		out.writeBoolean(cookie.httpOnly());
		out.writeBoolean(cookie.hostOnly());
	}

	private static Cookie fromInput(DataInputStream in) throws IOException {
		Cookie.Builder builder = new Cookie.Builder();

		String domain;
		builder.name(in.readUTF())
			   .value(in.readUTF())
			   .expiresAt(in.readLong())
			   .domain(domain = in.readUTF())
			   .path(in.readUTF());

		if (in.readBoolean())
			builder.secure();
		if (in.readBoolean())
			builder.httpOnly();
		if (in.readBoolean())
			builder.hostOnlyDomain(domain);

		return builder.build();
	}

	public static List<Cookie> loadCookies(String username) {
		DataInputStream in = null;

		File file = getDatafile(username);
		ArrayList<Cookie> cookies = new ArrayList<>();

		if (file.exists()) {
			try {
				in = new DataInputStream(new FileInputStream(file));
				int size = in.readInt();

				for (int i = 0; i < size; i++) {
					cookies.add(fromInput(in));
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
			}

		}

		return cookies;
	}

	private static File getDatafile(String username) {
		return new File("cookies" + File.separatorChar + username + ".data");
	}

	public static void saveCookies(List<Cookie> cookies, String username) {
		DataOutputStream out = null;
		try {
			File file = getDatafile(username);

			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			out = new DataOutputStream(new FileOutputStream(file));
			out.writeInt(cookies.size());
			for (Cookie cookie : cookies) {
				toOutput(cookie, out);
			}

			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
