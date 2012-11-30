#!/usr/bin/python
# -*- coding: utf-8 -*-
# connection/view.py

import wx

class View(wx.Panel):

    def __init__(self,parent):
        super(View,self).__init__(parent)
        #bbox = wx.BoxSizer(wx.VERTICAL)
        box = wx.GridBagSizer(10,10)
        self.loginEntry = wx.TextCtrl(self,value="",
                                      style=wx.wx.TE_LEFT|wx.TE_PROCESS_ENTER)
        self.serveurEntry = wx.TextCtrl(self,value="",
                                        style=wx.wx.TE_LEFT|wx.TE_PROCCES_ENTER)
        self.buttonEnter = wx.ToggleButton(self,label="Entrer",id=wx.ID_ANY)
        self.buttonQuit = wx.Button(self,label="Quitter",id=wx.ID_ANY)
        # self.parent.Bind(wx.EVT_CHAR,self.onTabLogin,source=self.loginEntry)
        # self.parent.Bind(wx.EVT_COMMAND_TEXT_ENTER,self.onEnterServeur,source=self.serveurEntry)
        # self.parent.Bind(wx.EVT_CHAR,self.onTabServeur,source=self.serveurEntry)
        loginLabel = wx.StaticText(self,label="Pseudo :")
        serveurLabel = wx.StaticText(self,label="Adresse du serveur : ")
        spanTwoCol = wx.GBSpan(1,2)
        box.Add(loginLabel,wx.GBPosition(1,1))
        box.Add(serveurLabel,wx.GBPosition(2,1))
        box.Add(self.loginEntry,wx.GBPosition(1,2))
        box.Add(self.serveurEntry,wx.GBPosition(2,2))
        box.Add(self.buttonQuit,wx.GBPosition(3,1))
        box.Add(self.buttonEnter,wx.GBPosition(3,2))
        # bbox.Add(box,proportion=1)
        # bbox.SetSizeHints(self)
        # self.Add(box,proportion=1)
        #self.SetSizeHints(self)
        #self.SetSizer(bbox)
        self.logiEntry.Bind(wx.EVT_COMMAND_TEXT_ENTER,self.onEnter)
        self.buttonQuit.Bind(wx.EVT_BUTTON,self.onQuit, 
                  id=self.buttonQuit.GetId())
        self.buttonEnter.Bind(wx.EVT_TOGGLEBUTTON,self.onConnect, 
                  id=self.buttonEnter.GetId())
        self.SetSizer(box)
        self.Show()

    def onQuit(self,event):
        event.Skip()

    def onConnect(self,event):
        self.buttonEnter.SetValue(True)
        self.buttonEnter.Disable()
        event.Skip()
        #self.Close()
     
    def onEnter(self,e):
        if self.loginEntry.GetValue() <> "" and self.serveurEntry.GetValue() <> "":
            self.onConnect(None)
        

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
            
