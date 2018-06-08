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
	//定义界面需要的面板、文本框、按钮、进度条、复选框等
		private Container contentPane;//用于获取frame中的默认面板
		private JPanel progressPane;//进度条面板
		private JTextField textField1=new JTextField();//得到下载文件的url
		private JTextField textField2=new JTextField();//得到保存的路径
		private JButton downLoadButton=new JButton("下载");
		private JButton openButton = new JButton("打开");
		private JButton stopButton = new JButton("停止");
		private JComboBox nThreadBox;
		private JCheckBox proxybutton = new JCheckBox();
		private JLabel label1=new JLabel("下载的目标文件为:");
		private JLabel label2=new JLabel("下载的文件另存为:");
		private JLabel label3=new JLabel("设置代理 : ");
		private JLabel label4=new JLabel("下载进度：");
		private JLabel label5=new JLabel("线程数：");
		private JTextArea textArea=new JTextArea();
		private JProgressBar jProgressBar = new JProgressBar();
	    private int nTread = 5;// 默认5个线程下载
	    private DownLoadFile downloadfile;
	
		
		public Myxunlei(){
			//定义组件的默认值，标题、大小、位置，添加监听，并将组件添加到相应的容器中
			contentPane = this.getContentPane();
			contentPane.setLayout(null);//设置面板布局为空布局
			//设置默认下载地址
			textField1.setText("http://mirror.bit.edu.cn/apache/spark/spark-2.3.0/spark-2.3.0-bin-hadoop2.7.tgz");
			textField2.setText("D:\\my-test-file");//设置默认保存地址
			textField1.setBounds(150,200,200,20);//设置位置、大小
			textField2.setBounds(150,240,120,20);//设置位置、大小
			label1.setBounds(20,200,120,20);
			label2.setBounds(20,240,120,20);
			openButton.addActionListener(this);//添加监听器
			openButton.setBounds(280, 240, 60, 20);
			downLoadButton.addActionListener(this);
			downLoadButton.setBounds(20,280,60,20);
			stopButton.addActionListener(this);
			stopButton.setBounds(100, 280, 60, 20);
			label3.setBounds(165, 280, 80, 20);
			proxybutton.setBounds(225, 280, 20, 20);
			proxybutton.addActionListener(this);
			nThreadBox= new JComboBox(new String[]{"1","2","3","4","5","6","7","8","9","10"});
			//为下拉列表框初始化数据
			label5.setBounds(250, 280, 60, 20);
			nThreadBox.setBounds(300, 280, 40, 20);
			nThreadBox.addActionListener(this);
			//将组件添加到面板容器中
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
			textArea.setEnabled(false);//设置文本区不能编辑
			textArea.setForeground(Color.black);//设置前景颜色
			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setBounds(20,20,330,170);
			contentPane.add(scrollPane);
			
			progressPane = new JPanel();
			progressPane.setBounds(20, 320,330,100);
			progressPane.add(label4);
			progressPane.add(jProgressBar);
			contentPane.add(progressPane);
			this.setTitle("我的迅雷");//设置标题
			this.setSize(380, 400);
			int height=Toolkit.getDefaultToolkit().getScreenSize().height;
			int width=Toolkit.getDefaultToolkit().getScreenSize().width;
			
			this.setLocation((width-this.getSize().width)/2, (height-this.getSize().height)/2);		
			this.setVisible(true);
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);//设置关闭动作
					 	
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
				textArea.setText("请输入要的文件和保存下载文件完整地址");
			} else {
				downloadfile = new DownLoadFile(url, saveurl, jProgressBar,textArea,nTread);
				this.downloadfile.startDownload();
				textArea.append("开始分配任务并开始下载…");

			}

		}else if(e.getSource() == nThreadBox)
		{//如果选择线程数，相应改变当前的线程数
			String item = nThreadBox.getSelectedItem().toString();
			System.out.println("item is :"+item);
			nTread = Integer.parseInt(item);
		}else if(e.getSource() == openButton)
		{//按下打开按钮时，显示文件选择对话框
			JFileChooser fc=new JFileChooser();
			if(fc.showSaveDialog(this)==fc.APPROVE_OPTION)//fc.APPROVE_OPTION选择确认（yes、ok）后返回该值
			{
				File f=fc.getSelectedFile() ;
				//获取选中或填写的文件对象
				textField2.setText(f.getAbsolutePath()) ;//返回此抽象路径名的绝对路径名字符串。
			 
			}
		}
		
		
	}

}
