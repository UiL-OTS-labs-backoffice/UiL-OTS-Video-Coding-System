#!/bin/bash

# ./copyapplefiles.sh [path-to-app-file] [name-of-app-file]

BASEDIR=$(dirname "$0")

SIZE=$(du -s "$1" | sed -e "s/\t.*//")
COUNT=$(expr $SIZE / 1000 + 2)

dd if=/dev/zero of="/tmp/$2.dmg" bs=1M count=$COUNT
mkfs.hfsplus -v "$2" "/tmp/$2.dmg"

sudo mkdir /mnt/appledisk
sudo mount -o loop "/tmp/$2.dmg" /mnt/appledisk
cp -R "$1" /mnt/appledisk
cp $BASEDIR/DS_Store_backup /mnt/appledisk/.DS_Store

ln -s /Applications /mnt/appledisk/Applications
mkdir -p /mnt/appledisk/.background
cp $BASEDIR/background.png /mnt/appledisk/.background/background.png
cp $BASEDIR/VolumeIcon.icns /mnt/appledisk/.VolumeIcon.icns

sleep 1

sudo umount /mnt/appledisk
sudo rm -R /mnt/appledisk

