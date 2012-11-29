#!/usr/bin/python
# -*- coding: utf-8 -*-
# client/view.py

import wx
import chat
import wx.lib.newevent

class View(wx.BoxSizer):
    def __init__(self,parent):
        super(View, self).__init__(wx.HORIZONTAL)
        self.parent=parent
        self.buts = []
        self.InitUI()

    def InitUI(self):
        gs=wx.GridSizer(16,16,0,0)
        for i in range(16*16) :
            pnl = wx.Panel(self.parent)
            but = wx.Button(pnl,id=i,label=str(i))
            but.Bind(wx.EVT_BUTTON,self.onBut)
            self.buts.append(but)
            gs.Add(pnl,proportion=0,flag=wx.EXPAND)
        self.Add(gs, proportion=1, flag=wx.EXPAND|wx.LEFT)
    
    def onBut(self,e):
        e.Skip()
