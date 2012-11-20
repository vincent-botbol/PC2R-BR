#!/usr/bin/python
# -*- coding: utf-8 -*-

import wx
import socket

class ClientSocket():

    def __init__(self,servAddr,timeout=None):
        self.servAddr = socket.gethostbyname(servAddr)
        self.timeout = tiemout
        try:
            self.sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        except socket.error as msg :
            self.sock = None
        
    
    def connect(self, host, port):
        try:
            self.sock.settimeout(self.timeout)
            self.sock.connect((self.servAddr,2012))
        except socket.error as msg :
            self.sock.close()
            self.sock=None
        if self.sock is None:
            print 'couldn\'t open socket, go fuck yourself'
            self=None

    def send(self, msg):
        return  self.sock.send()

    def receive(self):
        return self.sock.recv()

    def close(self):
        self.sock.close()
    
    def setTimeOut(timeout):
        self.sock.settimeout(timeout)


if __name__ == '__main__':
    client = ClientSocket("localhost")
        
