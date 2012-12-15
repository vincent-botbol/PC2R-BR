#!/usr/bin/python
# -*- coding: utf-8 -*-
# client/controler.py

import view
import wx
import re
import time
import threading
import mysocket
import mybuttons

View = view.View

onButEvt = threading.Event()
finishWaitEvt = threading.Event()
chatEvt = threading.Event()
finishPutShipEvt = threading.Event()
finishEvt = threading.Event()

myEVT_UPDATE_BAR = wx.NewEventType()
EVT_UPDATE_BAR = wx.PyEventBinder(myEVT_UPDATE_BAR, 1)

myEVT_SOCKET = wx.NewEventType()
EVT_SOCKET = wx.PyEventBinder(myEVT_SOCKET, 1)

myEVT_UPDATE_CHAT = wx.NewEventType()
EVT_UPDATE_CHAT = wx.PyEventBinder(myEVT_UPDATE_CHAT, 1)

myEVT_TOUCHE = wx.NewEventType()
EVT_TOUCHE = wx.PyEventBinder(myEVT_TOUCHE, 1)

myEVT_MISS = wx.NewEventType()
EVT_MISS = wx.PyEventBinder(myEVT_MISS, 1)

myEVT_OUCH = wx.NewEventType()
EVT_OUCH = wx.PyEventBinder(myEVT_OUCH, 1)

myEVT_REFRESH_DRONE = wx.NewEventType()
EVT_REFRESH_DRONE = wx.PyEventBinder(myEVT_REFRESH_DRONE,1)

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
                print "appel du chat"
                l = self.sock.readline()
                # l=self.sock.recv(4096)
                listPlayers = re.split("(?<!\\\)/",l)
                if listPlayers[0]=="HEYLISTEN":
                    self.sock.Ok()
                    print "chat : "+str(listPlayers)
                    e = myEvent(myEVT_UPDATE_CHAT,-1,listPlayers)
                    wx.PostEvent(self.parent,e)
                else:
                    self.sock.reput(l)
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
        l = self.sock.readline()
        
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
            l = self.sock.readline()
            # l=self.sock.recv(4096)
            listPlayers = re.split("(?<!\\\)/",l)
        #
        print "waitPlayers2 :" + str(listPlayers)
        # l = self.sock.readline()
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
        # print "ici"
        # rep = self.sock.recv(4096)
        print "appel du putship"
        rep = self.sock.readline()
        l = re.split("(?<!\\\)/",rep)
        # print l 
        while l[0] <> 'ALLYOURBASE':
            print "putShip :"+str(l)
            if l[0] == 'SHIP':
                
                n = int(l[1])
                e = myEvent(myEVT_UPDATE_BAR,-1,"Placez un sous-marin de taille "+str(n))
                wx.PostEvent(self.parent,e)
                pos = []
                self.parent.count=1

                # chatEvt.set()
                # c = Chat(self.parent,self.sock)
                # c.start()

                for i in range(n):
                    onButEvt.wait()
                    onButEvt.clear()
                    pos.append(self.parent.currentBut)
                    print pos
                
                chatEvt.clear()
                self.parent.count=0
                print pos
                self.sock.send('PUTSHIP/'+''.join(pos)+'\n')
                # print "ici"
                # rep = self.sock.recv(4096)
                print "ici"
                rep = self.sock.readline()
                # print self.sock.readline()
                l = re.split("(?<!\\\)/",rep)
                print "recue apres placement :  "+str(l)
                while l[0] != 'OK':
                    if l[0] == 'WRONG':                        
                        # print "pas content"
                        self.parent.resetBmp(pos)
                        e = myEvent(myEVT_UPDATE_BAR,-1,"Mauvais Placement. Placez un sous-marin de taille "+str(n))
                        wx.PostEvent(self.parent,e)
                        break
                    if l[0] == "HEYLISTEN":
                        e = myEvent(myEVT_UPDATE_CHAT,-1,l)
                        wx.PostEvent(self.parent,e)
                    rep = self.sock.readline()
                    l = re.split("(?<!\\\)/",rep)
                
            if l[0] == "HEYLISTEN":
                
                e = myEvent(myEVT_UPDATE_CHAT,-1,l)
                wx.PostEvent(self.parent,e)
            
            # self.sock.settimeout(None)
            # rep = self.sock.recv(4096)
            rep = self.sock.readline()
            l = re.split("(?<!\\\)/",rep)
                
        self.parent.pos.append(pos)
        finishPutShipEvt.set()
        

class Action(threading.Thread):
     def __init__(self,parent,sock):
        threading.Thread.__init__(self)
        self.parent = parent
        self.sock = sock

     def run(self):
         finishPutShipEvt.wait()
         rep = self.sock.readline()
         l = re.split("(?<!\\\)/",rep)
         
         while ((l[0] <> 'AWINNERIS') or (l[0] <> 'DRAWGAME')):
             if l[0] == "HEYLISTEN":
                 
                 e = myEvent(myEVT_UPDATE_CHAT,-1,l)
                 wx.PostEvent(self.parent,e) 
                 
             if l[0] == 'OUCH':
                 
                 e = myEvent(myEVT_OUCH, -1, l)
                 wx.PostEvent(self.parent,e)

             if l[0] == 'YOURTURN':
                 
                 e = myEvent(myEVT_REFRESH_DRONE,-1,l)
                 wx.PostEvent(self.parent,e)
                 break

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
        self.Bind(EVT_TOUCHE,self.touche)
        self.Bind(EVT_MISS,self.miss)
        self.Bind(EVT_OUCH,self.ouch)
        self.Bind(EVT_REFRESH_DRONE,self.refreshDrone)


        #self.mask = wx.Bitmap("img/mask.jpg",wx.BITMAP_TYPE_JPEG)
        #self.unmask = wx.Bitmap("img/unmask.jpg",wx.BITMAP_TYPE_JPEG)

        finishEvt.clear()
        finishWaitEvt.clear()
        finishPutShipEvt.clear()

        wp = WaitPlayers(self,self.sock)
        wp.start()
        
        t = PutShip(self,self.sock)
        t.start()

        a = Action(self,self.sock)
        a.start()
    
    def refreshDrone(self,e):
        l = e.GetValue()
        x = int(l[1])
        y = l[2]
        a = int(l[3])
        but = self.viou.buts[x+(ord('P')-ord(y))*16]
        but.changeBMP(mybuttons.SEA|mybuttons.RED)
        
    def miss(self,e):
        pass

    def touche(self,e):
        pass
        
    def ouch(self,e):
        pass

    def waitPlayers(self,evt):
        self.players = evt.GetValue()
        

    def putShip(self):
        #finishEvt.clear()
        t = PutShip(self,self.sock)
        t.start()


    def onBut(self,e):
        if self.count != 0:
            self.b = e.GetEventObject()
            #self.b.SetBitmapLabel(self.viou.subBmp)
            self.b.changeBMP(mybuttons.SUBM)
            self.currentBut = e.GetEventObject().GetName()
            onButEvt.set()
            

    def resetBmp(self,pos):
        for name in pos:
            l = re.split("(?<!\\\)/",name)
            print l
            print int(l[0])+(ord('P')-ord(l[1]))*16
            self.viou.buts[int(l[0])+(ord('P')-ord(l[1]))*16].changeBMP(mybuttons.SEA)

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
