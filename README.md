# SnakeAI

An AI snake algorithm which uses A* with a heuristic to eat the apples on the board in addition to avoiding obstacles and other snakes.

## Game Mechanics

Here's how it all works. At any point in time, there is a single apple on the board. Eating the apple causes your snake to grow by a certain length. There are three things you need to know about: snakes, apples and obstacles. Let's look at the mechanics of each in turn.

### Snakes

Snakes move in one of the four cardinal directions at a speed of one square per timestep, with all moves executed simultaneously. A snake dies when it collides with any of the grid's sides, or moves into a non-empty and non-apple square. If a snake collides with the body of another snake, the latter is credited with a kill. In the event of a head-on collision between two snakes, both snakes are killed, but neither is credited with a kill. When a snake dies, it will miss the next timestep, and be respawned on the following step at a random location on the board. Note that if your program has crashed, your snake will be removed from the board for the duration of the round.

### Apples

There is a single apple on the board at all times during the game. It appears at a random location at the beginning of the game, and immediately respawns at a random location every time it is eaten. Eating an apple causes a snake to grow for the next few rounds by having its tail remain in its current position. If multiple snakes consume the same apple at the same time, both snakes are killed and the apple is respawned at a new location. Additionally, the apple will be respawned if it hasn't been eaten within a certain number of moves. Unfortunately, we purchased the apples from a farm that doesn't use preservatives. As a result, the apple decays over time, becoming worse and worse. Initially, eating the apple is worth 5 points. However, the value of the apple decreases by 0.1 every timestep (rounded up to the nearest integer). When the apple's value is negative, eating it will cause the snake to shrink! Additionally, once the value of the apple worth -4 or less, eating it will immediately kill the snake doing so! So be careful!!

### Obstacles

At the beginning of a game, immovable obstacles are placed on the board. If a snake collides with an obstacle, it dies. That's really all there is to it.

Visit https://snake.wits.ai for more info.
