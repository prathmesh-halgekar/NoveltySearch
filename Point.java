public class Point implements  Cloneable{
    int positionX = 0;
    int positionY = 0;

    public Point(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public Object getClone(){
        try{
            return  this.clone();
        }catch (Exception e){
            // swallowing exception
            System.out.println(e.toString());
        }
        return null;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (getPositionX() != point.getPositionX()) return false;
        return getPositionY() == point.getPositionY();
    }

    @Override
    public int hashCode() {
        int result = getPositionX();
        result = 31 * result + getPositionY();
        return result;
    }

    @Override
    public String toString() {
        return "Point{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                '}';
    }
}
