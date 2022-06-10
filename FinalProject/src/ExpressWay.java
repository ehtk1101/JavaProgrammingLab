// This class is for the crawled data of each junction of expressway.
// And this class is parent class of ExpressClear, ExpressSlow, ExpressDelay.
public class ExpressWay {
    private String name; // for store the name of expressway
    private String subname; // for store the name of junction
    private String distance; // for store the length of the expressway
    private String speed; // for store real-time speed of the expressway
    private String time; // for store the time which take for through the express way
    protected float condition; // this is for polymorphism and it will use to calculate the score of the plan user makes.

    public ExpressWay() {
    	name = null;
    	subname = null;
    	distance = null;
    	speed = null;
    	time = null;
    	condition = 0;
    }
    
    // Constructor
    public ExpressWay(String name, String subname, String distance, String speed, String time) {
        this.name = name;
        this.subname = subname;
        this.distance = distance;
        this.speed = speed;
        this.time = time;
        this.condition = 0;
    }

    // getter and setter
    public String getName() {
        return name;
    }

    public String getSubname() {
        return subname;
    }

    public String getDistance() {
        return distance;
    }

    public String getSpeed() {
        return speed;
    }

    public String getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setTime(String time) {
        this.time = time;
    }
    
    public float getCondition() {
    	return this.condition;
    }
    
    public int score() {
    	return 0;
    }

    // toString method of this class
    @Override
    public String toString() {
        return "ExpressWay{" +
                "name=" + name +
                ", subname=" + subname +
                ", distance=" + distance +
                ", speed=" + speed +
                ", time=" + time +
                '}';
    }
}