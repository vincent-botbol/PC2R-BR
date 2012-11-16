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
        try:
            self.sock.settimeout(self.timeout)
            self.sock.connect((self.servAddr,2012))
        except socket.error as msg :
            self.sock.close()
            self.sock=None
        if self.sock is None:
            print 'couldn\'t open socket, go fuck yourself'
            self=None
            
    def mysend(self, msg):
        totalsent = 0
        while totalsent < MSGLEN:
            sent = self.sock.send(msg[totalsent:])
            if sent == 0:
                raise RuntimeError("socket connection broken")
            totalsent = totalsent + sent

    def myreceive(self):
        msg = ''
        while len(msg) < MSGLEN:
            chunk = self.sock.recv(MSGLEN-len(msg))
            if chunk == '':
                raise RuntimeError("socket connection broken")
            msg = msg + chunk
        return msg

if __name__ == '__main__':
    client = ClientSocket("localhost")
        
