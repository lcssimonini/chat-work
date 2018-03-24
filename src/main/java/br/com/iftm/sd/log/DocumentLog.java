package br.com.iftm.sd.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DocumentLog {
	
	public void writeLog(List<String> messages) throws IOException {
	
	   InetAddress addr = InetAddress.getLocalHost();
	   String hostname = addr.getHostName();
		
	   BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\lcarvalm\\Desktop\\"+hostname+"@"+addr+".txt"));
	   
	   for(String a : messages){
		   writer.write(a);
		   writer.newLine();
		}
	   
	   writer.close();
	}
}
