#!/usr/bin/python
# -*- coding: utf-8 -*-
# mybuttons

import wx

SEA=0b00000001
SUBM=0b00000010
OUCH=0b00000100
MISS=0b00001000
TOUCHE=0b00010000
RED=0b10000000
GREEN=0b01000000

class BitmapButton(wx.BitmapButton):
    def __init__(self,parnt,iD,nme,stle,myFlag=SEA):
        super(BitmapButton,self).__init__(parnt,id=iD,name=nme,style=stle)
        self.flag = myFlag
        self.sea_normal = wx.Bitmap("img/sea/normal.jpg",wx.BITMAP_TYPE_JPEG)
        self.sea_green = wx.Bitmap("img/sea/green.jpg",wx.BITMAP_TYPE_JPEG)
        self.sea_miss_green = wx.Bitmap("img/sea/miss/green.jpg",wx.BITMAP_TYPE_JPEG)
        self.sea_miss_normal = wx.Bitmap("img/sea/miss/normal.jpg",wx.BITMAP_TYPE_JPEG)
        self.sea_miss_red = wx.Bitmap("img/sea/miss/red.jpg",wx.BITMAP_TYPE_JPEG)
        self.sea_red = wx.Bitmap("img/sea/red.jpg",wx.BITMAP_TYPE_JPEG)
        self.sea_touche_green = wx.Bitmap("img/sea/touche/green.jpg",wx.BITMAP_TYPE_JPEG)
        self.sea_touche_normal = wx.Bitmap("img/sea/touche/normal.jpg",wx.BITMAP_TYPE_JPEG)
        self.sea_touche_red = wx.Bitmap("img/sea/touche/red.jpg",wx.BITMAP_TYPE_JPEG)
        self.submarin_green = wx.Bitmap("img/submarin/green.jpg",wx.BITMAP_TYPE_JPEG)
        self.submarin_normal = wx.Bitmap("img/submarin/normal.jpg",wx.BITMAP_TYPE_JPEG)
        self.submarin_ouch_green = wx.Bitmap("img/submarin/ouch/green.jpg",wx.BITMAP_TYPE_JPEG)
        self.submarin_ouch_normal = wx.Bitmap("img/submarin/ouch/normal.jpg",wx.BITMAP_TYPE_JPEG)
        self.submarin_ouch_red = wx.Bitmap("img/submarin/ouch/red.jpg",wx.BITMAP_TYPE_JPEG)
        self.submarin_red = wx.Bitmap("img/submarin/red.jpg",wx.BITMAP_TYPE_JPEG)
        self.submarin_touche_green = wx.Bitmap("img/submarin/touche/green.jpg",wx.BITMAP_TYPE_JPEG)
        self.submarin_touche_normal = wx.Bitmap("img/submarin/touche/normal.jpg",wx.BITMAP_TYPE_JPEG)
        self.submarin_touche_red = wx.Bitmap("img/submarin/touche/red.jpg",wx.BITMAP_TYPE_JPEG)
        self.changeBMP(self.flag)
        

    def changeBMP(self,flag):
        self.flag= flag
        bmp = self.choose(flag)
        self.SetBitmapLabel(bmp)

    def choose(self,flag):
        if SEA & flag:
            if MISS & flag:
                if RED & flag:
                    return self.sea_miss_red
                else:
                    if GREEN & flag:
                        return self.sea_miss_green
                    else:
                        return self.sea_miss_normal
            else :
                if TOUCHE & flag:
                    if RED & flag:
                        return self.sea_touche_red
                    else:
                        if GREEN & flag:
                            return self.sea_touche_green
                        else:
                            return self.sea_touche_normal
                else:
                    if RED & flag:
                        return self.sea_red
                    else:
                        if GREEN & flag:
                            return self.sea_green
                        else:
                            return self.sea_normal
        else:
            if OUCH & flag:
                if RED & flag:
                    return self.submarin_ouch_red
                else:
                    if GREEN & flag:
                        return self.submarin_ouch_green
                    else:
                        return self.submarin_ouch_normal
            else:
                if TOUCHE & flag:
                    if RED & flag:
                        return self.submarin_touche_red
                    else:
                        if GREEN & flag:
                            return self.submarin_touche_green
                        else:
                            return self.submarin_touche_normal
                else:
                    if RED & flag:
                        return self.submarin_red
                    else:
                        if GREEN & flag:
                            return self.submarin_green
                        else:
                            return self.submarin_normal
