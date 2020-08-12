import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Snake implements Iterable<Point> {
    private int timeOfGrowthLeft;
    private final int id;
    private boolean alive;
    private int length;
    private int kills;
    private int deaths;
    private int timeToRespawn;
    private boolean invisible;
    private int invisibilityTimeLeft;
    private int timeSinceInvisible;
    private Point invisiblePoint;
    private ArrayDeque<Point> orderedPoints;
    private Set<Point> unorderedPoints;

    public Snake(Point head, Point tail, int id) {
        this.id = id;
        this.unorderedPoints = new HashSet(100);
        this.orderedPoints = new ArrayDeque(100);
        this.addSegment(head, tail);
        this.length = this.unorderedPoints.size();
        this.invisible = false;
        this.invisibilityTimeLeft = 0;
    }

    public void addKill() {
        ++this.kills;
    }

    public int getId() {
        return this.id;
    }

    public void setPoints(Point... points) {
        this.unorderedPoints.clear();
        this.orderedPoints.clear();

        for(int i = 0; i < points.length - 1; ++i) {
            this.addSegment(points[i], points[i + 1]);
        }

        this.length = this.unorderedPoints.size();

        assert this.orderedPoints.size() == this.unorderedPoints.size();

    }

    public String toString() {
        String status = this.isInvisible() ? "invisible" : (this.isAlive() ? "alive" : "dead");
        StringBuilder sb;
        if (this.isInvisible()) {
            sb = new StringBuilder(String.format("%s %d %d %d %s", status, this.length, this.kills, this.timeSinceInvisible, this.invisiblePoint.x + "," + this.invisiblePoint.y));
        } else {
            sb = new StringBuilder(String.format("%s %d %d", status, this.length, this.kills));
        }

        if (this.isAlive()) {
            Point[] points = new Point[this.length];
            Direction[] directions = new Direction[this.length];
            boolean[] vertices = new boolean[this.length];
            int i = 0;

            for(Iterator var7 = this.iterator(); var7.hasNext(); ++i) {
                Point p = (Point)var7.next();
                points[i] = p;
                if (i == 0) {
                    vertices[i] = true;
                } else {
                    directions[i] = Direction.directionTo(p, points[i - 1]);
                    vertices[i - 1] = directions[i] != directions[i - 1];
                }
            }

            vertices[this.length - 1] = true;

            for(int j = 0; j < vertices.length; ++j) {
                if (vertices[j]) {
                    sb.append(' ').append(points[j].x).append(',').append(points[j].y);
                }
            }
        }

        return sb.toString();
    }

    public String getSemiVisible() {
        if (!this.isInvisible()) {
            return this.toString();
        } else {
            String status = this.isInvisible() ? "invisible" : (this.isAlive() ? "alive" : "dead");
            StringBuilder sb = new StringBuilder(String.format("%s %d %d %d %s", status, this.length, this.kills, this.timeSinceInvisible, this.invisiblePoint.x + "," + this.invisiblePoint.y));
            if (this.isAlive() && this.timeSinceInvisible < this.getLength()) {
                Point[] points = new Point[this.length];
                Direction[] directions = new Direction[this.length];
                boolean[] vertices = new boolean[this.length];
                int i = 0;
                boolean foundCut = false;

                for(Iterator var8 = this.iterator(); var8.hasNext(); ++i) {
                    Point p = (Point)var8.next();
                    points[i] = p;
                    if (p.equals(this.invisiblePoint)) {
                        vertices[i] = true;
                        foundCut = true;
                    } else if (foundCut) {
                        directions[i] = Direction.directionTo(p, points[i - 1]);
                        vertices[i - 1] = directions[i] != directions[i - 1];
                    }
                }

                if (foundCut) {
                    vertices[this.length - 1] = true;

                    for(int j = 0; j < vertices.length; ++j) {
                        if (vertices[j]) {
                            sb.append(' ').append(points[j].x).append(',').append(points[j].y);
                        }
                    }

                    if (points[this.length - 1].equals(this.invisiblePoint)) {
                        sb.append(' ').append(this.invisiblePoint.x).append(',').append(this.invisiblePoint.y);
                    }
                }
            }

            return sb.toString();
        }
    }

    public Iterator<Point> iterator() {
        return this.orderedPoints.iterator();
    }

    public void clear() {
        this.alive = false;
        this.invisible = false;
        this.invisibilityTimeLeft = 0;
        this.timeSinceInvisible = 0;
        this.invisiblePoint = null;
        this.length = 0;
        this.kills = 0;
        this.deaths = 0;
        this.timeOfGrowthLeft = 0;
        this.orderedPoints.clear();
        this.unorderedPoints.clear();
    }

    public void setDead() {
        this.alive = false;
    }

    public void kill() {
        assert this.timeToRespawn == -1;

        assert this.alive;

        this.invisible = false;
        this.alive = false;
        this.length = 0;
        this.timeOfGrowthLeft = 0;
        this.orderedPoints.clear();
        this.unorderedPoints.clear();
        ++this.deaths;
        this.timeToRespawn = 1;
        this.invisibilityTimeLeft = 0;
        this.timeSinceInvisible = 0;
    }

    public void setAlive() {
        this.alive = true;
        this.timeToRespawn = -1;
    }

    public Point getHead() {
        return this.orderedPoints.isEmpty() ? null : (Point)this.orderedPoints.getFirst();
    }

    public Collection<Point> getFullBody() {
        return this.orderedPoints;
    }

    public Point getTail() {
        return this.orderedPoints.isEmpty() ? null : (Point)this.orderedPoints.getLast();
    }

    public boolean isAlive() {
        return this.alive;
    }

    public boolean isInvisible() {
        return this.invisible;
    }

    public int getLength() {
        return this.length;
    }

    public int getKills() {
        return this.kills;
    }

    public boolean contains(Point point) {
        return this.unorderedPoints.contains(point);
    }

    public boolean isGrowing() {
        return this.timeOfGrowthLeft > 0;
    }

    public boolean isShrinking() {
        return this.timeOfGrowthLeft < 0;
    }

    public void addAppleBonus(int appleGrowLength) {
        this.timeOfGrowthLeft += appleGrowLength;
    }

    public void advance() {
        assert !this.isAlive();

        --this.timeToRespawn;
    }

    public int getTimeToRespawn() {
        return this.timeToRespawn;
    }

    public void advance(Point nextPoint) {
        if (this.isInvisible()) {
            if (this.invisibilityTimeLeft == 0) {
                this.invisible = false;
                this.timeSinceInvisible = 0;
            } else {
                --this.invisibilityTimeLeft;
                ++this.timeSinceInvisible;
            }
        }

        if (this.isGrowing()) {
            --this.timeOfGrowthLeft;
        } else {
            int nPointsToRemove = 1;
            if (this.isShrinking()) {
                ++this.timeOfGrowthLeft;
                ++nPointsToRemove;
            }

            for(int i = 0; i < nPointsToRemove; ++i) {
                Point tail = (Point)this.orderedPoints.pollLast();
                this.unorderedPoints.remove(tail);
            }
        }

        this.orderedPoints.addFirst(nextPoint);
        this.unorderedPoints.add(nextPoint);
        this.length = this.unorderedPoints.size();
    }

    public Direction getDirection() {
        if (this.isAlive() && this.getLength() != 1) {
            Iterator<Point> it = this.orderedPoints.iterator();
            return Direction.directionTo((Point)it.next(), (Point)it.next());
        } else {
            return Direction.NONE;
        }
    }

    protected boolean isConsistent() {
        if (this.length == this.unorderedPoints.size() && this.length == this.orderedPoints.size()) {
            Point prev = null;

            Point point;
            for(Iterator var2 = this.orderedPoints.iterator(); var2.hasNext(); prev = point) {
                point = (Point)var2.next();
                if (!this.contains(point)) {
                    return false;
                }

                if (prev != null && prev.distSq(point) != 1.0D) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private List<Point> getPointsBetween(Point A, Point B) {
        assert A.x == B.x ^ A.y == B.y;

        List<Point> points = new ArrayList();
        int y;
        if (A.x == B.x) {
            for(y = A.y; y <= B.y; ++y) {
                points.add(new Point(A.x, y));
            }

            for(y = A.y; y >= B.y; --y) {
                points.add(new Point(A.x, y));
            }
        } else {
            for(y = A.x; y <= B.x; ++y) {
                points.add(new Point(y, A.y));
            }

            for(y = A.x; y >= B.x; --y) {
                points.add(new Point(y, A.y));
            }
        }

        return points;
    }

    private void addSegment(Point A, Point B) {
        List<Point> points = this.getPointsBetween(A, B);
        Iterator var4 = points.iterator();

        while(var4.hasNext()) {
            Point C = (Point)var4.next();
            if (!this.unorderedPoints.contains(C)) {
                this.orderedPoints.addLast(C);
                this.unorderedPoints.add(C);
            }
        }

        assert this.orderedPoints.size() == this.unorderedPoints.size();

    }

    public boolean cut(Point cutPoint) {
        assert this.isAlive();

        List<Point> newPoints = new ArrayList(this.getLength());
        Iterator var3 = this.iterator();

        while(var3.hasNext()) {
            Point point = (Point)var3.next();
            if (point.equals(cutPoint)) {
                break;
            }

            newPoints.add(point);
        }

        if (newPoints.size() < 2) {
            this.kill();
            return true;
        } else {
            this.setPoints((Point[])newPoints.toArray(new Point[0]));

            assert this.isConsistent();

            return false;
        }
    }

    public void setInvisible(int period) {
        this.invisible = true;
        this.invisiblePoint = this.getHead();
        this.invisibilityTimeLeft = period;
        this.timeSinceInvisible = 0;
    }
}