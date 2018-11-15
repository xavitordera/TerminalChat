/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package firstchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lsadusr11
 */
public class ChatServer {
     public static void main(String[] args) {
        final MyServerSocket serverSocket = new MyServerSocket(Integer.parseInt(args[0]));
        final ConcurrentHashMap<String, MySocket> dictionary;
        dictionary = new ConcurrentHashMap<>();
        
        while(true){
            final MySocket socket = serverSocket.accept();
            final String nick = socket.readString();
            dictionary.put(nick, socket);
            final Collection<MySocket> collection = dictionary.values();
            
            System.out.println("New user "+ nick);
            
            new Thread(){
                public void run() {
                    String line;
                    while((line = socket.readString()) != null ){
                        for(MySocket s:collection){
                            if(s != socket){
                                s.printString(line);
                            }
                        }
                    }
                    socket.closeWriter();
                    socket.closeReader();
                    socket.closeSocket();
                }
            }.start();
        }
    }
}
