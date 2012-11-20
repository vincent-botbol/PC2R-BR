#!/usr/bin/python
# -*- coding: utf-8 -*-

import wx
import clientView
import clientSocket

class connectionView(wx.Frame):

    def __init__(self,parent,title):
        super(connectionView,self).__init__(parent,title=title)
        self.waitingoccupy = 0
        self.InitUI()
        self.Centre()
        self.Show()

    def InitUI(self):
        bbox = wx.BoxSizer(wx.VERTICAL)
        box = wx.GridBagSizer(10,10)
        self.loginEntry = wx.TextCtrl(self,value="",style=wx.wx.TE_LEFT|wx.TE_PROCESS_ENTER)
        self.serveurEntry = wx.TextCtrl(self,value="",style=wx.wx.TE_LEFT|wx.TE_PROCESS_ENTER)
        self.buttonEnter = wx.ToggleButton(self,label="Entrer",id=wx.ID_ANY)
        self.buttonQuit = wx.Button(self,label="Quitter",id=wx.ID_ANY)
        loginLabel = wx.StaticText(self,label="Pseudo :")
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
        self.Bind(wx.EVT_BUTTON,  self.onQuit, id=self.buttonQuit.GetId())
        self.Bind(wx.EVT_TOGGLEBUTTON,  self.onConnect, id=self.buttonEnter.GetId())

    def onQuit(self,event):
        self.Close()

    def onConnect(self,event):
        self.buttonEnter.setValue(True)
        if(self.occupy==0):
            self.occupy = 1
            #traitement de la connection
            self.occupy = 0
            clientView.ClientView(None, title='Battle Royale')
            self.Close()
        else :
            print "occupé"

if __name__ == '__main__':
    app = wx.App()
    connectionView(None,title="Connection")
    app.MainLoop()
            
