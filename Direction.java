
public enum Direction {
    FROZEN(-2),
    NONE(-1),
    NORTH(0),
    SOUTH(1),
    WEST(2),
    EAST(3),
    LEFT(4),
    STRAIGHT(5),
    RIGHT(6);

    private final int value;

    private Direction(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static Direction fromValue(int value) {
        Direction[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Direction direction = var1[var3];
            if (direction.value == value) {
                return direction;
            }
        }

        return NONE;
    }

    public static Direction toFixedDirection(Direction direction, Snake snake) {
        Direction currentDirection = snake.getDirection();
        if (currentDirection != EAST && currentDirection != WEST) {
            switch(direction) {
                case LEFT:
                    return currentDirection == NORTH ? WEST : EAST;
                case STRAIGHT:
                    return currentDirection;
                case RIGHT:
                    return currentDirection == NORTH ? EAST : WEST;
            }
        } else {
            switch(direction) {
                case LEFT:
                    return currentDirection == EAST ? NORTH : SOUTH;
                case STRAIGHT:
                    return currentDirection;
                case RIGHT:
                    return currentDirection == EAST ? SOUTH : NORTH;
            }
        }

        return direction;
    }

    public static Direction fromString(String value) {
        try {
            int val = Integer.parseInt(value);
            return fromValue(val);
        } catch (NumberFormatException var5) {
            value = value.toUpperCase();
            Direction[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Direction direction = var1[var3];
                if (value.equals(direction.name())) {
                    return direction;
                }
            }

            return NONE;
        }
    }

    public static boolean isOpposite(Direction A, Direction B) {
        switch(A) {
            case NORTH:
                return B == SOUTH;
            case SOUTH:
                return B == NORTH;
            case WEST:
                return B == EAST;
            case EAST:
                return B == WEST;
            case NONE:
                return false;
            default:
                assert false;

                return false;
        }
    }

    public static Direction directionTo(Point to, Point from) {
        assert to.distSq(from) == 1.0D;

        if (to.x == from.x) {
            return to.y < from.y ? NORTH : SOUTH;
        } else {
            return to.x < from.x ? WEST : EAST;
        }
    }
}