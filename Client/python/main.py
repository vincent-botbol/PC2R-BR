#!/usr/bin/python
# -*- coding: utf-8 -*-
# main.py

import wx
import client
import gevent.socket
import connection

class  Main(wx.App):
    
    def OnInit(self):
        self.con = connection.Controler(self)
        self.Bind(wx.EVT_TOGGLEBUTTON, self.onConnect,source=self.con.viou.buttonEnter)
        self.Bind(wx.EVT_BUTTON, self.onQuit,source=self.con.viou.buttonQuit)
        # self.cli = client.Controler()
        return True

    def onConnect(self,e):
        self.sock = self.con.sock
        print str(self.sock.recv(4096))
        
    def onQuit(self,event):
        self.ExitMainLoop()

if __name__ == '__main__':
    app = Main()
    app.MainLoop()
