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
        self.SetTopWindow(self.con)
        self.sock = None
        self.name = None
        self.Bind(wx.EVT_TOGGLEBUTTON, self.onConnect,source=self.con.viou.buttonEnter)
        self.Bind(wx.EVT_BUTTON, self.onQuit,source=self.con.viou.buttonQuit)
        self.Bind(wx.EVT_CLOSE,self.onQuit)
        self.Bind(client.controler.EVT_NEW_CLIENT,self.newClient)
        # self.cli = client.Controler()
        self.client = None
        return True

    def onConnect(self,e):
        self.sock = self.con.sock
        self.name = self.con.name
        self.serVaddress = self.con.address
        rep = self.sock.readline()# self.sock.readline().next()
        l = re.split("(?<!\\\)/",rep)

        if l[0] == 'ACCESSDENIED':
            dial = wx.MessageDialog(self.con,"Vous vous êtes trompé(e) de mot de passe ou vous n'êtes pas inscit(e)"
                                    ,"Error",wx.CANCEL)
            dial.ShowModal()
            
            self.con.Destroy()
            self.con = connection.Controler(self)
            self.Bind(wx.EVT_TOGGLEBUTTON, self.onConnect,source=self.con.viou.buttonEnter)
            self.Bind(wx.EVT_BUTTON, self.onQuit,source=self.con.viou.buttonQuit)

        if l[0]=='WELCOME':
            self.name = l[1]
            self.con.Destroy()
            self.client = client.Controler(self,self.sock,self.name)
            self.SetTopWindow(self.client)
        
        

    def newClient(self,e):
        self.client.Destroy()
        self.client = client.Controler(self,self.sock,self.name)
        self.SetTopWindow(self.client)

    def onQuit(self,event):
        # print "ici"
        self.ExitMainLoop()

if __name__ == '__main__':
    app = Main()
    app.MainLoop()
