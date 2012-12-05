#!/usr/bin/python
# -*- coding: utf-8 -*-
# client/controler.py

import view
import socket
import wx
import re

View = view.View

class Controler(wx.Frame):
    
    def __init__(self,parent,sock,name,address=None):
        super(Controler,self).__init__(parent,title="Battle Royale")
        self.viou = View(self)
        self.Bind(wx.EVT_BUTTON,self.onBut)
        self.sock = sock

        self.SetStatusBar(wx.StatusBar(self))
        self.SetStatusText("En attente du serveur")

        self.Centre()
        self.Show()

    def putShip(self):
        for b in self.buts:
            b.SetBitmapHover(self.subBmp)
            b.Bind(wx.EVT_BUTTON,self.onBut)
        rep = self.sock.recv(4096)
        l = re.split("(?<!\\\)/",rep)
        while l[O] <> 'ALLYOURBASE':
            if l[O] == 'SHIP':
                n = int(l[1])
                self.SetStatusText("Placez un sous-marin de taille "+str(n))
                pos = self.viou.putShip(n)

    def onBut(self,e):
        print e.GetEventObject().GetName()

if __name__ == '__main__':
    app = wx.App()
    Controler(None,None,"")
    app.MainLoop()
