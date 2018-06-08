package com.hs.minixunlei;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownLoadFileThread extends Thread{
	
	
	String URL;
	String saveURL;
	long startPos;
	long endPos;
	RandomAccessFile file;
	int readPos=0;



	public int getReadPos() {
		return readPos;
	}



	public DownLoadFileThread(String uRL, String saveURL, long startPos,
			long endPos) {
		super();
		URL = uRL;
		this.saveURL = saveURL;
		this.startPos = startPos;
		this.endPos = endPos;
		
		try {
			file = new RandomAccessFile(saveURL, "rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 创建名为saveURL，权限为读写的文件对象引用
		try {
			file.seek(startPos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 从startPos字节数进行读写操作
		
	}




	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		
		int in=0;
		
		 int offset;
		try
    	{//连接地址，创建连接
    	URL url = new URL(URL);
    	HttpURLConnection httpConnection=(HttpURLConnection)url.openConnection();
    	String sProperty="bytes="+startPos+"-";
    	httpConnection.setRequestProperty("RANGE",sProperty);//设置请求属性");
        
        InputStream input=httpConnection.getInputStream();//得到输入流
        byte[] buf=new byte[1024];
       
        offset=(int)endPos-(int)startPos+1;//每个线程读取的字节数
        
        
        if(offset>1024)
            offset=1024;
        
        while((in = input.read(buf,0,offset))>0 && startPos<endPos) 
        {
        	readPos +=in;
        	file.write(buf,0,in);//写入文件
            startPos+=in;  
            offset = (int) endPos - (int) startPos+1;
        	if (offset > 1024)
        		offset = 1024;
        }
        file.close();
        input.close();
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }		
	}

}
