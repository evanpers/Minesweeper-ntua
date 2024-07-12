# MediaLab Minesweeper

## Project Overview
This project involves the development of a variation of the classic game "Minesweeper" for the Multimedia Technology course at the National Technical University of Athens. The game pits a player against the computer with a twist involving time constraints and a "super-mine". The game is developed using JavaFX, showcasing both foundational and innovative gameplay elements alongside a graphical user interface.

## Features
- **Two Difficulty Levels**: Each level has predefined grid sizes and mine counts, with the harder level including a super-mine.
- **Time Constraints**: Each game session has a maximum allowed time, adding urgency to gameplay.
- **Super-mine Mechanic**: Introduces a 'super-mine' that, when marked correctly, reveals all mines located in the same vertical and horizontal lines.
- **Graphical User Interface**: Implemented using JavaFX to provide an intuitive and visually appealing user experience.

## Game Parameters

| Difficulty Level | Grid Size | Mines Range | Available Time (secs) | Super-mine |
| ---------------- | --------- | ----------- | --------------------- | ---------- |
| 1                | 9 x 9     | 9 - 11      | 120 - 180             | No         |
| 2                | 16 x 16   | 35 - 45     | 240 - 360             | Yes        |

This table details the parameters for each difficulty level including grid sizes, mine ranges, time limits, and the existence of a super-mine.

## Game Setup and Operation
From the "Application" menu you have the following options:
- **Create**: Use the GUI to specify a new scenario by entering the details according to the parameters outlined in the table. This input is facilitated through a popup window. Once confirmed, this will generate and save a new description file in the `medialab` folder. The path to this folder is specified by the `path` variable on line 13 of the MinesweeperApp.java file. **You must modify this filepath to suit your specific setup requirements.**
- **Load**: Before starting a new game, load an existing game scenario by selecting its `SCENARIO-ID` through the GUI. The game will initialize according to the parameters defined in the loaded file.
- **Start**: Once a scenario is loaded, you can start the game through the GUI. If there is an active game, it must be stopped before a new game begins.

## Contributing
Contributions to the Minesweeper project are welcome. Please follow these steps:
1. Fork the repository.
2. Create a new branch (`git checkout -b improve-feature`).
3. Make changes or add new features.
4. Commit your changes (`git commit -am 'Add some feature'`).
5. Push to the branch (`git push origin improve-feature`).
6. Create a Pull Request.
