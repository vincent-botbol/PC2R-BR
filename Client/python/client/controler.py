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
mouseRightClickEvt = threading.Event()
resEvt = threading.Event()

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

myEVT_DEATH = wx.NewEventType()
EVT_DEATH = wx.PyEventBinder(myEVT_DEATH,1)

myEVT_RM_DRONE = wx.NewEventType()
EVT_RM_DRONE = wx.PyEventBinder(myEVT_RM_DRONE,1)

myEVT_REFRESH_BUT = wx.NewEventType()
EVT_REFRESH_BUT = wx.PyEventBinder(myEVT_REFRESH_BUT,1)

myEVT_RELAUNCH = wx.NewEventType()
EVT_RELAUNCH = wx.PyEventBinder(myEVT_RELAUNCH,1)

myEVT_FINISH = wx.NewEventType()
EVT_FINISH = wx.PyEventBinder(myEVT_FINISH,1)

myEVT_NEW_CLIENT = wx.NewEventType()
EVT_NEW_CLIENT = wx.PyEventBinder(myEVT_NEW_CLIENT,1)

myEVT_MAKE_DIALOG = wx.NewEventType()
EVT_MAKE_DIALOG = wx.PyEventBinder(myEVT_MAKE_DIALOG,1)

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
                # print "appel du chat"
                l = self.sock.readline()
                # l=self.sock.recv(4096)
                listPlayers = re.split("(?<!\\\)/",l)
                if listPlayers[0]=="HEYLISTEN":
                    # self.sock.Ok()
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
                
                # print "waitPlayers : "+str(listPlayers)
                e = myEvent(myEVT_UPDATE_CHAT,-1,listPlayers)
                wx.PostEvent(self.parent,e)
            else:
                print "ceci ne devrait pas arrivé"
            l = self.sock.readline()
            # l=self.sock.recv(4096)
            listPlayers = re.split("(?<!\\\)/",l)
        #
        # print "waitPlayers2 :" + str(listPlayers)
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
        # print "appel du putship"
        rep = self.sock.readline()
        l = re.split("(?<!\\\)/",rep)
        # print l 
        while l[0] <> 'ALLYOURBASE':
            # print "putShip :"+str(l)
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
                    # print pos
                
                #chatEvt.clear()
                self.parent.count=0
                print "PUTSHIP/"+''.join(pos)+'\n'
                self.sock.send('PUTSHIP/'+''.join(pos)+'\n')
                # print "ici"
                # rep = self.sock.recv(4096)
                #print "ici"
                rep = self.sock.readline()
                # print self.sock.readline()
                l = re.split("(?<!\\\)/",rep)
                #print "recue apres placement :  "+str(l)
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
                
        # self.parent.pos.append(pos)
        e=myEvent(myEVT_UPDATE_BAR,-1,"En attente de votre tour")
        wx.PostEvent(self.parent,e)        
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
         while ((l[0] <> 'AWINNERIS') and (l[0] <> 'DRAWGAME')):
             if l[0] == "HEYLISTEN":
                 e = myEvent(myEVT_UPDATE_CHAT,-1,l)
                 wx.PostEvent(self.parent,e) 
             if l[0] == 'OUCH':
                 e = myEvent(myEVT_OUCH, -1, l)
                 wx.PostEvent(self.parent,e)
                 e=myEvent(myEVT_UPDATE_BAR,-1,"OUCH! Vous êtes touché")
                 wx.PostEvent(self.parent,e)
             if l[0] == 'DEATH':
                 e = myEvent(myEVT_DEATH, -1, l)
                 wx.PostEvent(self.parent,e)
                 e=myEvent(myEVT_UPDATE_BAR,-1,l[1]+" est mort :'(")
                 wx.PostEvent(self.parent,e)
             if l[0] == 'YOURTURN':
                 x = int(l[1])
                 y = l[2]
                 a = int(l[3])
                 action=[]
                 actif=True
                 e = myEvent(myEVT_REFRESH_DRONE,-1,(l,actif))
                 wx.PostEvent(self.parent,e)
                 e=myEvent(myEVT_UPDATE_BAR,-1,"A votre tour de jouer")
                 wx.PostEvent(self.parent,e)
                 self.parent.count = 2
                 while(a>0):
                     onButEvt.wait()
                     onButEvt.clear()
                     if  mouseRightClickEvt.is_set():
                         mouseRightClickEvt.clear()
                         break
                     but = re.split("(?<!\\\)/",self.parent.currentBut)
                     if int(but[0]) == x and but[1] == y:
                         if actif :
                             # print "activation du laser"
                             action.append('E/')
                             actif=False
                             a = a-1
                         else:
                             # print "passage de tour"
                             break
                     else :
                         dx = int(but[0])-x
                         dy = ord(but[1])-ord(y)
                         if dx == abs(dx):
                             action.append('R/'*dx)
                         else:
                             action.append('L/'*abs(dx))
                         if dy == abs(dy):
                             action.append('U/'*dy)
                         else:
                             action.append('D/'*abs(dy))
                         # print "on a bougé et a vaut : "+str(a)
                         a -= abs(dx)+abs(dy)
                         x=int(but[0])
                         y=but[1]
                     # print "a enfin de boucle : "+str(a)
                     if a<=0:
                          break
                     e=myEvent(myEVT_RM_DRONE,-1,None)
                     wx.PostEvent(self.parent,e)
                     e=myEvent(myEVT_REFRESH_DRONE,-1,(['',str(x),y,str(a)],actif))
                     wx.PostEvent(self.parent,e)
                 self.parent.count = 0
                 e=myEvent(myEVT_RM_DRONE,-1,None)
                 wx.PostEvent(self.parent,e)
                 action.append('\n')
                 print "ACTION/"+''.join(action)
                 self.sock.send('ACTION/'+''.join(action))
                 rep = self.sock.readline()
                 l=re.split("(?<!\\\)/",rep)
                 # print l
                 e=myEvent(myEVT_UPDATE_BAR,-1,"En attente de votre tour")
                 wx.PostEvent(self.parent,e)
                 while l[0] == "HEYLISTEN":
                     e = myEvent(myEVT_UPDATE_CHAT,-1,l)
                     wx.PostEvent(self.parent,e)
                     rep = self.sock.readline()
                     l=re.split("(?<!\\\)/",rep)
                 but = self.parent.viou.buts[int(l[1])+(ord('P')-ord(l[2]))*16]
                 if l[0] =='MISS':
                     e = myEvent(myEVT_REFRESH_BUT,-1,(but,mybuttons.MISS))
                     wx.PostEvent(self.parent,e)
                     e=myEvent(myEVT_UPDATE_BAR,-1,"Vous avez raté")
                     wx.PostEvent(self.parent,e)
                 elif l[0] =='TOUCHE':
                     e = myEvent(myEVT_REFRESH_BUT,-1,(but,mybuttons.TOUCHE))
                     wx.PostEvent(self.parent,e)
                     e=myEvent(myEVT_UPDATE_BAR,-1,"Vous avez touché")
                     wx.PostEvent(self.parent,e)
                 else:
                     continue
             rep = self.sock.readline()
             l = re.split("(?<!\\\)/",rep)
         if l[0] =='AWINNERIS':
             e=myEvent(myEVT_UPDATE_BAR,-1,"Le gagnat est : "+l[1]+" !")
             wx.PostEvent(self.parent,e)
         if l[0] =='DRAWGAME':
             e=myEvent(myEVT_UPDATE_BAR,-1,"Il y a égalité !")
             wx.PostEvent(self.parent,e)
         e=myEvent(myEVT_MAKE_DIALOG,-1,"Voulez vous rejouer ?")
         wx.PostEvent(self.parent,e)
         resEvt.wait()
         resEvt.clear()
         res = self.parent.res
         if res == wx.ID_YES:
             self.sock.send("PLAYAGAIN/\n")
             print "PLAYAGAIN/\n"
             e=myEvent(myEVT_RELAUNCH,-1,"")
             wx.PostEvent(self.parent,e)
         else :
             self.sock.send("BYE/\n")
             e=myEvent(myEVT_FINISH,-1,"")
             wx.PostEvent(self.parent,e)
                 

class Controler(wx.Frame):
    
    def __init__(self,pere,sock,name):
        super(Controler,self).__init__(None,title="Battle Royale",size=(1200,800))
        self.pere = pere
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
        self.res = None
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
        self.Bind(EVT_REFRESH_DRONE,self.drawDrone)
        self.Bind(EVT_RM_DRONE,self.removeDrone)
        self.Bind(wx.EVT_KEY_DOWN,self.onRightClick)
        self.Bind(EVT_REFRESH_BUT,self.refreshBut)
        self.Bind(EVT_DEATH,self.death)
        self.Bind(EVT_MAKE_DIALOG,self.makedialog)
        self.Bind(wx.EVT_KEY_UP,self.onRightClick)
        self.Bind(wx.EVT_CHAR,self.onRightClick)

        self.Bind(EVT_FINISH,lambda e: e.Destroy())
        self.Bind(EVT_RELAUNCH,self.relaunch)
        self.Bind(wx.EVT_CLOSE,lambda e: e.Skip())

        self.viou.SetFocus()
        #self.mask = wx.Bitmap("img/mask.jpg",wx.BITMAP_TYPE_JPEG)
        #self.unmask = wx.Bitmap("img/unmask.jpg",wx.BITMAP_TYPE_JPEG)

        finishEvt.clear()
        finishWaitEvt.clear()
        finishPutShipEvt.clear()
        onButEvt.clear()

        wp = WaitPlayers(self,self.sock)
        wp.start()
        
        t = PutShip(self,self.sock)
        t.start()

        a = Action(self,self.sock)
        a.start()
    
    def makedialog(self,e):
        dial = wx.MessageDialog(self,e.GetValue(),"Info",wx.YES_NO)
        self.res = dial.ShowModal()
        resEvt.set()

    def relaunch(self,e):
        e = myEvent(myEVT_NEW_CLIENT,-1,"")
        wx.PostEvent(self.pere,e)

    def death(self,e):
        pd = (e.GetValue())[1]
        l=[]
        for p in self.players:
            if p == pd:
                l.append(p+" (RIP)\n")
            else:
                l.append(p+"\n")
        self.viou.playerInfo.SetLabel(''.join(l))
             
                

    def refreshBut(self,e):
        (but,flag)=e.GetValue()
        but.addFlag(flag)
        # print "flag : "+str(but.flag)
        but.changeBMP()
        # print "bitmap OK"

    def onRightClick(self,e):
        if self.count == 2 and e.GetUniChar() == ord('S'):
            mouseRightClickEvt.set()
            onButEvt.set()

    def drawDrone(self,e):
        (l,actif) = e.GetValue()
        x = int(l[1])
        y = l[2]
        a = int(l[3])
        self.pos = []
        if actif :
            self.pos.append(x+(ord('P')-ord(y))*16)
            but = self.viou.buts[x+(ord('P')-ord(y))*16]
            but.SetFocus()
            but.addFlag(mybuttons.RED)
            but.changeBMP()
        for i in range(-a,a+1):
            u=x+i
            if u>=0 and u <=15:
                for j in range(-a,a+1):
                    v=ord(y)+j
                    if v >=ord('A') and v<=ord('P'):
                        d = abs(x-u)+abs(ord(y)-v)
                        if (d <> 0 and d <=a):
                            self.pos.append(u+(ord('P')-v)*16)
                            but = self.viou.buts[u+(ord('P')-v)*16]
                            but.addFlag(mybuttons.GREEN)
                            but.changeBMP()
    
    def removeDrone(self,e):
        for p in self.pos :
            but = self.viou.buts[p]
            but.removeFlag(mybuttons.RED|mybuttons.GREEN)
            but.changeBMP()
        self.pos = []

        
    def miss(self,e):
        l = e.GetValue()
        x = int(l[1])
        y = l[2]
        but = self.viou.buts[x+(ord('P')-ord(y))*16]
        but.addFlag(mybuttons.MISS)
        but.changeBMP()

    def touche(self,e):
        l = e.GetValue()
        x = int(l[1])
        y = l[2]
        but = self.viou.buts[x+(ord('P')-ord(y))*16]
        but.addFlag(mybuttons.TOUCHE)
        but.changeBMP()
        
    def ouch(self,e):
        l = e.GetValue()
        x = int(l[1])
        y = l[2]
        but = self.viou.buts[x+(ord('P')-ord(y))*16]
        but.addFlag(mybuttons.OUCH)
        but.changeBMP()

    def waitPlayers(self,evt):
        self.players = evt.GetValue()
        l=[]
        for p in self.players:
            l.append(p+'\n')
        self.viou.playerInfo.SetLabel(''.join(l))

    def onBut(self,e):
        if self.count == 1:
            self.b = e.GetEventObject()
            #self.b.SetBitmapLabel(self.viou.subBmp)
            self.b.changeFlag(mybuttons.SUBM)
            self.b.changeBMP()
            self.currentBut = e.GetEventObject().GetName()
            onButEvt.set()
        if self.count == 2:
            self.b = e.GetEventObject()
            self.currentBut = e.GetEventObject().GetName()
            onButEvt.set()

    def resetBmp(self,pos):
        for name in pos:
            l = re.split("(?<!\\\)/",name)
            # print l
            # print int(l[0])+(ord('P')-ord(l[1]))*16
            but = self.viou.buts[int(l[0])+(ord('P')-ord(l[1]))*16]
            but.changeFlag(mybuttons.SEA)
            but.changeBMP()

    def onEnter(self,e):
        mes = self.viou.chatEntry.GetValue()
        self.viou.chatEntry.SetValue("")
        self.sock.send("TALK/"+mes+"/\n")

    def refreshChat(self,e):
        l = e.GetValue()
        # print "refreshChat"+str(l)
        self.viou.chatAll.AppendText(l[1]+" : "+l[2]+"\n")

if __name__ == '__main__':
    app = wx.App()
    Controler(None,None,"")
    app.MainLoop()
