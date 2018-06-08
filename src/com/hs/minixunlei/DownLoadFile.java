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
	
	
	private String URL;//url��ַ
	private String saveURL;//�����ַ
	private JProgressBar jProgressBar;
	JTextArea textArea = new JTextArea();//�ı�������
	private int nthread ;

	long[] startPos;//��ʼ�ֽ���
	long[] endPos;//�����ֽ���
	DownLoadFileThread[] downFileSplitter;//�Զ�����ļ������߳�����
	private long fileLength;
	
	private Timer timer;
	
	
	
	public DownLoadFile(String uRL, String saveURL,JProgressBar jProgressBar, JTextArea textArea,
			int nTread) {
		
		URL = uRL;
		this.saveURL = saveURL;
		this.textArea = textArea;
		this.nthread = nTread;
		
		this.jProgressBar=jProgressBar;
		
		this.startPos = new long[nthread];//��������Ҫ���߳�����ʼ��ÿ���߳���Ҫ���ļ���ʼ�ֽ�������ʼ�����飩
		this.endPos = new long[nthread];//��ʼ�������ֽ���
		this.downFileSplitter = new DownLoadFileThread[startPos.length];

	}





	public void startDownload() {

		try {
			fileLength = getFileSize(URL);// ����ļ����ȴ�С��getfilesize����Ϊ�Զ��庯��
			if (fileLength == -1) {// ����޷��õ��ļ����ȣ������ʾ
				textArea.append("\n ����֪���ļ����ȣ������ԣ���");
			} else {
				if (fileLength == -2) {// ����޷���ȡ�ļ��������ʾ�����·��
					textArea.append("\n �ļ��޷���ȡ�������Ի���·������");
				} else {// �����ļ����ȣ�ȷ��ÿ���̵߳Ŀ�ʼ�ֽ����ͽ����ֽ���������̺߳����ط�Χ
					for (int i = 0; i < startPos.length; i++)
						startPos[i] = (i * (fileLength / startPos.length));
					
					for (int i = 0; i < endPos.length - 1; i++)
						endPos[i] = startPos[i + 1]-1;
					endPos[endPos.length - 1] = fileLength-1;
					for (int i = 0; i < startPos.length; i++) {
						
						textArea.append("\n" + "�̣߳�" + i + "���ط�Χ��" + startPos[i] + "--"+ endPos[i]);
					}
					
					for (int i = 0; i < startPos.length; i++) {// �����ļ������̶߳��������̲߳������Ϣ
						downFileSplitter[i] = new DownLoadFileThread(this.URL, this.saveURL, this.startPos[i],
							this.endPos[i]);			
						textArea.append("\n" + "�߳� " + i + "����");
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
					readTotal += downFileSplitter[i].getReadPos();//��ÿ���߳��Ѿ����ص��ֽ�����һ���ۼ�ͳ��
				}
				jProgressBar.setValue((int) ( (readTotal) * 100f / fileLength));
				jProgressBar.setString((int) ( (readTotal) * 100f / fileLength)+ "%");
				if (finished)
				{
					if ((long) readTotal == fileLength)
						JOptionPane.showMessageDialog(null,"�������!!!");
					else
						JOptionPane.showMessageDialog(null,"δ���������!!!");
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
		{//�������磬��ȡ��Ӧ
			URL url = new URL(URL);
			HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
			int responseCode = httpConnection.getResponseCode();//�� HTTP ��Ӧ��Ϣ��ȡ״̬��
			if (responseCode >= 400)
			{
				System.out.println("Web��������Ӧ����");
				return -2;// Web��������Ӧ����
			}
			String sHeader;//����һ��string����sHeader�����ļ����ȵ��ļ�ͷ
			for (int i = 1;; i++)// ���ұ�ʶ�ļ����ȵ��ļ�ͷ����ȡ�ļ�����
			{
				sHeader = httpConnection.getHeaderFieldKey(i);
				if (sHeader != null)
				{
					
					if (sHeader.equals("Content-Length"))
					
					{//��ʾ�ļ��ĳ���
						fileLength=httpConnection.getContentLength();
						textArea.append("\n �ļ�����:---"+fileLength);
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
