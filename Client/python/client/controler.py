#!/usr/bin/python
# -*- coding: utf-8 -*-
# client/controler.py

import view
import socket
import chat
import wx

View = view.View

class Controler(wx.Frame):
    
    def __init__(self,parent,sock,name,address=None):
        super(Controler,self).__init__(parent,title="Battle Royale")
        self.viou = View(self)
        self.chat = chat.Widget(self)
        self.viou.Add(self.chat,proportion=1,flag=wx.EXPAND|wx.RIGHT)
        self.sock = sock
        self.SetSizer(self.viou)
        self.Centre()
        self.Show()

if __name__ == '__main__':
    app = wx.App()
    Controler(None,None,"")
    app.MainLoop()
