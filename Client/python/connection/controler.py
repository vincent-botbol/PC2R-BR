#!/usr/bin/python
# -*- coding: utf-8 -*-
# connection/controler.py

import wx
import view
import socket
import re

View = view.View

class Controler(wx.Frame):
    
    def __init__(self,parent):
        super(Controler,self).__init__(None,wx.ID_ANY,"connection")
        self.name=None
        self.address = None
        self.sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        self.parent = parent
        self.viou = View(self)
        self.SetSizer(self.viou)
        self.Bind(wx.EVT_TOGGLEBUTTON, self.onConnect,source=self.viou.buttonEnter)
        self.Bind(wx.EVT_BUTTON, self.onQuit,source=self.viou.buttonQuit)
        self.Fit()
        self.Centre()
        self.Show()

    def onConnect(self,event):
        self.name = self.viou.loginEntry.GetValue()
        self.name = re.sub(r'\\',r'\\\\',self.name)
        self.name = re.sub('/','\/',self.name)
        self.address = self.viou.serveurEntry.GetValue()
        try :
            self.address = socket.gethostbyname(self.address)
            self.sock.connect((self.address,2012))
            self.sock.send("CONNECT/"+self.name+"/\n")
            # print self.sock.recv(4096)
        except socket.error as e:
            print e.strerror
            self.sock.shutdown(socket.SHUT_RDWR)
            self.sock.close()
            self.viou.buttonEnter.SetValue(False)
            self.viou.buttonEnter.Enable()
        event.Skip()

    def onQuit(self,event):
        event.Skip()
    

if __name__ == '__main__':
    app = wx.App()
    Controler(None)
    app.MainLoop()
