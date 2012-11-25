#!/usr/bin/python
# -*- coding: utf-8 -*-
# client/view.py

import wx
import chat


class View(wx.Frame):
    def __init__(self, parent, title):
        super(View, self).__init__(parent, title=title)
        self.InitUI()
        self.Centre()
        #self.Show()

    def InitUI(self):
        vbox = wx.BoxSizer(wx.HORIZONTAL)
	chatbox = chat.Widget(self)
        gs=wx.GridSizer(16,16,0,0)
        for i in range(16*16) :
            gs.Add(wx.Button(self,label=str(i)),0,wx.EXPAND)
        vbox.Add(gs, proportion=1, flag=wx.EXPAND|wx.LEFT)
        vbox.Add(chatbox, proportion=1,flag=wx.EXPAND|wx.RIGHT)
        self.SetSizer(vbox)


if __name__ == '__main__':
    app = wx.App()
    View(None, title='Battle Royale')
    app.MainLoop()
