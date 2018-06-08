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
		}// ������ΪsaveURL��Ȩ��Ϊ��д���ļ���������
		try {
			file.seek(startPos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// ��startPos�ֽ������ж�д����
		
	}




	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		
		int in=0;
		
		 int offset;
		try
    	{//���ӵ�ַ����������
    	URL url = new URL(URL);
    	HttpURLConnection httpConnection=(HttpURLConnection)url.openConnection();
    	String sProperty="bytes="+startPos+"-";
    	httpConnection.setRequestProperty("RANGE",sProperty);//������������");
        
        InputStream input=httpConnection.getInputStream();//�õ�������
        byte[] buf=new byte[1024];
       
        offset=(int)endPos-(int)startPos+1;//ÿ���̶߳�ȡ���ֽ���
        
        
        if(offset>1024)
            offset=1024;
        
        while((in = input.read(buf,0,offset))>0 && startPos<endPos) 
        {
        	readPos +=in;
        	file.write(buf,0,in);//д���ļ�
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
