package org.cdsdemo.shoppingcart;

import java.io.*;  
import java.net.*;  

public class Test{  
	public static void main(String[] args){  
		try{  
			InetAddress ip=InetAddress.getByName("www.javatpoint.com");
			System.out.println("Host Name: "+ip.getHostName()); 
			System.out.println("IP Address: "+ip.getHostAddress());
			System.out.println("IP Address: "+ip.getLocalHost().getHostAddress());
		} catch(Exception e){
			System.out.println(e);
		}
	}  
}  
