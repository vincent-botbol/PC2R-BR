#!/usr/bin/python
# -*- coding: utf-8 -*-
# connection/controler.py

import wx
import view
from gevent import socket

View = view.View

class Controler(wx.Frame):
    
    def __init__(self,parent):
        super(Controler,self).__init__(None,wx.ID_ANY,"connection")
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
        name = self.viou.loginEntry.GetValue()
        name.replace("\\","\\\\")
        name.replace("/","\/")
        address = self.viou.serveurEntry.GetValue()
        address = socket.gethostbyname(address)
        self.sock.connect((address,2012))
        self.sock.send("CONNECT/"+name+"/\n")
        event.Skip()

    def onQuit(self,event):
        event.Skip()
    

if __name__ == '__main__':
    app = wx.App()
    Controler(None)
    app.MainLoop()
