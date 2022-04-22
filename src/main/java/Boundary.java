import java.util.List;

public class Boundary {
    private List<Point> points;

    public Boundary(List<Point> points) {
        this.points = points;
    }

    public boolean contains(Point other) {
        int i, j;
        boolean result = false;
        for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            if ((points.get(i).y > other.y) != (points.get(j).y > other.y) &&
                    (other.x < (points.get(j).x - points.get(i).x) * (other.y - points.get(i).y) / (points.get(j).y - points.get(i).y) + points.get(i).x)) {
                result = !result;
            }
        }
        return result;
    }

}
