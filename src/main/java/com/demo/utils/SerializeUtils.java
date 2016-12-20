package com.demo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化工具类
 * 
 * @author admin
 */
public class SerializeUtils {
	public static byte[] serialize(Object object) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream  oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		return baos.toByteArray();

	}

	public static Object unserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
	}
	public static void main(String[] args) throws IOException {
		String a = "publicKey";
		System.out.println(new String(serialize(a)));
		try {
			System.out.println(unserialize(serialize(a)));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
