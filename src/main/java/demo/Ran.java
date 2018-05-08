package demo;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

public class Ran {
	public Ran() {
	}
	
	public String getRan() {
		String uuid = UUID.randomUUID().toString();
        return uuid;
	}
}
