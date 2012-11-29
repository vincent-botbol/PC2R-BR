#!/usr/bin/python
# -*- coding: utf-8 -*-
# connection/view.py

import wx

class View(wx.BoxSizer):

    def __init__(self,parent):
        super(View,self).__init__(wx.VERTICAL)
        self.parent = parent
        self.occupy = 0
        self.InitUI()
        #self.Centre()
        #self.Show()

    def InitUI(self):
        #bbox = wx.BoxSizer(wx.VERTICAL)
        box = wx.GridBagSizer(10,10)
        self.loginEntry = wx.TextCtrl(self.parent,value="",
                                      style=wx.wx.TE_LEFT|wx.TE_PROCESS_TAB)
        self.serveurEntry = wx.TextCtrl(self.parent,value="",
                                        style=wx.wx.TE_LEFT|wx.TE_PROCESS_ENTER|wx.TE_PROCESS_TAB)
        self.buttonEnter = wx.ToggleButton(self.parent,label="Entrer",id=wx.ID_ANY)
        self.buttonQuit = wx.Button(self.parent,label="Quitter",id=wx.ID_ANY)
        # self.parent.Bind(wx.EVT_CHAR,self.onTabLogin,source=self.loginEntry)
        # self.parent.Bind(wx.EVT_COMMAND_TEXT_ENTER,self.onEnterServeur,source=self.serveurEntry)
        # self.parent.Bind(wx.EVT_CHAR,self.onTabServeur,source=self.serveurEntry)
        loginLabel = wx.StaticText(self.parent,label="Pseudo :")
        serveurLabel = wx.StaticText(self.parent,label="Adresse du serveur : ")
        spanTwoCol = wx.GBSpan(1,2)
        box.Add(loginLabel,wx.GBPosition(1,1))
        box.Add(serveurLabel,wx.GBPosition(2,1))
        box.Add(self.loginEntry,wx.GBPosition(1,2))
        box.Add(self.serveurEntry,wx.GBPosition(2,2))
        box.Add(self.buttonQuit,wx.GBPosition(3,1))
        box.Add(self.buttonEnter,wx.GBPosition(3,2))
        # bbox.Add(box,proportion=1)
        # bbox.SetSizeHints(self)
        self.Add(box,proportion=1)
        #self.SetSizeHints(self)
        #self.SetSizer(bbox)
        self.buttonQuit.Bind(wx.EVT_BUTTON,self.onQuit, 
                  id=self.buttonQuit.GetId())
        self.buttonEnter.Bind(wx.EVT_TOGGLEBUTTON,self.onConnect, 
                  id=self.buttonEnter.GetId())

    def onQuit(self,event):
        event.Skip()

    def onConnect(self,event):
        self.buttonEnter.SetValue(True)
        self.buttonEnter.Disable()
        event.Skip()
        #self.Close()
     
    # def onEnterServeur(self,event):
    #     self.buttonEnter.command(None)
    
    # def onTabLogin(self,event):
    #     self.serveurEntry.GetFocus()

    # def onTabServeur(self,event):
    #     self.loginEntry.GetFocus()

  
if __name__ == '__main__':
    app = wx.App()
    View(None,title="Connection")
    app.MainLoop()
            
