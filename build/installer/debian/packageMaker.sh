#!/bin/bash

# copy file structure for i386
cd $1
mkdir -p /tmp/uilots/debian/
cp -R debian/ /tmp/uilots/
cp control-amd64 /tmp/uilots/debian/DEBIAN/control
cp control-i386 /tmp/uilots/
cd /tmp/uilots/

# copy jar file
mkdir -p debian/usr/share/coding-system/
cp $2 debian/usr/share/coding-system/$4

# create executable
mkdir -p debian/usr/bin/
echo "#!/bin/bash" > debian/usr/bin/coding-system
echo "java -jar /usr/share/coding-system/$4 \"\$@\"" >> debian/usr/bin/coding-system
chmod +x debian/usr/bin/coding-system

# Set file permissions
chmod -x debian/usr/share/doc/coding-system/*
sudo su
chown -R root:root debian

# Build amd 64 package
dpkg --build debian
mv debian.deb $3-amd64.deb

# Build i386 package
cp /tmp/uilots/control-i386 /tmp/uilots/debian/DEBIAN/control
dpkg --build debian
mv debian.deb $3-i386.deb

# Clean
cd /tmp
rm -R uilots