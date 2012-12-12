#!/usr/bin/python
# -*- coding: utf-8 -*-
# client/view.py

import wx
import wx.lib.stattext
import wx.lib.buttons
import os

#img = wx.Image("../img/sea.jpg")


class View(wx.Panel):
    def __init__(self,parent):
        super(View, self).__init__(parent)
        print os.getcwd()
        self.seaBmp = wx.Bitmap("img/sea.jpg",wx.BITMAP_TYPE_JPEG)
        self.subBmp =  wx.Bitmap("img/submarin.jpeg",wx.BITMAP_TYPE_JPEG)
        self.buts = []
        self.count = 0
        self.pos = None

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
                but = wx.BitmapButton(self,wx.ID_ANY,self.seaBmp
                                      ,name=str(i)+'/'+str(chr(ord('A')+j))+'/'
                                      ,style=wx.BORDER_NONE)
                but.Bind(wx.EVT_BUTTON,parent.onBut)
                but.SetBitmapLabel(self.seaBmp)
                #but.SetMargins(0,0)
                #but = wx.StaticBitmap(self,wx.ID_ANY,self.bmp)
                #but.Bind(wx.EVT_SET_FOCUS,self.onFocus)
                self.buts.append(but)
                gs.Add(but,proportion=2,flag=wx.EXPAND)

        bs.Add(gs,proportion=2,flag=wx.EXPAND|wx.LEFT)
        
        vbox=wx.BoxSizer(wx.VERTICAL)
        vbox.AddStretchSpacer(prop=1)

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
        vbox.AddStretchSpacer(prop=1)


        # self.Bind(wx.EVT_SIZE,self.onResize) 
        # self.onResize(None)

        bs.Add(vbox,proportion=1,flag=wx.EXPAND|wx.RIGHT)
        self.SetSizer(bs)

        self.Bind(wx.EVT_TEXT_ENTER,self.onEnter)

        self.Show()
        # self.Bind(wx.EVT_KEY_DOWN,onPressKey)    
            

    # def onPressKey(self,event):
    #     win = FindFocus()
    #     if win.__class__ = wx.Button:
    #         i = wing.GetId()
    #         if i < 16*16 and i > 0 :
    #             kc = event.GetKeyCode()
    #             if kc = WXK_UP
                
        
    # def onResize(self,e):
    #     (w,h) = self.buts[0].GetSizeTuple()
    #     self.img.Rescale(w,h)
    #     bmp = wx.BitmapFromImage(self.img)
    #     for b in self.buts:
    #         b.SetBitmapLabel(bmp)

    # def onFocus(self,e):
    #     print e.GetWindow().GetName()

    def onEnter(self,e):
        e.Skip()

    def onBut(self,e):
        e.GetEventObject().SetBitmapLabel(self.subBmp)
        e.Skip()
