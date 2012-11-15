#!/usr/bin/python
# -*- coding: utf-8 -*-

import wx
import socket

if __name__ == '__main__':
    s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    s.bind(("localhost",2012))
    s.listen(1)
    conn,addr = s.accept()
    print 'Connected by', addr
    test = conn.recv(1024)
    print test
    conn.send("Bien recu")
    conn.close()
    s.close()
