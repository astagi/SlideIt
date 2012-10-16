SlideIt
=======

Android app to manage your slide via bluetooth with a Linux server

Compile and install the Android app
-----------------------------------

You can build this application just typing:

	make
	make upload

You need ant.

Server dependencies
-------------------

- python 2.x
- python-bluetooth
- xvkbd

Run the server
--------------

Scan all the device near you

	./slideserver list

Pair the device

	./slideserver pair pair_code device_address

Run the server typing

	./slideserver start

License
-------

This software is released under MIT License included in the source.
Copyright (C) 2012 Andrea Stagi <stagi.andrea@gmail.com>.
