public class Point {
    public final int x;
    public final int y;

    public Point() {
        this.x = -1;
        this.y = -1;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public final double distSq(Point b) {
        return (double)((this.x - b.x) * (this.x - b.x) + (this.y - b.y) * (this.y - b.y));
    }

    public final double dist(Point b) {
        return Math.sqrt(this.distSq(b));
    }

    public String toString() {
        return String.format("%d,%d", this.x, this.y);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Point point = (Point)o;
            if (this.x != point.x) {
                return false;
            } else {
                return this.y == point.y;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (int)((double)this.y + 0.5D * (double)(this.x + this.y) * (double)(this.x + this.y + 1));
    }
}
