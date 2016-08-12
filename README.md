![Voxel](/docs/images/Voxel-Logo.png?raw=true)

Voxel is a Minecraft inspired game made using Java and LWJGL3 library.

[![Build Status](https://travis-ci.org/Lux-Vacuos/Voxel.svg?branch=develop)](https://travis-ci.org/Lux-Vacuos/Voxel)


## Development

The main development are taken in the 'develop' branch, for stable code please use the 'master' branch.

## Project Layout

The project is divided into sub projects within these we find different parts of code, as the client, server, etc.

This is divided into.

### Client
This contains code that is used only for the client side, like the graphics engine.

### Server
This contains the code from the server version.

### Launcher
This contains the launcher used to download all the libraries and run the client.

### Universal
This contains code shared between client and server, in this case some parts of the netcode and API for mods.

### Ashley
This is a fork of libgdx/Ashley that was modified so it can run without all the libgdx library.

### Utils
This contains code that is used in all other sub projects, some code from libgdx and LWJGL2 was exported so the other projects do not have to implement the same code.

## Running Voxel

The main method is stored inside the Bootstrap class found in the package of the same name, this is for Client and Server. Has support to the following parameters.

| Name          | Variable Type | Default Value  | Description    | Client, Server or Universal |
| ------------- | -------------:| :-------------:| --------------:| -----------:|
| -width        | integer       | 720            | Display Width  | Client Only |
| -height       | integer       | 1280           | Display Height | Client Only |
| -username     | string        | (empty)        | Username to use when connecting to a server | Client Only |
| (first param) | integer       | 4059           | Server's port  | Server Only |

