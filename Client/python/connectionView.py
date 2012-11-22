#!/usr/bin/python
# -*- coding: utf-8 -*-

import wx
import clientView
import clientSocket
import time

class AbortDialog(wx.Frame):
    def __init__(self,parent,title,message):
        super(AbortDialog, self).__init__(parent,-1,title)
        self.parent = parent
        self.message = message
        self.InitUI()
        self.Show()

    def InitUI(self):
        vbox = wx.BoxSizer(wx.VERTICAL)
        text = wx.StaticText(self,-1,self.message)
        button = wx.Button(self,wx.ID_CANCEL,"Annuler")
        vbox.Add(text)
        vbox.Add(button)
        self.SetSizer(vbox)
        button.Bind(wx.EVT_BUTTON, self.onClose)

    def onClose(self,e):
        e.Skip()
        

class connectionView(wx.Frame):

    def __init__(self,parent,title):
        super(connectionView,self).__init__(parent,title=title)
        self.occupy = 0
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
        self.buttonEnter.SetValue(True)
        self.buttonEnter.Disable()
        #dial = wx.MessageDialog(None,"Connection en cours, veuillez patienter","Connection"
        #                        ,style=wx.CANCEL|wx.ICON_EXCLAMATION)
        dial = AbortDialog(self,"annuler","annuler la connection")
        dial.Bind(wx.EVT_BUTTON,self.onQuit,id=wx.ID_CANCEL) #Permet de rediriger les annulations
        
        clientView.ClientView(None, title='Battle Royale')
        #self.Close()
        
if __name__ == '__main__':
    app = wx.App()
    connectionView(None,title="Connection")
    app.MainLoop()
            
