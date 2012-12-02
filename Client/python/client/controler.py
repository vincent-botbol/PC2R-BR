#!/usr/bin/python
# -*- coding: utf-8 -*-
# client/controler.py

import view
import socket
import wx

View = view.View

class Controler(wx.Frame):
    
    def __init__(self,parent,sock,name,address=None):
        super(Controler,self).__init__(parent,title="Battle Royale")
        self.viou = View(self)
        self.Bind(wx.EVT_BUTTON,self.onBut)
        self.sock = sock
        self.Centre()
        self.Show()

    def onBut(self,e):
        print e.GetId()

if __name__ == '__main__':
    app = wx.App()
    Controler(None,None,"")
    app.MainLoop()
