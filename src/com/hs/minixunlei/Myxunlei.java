package com.hs.minixunlei;

import java.awt.Color;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Myxunlei extends JFrame implements ActionListener{
	//���������Ҫ����塢�ı��򡢰�ť������������ѡ���
		private Container contentPane;//���ڻ�ȡframe�е�Ĭ�����
		private JPanel progressPane;//���������
		private JTextField textField1=new JTextField();//�õ������ļ���url
		private JTextField textField2=new JTextField();//�õ������·��
		private JButton downLoadButton=new JButton("����");
		private JButton openButton = new JButton("��");
		private JButton stopButton = new JButton("ֹͣ");
		private JComboBox nThreadBox;
		private JCheckBox proxybutton = new JCheckBox();
		private JLabel label1=new JLabel("���ص�Ŀ���ļ�Ϊ:");
		private JLabel label2=new JLabel("���ص��ļ����Ϊ:");
		private JLabel label3=new JLabel("���ô��� : ");
		private JLabel label4=new JLabel("���ؽ��ȣ�");
		private JLabel label5=new JLabel("�߳�����");
		private JTextArea textArea=new JTextArea();
		private JProgressBar jProgressBar = new JProgressBar();
	    private int nTread = 5;// Ĭ��5���߳�����
	    private DownLoadFile downloadfile;
	
		
		public Myxunlei(){
			//���������Ĭ��ֵ�����⡢��С��λ�ã���Ӽ��������������ӵ���Ӧ��������
			contentPane = this.getContentPane();
			contentPane.setLayout(null);//������岼��Ϊ�ղ���
			//����Ĭ�����ص�ַ
			textField1.setText("http://mirror.bit.edu.cn/apache/spark/spark-2.3.0/spark-2.3.0-bin-hadoop2.7.tgz");
			textField2.setText("D:\\my-test-file");//����Ĭ�ϱ����ַ
			textField1.setBounds(150,200,200,20);//����λ�á���С
			textField2.setBounds(150,240,120,20);//����λ�á���С
			label1.setBounds(20,200,120,20);
			label2.setBounds(20,240,120,20);
			openButton.addActionListener(this);//��Ӽ�����
			openButton.setBounds(280, 240, 60, 20);
			downLoadButton.addActionListener(this);
			downLoadButton.setBounds(20,280,60,20);
			stopButton.addActionListener(this);
			stopButton.setBounds(100, 280, 60, 20);
			label3.setBounds(165, 280, 80, 20);
			proxybutton.setBounds(225, 280, 20, 20);
			proxybutton.addActionListener(this);
			nThreadBox= new JComboBox(new String[]{"1","2","3","4","5","6","7","8","9","10"});
			//Ϊ�����б���ʼ������
			label5.setBounds(250, 280, 60, 20);
			nThreadBox.setBounds(300, 280, 40, 20);
			nThreadBox.addActionListener(this);
			//�������ӵ����������
			contentPane.add(textField1);
			contentPane.add(textField2);
			contentPane.add(label1);
			contentPane.add(label2);
			contentPane.add(openButton);
			contentPane.add(downLoadButton);
			contentPane.add(stopButton);
			contentPane.add(label3);
			contentPane.add(proxybutton);
			contentPane.add(label5);
			contentPane.add(nThreadBox);
			textArea.setEnabled(false);//�����ı������ܱ༭
			textArea.setForeground(Color.black);//����ǰ����ɫ
			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setBounds(20,20,330,170);
			contentPane.add(scrollPane);
			
			progressPane = new JPanel();
			progressPane.setBounds(20, 320,330,100);
			progressPane.add(label4);
			progressPane.add(jProgressBar);
			contentPane.add(progressPane);
			this.setTitle("�ҵ�Ѹ��");//���ñ���
			this.setSize(380, 400);
			int height=Toolkit.getDefaultToolkit().getScreenSize().height;
			int width=Toolkit.getDefaultToolkit().getScreenSize().width;
			
			this.setLocation((width-this.getSize().width)/2, (height-this.getSize().height)/2);		
			this.setVisible(true);
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);//���ùرն���
					 	
		}

	public static void main(String[] args) {
		new Myxunlei();
//		long[] a;
//		a = new long[5];
//		System.out.println(a.length);
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==downLoadButton){
			
			
			
			String url = textField1.getText();
			String saveurl = textField2.getText();
			if (url.compareTo("") == 0 && saveurl.compareTo("") == 0)
			{
				textArea.setText("������Ҫ���ļ��ͱ��������ļ�������ַ");
			} else {
				downloadfile = new DownLoadFile(url, saveurl, jProgressBar,textArea,nTread);
				this.downloadfile.startDownload();
				textArea.append("��ʼ�������񲢿�ʼ���ء�");

			}

		}else if(e.getSource() == nThreadBox)
		{//���ѡ���߳�������Ӧ�ı䵱ǰ���߳���
			String item = nThreadBox.getSelectedItem().toString();
			System.out.println("item is :"+item);
			nTread = Integer.parseInt(item);
		}else if(e.getSource() == openButton)
		{//���´򿪰�ťʱ����ʾ�ļ�ѡ��Ի���
			JFileChooser fc=new JFileChooser();
			if(fc.showSaveDialog(this)==fc.APPROVE_OPTION)//fc.APPROVE_OPTIONѡ��ȷ�ϣ�yes��ok���󷵻ظ�ֵ
			{
				File f=fc.getSelectedFile() ;
				//��ȡѡ�л���д���ļ�����
				textField2.setText(f.getAbsolutePath()) ;//���ش˳���·�����ľ���·�����ַ�����
			 
			}
		}
		
		
	}

}
