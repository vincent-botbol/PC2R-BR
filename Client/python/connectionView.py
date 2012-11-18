#!/usr/bin/python
# -*- coding: utf-8 -*-

import wx

class connectionView(wx.Frame):

    def __init__(self,parent,title):
        super(connectionView,self).__init__(parent,title=title)
        self.InitUI2()
        self.Centre()
        self.Show()

    def InitUI(self):
        box = wx.BoxSizer(wx.VERTICAL)
        h1box = wx.BoxSizer(wx.HORIZONTAL)
        h2box = wx.BoxSizer(wx.HORIZONTAL)
        self.loginEntry = wx.TextCtrl(self,value="",style=wx.wx.TE_LEFT|wx.TE_PROCESS_ENTER)
        self.serveurEntry = wx.TextCtrl(self,value="",style=wx.wx.TE_LEFT|wx.TE_PROCESS_ENTER)
        loginLabel = wx.StaticText(self,label="Pseudo :   ")
        serveurLabel = wx.StaticText(self,label="Adresse du serveur :  ")
        h1box.Add(loginLabel)
        h1box.Add(self.loginEntry)
        h2box.Add(serveurLabel)
        h2box.Add(self.serveurEntry)
        self.button = wx.Button(self,label="Go")
        box.Add(h1box)
        box.Add(h2box)
        box.Add(self.button)
        self.SetSizer(box)

    def InitUI2(self):
        bbox = wx.BoxSizer(wx.VERTICAL)
        box = wx.GridBagSizer(10,10)
        self.loginEntry = wx.TextCtrl(self,value="",style=wx.wx.TE_LEFT|wx.TE_PROCESS_ENTER)
        self.serveurEntry = wx.TextCtrl(self,value="",style=wx.wx.TE_LEFT|wx.TE_PROCESS_ENTER)
        self.buttonEnter = wx.Button(self,label="Entrer")
        self.buttonQuit = wx.Button(self,label="Quitter")
        loginLabel = wx.StaticText(self,label="Pseudo : ")
        serveurLabel = wx.StaticText(self,label="Adresse du serveur : ")
        spanTwoCol = wx.GBSpan(1,2)
        box.Add(loginLabel,wx.GBPosition(1,1))
        box.Add(serveurLabel,wx.GBPosition(2,1))
        box.Add(self.loginEntry,wx.GBPosition(1,2))
        box.Add(self.serveurEntry,wx.GBPosition(2,2))
        box.Add(self.buttonQuit,wx.GBPosition(3,1))
        box.Add(self.buttonEnter,wx.GBPosition(3,2))
        bbox.Add(box,proportion=1)
        bbox.SetSizeHints(self)
        self.SetSizer(bbox)


if __name__ == '__main__':
    app = wx.App()
    connectionView(None,title="Connection")
    app.MainLoop()
            
