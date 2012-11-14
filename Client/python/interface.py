#!/usr/bin/python
# -*- coding: utf-8 -*-

import wx

class InterfaceTest(wx.Frame):

    def __init__(self, parent, title):
        super(InterfaceTest, self).__init__(parent, title=title)
        self.InitUI()
        self.Centre()
        self.Show()

    def InitUI(self):
        vbox = wx.BoxSizer(wx.HORIZONTAL)
        tc=wx.TextCtrl(self,value="Ceci est un test")
        tc.AppendText("un peu de test encore")
        gs=wx.GridSizer(16,16,5,5)
        for i in range(16*16) :
            gs.Add(wx.Button(self,label=str(i)),0,wx.EXPAND)
        vbox.Add(gs, proportion=0, flag=wx.EXPAND)
        vbox.Add(tc, proportion=0,flag=wx.EXPAND)
        self.SetSizer(vbox)

if __name__ == '__main__':
    app = wx.App()
    InterfaceTest(None, title='Test')
    app.MainLoop()
