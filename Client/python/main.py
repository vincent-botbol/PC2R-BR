#!/usr/bin/python
# -*- coding: utf-8 -*-

import wx
import client
import gevent.socket
import connection

class  Main(wx.App):
    
    def OnInit(self):
        self.con = connection.Controler(None)
        self.cli = client.Controler()
        return True

    def onConnect(self,e):
        print "coucou"

if __name__ == '__main__':
    app = Main()
    app.MainLoop()
