#!/usr/bin/python
# -*- coding: utf-8 -*-
# mysocket.py

from socket import *
import itertools
import threading

bufferSocket = [] # self.recv(4096)

class Readline(threading.Thread):
    def __init__(self,parent,sock):
        threading.Thread.__init__(self)
        self.parent = parent
        self.sock = sock
        self.g = None

    def run(self):
        self.g = self.gen()

    def gen(self):
        bufferSocket = self.sock.recv(4096)
        while True:
            if "\n" in bufferSocket:
                (line, bufferSocket) = bufferSocket.split("\n", 1)
                yield line+"\n"
            else :
                #self.parent.g = itertools.tee(self.g,1)[0]
                bufferSocket += self.sock.recv(4096)



class Socket(socket):
    
    # def __init__(self,a,b):
    #     socket.__init__(self,a,b)
    #     self.g = None
    #     self.t = Readline(self,self)
    #     self.t.start()

    def readline(self):
        global bufferSocket
        if bufferSocket == []:
            ls = self.linesplit()
            for s in ls:
                bufferSocket.append(s)
        #print "bufferSocket : "+str(bufferSocket)
        x = bufferSocket[0]
        print "x : "+x
        del bufferSocket[0]
        return x

    def linesplit(self):
        buffer = self.recv(4096)
        print "buffer : " + buffer
        while "\n" in buffer:
            (line, buffer) = buffer.split("\n", 1)
            yield line+"\n"

    def reput(l):
        global bufferSocket
        bufferSocket.insert(0,l)


# def readline(sock):
#     #global bufferSocket
#     bufferSocket = sock.recv(4096)
#     #print "état du buffer : "+ bufferSocket +"fin"
#     #stop = True
#     while "\n" in bufferSocket:
#         print "état du buffer : "+ bufferSocket +"fin"
#                 # print "bufferSocket avant : \n" + bufferSocket+"fin"
#         (line, bufferSocket) = bufferSocket.split("\n", 1)
#                 # print "bufferSocket : \n"+bufferSocket+"fin"
#         print "line : "+line
#         yield line+"\n"
