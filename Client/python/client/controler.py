#!/usr/bin/python
# -*- coding: utf-8 -*-
# client/controler.py

import view
import gevent.socket

View = view.View

class Controler():
    
    def __init__(self,sock):
        self.viou = View(title='Battle Royale')
        self.sock = sock
