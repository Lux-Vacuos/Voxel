![Voxel](/docs/images/Voxel-Logo.png?raw=true)

Voxel is a Minecraft inspired game made using Java and LWJGL3 library.

[![Build Status](https://travis-ci.org/Lux-Vacuos/Voxel.svg?branch=develop)](https://travis-ci.org/Lux-Vacuos/Voxel)


## Development

The main development are taken in the 'develop' branch, for stable code please use the 'master' branch.

## Project Layout

The project is divided into sub projects within these you find different parts of code: client, server, etc.

## Requeriments

Voxel on the client side requires a modern OpenGL 3.3 compatible card with extra extensions:

| Name | Notes |
| ------------- | ------------- |
| ARB_shading_language_include | Required to run |
| EXT_texture_filter_anisotropic | Optional, may cause texture issues if isn't supported |

## Bulding Voxel

Voxel uses Gradle as build system.

Windows: Java 8.
Linux & OS X: OpenJDK 8 and JavaFX.

## Running Voxel

For fast test we recomend using "./gradlew run".

The main method is stored inside the Bootstrap class found in the package of the same name, this is for Client and Server. Has support to the following parameters:

| Name          | Variable Type | Default Value  | Description    | Client, Server or Universal |
| ------------- | -------------:| :-------------:| --------------:| -----------:|
| -width        | integer       | 720            | Display Width  | Client Only |
| -height       | integer       | 1280           | Display Height | Client Only |
| -username     | string        | (empty)        | Username to use when connecting to a server | Client Only |
| -port         | integer       | 4059           | Server's port  | Server Only |
| -ui           | (boolean)     | false          | Enable Server UI  | Server Only |

