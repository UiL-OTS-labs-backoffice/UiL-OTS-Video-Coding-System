# UiL OTS Video Coding System
The UiL OTS Video Coding System is a piece of software for coding videos created in the UiL OTS Baby Lab, or in experiments with a similar setup. The software can be used to code looks of participants for each of the trials in the experiment. For each trial, the total look time is calculated, which can then be exported to a csv file.

## Type of experiment
This software is specifically made for researchers of the UiL OTS using a setup like in the [babylab of the UiL OTS labs](http://uilots-labs.wp.hum.uu.nl/facilities/baby-lab/). This software does not allow for much more than is required by those experimenters, but exactly this makes it simple to use for the purposes for which it was made. 
In the babylab of the UiL OTS Lab, participants are usually presented with 8 trials in which stimuli have the form of sound or light from either the left or the right, or right in front of the participant. The participant is recorded and this recording gets an overlay, indicating the start and end times and relative location to the participant of the presented stimuli.
With this software, every time the participant looks at the source of a stimulus, a new look can be registered within that trial. The total look time for each trial is then calculated automatically and can later be exported to a csv.
The amount of trials and looks is not set, so any amount can be used, but before registering a look, a trial has to be registered first.

## Features
* Supports multiple video formats
* Add any number of trials
* Add any number of looks in any trial
* Automatically check for timeouts and indicate when timeouts occur
* Add comments to trials and looks
* Add research meta information about researcher, participant, etc
* Use quickeys for easy navigation
* Frame-by-frame step-through, both forward and backward
* Exports to csv
* Projects can be saved for later continuation
* Automatic file recovery option on inexpected shutdown

### Version 2
Version 2 of the UiL OTS Video Coding System has been officially released in January 2016. In this version, the user interface received a mayor overhaul. A time line like interface was added, to provided an even clearer overview of where trials and looks have been added inside the project. This time line also provides quick access to all sorts of actions that can be performed on trials and looks in the project.

An installer and a (portable) launcher for Windows are now provided as well. See the releases tab to download.

## Requires:
 * [VLC](http://www.videolan.org/vlc/)
 * [Java Runtime Environment (JRE)](https://www.java.com/en/download/) 1.6 or later (although it is adviced to always use the latest version of Java).

## Support
The UiL OTS Video Coding System is developed by the UiL OTS Labs of the Utrecht University. A [how-to for using the UiL OTS Video Coding System](http://uilots-labs.wp.hum.uu.nl/how-to/how-to-recode-videos-offline-in-the-babylab/) is provided on the lab's website, as well as an [FAQ](http://uilots-labs.wp.hum.uu.nl/how-to/faq-troubleshooting-uil-ots-video-coding-system/).
Please use the issues tracker if you encounter any problems.

## License
Copyright (C) 2015 UiL OTS Labs

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; version 2 of the License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
