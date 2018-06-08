package com.hs.minixunlei;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.Timer;


public class DownLoadFile {
	
	
	private String URL;//url地址
	private String saveURL;//保存地址
	private JProgressBar jProgressBar;
	JTextArea textArea = new JTextArea();//文本区对象
	private int nthread ;

	long[] startPos;//开始字节数
	long[] endPos;//结束字节数
	DownLoadFileThread[] downFileSplitter;//自定义的文件下载线程数组
	private long fileLength;
	
	private Timer timer;
	
	
	
	public DownLoadFile(String uRL, String saveURL,JProgressBar jProgressBar, JTextArea textArea,
			int nTread) {
		
		URL = uRL;
		this.saveURL = saveURL;
		this.textArea = textArea;
		this.nthread = nTread;
		
		this.jProgressBar=jProgressBar;
		
		this.startPos = new long[nthread];//根据下载要的线程数初始化每个线程所要的文件起始字节数（初始化数组）
		this.endPos = new long[nthread];//初始化结束字节数
		this.downFileSplitter = new DownLoadFileThread[startPos.length];

	}





	public void startDownload() {

		try {
			fileLength = getFileSize(URL);// 获得文件长度大小，getfilesize函数为自定义函数
			if (fileLength == -1) {// 如果无法得到文件长度，输出提示
				textArea.append("\n 不可知的文件长度！请重试！！");
			} else {
				if (fileLength == -2) {// 如果无法获取文件，输出提示，检查路径
					textArea.append("\n 文件无法获取！请重试或检查路径！！");
				} else {// 根据文件长度，确定每个线程的开始字节数和结束字节数，输出线程和下载范围
					for (int i = 0; i < startPos.length; i++)
						startPos[i] = (i * (fileLength / startPos.length));
					
					for (int i = 0; i < endPos.length - 1; i++)
						endPos[i] = startPos[i + 1]-1;
					endPos[endPos.length - 1] = fileLength-1;
					for (int i = 0; i < startPos.length; i++) {
						
						textArea.append("\n" + "线程：" + i + "下载范围：" + startPos[i] + "--"+ endPos[i]);
					}
					
					for (int i = 0; i < startPos.length; i++) {// 创建文件下载线程对象，启动线程并输出信息
						downFileSplitter[i] = new DownLoadFileThread(this.URL, this.saveURL, this.startPos[i],
							this.endPos[i]);			
						textArea.append("\n" + "线程 " + i + "启动");
						downFileSplitter[i].start();
					}	 			
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		jProgressBar.setMaximum(100);
		jProgressBar.setMinimum(0);
		jProgressBar.setStringPainted(true);
		jProgressBar.setString("0%");
		timer = new Timer(100, new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				int readTotal = 0;
				boolean finished = true;
				for (int i = 0; i < startPos.length; i++)
				{
					if (downFileSplitter[i].isAlive())
						finished = false;
					readTotal += downFileSplitter[i].getReadPos();//把每个线程已经下载的字节数做一个累加统计
				}
				jProgressBar.setValue((int) ( (readTotal) * 100f / fileLength));
				jProgressBar.setString((int) ( (readTotal) * 100f / fileLength)+ "%");
				if (finished)
				{
					if ((long) readTotal == fileLength)
						JOptionPane.showMessageDialog(null,"下载完成!!!");
					else
						JOptionPane.showMessageDialog(null,"未能完成下载!!!");
					timer.stop();
			}
			}
		});
		timer.start();

	}

	
	
	public long getFileSize(String URL)
	{
		int fileLength = -1;
		try
		{//连接网络，获取响应
			URL url = new URL(URL);
			HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
			int responseCode = httpConnection.getResponseCode();//从 HTTP 响应消息获取状态码
			if (responseCode >= 400)
			{
				System.out.println("Web服务器响应错误");
				return -2;// Web服务器响应错误
			}
			String sHeader;//定义一个string变量sHeader代表文件长度的文件头
			for (int i = 1;; i++)// 查找标识文件长度的文件头，获取文件长度
			{
				sHeader = httpConnection.getHeaderFieldKey(i);
				if (sHeader != null)
				{
					
					if (sHeader.equals("Content-Length"))
					
					{//显示文件的长度
						fileLength=httpConnection.getContentLength();
						textArea.append("\n 文件长度:---"+fileLength);
						break;
					}
				} else
				{
					break;
				}
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return fileLength;
	}

	
	
	

}
