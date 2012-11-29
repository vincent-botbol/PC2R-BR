#!/usr/bin/python
# -*- coding: utf-8 -*-
# client/view.py

import wx
import chat


class View(wx.BoxSizer):
    def __init__(self,parent):
        super(View, self).__init__(wx.HORIZONTAL)
        self.parent=parent
        self.InitUI()
        #self.Centre()
        #self.Show()

    def InitUI(self):
	#chatbox = chat.Widget(self)
        gs=wx.GridSizer(16,16,0,0)
        for i in range(16*16) :
            gs.Add(wx.Button(self.parent,label=str(i)),proportion=0,flag=wx.EXPAND)
        self.Add(gs, proportion=1, flag=wx.EXPAND|wx.LEFT)
        #self.Add(chatbox, proportion=1,flag=wx.EXPAND|wx.RIGHT)
        #self.SetSizer(vbox)
