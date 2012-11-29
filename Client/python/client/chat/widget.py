#!/usr/bin/python
# -*- coding: utf-8 -*-
# client/chat/widget.py

import wx

class Widget(wx.BoxSizer):
    
    def __init__(self,pere):
        super(Widget,self).__init__(wx.VERTICAL)
        self.pere = pere
        self.box = wx.BoxSizer(wx.HORIZONTAL)
        self.chatEntry = wx.TextCtrl(self.pere,style=wx.TE_LEFT
                                     |wx.TE_PROCESS_ENTER)
        self.chatAll = wx.TextCtrl(self.pere,style=wx.TE_MULTILINE
                                   |wx.TE_READONLY|wx.TE_LEFT
                                   |wx.TE_BESTWRAP)
        self.button = wx.Button(self.pere,id=wx.ID_OK)
        self.box.Add(self.chatEntry,proportion=1)
        self.box.Add(self.button,proportion=1)
        self.Add(self.chatAll,proportion=1,flag=wx.EXPAND|wx.CENTRE)
        self.Add(self.box,proportion=0,flag=wx.EXPAND|wx.CENTRE)

if __name__ == '__main__':
    app = wx.App()
    pere = wx.Frame(None)
    pere.SetSizer(Widget(pere))
    pere.Show()
    app.MainLoop()
