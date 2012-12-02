#!/usr/bin/python
# -*- coding: utf-8 -*-
# client/view.py

import wx
import chat
import wx.lib.newevent

class View(wx.Panel):
    def __init__(self,parent):
        super(View, self).__init__(parent)
        self.buts = []
        bs=wx.BoxSizer(wx.HORIZONTAL)
        gs=wx.GridSizer(16,16,0,0)
        for i in range(16*16) :
            but = wx.Button(self,id=i,label=str(i))
            but.Bind(wx.EVT_BUTTON,self.onBut)
            self.buts.append(but)
            gs.Add(but,proportion=1,flag=wx.EXPAND)
        bs.Add(gs,proportion=1,flag=wx.EXPAND|wx.LEFT)
        cs=chat.Widget(self)
        bs.Add(cs,proportion=1,flag=wx.EXPAND|wx.RIGHT)
        self.SetSizer(bs)
        self.Show()
        self.Bind(wx.EVT_KEY_DOWN,onPressKey)

    # def onPressKey(self,event):
    #     win = FindFocus()
    #     if win.__class__ = wx.Button:
    #         i = wing.GetId()
    #         if i < 16*16 and i > 0 :
    #             kc = event.GetKeyCode()
    #             if kc = WXK_UP
                
        


    def onBut(self,e):
        e.Skip()
