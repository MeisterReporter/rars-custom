RARS -- RISC-V Assembler and Runtime Simulator
----------------------------------------------

RARS, the RISC-V Assembler, Simulator, and Runtime, will assemble and simulate
the execution of RISC-V assembly language programs. Its primary goal is to be
an effective development environment for people getting started with RISC-V. 

## New-Features

- **Themes:**
  - Multiple Themes, including Darklaf (a customizable theme)
- **Tabs**
  - Closing files via the tab
  - Reordering tabs via drag and drop
- **Editor**
  - Macros: Define your own code shortcuts with parameters. Write your macro and press enter to replace it with the code snippet.
  - Live syntax and error checker. Underlines errors and warnings in the editor
  - Jump directly to used subroutines by clicking on the name (CTRL + Click)
  - Suggest subroutines in the autocomplete popup
- **Other**
  - Support for opening multiple files at once
  - Created new icons, which where missing
  - Fixed Bugs & Updated to newer Java Version (21)

## Features

  - RISC-V IMFDN Base (riscv32 and riscv64)
  - Several system calls that match behaviour from MARS or SPIKE.
  - Support for debugging using breakpoints and/or `ebreak`
  - Side by side comparison from pseudo-instruction to machine code with
    intermediate steps
  - Multifile assembly using either files open or a directory

## Documentation

Documentation for supported [instructions](https://github.com/TheThirdOne/rars/wiki/Supported-Instructions), [system calls](https://github.com/TheThirdOne/rars/wiki/Environment-Calls), [assembler directives](https://github.com/TheThirdOne/rars/wiki/Assembler-Directives) and more can be found on the [wiki](https://github.com/TheThirdOne/rars/wiki). Documentation included in the download can be accessed via the help menu. 

## Download

RARS is distributed as an executable jar. You will need at least Java 21 to run it. 

The latest stable release can be found [here](https://github.com/MeisterReporter/rars-custom/releases/latest), the latest developments can be found on the [continuous release](https://github.com/MeisterReporter/rars-custom), and the [releases page](https://github.com/MeisterReporter/rars-custom/releases/) contains all previous stable releases with patch notes.

Alternatively, if you wish to make your own jar and/or modify the code, you
should clone the repo with `git clone https://github.com/MeisterReporter/rars-custom.git --recursive`.
Running the script `./build-jar.sh` on a Unix system will build `rars.jar`.

## Screenshot

![Screenshot of sample program](screenshot.png)

## Changes from MARS 4.5

RARS was built on MARS 4.5 and owes a lot to the development of MARS; its
important to note what are new developments and what come straight from MARS.
Besides moving from supporting MIPS to RISC-V and the associated small changes,
there are several general changes worth noting.

  - Instructions can now be hot-loaded like Tools. If you want to support an additional extension to the RISC-V instruction set. the .class files just need to be added to the right folder
  - ScreenMagnifier, MARS Bot, Intro to Tools, Scavenger Hunt, and MARS Xray were removed from the included tools. ScreenMagnifier, MARS Bot, Intro to Tools, and Scavenger Hunt were removed because they provide little benefit. And MARS Xray was removed because it is not set up to work with RISC-V, however if someone ports it, it could be merged in.
  - Removed delayed branching
  - Removed the print feature
  - Added a testing framework to verify compatibility with the RISC-V specification
  - Significant internal restructuring and refactoring.
