# RoomMap
Sound Pressure (SPL) Mapper for analysis of the distribution of sound pressure levels across a room.

This application is used to evaluate field measurements made with REW (Room EQ Wizard), in order to gain
a better overview about the distribution of sound pressure levels in a room at low frequencies. While 
doing acoustical room treatments, this can help identifying the locations in a room where a specific 
frequency is building up significantly, for example. 

The program is especially useful when analyzing rooms which are not rectangular or non-symmetric. Modal behaviour can not be calculated properly for such rooms, but this program can deliver a good overview of the room´s low frequency distribution. 

## Installation / Requirements

The program is fully implemented in Java using Swing, the minimum version requirement is Java 1.8. A complex installation is in general not necessary:

### Mac OS
For Mac OS X users (minimum is OS 10.8 Mountain Lion), there is a DMG package ready for [download](https://github.com/Tunetown/RoomMap/tree/master/release/macosx/bundles).
However, also OS X users can manually start the JAR package as described for Windows users, if the DMG should not work.

### Windows
Currently there is no installer or EXE file for Windows users (yet), but as for all Java applications, you can
just download the JAR package [RoomMap.jar](https://github.com/Tunetown/RoomMap/tree/master/release/macosx) and run it manually from the command line. If you need advice on this, see [this documentation](https://docs.oracle.com/javase/tutorial/deployment/jar/run.html) under 'JAR Files as Applications'. 

### Linux
The application has not been tested with Linux, but just running the JAR file with the correct Java JRE should work (see Windows).   

## Procedure of Measurement

After launching, the program asks you to select some files. These files have to be created with REW ([Room EQ Wizard](https://www.roomeqwizard.com/)) first, which is an open-source, free Java program to make measurements with a measurement mic.  If you don´t have experience with REW already, there is plenty of information and tutorials around the web about this.
Also, you can download the examples provided with RoomMap (folder [examples](https://github.com/Tunetown/RoomMap/tree/master/examples)), which can give you a hint how things work here, comments on these examples follow later in this document.

Proceed as follows:
- In your room, mark a grid of (for example) 1 meter distance on the floor with some tape.
  ![markings](https://github.com/Tunetown/RoomMap/blob/master/resources/doc/images/markings.jpg) 

  Because we will analyze low frequencies mainly, accuracy is not too critical here, +/- 5cm is enough. Also, if your room has a non-rectangular shape like an attic for example, this is no problem at all. Just take care to have enough measuring points, and to distribute them well. 
- At each of these points, make measurements with REW at heights of (for example) 0m (floor), 1m, 2m and so on. This way you have "quantized" your room with measurements. It is a good idea to use some kind of remote control like VNC to trigger measuring for example with a smart phone. All in all the process is not taking too long if done effectively: For example, an attic room (used for creating the RoomMap examples) with size of about 4,5m x 8m took only about 2 hours for measuring.

  Parameters of REW are not critical, a short sweep of 128k will be enough, also it does not make sense to measure up to high frequencies. The [Shannon/Nyquist sampling theorem](https://en.wikipedia.org/wiki/Nyquist%E2%80%93Shannon_sampling_theorem) applies here. If doing the grid with 1m distances, the maximum accurate frequency will be around 2m in wavelength, which corresponds to a maximum accurate frequency of about 170 Hz. However, the program will only visualize data which is within these accuracy borders anyway, but be aware of this.
- For each measurement, write the coordinates in REW´s description fields like this: ![REW1](https://github.com/Tunetown/RoomMap/blob/master/resources/doc/images/REW1.jpg)
  The syntax is:
  X Y Z
  In words: X coordinate in meters, followed by space, then Y, then space again, then Z (all in meters). You can also use non-integer values, in this case, use the dot (.) as the decimal separator (commas will not work). 
  It is also allowed to do additional measurements with less grid distance in areas where more accuracy is needed. In this case, just specify the coordinates correctly, the interpolation of the program can handle this nicely. 
- Export the measurements from REW as text files. This is done in the menu:
  ![REW2](https://github.com/Tunetown/RoomMap/blob/master/resources/doc/images/REW2.jpg)
  REW then creates one file per measurement, which contains the SPL for given frequencies. The file name will contain the coordinates as entered before in REW.
- Launch the RoomMap program, browse to the folder where you stored the REW export files, and select all of them. Then hit "Open". The program will soon show you a first SPL map of your room :)
  ![RM1](https://github.com/Tunetown/RoomMap/blob/master/resources/doc/images/RM1.jpg)

## Operation Tips
TODO

## Examples
TODO

## Licensing 
This program is licensed as free, open-source software under the GNU Public License (GPLv3).

### Dependencies to External Libraries
The following external libraries are used by this program: 
- *CONRAD*: A library developed by the Stanford university in cooperation with Friedrich-Alexander Univerität Erlangen/Germany for cone-beam imaging in radiology, which is being used here just for thin plate spline interpolation in three-dimensional space. Only the needed sources are included directly in this project, only altered minimally for compatibility issues. See http://www5.cs.fau.de/conrad/ for details.
- ij.jar: Dependency of *CONRAD*
- Jama-1.0.2.jar: Dependency of *CONRAD*
- jpop.0.7.5.jar: Dependency of *CONRAD*
- *rainbowvis*: This is also included as source, and (slightly altered for compatibility) used for generating the rainbow color visualization.
