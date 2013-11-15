package com.roy.wolf.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class CompressManager {
	/**
	* 压缩
	* 
	* @param data
	*            待压缩的数据
	* @return byte[] 压缩后的数据
	*/
	public static byte[] compress(byte[] data) {
		byte[] output = new byte[0];

		Deflater compresser = new Deflater();

		compresser.reset();
		compresser.setInput(data);
		compresser.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		compresser.end();
		return output;
	}
	
	public byte[] compressData(OutputStream os) throws IOException {
		byte[] output = new byte[0];

		Deflater d = new Deflater();
		DeflaterOutputStream dout = null;
		
		try {
			dout = new DeflaterOutputStream(os, d);
			dout.write(output);
		} catch (Exception e) {
			output = null;
			e.printStackTrace();
		} finally {
			try {
				os.close();
				dout.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		return output;
	}

	/**
	* 解压缩
	* 
	* @param data
	*            待解压的数据
	* @return byte[] 解压缩后的数据
	*/
	public static byte[] decompress(byte[] data) {
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		decompresser.end();
		return output;
	}
	    
	public byte[] decompressData(InputStream is) throws IOException {
		byte[] output = new byte[0];
	    	
		InflaterInputStream in = null;
		ByteArrayOutputStream bout = null;
		int b;
	    	
		try {
			in = new InflaterInputStream(is);
			bout = new ByteArrayOutputStream(512);
	    		
			while ((b = in.read()) != -1) {
				bout.write(b);
			}
	    		
			output = bout.toByteArray();
		} catch (Exception e) {
			output = null;
			e.printStackTrace();
		} finally {
			try {
				in.close();
				bout.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	    		
		return output;
	}
}
