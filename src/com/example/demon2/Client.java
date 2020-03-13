package com.example.demon2;

import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
 
public class Client {
   static String s="0";
   static String str=null;
   public Client(String str){this.str=str;} 
   static String host=new String("192.168.0.102");//使用本地ip初始化
   static Scanner scanner=null;
   public  static void send() {
      try {
         Log.i("Client","start conncet");     
         Socket socket =new Socket(host,10006); //定义socket对象，传入ip地址，和端口，端口尽量定义在1024以上
         OutputStream os=socket.getOutputStream();     //得到outputStream对象
         PrintWriter pw=new PrintWriter(os);          //转换成字符流
         pw.write(str+'\n');                              //把字符串传给服务器
         pw.flush();
         InputStream is=socket.getInputStream();  //定义输入流用来接收socket的
		 InputStreamReader isr=new InputStreamReader(is);//把字节流转成字符流
	     BufferedReader br=new BufferedReader(isr);
	     String  st;
	     /*while((st=br.readLine())==null);
		 while((st=br.readLine())!=null)
		 {
			s=st;
		 }*/
	     s=br.readLine();
		 is.close();
		 isr.close();
		 br.close();
         //socket.shutdownInput();                    //关闭相应的资源
         os.close();
         pw.close();
         socket.close();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   public String Return()
   {
 	  return s;
   }
}