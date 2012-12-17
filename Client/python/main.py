#!/usr/bin/python
# -*- coding: utf-8 -*-
# main.py

import wx
import client
import re
import mysocket
import connection

class  Main(wx.App):
    
    def OnInit(self):
        self.con = connection.Controler(self)
        self.sock = None
        self.name = None
        self.Bind(wx.EVT_TOGGLEBUTTON, self.onConnect,source=self.con.viou.buttonEnter)
        self.Bind(wx.EVT_BUTTON, self.onQuit,source=self.con.viou.buttonQuit)
        self.Bind(wx.EVT_CLOSE,self.onQuit)
        # self.cli = client.Controler()
        return True

    def onConnect(self,e):
        self.sock = self.con.sock
        self.name = self.con.name
        self.serVaddress = self.con.address
        rep = self.sock.readline()# self.sock.readline().next()
        l = re.split("(?<!\\\)/",rep)
        print "main : "+str(l)
        if l[0]=='WELCOME':
            self.name = l[1]
            # print self.name
            print "ok"
            self.con.Destroy()
            client.Controler(self.sock,self.name)
        elif l[0] == 'ACCESSDENIED':
            print "ah merde"
            print rep
            print self.name
        else:
            print "ah merde"
            print rep
            print self.name

    def onQuit(self,event):
        self.ExitMainLoop()

if __name__ == '__main__':
    app = Main()
    app.MainLoop()
