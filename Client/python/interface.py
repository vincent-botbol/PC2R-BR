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
        chatBox = wx.BoxSizer(wx.VERTICAL)
        self.chatEntry = wx.TextCtrl(self,value="Tapez le texte ici",
                                style=wx.TE_LEFT|wx.TE_PROCESS_ENTER)
        self.tc=wx.TextCtrl(self,value="Ceci est un test",
                       style=wx.TE_MULTILINE|wx.TE_READONLY|
                       wx.TE_LEFT|wx.TE_BESTWRAP)
        tc.AppendText("un peu de test encore")
        gs=wx.GridSizer(16,16,0,0)
        for i in range(16*16) :
            gs.Add(wx.Button(self,label=str(i)),0,wx.EXPAND)
        vbox.Add(gs, proportion=1, flag=wx.EXPAND)
        chatBox.Add(tc, proportion=1,flag=wx.EXPAND)
        chatBox.Add(chatEntry, proportion=0,flag=wx.EXPAND)
        vbox.Add(chatBox, proportion=1,flag=wx.EXPAND)
        self.SetSizer(vbox)
        self.Bind(wx.EVT_COMMAND_TEXT_ENTER,self.chatRelease)
    
#    def onClicked()
#    def chatRelease(self,e):     
#        e.Skip()

# Créer une classe pour le chat = un nouveau widget

if __name__ == '__main__':
    app = wx.App()
    InterfaceTest(None, title='Test')
    app.MainLoop()
