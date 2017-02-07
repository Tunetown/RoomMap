# RoomMap

Sound Pressure (SPL) Mapper for analysis of the distribution of low frequency sound pressure levels across a room.

![RM2](https://github.com/Tunetown/RoomMap/blob/master/resources/doc/images/RM2.jpg)

This application is used to evaluate field measurements made with REW (Room EQ Wizard), in order to gain
a better overview about the distribution of sound pressure levels in a room at low frequencies. While 
doing acoustical room treatments, this can help identifying the locations in a room where a specific 
frequency is building up significantly, for example. 

The program is especially useful when analyzing rooms which are not rectangular or non-symmetric. Modal behaviour can not be calculated properly for such rooms, but this program can deliver a good overview of the room�s low frequency distribution. 

Currently, the application is released in beta state. It is fully functional, but there are some minor cosmetic flaws, as well as the release packages for some platforms are not ready yet. If there are any bugs etc., please inform me and i will take care of them.

## Version History

- Version 0.1 (07.02.2017): Initial release in beta state. 

## Installation / Requirements

The program is fully implemented in Java, the minimum version requirement is Java 1.8. A complex installation is in general not necessary:

### Mac OS
For Mac OS X users (minimum tested version is 10.8 Mountain Lion), there is a DMG package ready for [download](https://github.com/Tunetown/RoomMap/tree/master/release/macosx/bundles).
However, also OS X users can manually start the JAR package as described for Windows users, if the DMG should not work.

### Windows
Currently there is no installer or EXE file for Windows users (yet), but as for all Java applications, you can
just download the JAR package [RoomMap.jar](https://github.com/Tunetown/RoomMap/tree/master/release/macosx) and run it manually from the command line. If you need advice on this, see [this documentation](https://docs.oracle.com/javase/tutorial/deployment/jar/run.html) under 'JAR Files as Applications' and this [video](https://www.youtube.com/watch?v=2jdK8A863q8) (randomly picked) to see how to run a java program from the Windows command line. The application has been tested successfully with Windows 7 and 8.1. 

### Linux
The application has not been tested with Linux yet, but just running the JAR file with the correct Java JRE >= JRE 1.8 should work well as the program does not access any complicated resources of the system.   

## Procedure of Measurement

After launching, the program asks you to select some files. These files have to be created with REW ([Room EQ Wizard](https://www.roomeqwizard.com/)) first, which is an open-source, free Java program to make measurements with a measurement mic.  If you don�t have experience with REW already, there is plenty of information and tutorials around the web about this.
Also, you can download the examples provided with RoomMap (folder [examples](https://github.com/Tunetown/RoomMap/tree/master/examples)), which can give you a hint how things work here, comments on these examples follow later in this document.

Proceed as follows:
- In your room, mark a grid of (for example) 1 meter distance on the floor with some tape.

  ![markings](https://github.com/Tunetown/RoomMap/blob/master/resources/doc/images/markings.jpg) 

  Because we will analyze low frequencies mainly, accuracy is not too critical here, +/- 5cm is enough. Also, if your room has a non-rectangular shape like an attic for example, this is no problem at all. Just take care to have enough measuring points, and to distribute them well. 
  
- At each of these points, make measurements with REW at heights of (for example) 0m (floor), 1m, 2m and so on. This way you have "quantized" your room with measurements. It is a good idea to use some kind of remote control like VNC to trigger measuring for example with a smart phone. All in all the process is not taking too long if done effectively: For example, an attic room (used for creating the RoomMap examples) with size of about 4,5m x 8m took only about 2 hours for measuring.

  Parameters of REW are not critical, a short sweep of 128k will be enough, also it does not make sense to measure up to high frequencies. The [Shannon/Nyquist sampling theorem](https://en.wikipedia.org/wiki/Nyquist%E2%80%93Shannon_sampling_theorem) applies here. If doing the grid with 1m distances, the maximum accurate frequency will be around 2m in wavelength, which corresponds to a maximum accurate frequency of about 170 Hz. However, the program will only visualize data which is within these accuracy borders anyway, but be aware of this.
- For each measurement, write the coordinates in REW�s description fields like this: 

  ![REW1](https://github.com/Tunetown/RoomMap/blob/master/resources/doc/images/REW1.jpg)
  
  The syntax is:
  X Y Z
  In words: X coordinate (width) in meters, followed by space, then Y (length) in meters, then space again, then Z (height) in meters. You can also use non-integer values, in this case, use the dot (.) as the decimal separator (commas will not work). Examples: if 0 0 0 would be our starting point in the lower left corner in front of the listening position in an average control room, 0.5 0 0 would be half a meter towards the right from that corner, and 3 0 1 would be 3 meters to the right and 1 upwards from the floor, seen from the corner, you get it. 
  
  It is also allowed (and intended) to do additional measurements between the grid in areas where more accuracy is wanted, for example behind the speakers. In this case, just specify the coordinates as mentioned above, the interpolation of the program can handle this nicely. 

- Export the measurements from REW as text files. This is done in the menu:

  ![REW2](https://github.com/Tunetown/RoomMap/blob/master/resources/doc/images/REW2.jpg)
  
  REW then creates one file per measurement, which contains the SPL for given frequencies. The file name will contain the coordinates as entered before in REW.
- Launch the RoomMap program, browse to the folder where you stored the REW export files, and select all of them. Then hit "Open". The program will soon show you a first SPL map of your room :)

  ![RM1](https://github.com/Tunetown/RoomMap/blob/master/resources/doc/images/RM1.jpg)
  
  Here for example, the first axial room mode (and the only one because this is no rectangular room) of about 40 Hz distributes like expected: Between front and back walls.

## Operation Tips

### Data Visualization

The data visualization is generated by interpolating between the measured points. The view is from top down: You always see the SPL at a fixed height (adjustable with the corresponding slider), as seen from birds eye view. There is no 3d projection (yet). The data points are marked with small dots, accompanied with a 1 meter grid for orientation. Also, as you experiment with the height slider (which adjusts the Z coordinate), the points from which the current layer is interpolated the most, are shown a bit more focused than the others. In the real test examples provided (which have also been used for the screenshots here), you can see for example that the room has a cut out area: Behind the mixer, there is a small closet installed, occupying the left behind quarter of floor space. Also the room is an attic room. 

In general, you set the frequency you are interested in, and then, with the height slider, you "move" up and down in the room to see the SPL distribution at the corresponding height. This can be useful to determine where a certain frequency is treated most effectively. For example, pressure based absorbers are best placed in the hottest SPL areas, while velocity based absorbers have to be put at velocity maxima which can be approximated with some experience in acoustics, perhaps also using the "Show wavelength circles" option explained below. In general, the location of velocity maxima is NOT directly correlated to the SPL maxima, as the phase relationship between pressure and velocity can vary from 0 (linear sound wave) to 90 degrees (radial sound wave) or more. This clearly goes beyond the scope of this document. 

Also, the SPL maps give some hint if a specific low frequency problem is of modal nature (like in the 40 Hz example above), or if it is caused by speaker boundary interferences (SBIR). In the given example, 83 Hz has a severe dip in frequency response. The SPL map for 83 Hz shows that the pressure is mostly building up behind the left speaker (which is running alone for these measurements), so this is most likely caused by SBIR. Putting a wavelength circle at speaker position also verifies that 1/4 wavelength of 83 Hz (ca. 1.1 meters) indicates that the left wall might be the issue: 

![RM4](https://github.com/Tunetown/RoomMap/blob/master/resources/doc/images/RM4.jpg)


### Controls

#### Sliders
+ *Frequency* controls the frequency currently displayed. 
+ *Height* adjusts the height in the room which is visualized. 
+ *Margin* is used to set the extra space shown outside the outmost measuring points.
+ *Resolution* sets the accuracy of the visualization

#### Options
- *Normalize SPLrange for selected frequency* Can be used to extend the color scheme to be fully used for displaying the SPL at the current frequency. This increases contrast, but makes it more difficult to compare between frequencies on the other hand.
- *Show Grid* Switches the 1 meter grid on/off.
- *Show wavelength circles* If you click anywhere inside the visualization, circles of 1/4, 1/2, 3/4 and 4/4 wavelength are drawn around the clicked point. This can be handy to visualize the wave dimensions against the data. The ones who do acoustical treatment will know the use of this...
- *Only show accurate data* This hides data areas where not enough data points exist. This is determined using the [sampling theorem](https://en.wikipedia.org/wiki/Nyquist%E2%80%93Shannon_sampling_theorem). It is recommended to let this switched on.
- *Multithreaded interpolation with X Threads* As calculation of the interpolated data is CPU intensive, this job can be distributed among several threads. Use this to speed up frequency and height changes.
- *Precalc. in Background with X Threads* Additionally to the last option, changes in frequency can be accelerated by precalculating the interpolator coefficients for all frequencies in advance. To do this, check this option after startup and wait until calculation has finished. Frequency changes are usually much faster after that.   

## Examples
The examples delivered with RoomMap should help getting started with the program. They are created in an attic room of about 4,5 x 8 meters, while in the back part the room is halfed to host a small bathroom. See the data points in the visualization to get a picture. Also, the room�s side walls are completely slanted from the floor up, until they meet at about 3 meters above the floor, being an attic room. Provided examples are:

- *realtest1*: First room measurements, not very accurate, measured with relatively high noise floor. Anyway, it shows the capabilities of the program pretty good. Left speaker only, located at about 1 / 0 / 1.5 meters in the room (x/y/z), pointing towards the room middle.

- *simple16*: Generated testing example, not really useful
 
- *simpleCube8*: Generated testing example, not really useful 

## Licensing 
This program is licensed as free, open-source software under the GNU Public License (GPLv3).

### Dependencies to External Libraries
The following external libraries are used by this program: 
- *CONRAD*: A library developed by the Stanford university in cooperation with Friedrich-Alexander Univerit�t Erlangen/Germany for cone-beam imaging in radiology, which is being used here just for thin plate spline interpolation in three-dimensional space. Only the needed sources are included directly in this project, only altered minimally for compatibility issues. See http://www5.cs.fau.de/conrad/ for details.
- ij.jar: Dependency of *CONRAD*
- Jama-1.0.2.jar: Dependency of *CONRAD*
- jpop.0.7.5.jar: Dependency of *CONRAD*
- *rainbowvis*: This is also included as source, and (slightly altered for compatibility) used for generating the rainbow color visualization.
