#!/usr/bin/python
# -*- coding: utf-8 -*-
# client/controler.py

import view
import wx
import re
import time
import threading
from mysocket import Socket


View = view.View

onButEvt = threading.Event()
finishWaitEvt = threading.Event()
chatEvt = threading.Event()


myEVT_UPDATE_BAR = wx.NewEventType()
EVT_UPDATE_BAR = wx.PyEventBinder(myEVT_UPDATE_BAR, 1)

myEVT_SOCKET = wx.NewEventType()
EVT_SOCKET = wx.PyEventBinder(myEVT_SOCKET, 1)

myEVT_UPDATE_CHAT = wx.NewEventType()
EVT_UPDATE_CHAT = wx.PyEventBinder(myEVT_UPDATE_CHAT, 1)

class myEvent(wx.PyCommandEvent):
    def __init__(self, etype, eid, value=None):
        wx.PyCommandEvent.__init__(self, etype, eid)
        self._value = value

    def GetValue(self):
        return self._value

    def SetValue(self,val):
        self._value = val


class Chat(threading.Thread):
    def __init__(self,parent,sock):
        threading.Thread.__init__(self)
        self.parent = parent
        self.sock = sock

    def run(self):
        # self.sock.settimeout(1)
        while chatEvt.is_set():
            try :
                l = self.sock.readline().next()
                # l=self.sock.recv(4096)
                listPlayers = re.split("(?<!\\\)/",l)
                if listPlayers[0]=="HEYLISTEN":
                    print "chat : "+str(listPlayers)
                    e = myEvent(myEVT_UPDATE_CHAT,-1,listPlayers)
                    wx.PostEvent(self.parent,e)
            except:
                pass
        # self.sock.settimeout(None)

class WaitPlayers(threading.Thread):
    def __init__(self,parent,sock):
        threading.Thread.__init__(self)
        self.parent = parent
        self.sock = sock
    
    def run(self):
        e = myEvent(myEVT_UPDATE_BAR,-1,"En attente des joueurs")
        wx.PostEvent(self.parent,e)
        l = self.sock.readline().next()
        #l=self.sock.recv(4096)
        #self.sock.flush()
        print str(l)
        listPlayers = re.split("(?<!\\\)/",l)
        while listPlayers[0]!="PLAYERS":

            if listPlayers[0]=="HEYLISTEN":
                print "waitPlayers : "+str(listPlayers)
                e = myEvent(myEVT_UPDATE_CHAT,-1,listPlayers)
                wx.PostEvent(self.parent,e)
            else:
                print listPlayers
            l = self.sock.readline().next()
            # l=self.sock.recv(4096)
            listPlayers = re.split("(?<!\\\)/",l)
        print "waitPlayers2 :" + str(listPlayers)
        # l = self.sock.readline().next()
        # listPlayers = re.split("(?<!\\\)/",l)
        # print listPlayers
        value = listPlayers[1:-1]
        finishWaitEvt.set()
        e.SetValue("La partie va débuté")
        wx.PostEvent(self.parent,e)
        evt = myEvent(myEVT_SOCKET,-1,value)
        wx.PostEvent(self.parent,evt)
        

class PutShip(threading.Thread):
    def __init__(self,parent,sock):
        threading.Thread.__init__(self)
        self.parent = parent
        self.sock = sock
        
    def run(self):
        finishWaitEvt.wait()
        # rep = self.sock.recv(4096)
        rep = self.sock.readline().next()
        l = re.split("(?<!\\\)/",rep)

        while l[0] <> 'ALLYOURBASE':
            print "putShip :"+str(l)
            if l[0] == 'SHIP':
                n = int(l[1])
                e = myEvent(myEVT_UPDATE_BAR,-1,"Placez un sous-marin de taille "+str(n))
                wx.PostEvent(self.parent,e)
                pos = []
                self.parent.count=1

                chatEvt.set()
                c = Chat(self.parent,self.sock)
                c.start()

                for i in range(n):
                    onButEvt.wait()
                    onButEvt.clear()
                    pos.append(self.parent.currentBut)
                    print pos
                
                chatEvt.clear()
                self.parent.count=0
                print pos
                self.sock.send('PUTSHIP/'+''.join(pos)+'\n')
                print "ici"
                # rep = self.sock.recv(4096)
                rep = self.sock.readline().next()
                l = re.split("(?<!\\\)/",rep)
                if l[0] != 'OK':
                    print "pas content"
                    # self.parent.resetBmp(pos)
                    e = myEvent(myEVT_UPDATE_BAR,-1,"Mauvais Placement. Placez un sous-marin de taille "+str(n))
                    wx.PostEvent(self.parent,e)
                    continue
                if l[0] == "HEYLISTEN":
                    e = myEvent(myEVT_UPDATE_CHAT,-1,l)
                    wx.PostEvent(self.parent,e)
                    
            if l[0] == "HEYLISTEN":
                    e = myEvent(myEVT_UPDATE_CHAT,-1,l)
                    wx.PostEvent(self.parent,e)
            
            self.sock.settimeout(None)
            # rep = self.sock.recv(4096)
            rep = self.sock.readline().next()
            l = re.split("(?<!\\\)/",rep)
                
        self.parent.pos.append(pos)
        #finishEvt.set()
        

class Controler(wx.Frame):
    
    def __init__(self,sock,name):
        super(Controler,self).__init__(None,title="Battle Royale",size=(800,600))
        self.viou = View(self)
        self.Bind(wx.EVT_BUTTON,self.onBut)
        #sock.setblocking(0)
        self.sock = sock
        self.players=[]
        self.oldBut=""
        self.currentBut = ""
        self.count=0
        self.b = None
        self.pos =[]

        self.SetStatusBar(wx.StatusBar(self))
        self.SetStatusText("En attente du serveur")

        self.Bind(EVT_UPDATE_BAR,(lambda e: self.SetStatusText(e.GetValue())))

        self.Centre()
        self.Show()
                
        self.Bind(EVT_SOCKET,self.waitPlayers)
        
        self.Bind(wx.EVT_TEXT_ENTER,self.onEnter,source=self.viou.chatEntry)
        self.Bind(wx.EVT_BUTTON,self.refreshChat,source=self.viou.sendButton)
        self.Bind(EVT_UPDATE_CHAT,self.refreshChat)

        finishEvt.clear()

        wp = WaitPlayers(self,self.sock)
        wp.start()

        self.putShip()
        

    def waitPlayers(self,evt):
        self.players = evt.GetValue()
        

    def putShip(self):
        finishEvt.clear()
        t = PutShip(self,self.sock)
        t.start()


    def onBut(self,e):
        if self.count != 0:
            self.b = e.GetEventObject()
            self.b.SetBitmapLabel(self.viou.subBmp)
            self.currentBut = e.GetEventObject().GetName()
            onButEvt.set()
            

    def resetBmp(self,pos):
        for name in pos:
            l = re.split("(?<!\\\)/",name)
            self.viou.buts[16+int(l[0])*15+ord(l[1])-ord('A')].SetBitmapLabel(self.viou.seaBmp)

    def onEnter(self,e):
        mes = self.viou.chatEntry.GetValue()
        self.viou.chatEntry.SetValue("")
        self.sock.send("TALK/"+mes+"/\n")

    def refreshChat(self,e):
        l = e.GetValue()
        print "refreshChat"+str(l)
        self.viou.chatAll.AppendText(l[1]+" : "+l[2]+"\n")

if __name__ == '__main__':
    app = wx.App()
    Controler(None,None,"")
    app.MainLoop()
