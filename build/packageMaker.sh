#!/bin/bash
cd installer
mkdir -p /tmp/uilots/debian/
cp -R debian/ /tmp/uilots/
cd /tmp/uilots/
cp $1 debian/usr/share/coding-system/
chmod -x debian/usr/share/doc/coding-system/*
sudo su
chown -R root:root debian
#dpkg --build debian
#mv debian.deb $2
cd /tmp
rm -R uilots