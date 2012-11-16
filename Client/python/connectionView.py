#!/usr/bin/python
# -*- coding: utf-8 -*-

import wx

class connectionView(wx.Frame):

    def __init__(self,parent,title):
        super(connectionView,self).__init__(parent,title=title)
        self.InitUI()
        self.Centre()
        self.Show()

    def InitUI(self):
        box = wx.BoxSizer(wx.VERTICAL)
        h1box = wx.BoxSizer(wx.HORIZONTAL)
        h2box = wx.BoxSizer(wx.HORIZONTAL)
        self.loginEntry = wx.TextCtrl(self,value="",style=wx.wx.TE_LEFT|wx.TE_PROCESS_ENTER)
        self.serveurEntry = wx.TextCtrl(self,value="",style=wx.wx.TE_LEFT|wx.TE_PROCESS_ENTER)
        loginLabel = wx.StaticText(self,label="Pseudo")
        serveurLabel = wx.StaticText(self,label="Adresse du serveur")
        h1box.Add(loginLabel)
        h1box.Add(self.loginEntry)
        h2box.Add(serveurLabel)
        h2box.Add(self.serveurEntry)
        self.button = wx.Button(self,label="Go")
        box.Add(h1box)
        box.Add(h2box)
        box.Add(self.button)
        self.SetSizer(box)

if __name__ == '__main__':
    app = wx.App()
    connectionView(None,title="Connection")
    app.MainLoop()
            
