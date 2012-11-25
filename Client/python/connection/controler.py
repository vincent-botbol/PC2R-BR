#!/usr/bin/python
# -*- coding: utf-8 -*-
# connection/controler.py

import wx
import view
import gevent.socket

View = view.View

class Controler(wx.Frame):
    
    def __init__(self,parent):
        super(Controler,self).__init__(None,wx.ID_ANY,"connection")
        self.parent = parent
        self.viou = View(self)
        self.SetSizer(self.viou)
        self.Bind(wx.EVT_TOGGLEBUTTON, self.onConnect,source=self.viou.buttonEnter)
        self.Fit()
        self.Centre()
        self.Show()

    def onConnect(self,event):
        print "coucou"

if __name__ == '__main__':
    app = wx.App()
    Controler(None)
    app.MainLoop()
