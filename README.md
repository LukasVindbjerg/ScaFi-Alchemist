# Alchemist Simulation Basics

## Setup

1)	Install the virtual machine [Java SE 11 JDK](http://oracle.com/technetwork/java/javase/downloads).

2)	Install the [Eclipse](https://eclipse.org/downloads)  or [IntelliJ](https://www.jetbrains.com/idea/download) IDE.
	Eclipse has better support for Protelis (buggy on ScaFi), while IntelliJ has better support for ScaFi (no Protelis syntax highlighting).

3)	Install a YAML support plugin of your choice.

4)	If you plan to use Protelis on Eclipse, install the *Protelis* plugin from `Help > Eclipse Marketplace`
	for syntax highlighting (not available for IntelliJ).

5)	If you plan to use ScaFi on Eclipse, install the *Scala IDE* available from `Help > Eclipse Marketplace`.
	For using ScaFi on IntelliJ, click on `File > Project Structure > Global Libraries`.
	In the window that will open, search for `Create > Download`, then select version 2.12.3 and press OK.
	Wait for the download to end then press OK again.

6)	Download the [sample project](https://bitbucket.org/gaudrito/alchemist-example).

7)	Import the downloaded folder as a Gradle project.
	
	**Eclipse**: click on `File > Import`, then select the type `Gradle > Gradle Project`.
	Click on `Next` twice, then select the downloaded folder through `Browse` in the upper right corner.
	Complete the procedure by clicking on `Finish`. If errors appear, you may also need to right-click
	on the newly created project and then click on `Gradle > Refresh Gradle Project`.
	
	**IntelliJ**: click on `File > Open` then select the `build.gradle` file within the downloaded folder.
	Click on `Open as Project` then compile the resulting form.

See `README.pdf` for further details.

## The sample project

See `README.pdf` for further details.

## Esercises

Edit the file `example.pt` or `example.scala`, incrementally, in order to compute the following in every device.

1)	The number of neighbour devices.

2)	The maximum number of neighbour devices ever witnessed by the current device.

3)	The maximum number of neighbour devices ever witnessed by any device in the network.

4)	Move towards the neighbour with the lowest number of neighbours.

5)	Move away from the neighbour with the highest number of neighbours.

6)	Move as if the device was attracted by the neighbour with the lowest number of neighbours,
        and repulsed by the neighbour with the highest number of neighbours.

7)	Move as if the device was repulsed by every neighbour, and by the four walls of the rectangular box between points `[-4,-3]` and `[4,3]`.

### Hints

*	In the first few exercises, start by reasoning on when/where to use `nbr` (“collecting from neighbours”) and `rep` (“collecting from the past”).

*	In order to move a device, you need to store a coordinate in the `target` variable of the simulator, with `env.put("target", ...)` in Protelis, or `node.put("target", ...)` in ScaFi. Part of `example.*` is already handling this, so you can just edit that part.

*	You can use the defined function `getCoordinates()` to get the position of the current device. Coordinates can be composed as physical vectors in Protelis: `[1,3] + [2,-1] == [3,2]`, `[2,4] * 0.5 == [1,2]`. Similar operators are also defined in `example.scala`.

*	In the last few exercises, you can model attraction/repulsion using the classical inverse square law. More precisely, if `v` is the vector between two objects, the resulting force is `v / |v|` where `|v| = sqrt(v_x^2 + v_y^2)`.

## Links

See `README.pdf` for further links.
