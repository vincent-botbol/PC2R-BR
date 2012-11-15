#!/usr/bin/python
# -*- coding: utf-8 -*-

import wx
import socket

class Client():

    def __init__(self,servAddr,name):
        self.name = name
        self.servAddr = socket.gethostbyname(servAddr)
        self.sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        self.sock.connect((self.servAddr,2012))
        self.sock.send("CONNECT/"+name+"/\n")
        test = self.sock.recv(1024)
        print test
        self.sock.close()

if __name__ == '__main__':
    client = Client("localhost","Math")
        
