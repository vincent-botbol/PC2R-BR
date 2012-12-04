#!/usr/bin/python
# -*- coding: utf-8 -*-
# client/view.py

import wx
import chat
import wx.lib.newevent
import wx.lib.stattext

class View(wx.Panel):
    def __init__(self,parent):
        super(View, self).__init__(parent)
        self.buts = []
        bs=wx.BoxSizer(wx.HORIZONTAL)
        gs=wx.GridSizer(17,17,0,0)
        gs.AddStretchSpacer()
        for i in range(16):
            st = wx.lib.stattext.GenStaticText(self,wx.ID_ANY,str(chr(ord('A')+i)),style=wx.ALIGN_CENTRE)
            gs.Add(st,proportion=1,flag=wx.ALIGN_CENTRE)
        for i in range(16) :
            st = wx.lib.stattext.GenStaticText(self,wx.ID_ANY,str(15-i),style=wx.ALIGN_CENTRE)
            gs.Add(st,proportion=1,flag=wx.ALIGN_CENTRE)
            for j in range(16):
                but = wx.Button(self,id=i,label='('+str(i)+','+str(j)+')')
                but.Bind(wx.EVT_BUTTON,self.onBut)
                self.buts.append(but)
                gs.Add(but,proportion=1,flag=wx.EXPAND)
        bs.Add(gs,proportion=1,flag=wx.EXPAND|wx.LEFT)
        
        vbox=wx.BoxSizer(wx.VERTICAL)
        box = wx.BoxSizer(wx.HORIZONTAL)
        self.chatEntry = wx.TextCtrl(self,style=wx.TE_LEFT
                                     |wx.TE_PROCESS_ENTER)
        self.chatAll = wx.TextCtrl(self,style=wx.TE_MULTILINE
                                   |wx.TE_READONLY|wx.TE_LEFT
                                   |wx.TE_BESTWRAP)
        self.sendButton = wx.Button(self,id=wx.ID_OK)
        box.Add(self.chatEntry,proportion=1)
        box.Add(self.sendButton,proportion=1)
        
        self.playerInfo = wx.StaticText(self,wx.ID_ANY,"Aucun joueur")
        
        vbox.Add(self.playerInfo,proportion=1,flag=wx.EXPAND|wx.CENTRE)
        vbox.Add(self.chatAll,proportion=1,flag=wx.EXPAND|wx.CENTRE)
        vbox.Add(box,proportion=0,flag=wx.EXPAND|wx.CENTRE)

        
        bs.Add(vbox,proportion=1,flag=wx.EXPAND|wx.RIGHT)
        self.SetSizer(bs)
        self.Show()
        # self.Bind(wx.EVT_KEY_DOWN,onPressKey)

    # def onPressKey(self,event):
    #     win = FindFocus()
    #     if win.__class__ = wx.Button:
    #         i = wing.GetId()
    #         if i < 16*16 and i > 0 :
    #             kc = event.GetKeyCode()
    #             if kc = WXK_UP
                
        


    def onBut(self,e):
        e.Skip()
