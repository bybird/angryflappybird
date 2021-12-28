# AngryFlappyBird
## COMSC-225 final project
Team Bybird: Marina, Cindy, Jonny

In the AngryFlappyBird game, the player uses the GUI button to move the angry flappy bird in a scrolling scene.
In the scene, a set of two pipes of a random height show up after every fixed amount of time, some upward facing pipes have collectable eggs on top of them, while some downward facing pipes have pig thieves dropping down to steal the eggs.
The goal of the game is for the angry flappy bird to move through the pipes without collision while collecting as many eggs as possible along the way to prevent the pigs from stealing the eggs.

### Bird
- [x] A player uses the button to control the bird’s flight
- [x] Collects gift and wreath
### Pipe
- [x] Appears in pairs every fixed amount of time
- [x] One life is taken from the bird when a collision with any pipe occurs
<img src='http://g.recordit.co/QSiqtIHDQD.gif' title='Bird and Pipe' width='' alt='Bird and Pipe'/>

### Gift (White egg in our code)
- [x] Randomly on the upward facing pipes Could be collected either by the thief or the bird
- [x] If a gift is collected, points will be added
- [x] If a thief collects an gift, points were lost
<img src='http://g.recordit.co/VHMMkjsbon.gif' title='Bird and Gift' width='' alt='Bird and Gift'/>

### Wreath (Golden egg in our code)
- [x] Randomly on the upward facing pipes Could be collected either by the thief or the bird
- [x] If a wreath is collected, **6 seconds of autopilot mode** will be triggered.
- [x] If a thief collects an wreath, points were lost
<img src='http://g.recordit.co/BANbHuHSeO.gif' title='Bird and Wreath' width='' alt='Bird and Wreath'/>

### Thief (pig in our code)
- [x] Randomly from downward facing pipes
- [x] Could collect gift or wreath right beneath it and lead to points lost if the gift or wreath is not collected by the bird first
- [x] The game is over and score is reset to 0 if the bird collides with a thief
<img src='http://g.recordit.co/DHNveOuKC9.gif' title='Bird and Thief' width='' alt='Bird and Thief'/>

### Floor
- [x] Scrolls through the scene consistently during the game until a collision happens.
- [x] The game is over and the score is reset to 0 if the bird collides with the floor.
- [x] The bird stops moving immediately upon collision.
### Background
- [x] Changes from night to day periodically.
### UI panel
- [x] A button that controls the start of the game and the wing flap of the bird.
- [x] A selector for the user to choose the difficulty levels. (3 levels - easy, medium, hard)
- [x] An icon with text description of the white egg, golden egg and the pigs is required.
### Keep score and lives
- [x] Show score and lives text
- [x] Keep score and lives accumulative
# 
- **Cindy**: The algorithm to make the eggs and pigs randomly appears on the pipe with different heights.
- **Marina**: Implementing autopilot mode; changing the background day to night periodically; timeline.
- **Jonny**: Implementing UI panel and code for text label and data of lives and score; Working on GitHub.
# 
### The most challenging part about the project
- Merging the code on GitHub!


