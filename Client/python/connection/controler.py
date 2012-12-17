#!/usr/bin/python
# -*- coding: utf-8 -*-
# connection/controler.py

import wx
import view
import mysocket
import re

View = view.View

class Controler(wx.Frame):
    
    def __init__(self,parent):
        super(Controler,self).__init__(None,wx.ID_ANY,"connection",size=(300,200))
        self.name=None
        self.address = None
        self.sock = mysocket.Socket(mysocket.AF_INET,mysocket.SOCK_STREAM)
        self.parent = parent
        self.viou = View(self)
        #self.SetSizer(self.viou)
        self.Bind(wx.EVT_TOGGLEBUTTON, self.onConnect,source=self.viou.buttonEnter)
        self.Bind(wx.EVT_BUTTON, self.onQuit,source=self.viou.buttonQuit)
        #self.Fit()
        #self.Centre()
        self.Show()

    def onConnect(self,event):
        self.name = self.viou.loginEntry.GetValue()
        self.name = re.sub(r'\\',r'\\\\',self.name)
        self.name = re.sub('/','\/',self.name)
        self.address = self.viou.serveurEntry.GetValue()
        try :
            self.address = mysocket.gethostbyname(self.address)
            self.sock.connect((self.address,2012))
            mdp = self.viou.registerEntry.GetValue()
            if mdp <> "":
                dial=wx.MessageDialog(self,"Etes vous déjà inscrit(e) ?","Info",wx.YES_NO)
                if dial.ShowModal() == wx.ID_YES:
                    self.sock.send("LOGIN/"+self.name+"/"+mdp+"/\n")
                else:
                    self.sock.send("REGISTER/"+self.name+"/"+mdp+"/\n")
            else :
                self.sock.send("CONNECT/"+self.name+"/\n")
            # print self.sock.recv(4096)
        except:
            # print e.strerror
            dial = wx.MessageDialog(self,
                                    "Erreur de connection, le serveur est il lancé ?"
                                    ,"Error",wx.CANCEL)
            dial.ShowModal()
            self.sock.shutdown(mysocket.SHUT_RDWR)
            self.sock.close()
            self.viou.buttonEnter.SetValue(False)
            self.viou.buttonEnter.Enable()
        event.Skip()

    def onQuit(self,event):
        event.Skip()
    

if __name__ == '__main__':
    app = wx.App()
    frame = Controler(None)
    frame.Show()
    app.MainLoop()
